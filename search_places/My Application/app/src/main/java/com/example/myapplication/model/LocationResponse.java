package com.example.myapplication.model;

import java.util.List;

public class LocationResponse {
    private List<LocationDescription> hits;
    private String locale;

    public List<LocationDescription> getHits() {
        return hits;
    }

    public String getLocale() {
        return locale;
    }
}