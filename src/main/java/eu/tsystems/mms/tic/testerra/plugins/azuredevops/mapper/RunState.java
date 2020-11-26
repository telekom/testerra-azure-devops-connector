package eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper;

/**
 * Created on 26.11.2020
 *
 * @author mgn
 */
public enum RunState {

    IN_PROGRESS("InProgress"),

    COMPLETED("completed");

    private String value;

    private RunState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
