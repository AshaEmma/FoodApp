package com.cs407.zoomfoods;

import android.content.Context;

import com.cs407.zoomfoods.database.entities.ReminderItem;

public interface WeatherCallback {
    void onWeatherResult(Context context, double temperature, String description);
}
