
package com.example.mystashapp.mystashappproject.pojo.pojo_searchbusiness;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Body {

    @SerializedName("searchnearby")
    @Expose
    private List<Searchnearby> searchnearby = new ArrayList<Searchnearby>();

    /**
     * @return The searchnearby
     */
    public List<Searchnearby> getSearchnearby() {
        return searchnearby;
    }

    /**
     * @param searchnearby The searchnearby
     */
    public void setSearchnearby(List<Searchnearby> searchnearby) {
        this.searchnearby = searchnearby;
    }

}
