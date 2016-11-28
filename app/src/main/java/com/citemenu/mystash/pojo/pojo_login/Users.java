package com.citemenu.mystash.pojo.pojo_login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Users {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("current_lat")
    @Expose
    private String currentLat;
    @SerializedName("current_long")
    @Expose
    private String currentLong;
    @SerializedName("cfirstname")
    @Expose
    private String cfirstname;
    @SerializedName("clastname")
    @Expose
    private String clastname;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("contactnumber")
    @Expose
    private String contactnumber;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("logintype")
    @Expose
    private String logintype;
    @SerializedName("oAuthid")
    @Expose
    private String oAuthid;
    @SerializedName("sec_question")
    @Expose
    private String secQuestion;
    @SerializedName("sec_answer")
    @Expose
    private String secAnswer;
    @SerializedName("gcm_regid")
    @Expose
    private String gcmRegid;
    @SerializedName("birthday")
    @Expose
    private Object birthday;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("categories")
    @Expose
    private Object categories;
    @SerializedName("area_of_interest")
    @Expose
    private Object areaOfInterest;
    @SerializedName("fbid")
    @Expose
    private String fbid;
    @SerializedName("imgurl")
    @Expose
    private String imgurl;
    @SerializedName("platform")
    @Expose
    private String platform;
    @SerializedName("stamp_count")
    @Expose
    private String stampCount;
    @SerializedName("apple_push_id")
    @Expose
    private String applePushId;
    @SerializedName("android_push_id")
    @Expose
    private String androidPushId;

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
     * @return The currentLat
     */
    public String getCurrentLat() {
        return currentLat;
    }

    /**
     * @param currentLat The current_lat
     */
    public void setCurrentLat(String currentLat) {
        this.currentLat = currentLat;
    }

    /**
     * @return The currentLong
     */
    public String getCurrentLong() {
        return currentLong;
    }

    /**
     * @param currentLong The current_long
     */
    public void setCurrentLong(String currentLong) {
        this.currentLong = currentLong;
    }

    /**
     * @return The cfirstname
     */
    public String getCfirstname() {
        return cfirstname;
    }

    /**
     * @param cfirstname The cfirstname
     */
    public void setCfirstname(String cfirstname) {
        this.cfirstname = cfirstname;
    }

    /**
     * @return The clastname
     */
    public String getClastname() {
        return clastname;
    }

    /**
     * @param clastname The clastname
     */
    public void setClastname(String clastname) {
        this.clastname = clastname;
    }

    /**
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The contactnumber
     */
    public String getContactnumber() {
        return contactnumber;
    }

    /**
     * @param contactnumber The contactnumber
     */
    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The logintype
     */
    public String getLogintype() {
        return logintype;
    }

    /**
     * @param logintype The logintype
     */
    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

    /**
     * @return The oAuthid
     */
    public String getOAuthid() {
        return oAuthid;
    }

    /**
     * @param oAuthid The oAuthid
     */
    public void setOAuthid(String oAuthid) {
        this.oAuthid = oAuthid;
    }

    /**
     * @return The secQuestion
     */
    public String getSecQuestion() {
        return secQuestion;
    }

    /**
     * @param secQuestion The sec_question
     */
    public void setSecQuestion(String secQuestion) {
        this.secQuestion = secQuestion;
    }

    /**
     * @return The secAnswer
     */
    public String getSecAnswer() {
        return secAnswer;
    }

    /**
     * @param secAnswer The sec_answer
     */
    public void setSecAnswer(String secAnswer) {
        this.secAnswer = secAnswer;
    }

    /**
     * @return The gcmRegid
     */
    public String getGcmRegid() {
        return gcmRegid;
    }

    /**
     * @param gcmRegid The gcm_regid
     */
    public void setGcmRegid(String gcmRegid) {
        this.gcmRegid = gcmRegid;
    }

    /**
     * @return The birthday
     */
    public Object getBirthday() {
        return birthday;
    }

    /**
     * @param birthday The birthday
     */
    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    /**
     * @return The sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex The sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return The categories
     */
    public Object getCategories() {
        return categories;
    }

    /**
     * @param categories The categories
     */
    public void setCategories(Object categories) {
        this.categories = categories;
    }

    /**
     * @return The areaOfInterest
     */
    public Object getAreaOfInterest() {
        return areaOfInterest;
    }

    /**
     * @param areaOfInterest The area_of_interest
     */
    public void setAreaOfInterest(Object areaOfInterest) {
        this.areaOfInterest = areaOfInterest;
    }

    /**
     * @return The fbid
     */
    public String getFbid() {
        return fbid;
    }

    /**
     * @param fbid The fbid
     */
    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    /**
     * @return The imgurl
     */
    public String getImgurl() {
        return imgurl;
    }

    /**
     * @param imgurl The imgurl
     */
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    /**
     * @return The platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * @param platform The platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * @return The stampCount
     */
    public String getStampCount() {
        return stampCount;
    }

    /**
     * @param stampCount The stamp_count
     */
    public void setStampCount(String stampCount) {
        this.stampCount = stampCount;
    }

    /**
     * @return The applePushId
     */
    public String getApplePushId() {
        return applePushId;
    }

    /**
     * @param applePushId The apple_push_id
     */
    public void setApplePushId(String applePushId) {
        this.applePushId = applePushId;
    }

    /**
     * @return The androidPushId
     */
    public String getAndroidPushId() {
        return androidPushId;
    }

    /**
     * @param androidPushId The android_push_id
     */
    public void setAndroidPushId(String androidPushId) {
        this.androidPushId = androidPushId;
    }

}
