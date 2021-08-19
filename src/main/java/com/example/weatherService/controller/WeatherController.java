package com.example.weatherService.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherController {
    private OkHttpClient client;
    private Response response;
    private String location;
    String unit;

    // Api key which is needed to call the api. Needed to sign up to https://openweathermap.org/
    private String apiKey = "0a540de93fb373b6913617d7a4efee11";

    // Returns the location searched for
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Returns units of measurement which for this assignment is "metric" as the results should be in celsius only
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /* Retrieves all of the json from the openweathermap api.
       API call: api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}&units={units}
     */
    public JSONObject getWeather() {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q=" + getLocation() +"&appid=" + apiKey + "&units=" + getUnit())
                .build();

        try {
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    // Retrieves "weather" array from JSON
    public JSONArray retrieveWeatherArray() throws JSONException {
        JSONArray weatherArray = getWeather().getJSONArray("weather");
        return weatherArray;
    }

    // Retrieves "main" object from JSON
    public JSONObject retrieveMain() throws JSONException {
        JSONObject main = getWeather().getJSONObject("main");
        return main;
    }
}
