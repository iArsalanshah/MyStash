
package com.citemenu.mystash.pojo.pojo_login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Header {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * @return The success
     */
    public String getSuccess() {
        return success;
    }

    /**
     * @param success The success
     */
    public void setSuccess(String success) {
        this.success = success;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
