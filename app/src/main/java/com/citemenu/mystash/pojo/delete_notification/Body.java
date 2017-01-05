
package com.citemenu.mystash.pojo.delete_notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("reviewlist")
    @Expose
    private Reviewlist reviewlist;

    public Reviewlist getReviewlist() {
        return reviewlist;
    }

    public void setReviewlist(Reviewlist reviewlist) {
        this.reviewlist = reviewlist;
    }

}
