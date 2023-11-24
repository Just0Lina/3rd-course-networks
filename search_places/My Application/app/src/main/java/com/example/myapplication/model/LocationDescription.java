package com.example.myapplication.model;
public class LocationDescription {

    public final String name;
    public final String country;
    public final String state;
    public final String countrycode;

    private final Points point;

    public LocationDescription(String name, String country, String state, String countrycode, Points point) {
        this.name = name;
        this.country = country;
        this.state = state;
        this.countrycode = countrycode;
        this.point = point;
    }


    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public Points getPoints() {
        return point;
    }
}