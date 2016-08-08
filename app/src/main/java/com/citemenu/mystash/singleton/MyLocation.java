package com.citemenu.mystash.singleton;

/**
 * Created by dev.arsalan on 8/2/2016.
 */
public class MyLocation {
    private static MyLocation instance = null;
    private double lat, lng;

    private MyLocation() {

    }

    public static MyLocation getInstance() {
        if (instance == null)
            instance = new MyLocation();
        return instance;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
