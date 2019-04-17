package com.example.myapplication;

import org.json.JSONObject;

public interface VolleyResponseListener {

    void onError(String message);

    void onResponse(JSONObject response);
}
