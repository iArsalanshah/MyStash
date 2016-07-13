
package com.example.mystashapp.mystashappproject.pojo.meesages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("messageid")
    @Expose
    private String messageid;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("sent_time")
    @Expose
    private String sentTime;

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
     * @return The messageid
     */
    public String getMessageid() {
        return messageid;
    }

    /**
     * @param messageid The messageid
     */
    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
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
     * @return The sentTime
     */
    public String getSentTime() {
        return sentTime;
    }

    /**
     * @param sentTime The sent_time
     */
    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

}
