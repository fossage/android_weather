package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.Long.parseLong;

class WeatherItem {
    String description;
    String iconCode;
    String date;
    String minTemp;
    String maxTemp;

    WeatherItem(JSONObject weatherData) {
        try {
            JSONArray weatherItem = (JSONArray) weatherData.get("weather");
            JSONObject weather = (JSONObject) weatherItem.get(0);
            JSONObject temp = (JSONObject) weatherData.get("temp");

            String dt = weatherData.getString("dt");
            SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM d", Locale.US);
            String dateString = format.format(new Date(parseLong(dt + "000")));

            this.description = (String) weather.get("description");
            this.iconCode = (String) weather.get("icon");
            this.date = dateString;
            this.minTemp = getFormattedTemp((double)temp.get("min"));
            this.maxTemp = getFormattedTemp((double)temp.get("max"));
        } catch(Exception e) {
            //
        }
    }

    private String getFormattedTemp(double temp) {
        int i = Math.round((float)temp);
        return String.valueOf(i) + "º";
    }
}
