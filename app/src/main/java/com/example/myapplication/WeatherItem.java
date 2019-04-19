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
            this.iconCode = getIconURI((String)weather.get("icon"));
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

    private String getIconURI(String iconCode) {
        if(iconCode.matches("01d")) {
            return "@drawable/sunny";
        }else if(iconCode.matches("01n")) {
            return "@drawable/moon";
        }else if(iconCode.matches("02d|03d|04d")) {
            return "@drawable/partly_sunny";
        } else if(iconCode.matches("02n|03n|04n")) {
            return "@drawable/partly_cloudy_moon";
        }else if(iconCode.matches("10d")) {
            return "@drawable/drizzle";
        }else if(iconCode.matches("09d")) {
            return "@drawable/heavy_rain";
        }else if(iconCode.matches("13d")) {
            return "@drawable/heavy_snow";
        }else if(iconCode.matches("11d")) {
            return "@drawable/lightning";
        } else {
            return "@drawable/thermometer";
        }
    }
}
