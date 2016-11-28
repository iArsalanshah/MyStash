
package com.citemenu.mystash.pojo.upload_bills;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Files {

    @SerializedName("filepath")
    @Expose
    private List<String> filepath = new ArrayList<String>();

    /**
     * @return The filepath
     */
    public List<String> getFilepath() {
        return filepath;
    }

    /**
     * @param filepath The filepath
     */
    public void setFilepath(List<String> filepath) {
        this.filepath = filepath;
    }

}
