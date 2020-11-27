package eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper;

/**
 * Created on 27.11.2020
 *
 * @author mgn
 */
public enum FailureType {

    KNOWN_ISSUE("Known Issue"),

    NEW_ISSUE("New Issue");

    private String value;

    private FailureType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
