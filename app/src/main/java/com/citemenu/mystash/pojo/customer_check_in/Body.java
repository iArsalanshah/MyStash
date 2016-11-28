package com.citemenu.mystash.pojo.customer_check_in;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("checkin")
    @Expose
    private Checkin checkin;

    /**
     * @return The checkin
     */
    public Checkin getCheckin() {
        return checkin;
    }

    /**
     * @param checkin The checkin
     */
    public void setCheckin(Checkin checkin) {
        this.checkin = checkin;
    }

}
