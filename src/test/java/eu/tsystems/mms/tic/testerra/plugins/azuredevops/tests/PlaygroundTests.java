package eu.tsystems.mms.tic.testerra.plugins.azuredevops.tests;


import eu.tsystems.mms.tic.testerra.plugins.azuredevops.annotation.AzureTest;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created on 26.11.2020
 *
 * @author mgn
 */
public class PlaygroundTests extends TesterraTest {

    @Test
    @AzureTest(id = 2257)
    public void test_Passed01() {
        TimerUtils.sleep(5555, "Wait some time...");
        Assert.assertTrue(true);
    }

}
