package com.example.myapplication.model;

public class Points {
    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    private final String lat;
    private final String lng;

    public Points(String lat, String lon) {
        this.lat = lat;
        this.lng = lon;
    }

}
