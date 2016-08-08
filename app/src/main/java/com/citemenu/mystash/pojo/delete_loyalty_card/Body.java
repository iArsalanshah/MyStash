
package com.citemenu.mystash.pojo.delete_loyalty_card;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("reviewlist")
    @Expose
    private Reviewlist reviewlist;

    /**
     * @return The reviewlist
     */
    public Reviewlist getReviewlist() {
        return reviewlist;
    }

    /**
     * @param reviewlist The reviewlist
     */
    public void setReviewlist(Reviewlist reviewlist) {
        this.reviewlist = reviewlist;
    }

}
