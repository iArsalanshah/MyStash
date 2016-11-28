package com.citemenu.mystash.pojo.getcardslist_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Body {

    @SerializedName("getloyalty")
    @Expose
    private List<Getloyalty> getloyalty = new ArrayList<Getloyalty>();

    /**
     * @return The getloyalty
     */
    public List<Getloyalty> getGetloyalty() {
        return getloyalty;
    }

    /**
     * @param getloyalty The getloyalty
     */
    public void setGetloyalty(List<Getloyalty> getloyalty) {
        this.getloyalty = getloyalty;
    }

}
