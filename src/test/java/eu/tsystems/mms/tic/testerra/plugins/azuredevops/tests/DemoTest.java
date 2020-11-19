package eu.tsystems.mms.tic.testerra.plugins.azuredevops.tests;

import eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient.AzureDevOpsClient;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import org.testng.annotations.Test;

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

}
