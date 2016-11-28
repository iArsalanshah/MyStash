package com.citemenu.mystash.pojo.get_bills;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Body {

    @SerializedName("bills")
    @Expose
    private List<Bill> bills = new ArrayList<Bill>();

    /**
     * @return The bills
     */
    public List<Bill> getBills() {
        return bills;
    }

    /**
     * @param bills The bills
     */
    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

}
