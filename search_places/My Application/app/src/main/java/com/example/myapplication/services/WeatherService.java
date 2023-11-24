package com.example.myapplication.services;

import com.example.myapplication.model.WeatherDescription;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherService {
    @GET("/data/2.5/weather")
    Observable<WeatherDescription> getWeather(@Query("lat") Double lat, @Query("lon") Double lon, @Query("appid") String appid );

}