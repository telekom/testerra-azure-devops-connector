package eu.tsystems.mms.tic.testerra.plugins.azuredevops.synchronize;

import eu.tsystems.mms.tic.testframework.connectors.util.AbstractCommonSynchronizer;
import eu.tsystems.mms.tic.testframework.events.MethodEndEvent;
import eu.tsystems.mms.tic.testframework.logging.Loggable;

import java.util.Date;

/**
 * Created on 17.11.2020
 *
 * @author mgn
 */
public class AzureDevopsResultSynchronizer extends AbstractCommonSynchronizer implements Loggable {

    public AzureDevopsResultSynchronizer() {
        this.init();
    }

    @Override
    protected void pOnTestSuccess(MethodEndEvent event) {
        log().info("Method " + event.getTestMethod().getMethodName() + " passed");
    }

    @Override
    protected void pOnTestFailure(MethodEndEvent event) {
        log().info("Method " + event.getTestMethod().getMethodName() + " failed");
    }

    @Override
    protected void pOnTestSkip(MethodEndEvent event) {

    }

    private void init() {
        log().info("Start sync at " + new Date().toString());
    }

    public void shutdown() {
        log().info("End complete execution at " + new Date().toString());
    }
}
