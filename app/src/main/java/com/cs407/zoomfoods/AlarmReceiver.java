package com.cs407.zoomfoods;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cs407.zoomfoods.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class AlarmReceiver extends BroadcastReceiver {
    private String sharedkey = "TodayDrank";
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "786003ab359b90f85fa5ff9aecc3d2a4";
    DecimalFormat df = new DecimalFormat("#.##") ;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String weatherDescription = "";
    @Override
    public void onReceive(Context context, Intent intent) {
        // initialize mFusedLocationProviderClient
        mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context);
        // Retrieve the reminder_ID from the intent
        int notificationID = intent.getIntExtra("ReminderID", -1);
        String timeset = intent.getStringExtra("SelectedTime");
        String reminderMessage = "";
        // -3 is the code for weather checks
        if (notificationID == -3) {
            Log.i("Information", "From Alarm Receiver, notification id is -3");
            getLastLocation(context, new WeatherCallback() {
                @Override
                public void onWeatherResult(Context context, double temperature, String description) {
                    //Handle the weather data here
                    weatherDescription = "Weather, " + "Temp: " + df.format(temperature) + ", Description: " + description;
                    Log.i("Information", weatherDescription);
                    showNotification(context, notificationID, weatherDescription);
                }
            });
        }
        // Default Reminder: Need to drink after 2 hours
        else if (notificationID == -2){
            Log.i("Information", timeset + "is 2 hours after latest drink");
            reminderMessage = "It has been 2 hours since you last drank. Just ease your mind and drink more !!!";
            showNotification(context, notificationID, reminderMessage);
        }
        // Default Reminder: Morning
        else if (notificationID == -1){
            Log.i("Information", "Default Reminder Received!!!");
            reminderMessage = "Good morning! Hope you have a good day and being hydrated!";
            showNotification(context, notificationID, reminderMessage);
        }
        else {
            // SharedPreference
            SharedPreferences sharedPreferences = context.getSharedPreferences("HydrationReminder", Context.MODE_PRIVATE);
            int currentAmount = sharedPreferences.getInt(sharedkey, 0);
            Log.i("Information", timeset + " received with " + currentAmount);
            // Reminder String
            reminderMessage = "Today you have consumed " + currentAmount + " fl oz amount of water. Keep drinking to get hydrated.";
            showNotification(context, notificationID, reminderMessage);
        }
    }
    private void showNotification(Context context, int notificationID, String reminderMessage){
        // When the receiver gets an alarm event, it creates an Intent to open ActivityWater.
        Intent i = new Intent(context, ActivityWater.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationID, i, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "hydration")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Zoom Food Hydration")
                .setContentText(reminderMessage)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        // Check
        Log.i("Information", reminderMessage);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            notificationManagerCompat.notify(notificationID, builder.build());
        }
        catch (Exception e){
            Log.i("Information", "ReminderID = -1"+ e.getStackTrace());
        }
    }

    public void getWeatherDetails(Context context, double latitude, double longitude,  WeatherCallback callback){
        String tempUrl = url + "?lat=" + latitude + "&lon=" + longitude + "&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Information", response);
                String output = "";
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    // description
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    // Temperature information
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    // System information
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");
                    Log.i("Information", "Where we are " + cityName + ", " + countryName);
                    Log.i("Information", "Current temp" + temp);
                    Log.i("Information", "Current feelsLike: " + feelsLike);
                    Log.i("Information", "Description for current weather: " + description);
                    callback.onWeatherResult(context, temp, description);
                }
                catch(JSONException e){
                    e.printStackTrace();
                    Log.i("Information", "Problem: " + e.getMessage(), e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString().trim(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void getLastLocation(Context context, WeatherCallback callback){
        //Check if permission granted
        int permission = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);

        // If not, ask for it
        if (permission == PackageManager.PERMISSION_DENIED){
            Log.i("Imformation", "From AlarmReceiver getLastLocation, access denied!");
        }
        // if permission granted, display marker at current location
        else{
            Log.i("Information", "From Alarm Receiver, start to find last known location");
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            Log.i("Information", location.getLatitude() + " " + location.getLongitude());
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Log.i("Information", "Current Location" + latitude + ", " + longitude);
                            getWeatherDetails(context, latitude, longitude, callback);
                        }
                    })
                    .addOnFailureListener(e ->
                            {
                                Log.i("Information", "LocationError: ");
                            });
        }
        Log.i("Information", "There is no current location");
    }
}
