package com.citemenu.mystash.pojo.program_stamps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("programname")
    @Expose
    private String programname;
    @SerializedName("stampcount")
    @Expose
    private String stampcount;
    @SerializedName("totalstamp")
    @Expose
    private String totalstamp;
    @SerializedName("days")
    @Expose
    private String days;
    @SerializedName("all_day")
    @Expose
    private String allDay;
    @SerializedName("starttime")
    @Expose
    private String starttime;
    @SerializedName("endtime")
    @Expose
    private String endtime;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("toc")
    @Expose
    private String toc;

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
     * @return The pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid The pid
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * @return The programname
     */
    public String getProgramname() {
        return programname;
    }

    /**
     * @param programname The programname
     */
    public void setProgramname(String programname) {
        this.programname = programname;
    }

    /**
     * @return The stampcount
     */
    public String getStampcount() {
        return stampcount;
    }

    /**
     * @param stampcount The stampcount
     */
    public void setStampcount(String stampcount) {
        this.stampcount = stampcount;
    }

    /**
     * @return The totalstamp
     */
    public String getTotalstamp() {
        return totalstamp;
    }

    /**
     * @param totalstamp The totalstamp
     */
    public void setTotalstamp(String totalstamp) {
        this.totalstamp = totalstamp;
    }

    /**
     * @return The days
     */
    public String getDays() {
        return days;
    }

    /**
     * @param days The days
     */
    public void setDays(String days) {
        this.days = days;
    }

    /**
     * @return The allDay
     */
    public String getAllDay() {
        return allDay;
    }

    /**
     * @param allDay The all_day
     */
    public void setAllDay(String allDay) {
        this.allDay = allDay;
    }

    /**
     * @return The starttime
     */
    public String getStarttime() {
        return starttime;
    }

    /**
     * @param starttime The starttime
     */
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    /**
     * @return The endtime
     */
    public String getEndtime() {
        return endtime;
    }

    /**
     * @param endtime The endtime
     */
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    /**
     * @return The desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc The desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return The toc
     */
    public String getToc() {
        return toc;
    }

    /**
     * @param toc The toc
     */
    public void setToc(String toc) {
        this.toc = toc;
    }

}
