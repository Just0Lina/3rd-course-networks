package com.example.myapplication.client;

import androidx.annotation.NonNull;
import com.example.myapplication.model.LocationDescription;
import com.example.myapplication.model.PlacesDescription;
import com.example.myapplication.services.PlacesService;
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

public class PlacesClient {
    private static final String PLACES_BASE_URL =   "https://api.opentripmap.com/";
    private static PlacesClient instance;
    private final PlacesService placesService;

    private PlacesClient() {
        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(PLACES_BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        placesService = retrofit.create(PlacesService.class);
    }

    public static PlacesClient getInstance() {
        if (instance == null) {
            instance = new PlacesClient();
        }
        return instance;
    }
    public Observable<List<PlacesDescription>> createPlacesDescriptionsObservable(final List<PlacesDescription> placesDescriptions) {
        return Observable.create(new Observable.OnSubscribe<List<PlacesDescription>>() {
            @Override
            public void call(Subscriber<? super List<PlacesDescription>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(placesDescriptions);
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<PlacesDescription> getPlaces(@NonNull String key, @NonNull String lon, @NonNull String lat) {
        return placesService.getPlaces(key, Double.parseDouble(lon), Double.parseDouble(lat), 5, 10000);
    }


}
