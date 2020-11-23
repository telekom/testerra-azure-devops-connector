package eu.tsystems.mms.tic.testerra.plugins.azuredevops.tests;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.ext.javadatetime.JavaDateTimeBundle;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Run;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Testplan;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient.AzureDevOpsClient;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient.AzureDevOpsClient2;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        Run run = client.getRun(1701);
        System.out.println(run.getName());
    }

    @Test
    public void test2a_GetRun() {

        AzureDevOpsClient2 client = new AzureDevOpsClient2();
        Run run = client.getRun(1701);
        System.out.println(run.getName());
    }

    @Test
    public void createRun() {
        Run run = new Run();
        Testplan testplan = new Testplan();
        testplan.setId(2294);
        run.setName("Foobar run");
        run.setState("InProgress");
        run.setPlan(testplan);
        run.setStartedDate(Instant.now().toString());

//        Genson genson = new GensonBuilder().setSkipNull(true).create();
//
//
//        String json = genson.serialize(run);

        AzureDevOpsClient client = new AzureDevOpsClient();

        Run createdRun = client.createRun(run);

    }

    @Test
    public void test10_createRun() {
        Run run = new Run();
        Testplan testplan = new Testplan();
        testplan.setId(2294);
        run.setName("Foobar run");
        run.setState("InProgress");
        run.setPlan(testplan);
        run.setStartedDate(Instant.now().toString());

//        Genson genson = new GensonBuilder().setSkipNull(true).create();
//
//
//        String json = genson.serialize(run);

        AzureDevOpsClient2 client = new AzureDevOpsClient2();

        Run createdRun = client.createRun(run);

    }

    @Test
    public void testGenson() {

//        JavaDateTimeBundle dateTimeBundle = new JavaDateTimeBundle().setFormatter(LocalDate.class, DateTimeFormatter.ISO_INSTANT);
        JavaDateTimeBundle dateTimeBundle = new JavaDateTimeBundle().setFormatter(LocalDateTime.class, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        dateTimeBundle = new JavaDateTimeBundle();

//        Genson genson = new GensonBuilder().useDateAsTimestamp(false).withBundle(dateTimeBundle).setSkipNull(true).create();
        Genson genson = new GensonBuilder().create();

        String json = "{\n" +
                "\t\"id\": 1259,\n" +
                "\t\"name\": \"Foobar run\",\n" +
                "\t\"plan\": {\n" +
                "\t\t\"id\": 2294,\n" +
                "\t\t\"name\": \"aDNS\"\n" +
                "\t},\n" +
                "\t\"startedDate\": \"2020-11-17T14:27:23.587\",\n" +
                "\t\"state\": \"InProgress\"\n" +
                "}";

        Run run = genson.deserialize(json, Run.class);
    }

}
