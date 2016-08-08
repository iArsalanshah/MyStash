
package com.citemenu.mystash.pojo.remindme_coupon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Body {

    @SerializedName("remindme")
    @Expose
    private List<Object> remindme = new ArrayList<>();

    /**
     * @return The remindme
     */
    public List<Object> getRemindme() {
        return remindme;
    }

    /**
     * @param remindme The remindme
     */
    public void setRemindme(List<Object> remindme) {
        this.remindme = remindme;
    }

}
