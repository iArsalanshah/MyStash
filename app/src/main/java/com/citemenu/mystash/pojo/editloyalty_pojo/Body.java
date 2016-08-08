
package com.citemenu.mystash.pojo.editloyalty_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("editcards")
    @Expose
    private Editcards editcards;

    /**
     * @return The editcards
     */
    public Editcards getEditcards() {
        return editcards;
    }

    /**
     * @param editcards The editcards
     */
    public void setEditcards(Editcards editcards) {
        this.editcards = editcards;
    }

}
