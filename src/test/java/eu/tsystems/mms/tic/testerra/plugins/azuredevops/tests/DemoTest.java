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
 * Created on 17.11.2020
 *
 * @author mgn
 */
public class DemoTest extends TesterraTest {

    @Test
    public void test1() {
        AzureDevOpsClient client = new AzureDevOpsClient();
        client.showProjects();
    }

    @Test
    public void test2GetRun() {

        AzureDevOpsClient client = new AzureDevOpsClient();
        Run run = client.getRun(1700);
        System.out.println(run.getName());
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

        AzureDevOpsClient client = new AzureDevOpsClient();

        Run createdRun = client.createRun(run);

        createdRun.setState("Completed");
        createdRun.setComment("This tests was executed with Testerra.");
        client.updateRun(createdRun);

    }

    @Test
    public void test_FindTestpoint() {
        PointsFilter pointsFilter = new PointsFilter();
        pointsFilter.addTestcaseId(2257);

        AzureDevOpsClient azureDevOpsClient = new AzureDevOpsClient();
        Points points = azureDevOpsClient.getPoints(pointsFilter);

        // If there is no point or result is 404, the testcase is not added to a testplan
        // points contains all found
        System.out.println(points.getPoints().get(0).getId());
    }

    @Test
    public void test_AddTestResult() {
        AzureDevOpsClient client = new AzureDevOpsClient();
        int testcaseId = 2257;
        int testRunId = 1704;

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

    @Test
    public void test_updateRun() {
        Run run = new Run();
//        run.setId();
    }

//    @Test
//    public void testGenson() {
//
////        JavaDateTimeBundle dateTimeBundle = new JavaDateTimeBundle().setFormatter(LocalDate.class, DateTimeFormatter.ISO_INSTANT);
//        JavaDateTimeBundle dateTimeBundle = new JavaDateTimeBundle().setFormatter(LocalDateTime.class, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        dateTimeBundle = new JavaDateTimeBundle();
//
////        Genson genson = new GensonBuilder().useDateAsTimestamp(false).withBundle(dateTimeBundle).setSkipNull(true).create();
//        Genson genson = new GensonBuilder().create();
//
//        String json = "{\n" +
//                "\t\"id\": 1259,\n" +
//                "\t\"name\": \"Foobar run\",\n" +
//                "\t\"plan\": {\n" +
//                "\t\t\"id\": 2294,\n" +
//                "\t\t\"name\": \"aDNS\"\n" +
//                "\t},\n" +
//                "\t\"startedDate\": \"2020-11-17T14:27:23.587\",\n" +
//                "\t\"state\": \"InProgress\"\n" +
//                "}";
//
//        Run run = genson.deserialize(json, Run.class);
//    }

}
