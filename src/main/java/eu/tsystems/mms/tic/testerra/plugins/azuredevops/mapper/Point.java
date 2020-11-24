package eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper;

/**
 * Created on 24.11.2020
 *
 * @author mgn
 */
public class Point extends BasicObject {

    private Testplan testPlan;

    public Point() {

    }

    public Testplan getTestPlan() {
        return testPlan;
    }

    public void setTestPlan(Testplan testPlan) {
        this.testPlan = testPlan;
    }
}
