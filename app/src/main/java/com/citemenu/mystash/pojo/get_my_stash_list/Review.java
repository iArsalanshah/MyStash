package com.citemenu.mystash.pojo.get_my_stash_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("review_id")
    @Expose
    private String reviewId;
    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

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
     * @return The customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName The customer_name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return The reviewId
     */
    public String getReviewId() {
        return reviewId;
    }

    /**
     * @param reviewId The review_id
     */
    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
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
     * @return The review
     */
    public String getReview() {
        return review;
    }

    /**
     * @param review The review
     */
    public void setReview(String review) {
        this.review = review;
    }

    /**
     * @return The dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime The date_time
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
