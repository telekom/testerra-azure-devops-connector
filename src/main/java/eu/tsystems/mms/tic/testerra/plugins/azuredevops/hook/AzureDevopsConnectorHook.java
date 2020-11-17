package eu.tsystems.mms.tic.testerra.plugins.azuredevops.hook;

import eu.tsystems.mms.tic.testerra.plugins.azuredevops.synchronize.AzureDevopsResultSynchronizer;
import eu.tsystems.mms.tic.testframework.hooks.ModuleHook;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.report.TesterraListener;

/**
 * Created on 17.11.2020
 *
 * @author mgn
 */
public class AzureDevopsConnectorHook implements ModuleHook, Loggable {

    private static AzureDevopsResultSynchronizer synchronizer;

    @Override
    public void init() {
        synchronizer = new AzureDevopsResultSynchronizer();
        TesterraListener.getEventBus().register(synchronizer);
    }

    @Override
    public void terminate() {
        synchronizer.shutdown();
        TesterraListener.getEventBus().unregister(synchronizer);
    }
}
