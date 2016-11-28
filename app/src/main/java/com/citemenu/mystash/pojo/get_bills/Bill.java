package com.citemenu.mystash.pojo.get_bills;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Bill {

    @SerializedName("rec_id")
    @Expose
    private String recId;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("cid")
    @Expose
    private String cid;
    @SerializedName("res_name")
    @Expose
    private String resName;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("total_bill_amount")
    @Expose
    private String totalBillAmount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("comment")
    @Expose
    private Object comment;
    @SerializedName("bill_type")
    @Expose
    private String billType;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("file_path")
    @Expose
    private List<String> filePath = new ArrayList<String>();

    /**
     * @return The recId
     */
    public String getRecId() {
        return recId;
    }

    /**
     * @param recId The rec_id
     */
    public void setRecId(String recId) {
        this.recId = recId;
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
     * @return The resName
     */
    public String getResName() {
        return resName;
    }

    /**
     * @param resName The res_name
     */
    public void setResName(String resName) {
        this.resName = resName;
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

    /**
     * @return The totalBillAmount
     */
    public String getTotalBillAmount() {
        return totalBillAmount;
    }

    /**
     * @param totalBillAmount The total_bill_amount
     */
    public void setTotalBillAmount(String totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The comment
     */
    public Object getComment() {
        return comment;
    }

    /**
     * @param comment The comment
     */
    public void setComment(Object comment) {
        this.comment = comment;
    }

    /**
     * @return The billType
     */
    public String getBillType() {
        return billType;
    }

    /**
     * @param billType The bill_type
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }

    /**
     * @return The amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount The amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
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

    /**
     * @return The invoiceNo
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * @param invoiceNo The invoice_no
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     * @return The filePath
     */
    public List<String> getFilePath() {
        return filePath;
    }

    /**
     * @param filePath The file_path
     */
    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }

}
