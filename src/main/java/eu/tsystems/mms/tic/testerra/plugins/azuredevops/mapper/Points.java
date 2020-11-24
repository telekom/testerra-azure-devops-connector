package eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper;

import java.util.List;

/**
 * Created on 24.11.2020
 *
 * @author mgn
 */
public class Points {

    private List<Point> points;

    private PointsFilter pointsFilter;

    public Points() {

    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public PointsFilter getPointsFilter() {
        return pointsFilter;
    }

    public void setPointsFilter(PointsFilter pointsFilter) {
        this.pointsFilter = pointsFilter;
    }
}
