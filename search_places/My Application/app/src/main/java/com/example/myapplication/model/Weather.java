package com.example.myapplication.model;

public class Weather {
    public Weather(String main, String description) {
        this.main = main;
        this.description = description;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    private final String main;
    private final String description;



}
