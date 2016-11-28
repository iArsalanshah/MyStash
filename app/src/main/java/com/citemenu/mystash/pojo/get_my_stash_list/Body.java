package com.citemenu.mystash.pojo.get_my_stash_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Body {

    @SerializedName("stashlist")
    @Expose
    private List<Stashlist> stashlist = new ArrayList<Stashlist>();

    /**
     * @return The stashlist
     */
    public List<Stashlist> getStashlist() {
        return stashlist;
    }

    /**
     * @param stashlist The stashlist
     */
    public void setStashlist(List<Stashlist> stashlist) {
        this.stashlist = stashlist;
    }

}
