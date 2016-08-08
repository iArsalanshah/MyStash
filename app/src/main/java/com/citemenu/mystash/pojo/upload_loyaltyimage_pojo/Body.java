
package com.citemenu.mystash.pojo.upload_loyaltyimage_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("files")
    @Expose
    private Files files;

    /**
     * @return The files
     */
    public Files getFiles() {
        return files;
    }

    /**
     * @param files The files
     */
    public void setFiles(Files files) {
        this.files = files;
    }

}
