package eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper;

import java.util.List;

/**
 * Created on 24.11.2020
 *
 * @author mgn
 */
public class Results {

    private int count;

    private List<Result> value;

    public Results() {

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Result> getValue() {
        return value;
    }

    public void setValue(List<Result> value) {
        this.value = value;
    }
}
