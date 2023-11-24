package com.example.myapplication.services;

import com.example.myapplication.model.PlacesDescription;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface PlacesService {
    @GET("/0.1/ru/places/radius")
    Observable<PlacesDescription> getPlaces(@Query("apikey") String key, @Query("lon") Double lon, @Query("lat") Double lat, @Query("limit") int limit, @Query("radius") int radius );

}
