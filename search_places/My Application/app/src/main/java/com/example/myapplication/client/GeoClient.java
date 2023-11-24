
package com.example.myapplication.client;

import androidx.annotation.NonNull;
import com.example.myapplication.model.LocationDescription;
import com.example.myapplication.model.LocationResponse;
import com.example.myapplication.services.LocationsService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.List;

public class GeoClient {

    private static final String LOCATION_BASE_URL = "https://graphhopper.com/";

    private static GeoClient instance;
    private final LocationsService locationService;


    private GeoClient() {
        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        // Create an OkHttpClient instance with a logging interceptor
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(LOCATION_BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        locationService = retrofit.create(LocationsService.class);
    }


    public static GeoClient getInstance() {
        if (instance == null) {
            instance = new GeoClient();
        }
        return instance;
    }
    public Observable<List<LocationDescription>> createLocationDescriptionsObservable(final List<LocationDescription> locationDescriptions) {
        return Observable.create(new Observable.OnSubscribe<List<LocationDescription>>() {
            @Override
            public void call(Subscriber<? super List<LocationDescription>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(locationDescriptions);
                    subscriber.onCompleted();
                }
            }
        });
    }



    public Observable<LocationResponse> getDestination(@NonNull String key, @NonNull String name) {
        locationService.getDestination(key, name, 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        locationResponse -> {
                            List<LocationDescription> locationDescriptions = locationResponse.getHits();
                            for (LocationDescription locationDescription : locationDescriptions) {
                                System.out.println("Name: " + locationDescription.getName());
                                System.out.println("Country: " + locationDescription.getCountry());
                                System.out.println("State: " + locationDescription.getState());
                                System.out.println("Country Code: " + locationDescription.getCountrycode());
                                System.out.println("Points: " + locationDescription.getPoints().getLat() + " " + locationDescription.getPoints().getLng() );
                                System.out.println();
                            }
                        },
                        throwable -> {
                            System.err.println("Error!!!: " + throwable.getMessage());
                        }
                );
        return locationService.getDestination(key, name, 5);
    }


}