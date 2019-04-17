package com.example.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {
    private String currentCity = "Seattle";
    private ListView listView;
    private WeatherItemAdapter weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        startUpdateCurrentWeather();
        setCityText();
        startUpdateDate();
        addSubmitListener();

        updateForecast();
    }

    private void updateWeather() {
        WeatherService.getInstance(this).fetchWeather(currentCity, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("Error", message);
            }

            @Override
            public void onResponse(JSONObject response) {
                TextView currentTempTextView = findViewById(R.id.currentTempText);
                TextView minMaxTempTextView = findViewById(R.id.minMaxText);

                JSONObject main = (JSONObject) response.opt("main");
                String currentTemp = main.optString("temp", "");
                String minTemp = main.optString("temp_min", "");
                String maxTemp = main.optString("temp_max", "");

                String currentTempText = getFormattedTemp(currentTemp);
                String minMaxText = "Max " + getFormattedTemp(maxTemp) + " | Min " + getFormattedTemp(minTemp);

                currentTempTextView.setText(currentTempText);
                minMaxTempTextView.setText(minMaxText);
                setCityText();
            }
        });
    }

    private void updateForecast() {
        final Context that = this;

        WeatherService.getInstance(this).fetchForecast(currentCity, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("Error", message);
            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = (JSONArray) response.opt("list");
                    listView = findViewById(R.id.forecastListView);
                    ArrayList<WeatherItem> arrayList = new ArrayList<>();

                    for(int i = 0; i < list.length(); i++) {
                        WeatherItem item = new WeatherItem((JSONObject) list.get(i));
                        arrayList.add(item);
                    }

                    weatherAdapter = new WeatherItemAdapter(that, arrayList);
                    listView.setAdapter(weatherAdapter);
                } catch (Exception e) {
                    Log.w("oops", e);
                }
            }
        });
    }

    private void setCityText() {
        TextView cityTextView = findViewById(R.id.cityText);
        cityTextView.setText(WordUtils.capitalize(currentCity));
    }

    private EditText getCityInput() {
        return findViewById(R.id.cityInput);
    }

    private void addSubmitListener() {
        Button clickButton = findViewById(R.id.submitButton);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText textInput = getCityInput();

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textInput.getWindowToken(), 0);

                currentCity = textInput.getText().toString();
                textInput.getText().clear();
                updateWeather();
                updateForecast();
            }
        });
    }

    private void startUpdateDate() {
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                updateCurrentDate();
            }
        },0,5000);
    }

    private void startUpdateCurrentWeather() {
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                updateWeather();
            }
        },0,60000);
    }

    private void updateCurrentDate() {
        TextView dateText = findViewById(R.id.dateTextView);
        SimpleDateFormat format = new SimpleDateFormat("MMMM d, h:mm a", Locale.US);
        String dateString = format.format(new Date());
        dateText.setText(dateString);
    }

    private String getFormattedTemp(String tempString) {
        int i = Math.round(parseFloat(tempString));
        return String.valueOf(i) + "º";
    }

}
