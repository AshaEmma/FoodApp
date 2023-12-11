package com.cs407.zoomfoods;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cs407.zoomfoods.R;

public class AlarmReceiver extends BroadcastReceiver {
    private String sharedkey = "TodayDrank";
    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieve the reminder_ID from the intent
        int notificationID = intent.getIntExtra("ReminderID", -1);
        String timeset = intent.getStringExtra("SelectedTime");
        // Default Reminder
        if (notificationID == -1){
            Log.i("Information", "Default Reminder");
            return;
        }
        // SharedPreference
        SharedPreferences sharedPreferences = context.getSharedPreferences("HydrationReminder", Context.MODE_PRIVATE);
        int currentAmount = sharedPreferences.getInt(sharedkey, 0);
        Log.i("Information", timeset + " received with " + currentAmount);
        // Reminder String
        String reminderMessage = "Today you have consumed " + currentAmount + " fl oz amount of water. Keep drinking to get hydrated.";
        // When the receiver gets an alarm event, it creates an Intent to open ActivityWater.
        Intent i = new Intent(context, ActivityWater.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationID, i, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "hydration")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Zoom Food Hydration")
                .setContentText(reminderMessage)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
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
}
