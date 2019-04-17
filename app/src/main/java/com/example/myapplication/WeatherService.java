package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

class WeatherService {
    @SuppressLint("StaticFieldLeak")
    private static WeatherService instance;
    private RequestQueue requestQueue;
    @SuppressLint("StaticFieldLeak")
    private static Context ctx;

    private WeatherService(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    static synchronized WeatherService getInstance(Context context) {
        if (instance == null) {
            instance = new WeatherService(context);
        }
        return instance;
    }

    void fetchWeather(String city, final VolleyResponseListener listener) {
        String url = this.assembleUrl("weather", city);
        this.makeRequest(url, listener);
    }

    void fetchForecast(String city, final VolleyResponseListener listener) {
        String url = this.assembleUrl("forecast/daily", city);
        this.makeRequest(url, listener);
    }

    private void makeRequest(String url, final VolleyResponseListener listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.toString());
                    }
                });

        this.getRequestQueue().add(jsonObjectRequest);
    }

    private String assembleUrl(String requestType, String city) {
        return "https://api.openweathermap.org/data/2.5/" + requestType + "/?q=" + city + "&mode=json&units=imperial&appid=c36043dd046f0d256302ccfa30d8c6ad";
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
