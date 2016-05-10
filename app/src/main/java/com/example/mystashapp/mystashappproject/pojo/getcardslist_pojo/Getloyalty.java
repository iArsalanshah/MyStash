
package com.example.mystashapp.mystashappproject.pojo.getcardslist_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Getloyalty {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cardname")
    @Expose
    private String cardname;
    @SerializedName("carddetail")
    @Expose
    private String carddetail;
    @SerializedName("companyinfo")
    @Expose
    private String companyinfo;
    @SerializedName("companylogo")
    @Expose
    private String companylogo;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;

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
     * @return The cardname
     */
    public String getCardname() {
        return cardname;
    }

    /**
     * @param cardname The cardname
     */
    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    /**
     * @return The carddetail
     */
    public String getCarddetail() {
        return carddetail;
    }

    /**
     * @param carddetail The carddetail
     */
    public void setCarddetail(String carddetail) {
        this.carddetail = carddetail;
    }

    /**
     * @return The companyinfo
     */
    public String getCompanyinfo() {
        return companyinfo;
    }

    /**
     * @param companyinfo The companyinfo
     */
    public void setCompanyinfo(String companyinfo) {
        this.companyinfo = companyinfo;
    }

    /**
     * @return The companylogo
     */
    public String getCompanylogo() {
        return companylogo;
    }

    /**
     * @param companylogo The companylogo
     */
    public void setCompanylogo(String companylogo) {
        this.companylogo = companylogo;
    }

    /**
     * @return The imageurl
     */
    public String getImageurl() {
        return imageurl;
    }

    /**
     * @param imageurl The imageurl
     */
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}
