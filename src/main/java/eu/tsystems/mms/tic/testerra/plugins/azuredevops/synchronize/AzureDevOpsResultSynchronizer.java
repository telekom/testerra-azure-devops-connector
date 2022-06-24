/*
 * Testerra
 *
 * (C) 2021, Martin Großmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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

import com.google.common.eventbus.Subscribe;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.annotation.AzureTest;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.config.AzureDevOpsConfig;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.FailureType;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Outcome;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Point;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Points;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Result;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Run;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.RunState;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Testplan;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient.AzureDevOpsClient;
import eu.tsystems.mms.tic.testframework.annotations.Fails;
import eu.tsystems.mms.tic.testframework.events.TestStatusUpdateEvent;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.report.Status;
import eu.tsystems.mms.tic.testframework.report.model.context.ErrorContext;
import eu.tsystems.mms.tic.testframework.report.model.context.MethodContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

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
public class AzureDevOpsResultSynchronizer implements TestStatusUpdateEvent.Listener, Loggable {

    private AzureDevOpsClient client = null;

    private AzureDevOpsConfig config = null;

    private int currentRunId = 0;

    public AzureDevOpsResultSynchronizer() {
        this.init();
    }

    @Override
    @Subscribe
    public void onTestStatusUpdate(TestStatusUpdateEvent event) {

        MethodContext methodContext = event.getMethodContext();
        Status status = methodContext.getStatus();
        switch (status) {
            case PASSED:
            case REPAIRED:
            case RECOVERED:
                log().info("Method " + methodContext.getName() + " passed.");
                this.syncTestresult(event, Outcome.PASSED);
                break;
            case FAILED:
            case FAILED_EXPECTED:
                log().info("Method " + methodContext.getName() + " failed.");
                this.syncTestresult(event, Outcome.FAILED);
                break;
            case RETRIED:
                log().info("Method " + methodContext.getName() + " was retried and will not sync.");
                break;
            case SKIPPED:
                log().info("Method " + methodContext.getName() + " skipped.");
                this.syncTestresult(event, Outcome.NOT_EXECUTED);
                break;
            default:
                log().info(String.format("Method state %s of %s cannot handle.", status.toString(), methodContext.getName()));
        }

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
        } else {
            log().info("Azure DevOps connector is attached but result sync is disabled.");
        }
    }

    /**
     * Main method to sync the results of a test method with Azure DevOps.
     * <p>
     * Valid results are Passed, Failed and Skipped
     *
     * @param event
     * @param outcome
     */
    private synchronized void syncTestresult(TestStatusUpdateEvent event, Outcome outcome) {
        Optional<Method> method = this.getMethodFromEvent(event);

        if (method.isPresent()) {

            Optional<AzureTest> annotation = this.getAnnotation(event);
            if (this.config.isAzureSyncEnabled() && annotation.isPresent()) {

                if (annotation.get().enabled()) {

                    Points points = this.client.getPointsByTestCaseFilter(annotation.get().id());
                    if (points.getPoints().size() > 0) {

                        // Find the point with the current test plan id
                        Optional<Point> optionalPoint = points.getPoints().stream().filter(point -> this.config.getAzureTestPlanId() == point.getTestPlan().getId()).findFirst();
                        if (optionalPoint.isPresent()) {

                            // Create a new test result based on current test method
                            Result result = new Result();
                            result.setTestPoint(optionalPoint.get());
                            if (!outcome.equals(Outcome.NOT_EXECUTED)) {
                                result.setStartedDate(event.getMethodContext().getStartTime().toInstant().toString());
                                result.setCompletedDate(event.getMethodContext().getEndTime().toInstant().toString());
                            }
                            result.setOutcome(outcome.toString());
                            // Priority is taken from test case, cannot change with test result
                            // result.setPriority(this.getPriorityByFailureCorridor(event.getMethodContext().failureCorridorValue));

                            if (outcome.equals(Outcome.FAILED)) {
                                event.getMethodContext()
                                        .readErrors()
                                        .filter(ErrorContext::isNotOptional)
                                        .findFirst()
                                        .ifPresent(errorContext -> {
                                            Throwable throwable = errorContext.getThrowable();
                                            String errorMessage = throwable.getMessage();
                                            Optional<Fails> fails = event.getMethodContext().getFailsAnnotation();
                                            if (fails.isPresent() && StringUtils.isNotBlank(fails.get().description())) {
                                                errorMessage += "\n\nKnown issue: " + fails.get().description();
                                            }
                                            result.setErrorMessage(errorMessage);
                                            result.setFailureType(this.getFailureType(event).toString());
//                                            event.getMethodContext().getFailsAnnotation().get().description();
                                            final String stackTrace = ExceptionUtils.getStackTrace(throwable);
                                            result.setStackTrace(stackTrace);
                                        });
                            }

                            List<Result> resultList = new ArrayList<>();
                            resultList.add(result);
                            this.client.addResult(resultList, this.currentRunId);

                        } else {
                            log().warn(String.format(
                                    "Cannot sync %s: Testcase with ID %s is not mapped to test plan %s",
                                    method.get().getName(),
                                    annotation.get().id(),
                                    this.config.getAzureTestPlanId()
                            ));
                        }

                    } else {
                        log().warn(String.format(
                                "Cannot sync %s: Testcase with ID %s was not added to a test plan.",
                                method.get().getName(),
                                annotation.get().id()));
                    }

                } // end method enabled sync
                else {
                    log().info(String.format("Sync of %s is deactivated.", method.get().getName()));
                }

            } // end active sync and annotation found

        } // end method.isPresent

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

    private Optional<AzureTest> getAnnotation(TestStatusUpdateEvent event) {
        Optional<Method> methodFromEvent = this.getMethodFromEvent(event);
        if (methodFromEvent.isPresent() && methodFromEvent.get().isAnnotationPresent(AzureTest.class)) {
            return Optional.of(methodFromEvent.get().getAnnotation(AzureTest.class));
        } else {
            log().info("No annotation found for sync results with Azure DevOps");
            return Optional.empty();
        }
    }

    private Optional<Method> getMethodFromEvent(TestStatusUpdateEvent event) {
        return event.getMethodContext().getTestNgResult()
                .map(iTestResult -> iTestResult.getMethod().getConstructorOrMethod().getMethod());
    }

    private FailureType getFailureType(TestStatusUpdateEvent event) {
        if (event.getMethodContext().getStatus() == Status.FAILED_EXPECTED) {
            return FailureType.KNOWN_ISSUE;
        } else {
            return FailureType.NEW_ISSUE;
        }
    }

}
