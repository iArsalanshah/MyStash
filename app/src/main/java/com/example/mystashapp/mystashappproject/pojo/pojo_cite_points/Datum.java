
package com.example.mystashapp.mystashappproject.pojo.pojo_cite_points;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("totalpoints")
    @Expose
    private String totalpoints;

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
     * @return The cid
     */
    public String getCid() {
        return cid;
    }

    /**
     * @param cid The cid
     */
    public void setCid(String cid) {
        this.cid = cid;
    }

    /**
     * @return The businessName
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     * @param businessName The business_name
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

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

}
