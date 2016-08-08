
package com.citemenu.mystash.pojo.pojo_cite_points;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Body {

    @SerializedName("totalpoints")
    @Expose
    private String totalpoints;
    @SerializedName("history")
    @Expose
    private List<History> history = new ArrayList<History>();

    /**
     * @return The totalpoints
     */
    public String getTotalpoints() {
        return totalpoints;
    }

    /**
     * @param totalpoints The totalpoints
     */
    public void setTotalpoints(String totalpoints) {
        this.totalpoints = totalpoints;
    }

    /**
     * @return The history
     */
    public List<History> getHistory() {
        return history;
    }

    /**
     * @param history The history
     */
    public void setHistory(List<History> history) {
        this.history = history;
    }

}
