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

package eu.tsystems.mms.tic.testerra.plugins.azuredevops.tests;

import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Point;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Points;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.PointsFilter;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Result;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Results;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Run;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Testplan;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient.AzureDevOpsClient;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 25.11.2020
 *
 * @author mgn
 */
public class PlaygroundAzureClient extends TesterraTest {

    @Test
    public void test_GetProject() {
        AzureDevOpsClient client = new AzureDevOpsClient();
        client.showProjects();
    }

    @Test
    public void test_GetRun() {
        final int runId = 1707;
        AzureDevOpsClient client = new AzureDevOpsClient();
        Run run = client.getRun(runId);
    }

    @Test
    public void test_UpdateRun() {
        final int runId = 1707;
        Run run = new Run();
        run.setId(runId);
        run.setComment("This tests was executed with Testerra.");
        run.setState("Completed");

        AzureDevOpsClient client = new AzureDevOpsClient();
        client.updateRun(run);
    }

    /**
     * Create an empty run
     *
     */
    @Test
    public void createRun() {
        Run run = new Run();
        Testplan testplan = new Testplan();
        testplan.setId(2294);
        run.setName("AdminTool regression test");
        run.setState("InProgress");
        run.setPlan(testplan);
        run.setStartedDate(Instant.now().toString());

        AzureDevOpsClient client = new AzureDevOpsClient();

        Run createdRun = client.createRun(run);

        createdRun.setState("Completed");
        createdRun.setComment("This tests was executed with Testerra.");
        client.updateRun(createdRun);

    }

    @Test
    public void test_FindTestpoint() {
        PointsFilter pointsFilter = new PointsFilter();
        pointsFilter.addTestcaseId(2407);

        AzureDevOpsClient azureDevOpsClient = new AzureDevOpsClient();
        Points points = azureDevOpsClient.getPoints(pointsFilter);

        // If there is no point or result is 404, the testcase is not added to a testplan
        // points contains all found
        System.out.println(points.getPoints().get(0).getId());
    }

    /**
     * Add a test result to a specific run.
     */
    @Test
    public void test_AddTestResult() {
        AzureDevOpsClient client = new AzureDevOpsClient();
        int testcaseId = 2407;
        int testRunId = 25959;

        // Find out the test point
        PointsFilter pointsFilter = new PointsFilter();
        pointsFilter.addTestcaseId(testcaseId);
        Point point = client.getPoints(pointsFilter).getPoints().get(0);

        // Create result
        Result result = new Result();
        result.setTestPoint(point);
        result.setStartedDate(Instant.now().toString());
        TimerUtils.sleep(5000);
        result.setCompletedDate(Instant.now().toString());
        result.setOutcome("Passed");
        List<Result> resultList = new ArrayList<>();
        resultList.add(result);

//        Genson genson = new GensonBuilder().setSkipNull(true).create();
//        genson.serialize(resultList);

        Results results = client.addResult(resultList, testRunId);
        System.out.println(results.getCount());

    }

}
