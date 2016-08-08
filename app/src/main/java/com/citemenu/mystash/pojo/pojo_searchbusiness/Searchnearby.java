
package com.citemenu.mystash.pojo.pojo_searchbusiness;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Searchnearby {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("companyname")
    @Expose
    private String companyname;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("logourl")
    @Expose
    private String logourl;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("postalcode")
    @Expose
    private String postalcode;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("contactname")
    @Expose
    private String contactname;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("longt")
    @Expose
    private String longt;
    @SerializedName("rating_count")
    @Expose
    private String ratingCount;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("is_citepoint")
    @Expose
    private String isCitepoint;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_stamp")
    @Expose
    private String isStamp;
    @SerializedName("images")
    @Expose
    private List<Object> images = new ArrayList<Object>();
    @SerializedName("ratingvalue")
    @Expose
    private Float ratingvalue;
    @SerializedName("selected")
    @Expose
    private String selected;
    @SerializedName("stashid")
    @Expose
    private String stashid;
    @SerializedName("isstash")
    @Expose
    private String isstash;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = new ArrayList<Review>();

    public Searchnearby() {
    }

    public Searchnearby(String selected, String id, String name, String companyname, String address, String contact, String email, String logourl, String uid, String city, String postalcode, String province, String country, String contactname, String lat, String longt, String ratingCount, String rating, String isCitepoint, String status, String isStamp, List<Object> images, Float ratingvalue, String stashid, String isstash, Double distance, List<Review> reviews) {
        this.selected = selected;
        this.id = id;
        this.name = name;
        this.companyname = companyname;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.logourl = logourl;
        this.uid = uid;
        this.city = city;
        this.postalcode = postalcode;
        this.province = province;
        this.country = country;
        this.contactname = contactname;
        this.lat = lat;
        this.longt = longt;
        this.ratingCount = ratingCount;
        this.rating = rating;
        this.isCitepoint = isCitepoint;
        this.status = status;
        this.isStamp = isStamp;
        this.images = images;
        this.ratingvalue = ratingvalue;
        this.stashid = stashid;
        this.isstash = isstash;
        this.distance = distance;
        this.reviews = reviews;
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
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The companyname
     */
    public String getCompanyname() {
        return companyname;
    }

    /**
     * @param companyname The companyname
     */
    public void setCompanyname(String companyname) {
        this.companyname = companyname;
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
     * @return The contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact The contact
     */
    public void setContact(String contact) {
        this.contact = contact;
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
     * @return The logourl
     */
    public String getLogourl() {
        return logourl;
    }

    /**
     * @param logourl The logourl
     */
    public void setLogourl(String logourl) {
        this.logourl = logourl;
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
     * @return The city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return The postalcode
     */
    public String getPostalcode() {
        return postalcode;
    }

    /**
     * @param postalcode The postalcode
     */
    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    /**
     * @return The province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province The province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return The contactname
     */
    public String getContactname() {
        return contactname;
    }

    /**
     * @param contactname The contactname
     */
    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    /**
     * @return The lat
     */
    public String getLat() {
        return lat;
    }

    /**
     * @param lat The lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * @return The longt
     */
    public String getLongt() {
        return longt;
    }

    /**
     * @param longt The longt
     */
    public void setLongt(String longt) {
        this.longt = longt;
    }

    /**
     * @return The ratingCount
     */
    public String getRatingCount() {
        return ratingCount;
    }

    /**
     * @param ratingCount The rating_count
     */
    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    /**
     * @return The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     * @param rating The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * @return The isCitepoint
     */
    public String getIsCitepoint() {
        return isCitepoint;
    }

    /**
     * @param isCitepoint The is_citepoint
     */
    public void setIsCitepoint(String isCitepoint) {
        this.isCitepoint = isCitepoint;
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
     * @return The isStamp
     */
    public String getIsStamp() {
        return isStamp;
    }

    /**
     * @param isStamp The is_stamp
     */
    public void setIsStamp(String isStamp) {
        this.isStamp = isStamp;
    }

    /**
     * @return The images
     */
    public List<Object> getImages() {
        return images;
    }

    /**
     * @param images The images
     */
    public void setImages(List<Object> images) {
        this.images = images;
    }

    /**
     * @return The ratingvalue
     */
    public Float getRatingvalue() {
        return ratingvalue;
    }

    /**
     * @param ratingvalue The ratingvalue
     */
    public void setRatingvalue(Float ratingvalue) {
        this.ratingvalue = ratingvalue;
    }

    /**
     * @return The selected
     */
    public String getSelected() {
        return selected;
    }

    /**
     * @param selected The selected
     */
    public void setSelected(String selected) {
        this.selected = selected;
    }

    /**
     * @return The stashid
     */
    public String getStashid() {
        return stashid;
    }

    /**
     * @param stashid The stashid
     */
    public void setStashid(String stashid) {
        this.stashid = stashid;
    }

    /**
     * @return The isstash
     */
    public String getIsstash() {
        return isstash;
    }

    /**
     * @param isstash The isstash
     */
    public void setIsstash(String isstash) {
        this.isstash = isstash;
    }

    /**
     * @return The distance
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * @param distance The distance
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * @return The reviews
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * @param reviews The reviews
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
