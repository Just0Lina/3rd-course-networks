package com.example.myapplication.model;

import java.util.List;

public class WeatherDescription {
    private final String name;
    private final List<Weather> weather;
    private final Main main;
    public String getName() {
        return name;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }


    public WeatherDescription(String name, List<Weather> weather, Main main) {
        this.name = name;
        this.weather = weather;
        this.main = main;
    }





    public class Main {
        private final Double temp;
        private final Double feelsLike;
        private final Double tempMin;
        private final Double tempMax;
        private final Integer pressure;
        private final Integer humidity;

        public Double getTemp() {
            return temp;
        }

        public Double getFeelsLike() {
            return feelsLike;
        }

        public Double getTempMin() {
            return tempMin;
        }

        public Double getTempMax() {
            return tempMax;
        }

        public Integer getPressure() {
            return pressure;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public Main(Double temp, Double feelsLike, Double tempMin, Double tempMax, Integer pressure, Integer humidity) {
            this.temp = temp;
            this.feelsLike = feelsLike;
            this.tempMin = tempMin;
            this.tempMax = tempMax;
            this.pressure = pressure;
            this.humidity = humidity;
        }
    }


}
