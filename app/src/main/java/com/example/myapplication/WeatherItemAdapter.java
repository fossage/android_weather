package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WeatherItemAdapter extends ArrayAdapter<WeatherItem> {
    private List<WeatherItem> weatherList;

    WeatherItemAdapter(Context context, ArrayList<WeatherItem> weatherItems) {
        super(context, 0, weatherItems);
        weatherList = weatherItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        // Check if an existing view is being reused, otherwise inflate the view
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.weather_item, parent, false);
        }

        // Get the data item for this position
        WeatherItem currentWeatherItem = weatherList.get(position);

        // Lookup view for data population
        TextView dateView = listItem.findViewById(R.id.dateItemView);
        TextView descriptionView = listItem.findViewById(R.id.descriptionText);
        TextView minView = listItem.findViewById(R.id.minText);
        TextView maxView = listItem.findViewById(R.id.maxText);
        ImageView weatherImage = listItem.findViewById(R.id.weatherImage);

        // Populate the data into the template view using the data object
        dateView.setText(currentWeatherItem.date);
        descriptionView.setText(currentWeatherItem.description);
        minView.setText(currentWeatherItem.minTemp);
        maxView.setText(currentWeatherItem.maxTemp);

        // Return the completed view to render on screen
        return listItem;
    }

}
