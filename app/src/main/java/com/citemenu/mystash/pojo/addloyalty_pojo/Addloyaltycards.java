
package com.citemenu.mystash.pojo.addloyalty_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Addloyaltycards {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("cardname")
    @Expose
    private String cardname;
    @SerializedName("carddetail")
    @Expose
    private String carddetail;
    @SerializedName("cardno")
    @Expose
    private String cardno;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("companyinfo")
    @Expose
    private String companyinfo;
    @SerializedName("companylogo")
    @Expose
    private String companylogo;
    @SerializedName("frontimage")
    @Expose
    private String frontimage;
    @SerializedName("is_registerd_company")
    @Expose
    private String isRegisterdCompany;
    @SerializedName("backimage")
    @Expose
    private String backimage;

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
