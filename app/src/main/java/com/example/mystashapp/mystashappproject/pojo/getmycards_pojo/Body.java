
package com.example.mystashapp.mystashappproject.pojo.getmycards_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Body {

    @SerializedName("loyaltycards")
    @Expose
    private List<Loyaltycard> loyaltycards = new ArrayList<Loyaltycard>();

    /**
     * @return The loyaltycards
     */
    public List<Loyaltycard> getLoyaltycards() {
        return loyaltycards;
    }

    /**
     * @param loyaltycards The loyaltycards
     */
    public void setLoyaltycards(List<Loyaltycard> loyaltycards) {
        this.loyaltycards = loyaltycards;
    }

}
