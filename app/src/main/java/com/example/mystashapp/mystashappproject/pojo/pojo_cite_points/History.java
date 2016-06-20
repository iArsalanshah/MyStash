
package com.example.mystashapp.mystashappproject.pojo.pojo_cite_points;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class History {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("bill_number")
    @Expose
    private String billNumber;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("redeemedpoints")
    @Expose
    private String redeemedpoints;
    @SerializedName("date")
    @Expose
    private String date;

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
     * @return The billNumber
     */
    public String getBillNumber() {
        return billNumber;
    }

    /**
     * @param billNumber The bill_number
     */
    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The redeemedpoints
     */
    public String getRedeemedpoints() {
        return redeemedpoints;
    }

    /**
     * @param redeemedpoints The redeemedpoints
     */
    public void setRedeemedpoints(String redeemedpoints) {
        this.redeemedpoints = redeemedpoints;
    }

    /**
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    public void setDate(String date) {
        this.date = date;
    }

}
