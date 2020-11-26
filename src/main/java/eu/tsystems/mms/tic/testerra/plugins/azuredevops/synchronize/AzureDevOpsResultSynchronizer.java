/*
 * Testerra
 *
 * (C) 2020, Martin Großmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package eu.tsystems.mms.tic.testerra.plugins.azuredevops.synchronize;

import eu.tsystems.mms.tic.testerra.plugins.azuredevops.annotation.AzureTest;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.config.AzureDevOpsConfig;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Outcome;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Point;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Points;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Result;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Run;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.RunState;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Testplan;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient.AzureDevOpsClient;
import eu.tsystems.mms.tic.testframework.connectors.util.AbstractCommonSynchronizer;
import eu.tsystems.mms.tic.testframework.events.MethodEndEvent;
import eu.tsystems.mms.tic.testframework.logging.Loggable;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created on 17.11.2020
 *
 * @author mgn
 */
public class AzureDevOpsResultSynchronizer extends AbstractCommonSynchronizer implements Loggable {

    private AzureDevOpsClient client = null;

    private AzureDevOpsConfig config = null;

    private int currentRunId = 0;

    public AzureDevOpsResultSynchronizer() {
        this.init();
    }

    @Override
    protected void pOnTestSuccess(MethodEndEvent event) {
        log().info("Method " + event.getTestMethod().getMethodName() + " passed");
        AzureTest annotation = this.getAnnotation(event);
        if (annotation != null && annotation.enabled()) {

            Points points = this.client.getPointsByTestCaseFilter(annotation.id());
            if (points.getPoints().size() > 0) {

                // Find the point with the current test plan id
                Optional<Point> optionalPoint = points.getPoints().stream().filter(point -> this.config.getAzureTestPlanId() == point.getTestPlan().getId()).findFirst();
                if (optionalPoint.isPresent()) {

                    // Create a new test result
                    Result result = new Result();
                    result.setTestPoint(optionalPoint.get());
                    result.setStartedDate(event.getMethodContext().executionContext.getStartTime().toInstant().toString());
                    result.setCompletedDate(event.getMethodContext().executionContext.getEndTime().toInstant().toString());
                    result.setOutcome(Outcome.PASSED.toString());
                    List<Result> resultList = new ArrayList<>();
                    resultList.add(result);
                    this.client.addResult(resultList, this.currentRunId);

                } else {
                    log().warn(String.format(
                            "Cannot sync %s: Testcase with ID %s is not mapped to test plan %s",
                            event.getTestMethod().getMethodName(),
                            annotation.id(),
                            this.config.getAzureTestPlanId()
                    ));
                }

            } else {
                log().warn(String.format(
                        "Cannot sync %s: Testcase with ID %s was not added to a test plan.",
                        event.getTestMethod().getMethodName(),
                        annotation.id()));
            }

        }
        if (annotation != null && !annotation.enabled()) {
            log().info(String.format(
                    "Sync of %s is deactivated.",
                    event.getTestMethod().getMethodName()
            ));
        }
    }

    @Override
    protected void pOnTestFailure(MethodEndEvent event) {
        log().info("Method " + event.getTestMethod().getMethodName() + " failed");
//        event.getMethodContext().retryNumber == max-retry

    }

    @Override
    protected void pOnTestSkip(MethodEndEvent event) {
        log().info("Method " + event.getTestMethod().getMethodName() + " skipped");
    }

    private void init() {
        this.config = AzureDevOpsConfig.getInstance();

        if (this.config.isAzureSyncEnabled()) {
            log().info("Start test result sync with Azure DevOps at " + this.config.getAzureUrl());

            this.client = new AzureDevOpsClient();

            // Initialize a new run at Azure DevOps
            Testplan testplan = new Testplan(this.config.getAzureTestPlanId());
            Run run = new Run();
            run.setPlan(testplan);
            run.setName(this.config.getAzureRunName());
            run.setState(RunState.IN_PROGRESS.toString());
            run.setStartedDate(Instant.now().toString());

            Run createdRun = this.client.createRun(run);
            if (createdRun != null && createdRun.getId() != null) {
                this.currentRunId = createdRun.getId();
            } else {
                log().error("Cannot create test run, sync will be deactivated.");
                this.config.deactivateResultSync();
            }
        }
    }

    public void shutdown() {
        if (this.config.isAzureSyncEnabled()) {
            log().info("Finalize result sync with Azure DevOps");
            Run run = new Run();
            run.setState(RunState.COMPLETED.toString());
            run.setComment("This run was created by Testerra Azure DevOps connector.");
            run.setId(this.currentRunId);
            Run updatedRun = this.client.updateRun(run);
            if (updatedRun == null) {
                // Sometimes Azure DevOps returns an error like 'Test run with id 1711 has already been updated' (unclear why)
                // --> retry to update the run
                log().info("Retry updating the run...");
                this.client.updateRun(run);
            }
        }
    }

    private AzureTest getAnnotation(MethodEndEvent event) {
        final Method javaMethod = event.getTestResult().getMethod().getConstructorOrMethod().getMethod();
        if (javaMethod.isAnnotationPresent(AzureTest.class)) {
            return javaMethod.getAnnotation(AzureTest.class);
        } else {
            log().info("No annoation found for sync results with Azure DevOps");
            return null;
        }
    }
}
