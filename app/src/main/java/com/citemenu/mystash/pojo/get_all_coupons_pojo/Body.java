
package com.citemenu.mystash.pojo.get_all_coupons_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Body {

    @SerializedName("coupons")
    @Expose
    private List<Coupon> coupons = new ArrayList<Coupon>();

    /**
     * @return The coupons
     */
    public List<Coupon> getCoupons() {
        return coupons;
    }

    /**
     * @param coupons The coupons
     */
    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

}
