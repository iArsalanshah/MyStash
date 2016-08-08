
package com.citemenu.mystash.pojo.getmycards_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Loyaltycard {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("loyalty_id")
    @Expose
    private String loyaltyId;
    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("cardno")
    @Expose
    private String cardno;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("is_registerd_company")
    @Expose
    private String isRegisterdCompany;
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
    @SerializedName("list")
    @Expose
    private String list;
    @SerializedName("frontimage")
    @Expose
    private String frontimage;
    @SerializedName("backimage")
    @Expose
    private String backimage;

    public Loyaltycard(String id, String loyaltyId, String cid, String cardno, String notes, String isRegisterdCompany, String cardname, String carddetail, String companyinfo, String companylogo, String imageurl, String list, String frontimage, String backimage) {
        this.id = id;
        this.loyaltyId = loyaltyId;
        this.cid = cid;
        this.cardno = cardno;
        this.notes = notes;
        this.isRegisterdCompany = isRegisterdCompany;
        this.cardname = cardname;
        this.carddetail = carddetail;
        this.companyinfo = companyinfo;
        this.companylogo = companylogo;
        this.imageurl = imageurl;
        this.list = list;
        this.frontimage = frontimage;
        this.backimage = backimage;
    }

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
     * @return The loyaltyId
     */
    public String getLoyaltyId() {
        return loyaltyId;
    }

    /**
     * @param loyaltyId The loyalty_id
     */
    public void setLoyaltyId(String loyaltyId) {
        this.loyaltyId = loyaltyId;
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
     * @return The cardno
     */
    public String getCardno() {
        return cardno;
    }

    /**
     * @param cardno The cardno
     */
    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    /**
     * @return The notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes The notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return The isRegisterdCompany
     */
    public String getIsRegisterdCompany() {
        return isRegisterdCompany;
    }

    /**
     * @param isRegisterdCompany The is_registerd_company
     */
    public void setIsRegisterdCompany(String isRegisterdCompany) {
        this.isRegisterdCompany = isRegisterdCompany;
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

    /**
     * @return The list
     */
    public String getList() {
        return list;
    }

    /**
     * @param list The list
     */
    public void setList(String list) {
        this.list = list;
    }

    /**
     * @return The frontimage
     */
    public String getFrontimage() {
        return frontimage;
    }

    /**
     * @param frontimage The frontimage
     */
    public void setFrontimage(String frontimage) {
        this.frontimage = frontimage;
    }

    /**
     * @return The backimage
     */
    public String getBackimage() {
        return backimage;
    }

    /**
     * @param backimage The backimage
     */
    public void setBackimage(String backimage) {
        this.backimage = backimage;
    }

}
