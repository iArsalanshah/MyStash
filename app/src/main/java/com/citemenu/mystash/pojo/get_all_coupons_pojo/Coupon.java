
package com.citemenu.mystash.pojo.get_all_coupons_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupon {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("coupon_name")
    @Expose
    private String couponName;
    @SerializedName("coupon_desc")
    @Expose
    private String couponDesc;
    @SerializedName("coupon_expdate")
    @Expose
    private String couponExpdate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("imgurl")
    @Expose
    private String imgurl;
    @SerializedName("total_count")
    @Expose
    private String totalCount;
    @SerializedName("redeemed_count")
    @Expose
    private String redeemedCount;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The couponName
     */
    public String getCouponName() {
        return couponName;
    }

    /**
     * @param couponName The coupon_name
     */
    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    /**
     * @return The couponDesc
     */
    public String getCouponDesc() {
        return couponDesc;
    }

    /**
     * @param couponDesc The coupon_desc
     */
    public void setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
    }

    /**
     * @return The couponExpdate
     */
    public String getCouponExpdate() {
        return couponExpdate;
    }

    /**
     * @param couponExpdate The coupon_expdate
     */
    public void setCouponExpdate(String couponExpdate) {
        this.couponExpdate = couponExpdate;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid The uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return The imgurl
     */
    public String getImgurl() {
        return imgurl;
    }

    /**
     * @param imgurl The imgurl
     */
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    /**
     * @return The totalCount
     */
    public String getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount The total_count
     */
    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return The redeemedCount
     */
    public String getRedeemedCount() {
        return redeemedCount;
    }

    /**
     * @param redeemedCount The redeemed_count
     */
    public void setRedeemedCount(String redeemedCount) {
        this.redeemedCount = redeemedCount;
    }

}
