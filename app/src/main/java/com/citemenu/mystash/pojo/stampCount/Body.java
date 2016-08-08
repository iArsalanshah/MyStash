
package com.citemenu.mystash.pojo.stampCount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("stampcount")
    @Expose
    private String stampcount;

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

}
