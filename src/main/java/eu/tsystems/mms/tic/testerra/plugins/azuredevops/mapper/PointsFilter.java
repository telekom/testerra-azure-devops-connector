package eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 24.11.2020
 *
 * @author mgn
 */
public class PointsFilter {

    private List<Integer> testcaseIds;

    public PointsFilter() {
        this.testcaseIds = new ArrayList<>();
    }

    public void addTestcaseId(int id) {
        this.testcaseIds.add(id);
    }

    public List<Integer> getTestcaseIds() {
        return testcaseIds;
    }
}
