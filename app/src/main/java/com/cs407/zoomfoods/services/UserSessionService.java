package com.cs407.zoomfoods.services;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionService {
    private static final String PREFERENCES_FILE = "app_preferences";
    private static final String USER_ID_KEY = "userId";
    private static final String LATEST_DRINK_TIME = "latestDrinkTime";
    private SharedPreferences sharedPreferences;

    // Holder class for lazy initialization
    private static class Holder {
        private static final UserSessionService INSTANCE = new UserSessionService();
    }

    private UserSessionService() {
    }

    public void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        }
    }

    public static UserSessionService getInstance() {
        return Holder.INSTANCE;
    }

    public long getUserId() {
        return sharedPreferences.getLong(USER_ID_KEY, -1); // Returns -1 if no value is found
    }

    // Method to write the userId to SharedPreferences
    public void setUserId(long userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(USER_ID_KEY, userId);
        editor.apply(); // or editor.commit() if you need the write to be synchronous
    }

    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_ID_KEY); // Remove only the userId
        // Or use editor.clear() to remove all data in SharedPreferences
        editor.apply(); // or editor.commit() for synchronous write
    }

    public String getLatestDrinkTime(){
        return sharedPreferences.getString(LATEST_DRINK_TIME, "00:00");
    }

    public void setLatestDrinkTime(String latestDrinkTime){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LATEST_DRINK_TIME, latestDrinkTime);
        editor.apply();
    }

}

