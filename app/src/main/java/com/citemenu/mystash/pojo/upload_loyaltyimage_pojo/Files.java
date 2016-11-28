package com.citemenu.mystash.pojo.upload_loyaltyimage_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Files {

    @SerializedName("filepath")
    @Expose
    private String filepath;

    /**
     * @return The filepath
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * @param filepath The filepath
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

}
