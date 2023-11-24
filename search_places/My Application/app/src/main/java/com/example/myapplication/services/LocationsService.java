package com.example.myapplication.services;

import com.example.myapplication.model.LocationResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface LocationsService {
    @GET("/api/1/geocode")
    Observable<LocationResponse> getDestination(@Query("key") String key, @Query("q") String name, @Query("limit") int limit );

}