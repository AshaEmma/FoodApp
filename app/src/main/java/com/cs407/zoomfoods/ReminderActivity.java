package com.cs407.zoomfoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.cs407.zoomfoods.database.DBService;
import com.cs407.zoomfoods.database.FoodAppDatabase;
import com.cs407.zoomfoods.database.entities.ReminderItem;
import com.cs407.zoomfoods.services.UserSessionService;

public class ReminderActivity extends AppCompatActivity implements SelectListener{
    private Toolbar toolbar;
    private RecyclerView ReminderRecyclerView;
    private ReminderAdapter reminderAdapter;
    private ReminderDBHelper reminderDBHelper;
    private ImageView addReminderBtn;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    long userId;
    private FoodAppDatabase db;

    private List<ReminderItem> reminderItemArrayList;

    private void initializeViews(){
        toolbar = findViewById(R.id.toolbar_activity_reminder);
        ReminderRecyclerView = findViewById(R.id.recyclerReminderview);
        addReminderBtn = findViewById(R.id.add_reminder_button);
    }

    private void loadReminders(){
        reminderItemArrayList = reminderDBHelper.readReminder(userId);
        //Log.i("Information", "size of list: " + reminderItemArrayList.size());
        //reminderAdapter
        reminderAdapter = new ReminderAdapter(reminderItemArrayList, this);
        ReminderRecyclerView.setAdapter(reminderAdapter);
    }
    @Override
    protected void onResume(){
        super.onResume();
        loadReminders();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        UserSessionService userSessionService = UserSessionService.getInstance();
        userId = userSessionService.getUserId();
        checkLoggedIn();
        db = DBService.getAppDatabase();

        initializeViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ReminderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Database
        reminderDBHelper = new ReminderDBHelper(db);

        loadReminders();
        addReminderBtn.setOnClickListener(v -> openDialog(null,-1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_setting_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            openDialog(null,-1);
            return true;
        }
        else if (itemId == R.id.action_default_settings) {
            Toast.makeText(this, "Added default action clicked", Toast.LENGTH_LONG).show();
            setDefaultAlarm();
            return true;
        }
        else if (itemId == android.R.id.home){
            Intent intent = new Intent(ReminderActivity.this, ActivityWater.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDialog(ReminderItem reminderItem, int position){
        //Get the current time
        Calendar currentDate = Calendar.getInstance();
        int hour = currentDate.get(Calendar.HOUR_OF_DAY);
        int min = currentDate.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            String hour_string = "";
            String minute_string = "";
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay < 10) hour_string = "0"+ hourOfDay;
                else hour_string = ""+ hourOfDay;
                if(minute < 10)  minute_string = "0" + minute;
                else minute_string = "" + minute;
                String final_time = hour_string+" : "+minute_string;
                //reminderItem.setTime(final_time);
                // add to database
                if(position == -1){
                    // reminderItem is currently null, since we add another reminder
                    reminderDBHelper.saveReminder(userId, final_time, true);
                    List<ReminderItem> newReminderList = reminderDBHelper.readReminder(userId);
                    ReminderItem lastReminder = newReminderList.get(newReminderList.size()-1);
                    setAlarm(lastReminder.getId(), hourOfDay, minute);
                }
                else{
                    // Update the exist reminder
                    cancelAlarm(reminderItem.getId());
                    reminderItem.time = final_time;
                    reminderDBHelper.updateReminder(reminderItem);
                    setAlarm(reminderItem.getId(), hourOfDay, minute);
                }
                // Notify the adapter about the data changed
                loadReminders();


            }
        }, hour, min, true);

        dialog.show();
    }


    @Override
    public void onItemClicked(ReminderItem reminderItem, int position) {
        openDialog(reminderItem, position);
    }

    @Override
    public void onSwitchClicked(ReminderItem reminderItem, int position) {
        if (!reminderItem.getIsActive()){
            String oldTime = reminderItem.getTime();
            int oldHourTime = Integer.parseInt(oldTime.split(":")[0].trim());
            int oldMinsTime = Integer.parseInt(oldTime.split(":")[1].trim());
            // set the calendar
            setAlarm(reminderItem.getId(), oldHourTime, oldMinsTime);
        }
        // if reminder is true, it will be set to false
        if (reminderItem.getIsActive()){
            cancelAlarm(reminderItem.getId());
        }
        reminderDBHelper.updateReminder(reminderItem);
        loadReminders();
    }

    @Override
    public void onItemLongClicked(ReminderItem reminderItem, int position) {
        Log.i("Information", "Long pressed !!");
        showOptionsDialog(reminderItem, position);
    }

    private void showOptionsDialog(ReminderItem reminderItem, int position){
        CharSequence[] options = new CharSequence[]{"Delete","Update"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    // Delete option
                    cancelAlarm(reminderItem.getId());
                    reminderDBHelper.deleteReminder(reminderItem);
                    loadReminders();
                }
                else if (which == 1){
                    // Update option
                    openDialog(reminderItem, position);
                }
            }
        });
        builder.show();
    }

    private void setAlarm(int requestCode, int selectedHour, int selectedMins){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("ReminderID", requestCode);
        intent.putExtra("SelectedTime", selectedHour+":"+selectedMins);
        pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // set the calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMins);
        if (Calendar.getInstance().after(calendar)){
            Log.i("Information", "Date set behind");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(this,"Reminder set successfully", Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
            Log.i("Information", "Build version is larger");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager != null && !alarmManager.canScheduleExactAlarms()){
                Log.i("Information", "Cannot set exact alarm");
                Intent in = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(in);
            }
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        Log.i("Information", "Alarmed being set at " + selectedHour + ":" + selectedMins);
    }

    private void cancelAlarm(int idReminderCode){
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("ReminderID", idReminderCode);
        pendingIntent = PendingIntent.getBroadcast(this, idReminderCode, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_LONG).show();
    }

    private void setDefaultAlarm(){
        // Set Default notification in every day (drink typ)
        int DefaultMorningHour = 8;
        int DefaultMorningMin= 0;
        setAlarm(-1, DefaultMorningHour, DefaultMorningMin);
    }

    private void checkLoggedIn() {
        if (userId == -1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}