package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.adapters.FeatureAdapter;
import com.example.myapplication.adapters.LocationDescriptionAdapter;
import com.example.myapplication.client.GeoClient;
import com.example.myapplication.client.PlacesClient;
import com.example.myapplication.client.WeatherClient;
import com.example.myapplication.model.*;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final LocationDescriptionAdapter adapter = new LocationDescriptionAdapter();
    private final FeatureAdapter featureAdapter = new FeatureAdapter();
    private Subscription subscription;
    LinearLayout viewLocationWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("HERE");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        viewLocationWeather = findViewById(R.id.view_location_weather);

        final ListView listView = findViewById(R.id.list_view_locations);
        listView.setAdapter(adapter);
        final ListView featureList = findViewById(R.id.feature_item);
        featureList.setAdapter(featureAdapter);
        featureList.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView textlat = (TextView) view.findViewById(R.id.text_location_lat);
                String lat = textlat.getText().toString().substring(4);
                final TextView textlon = view.findViewById(R.id.text_location_lng);
                final String lon = textlon.getText().toString().substring(4);
                System.out.println(lat + " " + lon);
                viewLocationWeather.setVisibility(View.VISIBLE);
                getWeather(lat, lon); //todo добавить коорднаты во view
            }
        });


        final EditText editTextUsername = findViewById(R.id.edit_text_name);
        final Button buttonSearch = findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextUsername.getText().toString();
                System.out.println(name);
                if (!TextUtils.isEmpty(name)) {
                    String key = "00f96ba1-a9bf-4d3d-a3af-d33eac754c04";
                    getDestination(key, name);
                }
            }
        });
    }

    //
    private void getPlaces(String key, String lon, String lat) {
        PlacesClient.getInstance()
                .getPlaces(key, lon, lat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlacesDescription>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError()");
                    }

                    @Override
                    public void onNext(PlacesDescription response) {
                        Log.d(TAG, "In onNext()");
                        List<PlacesDescription.Feature> featureList = response.getFeatures();
                        System.out.println(featureList);
                        featureAdapter.setFeatures(featureList);

                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    private void getWeather(String lat, String lon) {
        WeatherClient.getInstance()
                .getWeather(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherDescription>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError()");
                    }

                    @Override
                    public void onNext(WeatherDescription response) {
                        Log.d(TAG, "In onNext()");
                        updateUIWithWeatherData(response, lat, lon);
                    }
                });
    }


    private void getDestination(String key, String name) {
        GeoClient.getInstance()
                .getDestination(key, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LocationResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError()");
                    }

                    @Override
                    public void onNext(LocationResponse response) {
                        Log.d(TAG, "In onNext()");
                        List<LocationDescription> locationsList = response.getHits();
                        adapter.setLocationDescriptions(locationsList);
                    }
                });
    }

    private void updateUIWithWeatherData(WeatherDescription weatherData, String lat, String lon) {
        View weatherDisplayLayout = getLayoutInflater().inflate(R.layout.weather_view, viewLocationWeather, false);
        if (viewLocationWeather != null) {
            viewLocationWeather.removeAllViews();
            viewLocationWeather.addView(weatherDisplayLayout);
        } else {
            Log.e(TAG, "viewLocationWeather is null");
        }

        TextView textLocationName = viewLocationWeather.findViewById(R.id.text_location_name);
        TextView textWeatherMain = viewLocationWeather.findViewById(R.id.text_location_weather_main);
        TextView textWeatherDescription = viewLocationWeather.findViewById(R.id.text_location_weather_description);
        TextView textLocationLon = viewLocationWeather.findViewById(R.id.text_location_lon);
        TextView textLocationLat = viewLocationWeather.findViewById(R.id.text_location_lat);
        textLocationName.setText(weatherData.getName());
        textLocationLon.setText(lon);
        textLocationLat.setText(lat);
        TextView textWeatherTemp = viewLocationWeather.findViewById(R.id.text_location_weather_temp);
        textWeatherTemp.setText(weatherData.getMain().getTemp().toString());

        List<Weather> weatherList = weatherData.getWeather();
        if (weatherList != null && !weatherList.isEmpty()) {
            Weather firstWeather = weatherList.get(0);
            textWeatherMain.setText("Main weather: " + firstWeather.getMain());
            textWeatherDescription.setText("Description: " + firstWeather.getDescription());
        } else {
            textWeatherMain.setText("No weather data available");
            textWeatherDescription.setText("");
        }
        final Button buttonSearchPlaces = viewLocationWeather.findViewById(R.id.button_search_places);
        buttonSearchPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HELLO");
                String key = "5ae2e3f221c38a28845f05b66b2ebd0c0a4a7428f0803525b45f11d8";
                TextView textLat = viewLocationWeather.findViewById(R.id.text_location_lat);
                TextView textLon = viewLocationWeather.findViewById(R.id.text_location_lon);
                String lat = textLat.getText().toString();
                String lon = textLon.getText().toString();
                ListView listView = findViewById(R.id.list_view_locations);
                listView.setVisibility(View.GONE);
                final ListView featureListView = findViewById(R.id.feature_item);
                featureListView.setVisibility(View.VISIBLE);
                viewLocationWeather.setVisibility(View.GONE);
                getPlaces(key, lon, lat);
            }
        });
    }
}