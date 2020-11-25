package eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper;

import com.owlike.genson.annotation.JsonDateFormat;
import eu.tsystems.mms.tic.testframework.logging.Loggable;

import java.util.Date;

/**
 * Created on 20.11.2020
 *
 * @author mgn
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Run extends BasicObject implements Loggable {

    private Testplan plan;

    private String state;

    private String startedDate;

    private String comment;

    public Run() {

    }

    public Testplan getPlan() {
        return plan;
    }

    public void setPlan(Testplan plan) {
        this.plan = plan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(String startedDate) {
        this.startedDate = startedDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
