package com.citemenu.mystash.singleton;

import com.citemenu.mystash.pojo.pojo_cite_points.History;

import java.util.List;

/**
 * Created by dev.arsalan on 8/22/2016.
 */
public class MyCitePoints {
    private static MyCitePoints ourInstance = new MyCitePoints();

    private List<History> citePointsHistory;

    private String totalPoints;

    private MyCitePoints() {
    }

    public static MyCitePoints getInstance() {
        return ourInstance;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<History> getCitePointsHistory() {
        return citePointsHistory;
    }

    public void setCitePointsHistory(List<History> citePointsHistory) {
        this.citePointsHistory = citePointsHistory;
    }
}
