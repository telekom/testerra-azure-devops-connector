package eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper;

/**
 * Created on 24.11.2020
 *
 * @author mgn
 */
public class Result extends BasicObject {

    private String startedDate;

    private String completedDate;
    /**
     * Test outcome of tet result.
     * Valid values = (Unspecified, None, Passed, Failed, Inconclusive, Timeout, Aborted, Blocked, NotExecuted, Warning, Error, NotApplicable, Paused, InProgress, NotImpacted)
     */
    private String outcome;

    private String state;

    private Point testPoint;

    /**
     * Failure type of test result.
     * Valid values = (Known Issue, New Issue, Regression, Unknown, None)
     */
    private String failureType;

    private String errorMessage;

    /**
     *  maxSize= 1000 chars.
     */
    private String stackTrace;

    public Result() {

    }

    public String getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(String startedDate) {
        this.startedDate = startedDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Point getTestPoint() {
        return testPoint;
    }

    public void setTestPoint(Point testPoint) {
        this.testPoint = testPoint;
    }

    public String getFailureType() {
        return failureType;
    }

    public void setFailureType(String failureType) {
        this.failureType = failureType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
