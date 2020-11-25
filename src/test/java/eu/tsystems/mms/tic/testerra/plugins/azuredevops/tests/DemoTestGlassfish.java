package eu.tsystems.mms.tic.testerra.plugins.azuredevops.tests;

import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Run;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient.AzureDevOpsClientGlassfish;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import org.testng.annotations.Test;

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

}
