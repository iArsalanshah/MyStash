
package com.example.mystashapp.mystashappproject.pojo.all_flyers_by_categ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("filepath")
    @Expose
    private String filepath;
    @SerializedName("imgpath")
    @Expose
    private String imgpath;
    @SerializedName("expirydate")
    @Expose
    private String expirydate;
    @SerializedName("noexpiry")
    @Expose
    private String noexpiry;
    @SerializedName("category")
    @Expose
    private String category;

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
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The filepath
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * @param filepath The filepath
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * @return The imgpath
     */
    public String getImgpath() {
        return imgpath;
    }

    /**
     * @param imgpath The imgpath
     */
    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    /**
     * @return The expirydate
     */
    public String getExpirydate() {
        return expirydate;
    }

    /**
     * @param expirydate The expirydate
     */
    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    /**
     * @return The noexpiry
     */
    public String getNoexpiry() {
        return noexpiry;
    }

    /**
     * @param noexpiry The noexpiry
     */
    public void setNoexpiry(String noexpiry) {
        this.noexpiry = noexpiry;
    }

    /**
     * @return The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

}
