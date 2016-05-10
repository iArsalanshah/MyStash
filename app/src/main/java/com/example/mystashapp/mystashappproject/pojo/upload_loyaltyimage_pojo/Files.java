
package com.example.mystashapp.mystashappproject.pojo.upload_loyaltyimage_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Files {

    @SerializedName("filepath")
    @Expose
    private Filepath filepath;

    /**
     * @return The filepath
     */
    public Filepath getFilepath() {
        return filepath;
    }

    /**
     * @param filepath The filepath
     */
    public void setFilepath(Filepath filepath) {
        this.filepath = filepath;
    }

}
