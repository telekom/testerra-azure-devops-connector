package eu.tsystems.mms.tic.testerra.plugins.azuredevops.tests;

import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Point;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Points;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.PointsFilter;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Result;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Results;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Run;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Testplan;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient.AzureDevOpsClientGlassfish;
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
public class DemoTestGlassfish extends TesterraTest {

    @Test
    public void test_GetProject() {
        AzureDevOpsClientGlassfish client = new AzureDevOpsClientGlassfish();
        client.showProjects();
    }

    @Test
    public void test_GetRun() {
        final int runId = 1707;
        AzureDevOpsClientGlassfish client = new AzureDevOpsClientGlassfish();
        Run run = client.getRun(runId);
    }

    @Test
    public void test_UpdateRun() {
        final int runId = 1707;
        Run run = new Run();
        run.setId(runId);
        run.setComment("This tests was executed with Testerra.");
        run.setState("Completed");

        AzureDevOpsClientGlassfish client = new AzureDevOpsClientGlassfish();
        client.updateRun(run);
    }

    @Test
    public void createRun() {
        Run run = new Run();
        Testplan testplan = new Testplan();
        testplan.setId(2294);
        run.setName("AdminTool regression test");
        run.setState("InProgress");
        run.setPlan(testplan);
        run.setStartedDate(Instant.now().toString());

        AzureDevOpsClientGlassfish client = new AzureDevOpsClientGlassfish();

        Run createdRun = client.createRun(run);

        createdRun.setState("Completed");
        createdRun.setComment("This tests was executed with Testerra.");
        client.updateRun(createdRun);

    }

    @Test
    public void test_FindTestpoint() {
        PointsFilter pointsFilter = new PointsFilter();
        pointsFilter.addTestcaseId(2257);

        AzureDevOpsClientGlassfish azureDevOpsClient = new AzureDevOpsClientGlassfish();
        Points points = azureDevOpsClient.getPoints(pointsFilter);

        // If there is no point or result is 404, the testcase is not added to a testplan
        // points contains all found
        System.out.println(points.getPoints().get(0).getId());
    }

    @Test
    public void test_AddTestResult() {
        AzureDevOpsClientGlassfish client = new AzureDevOpsClientGlassfish();
        int testcaseId = 2284;
        int testRunId = 1706;

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
