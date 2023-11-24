package com.example.myapplication.client;

import androidx.annotation.NonNull;
import com.example.myapplication.model.WeatherDescription;
import com.example.myapplication.services.WeatherService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeatherClient {
    private static final String WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private static WeatherClient instance;
    private final WeatherService weatherService;


    public static WeatherClient getInstance() {
        if (instance == null) {
            instance = new WeatherClient();
        }
        return instance;
    }


    public Observable<WeatherDescription> getWeather(@NonNull String lat, @NonNull String lon) {
        String key = "8e11c474652575fb7d3b864613d9e224";
        weatherService.getWeather(Double.parseDouble(lat), Double.parseDouble(lon), key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        weatherResponse -> {
                            System.out.println("Main: " + weatherResponse.getWeather().get(0).getMain());
                            System.out.println("Description: " + weatherResponse.getWeather().get(0).getDescription());
                            System.out.println("Name: " + weatherResponse.getName());
                            System.out.println();
                        },
                        throwable -> {
                            System.err.println("Error: " + throwable.getMessage());
                        }
                );
        return weatherService.getWeather(Double.parseDouble(lat), Double.parseDouble(lon), key);
    }


    public WeatherClient() {
        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(WEATHER_BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        weatherService = retrofit.create(WeatherService.class);
    }
}
