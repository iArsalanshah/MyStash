
package com.citemenu.mystash.pojo.redeem_coupon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Body {

    @SerializedName("categories")
    @Expose
    private List<Object> categories = new ArrayList<>();

    /**
     * @return The categories
     */
    public List<Object> getCategories() {
        return categories;
    }

    /**
     * @param categories The categories
     */
    public void setCategories(List<Object> categories) {
        this.categories = categories;
    }

}
