
package com.citemenu.mystash.pojo.addloyalty_pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("Addloyaltycards")
    @Expose
    private Addloyaltycards Addloyaltycards;

    /**
     * @return The Addloyaltycards
     */
    public Addloyaltycards getAddloyaltycards() {
        return Addloyaltycards;
    }

    /**
     * @param Addloyaltycards The Addloyaltycards
     */
    public void setAddloyaltycards(Addloyaltycards Addloyaltycards) {
        this.Addloyaltycards = Addloyaltycards;
    }

}
