package com.citemenu.mystash.pojo.update_registeration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("users")
    @Expose
    private Users users;

    /**
     * @return The users
     */
    public Users getUsers() {
        return users;
    }

    /**
     * @param users The users
     */
    public void setUsers(Users users) {
        this.users = users;
    }

}
