package com.cs407.zoomfoods;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
//import com.cs407.zoomfoods.databinding.ActivityReminderBinding;
import com.cs407.zoomfoods.database.DBService;
import com.cs407.zoomfoods.database.FoodAppDatabase;
import com.cs407.zoomfoods.database.entities.WaterLog;
import com.cs407.zoomfoods.databinding.ActivityWaterBinding;
import com.cs407.zoomfoods.services.UserSessionService;

public class ActivityWater extends AppCompatActivity {
    private double current_total = 0;
    private int current_progress = 0;
    private Button selectedAmountButton = null;
    private Button selectedTypeDrinkButton = null;
    private final String sharedkey = "TodayDrank";
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private ImageView add_btn;
    private NonScrollListView recordsListView;
    private ProgressBar progressBar;
    private TextView progressText;
    private TextView intake;
    private TextView remaining;
    private Toolbar toolbar;
    private Button drinkAmount1, drinkAmount2, drinkAmount3, drinkAmount4;
    private Button drinkType1, drinkType2, drinkType3, drinkType5, drinkType6, drinkType7;
    private Button quickAdd;
    private ActivityWaterBinding binding;

    long userId;
    private FoodAppDatabase db;

    /*
     * acts as the callback when permission is either granted or refused.
     * We send a toast if the permission is refused.
     * */
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted){
                    Toast.makeText(this, "Please allow all notifications for better user experience", Toast.LENGTH_LONG).show();
                }
            });
    private List<WaterLog> waterLogItems;

    private void amountButtonClicked(Button action_btn){

        //Reset the previous clicked button's color
        if (selectedAmountButton != null){
            selectedAmountButton.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        }
        selectedAmountButton = action_btn;
        action_btn.setBackground(getResources().getDrawable(R.drawable.clicked_btn_bg));
    }

    private void typeButtonClicked(Button action_btn){

        //Reset the previous clicked button's color
        if (selectedTypeDrinkButton != null){

            selectedTypeDrinkButton.setBackground(getResources().getDrawable(R.drawable.typedrink_btn_bg));
        }
        selectedTypeDrinkButton = action_btn;

        action_btn.setBackground(getResources().getDrawable(R.drawable.clicked_btn_bg));
    }

    private void checkLoggedIn() {
        if (userId == -1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initializeViews(){
        add_btn = binding.manualButton;
        recordsListView = binding.recyclerview;
        progressBar = binding.circularProgressbar;
        progressText = binding.progessText;
        intake = binding.tvIntakeValue;
        remaining = binding.tvRemaining;
        toolbar = binding.toolbar;
        drinkAmount1 = binding.addButton1;
        drinkAmount2 = binding.addButton2;
        drinkAmount3 = binding.addButton3;
        drinkAmount4 = binding.addButton4;
        drinkType1 = binding.typeDrink1;
        drinkType2 = binding.typeDrink2;
        drinkType3 = binding.typeDrink3;
        drinkType5 = binding.typeDrink5;
        drinkType6 = binding.typeDrink6;
        drinkType7 = binding.typeDrink7;
        quickAdd = binding.quickAddBtn;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Get service
        UserSessionService userSessionService = UserSessionService.getInstance();
        userId = userSessionService.getUserId();
        checkLoggedIn();
        db = DBService.getAppDatabase();
        initializeViews();
        // Ask for permission for notification
        requestPermission();
        // Set up tool bar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // create notification channel
        createNotificationchannel();
        // readRecords
        refreshWaterLogList();
        // set default alarm
        setDefaultAfterTwoHour();
        // ListView onClickListener
        Context context = getApplicationContext();
        recordsListView.setOnItemClickListener((parent, view, position, id) -> {
            WaterLog selected_item = (WaterLog) parent.getItemAtPosition(position);
            Intent intent = new Intent(context, WaterAddActivity.class);
            Log.i("Information", "selected_item.getAmount(): " + selected_item.getAmount());
            Log.i("Information", "selected_item.getTitle(): " + selected_item.getTitle());
            Log.i("Information", "selected_item.getId(): " + selected_item.getId());
            intent.putExtra("recordId", selected_item.id);
            startActivity(intent);
        });
        drinkAmount1.setOnClickListener(v -> amountButtonClicked(drinkAmount1));
        drinkAmount2.setOnClickListener(v -> amountButtonClicked(drinkAmount2));
        drinkAmount3.setOnClickListener(v -> amountButtonClicked(drinkAmount3));
        drinkAmount4.setOnClickListener(v -> amountButtonClicked(drinkAmount4));
        drinkType1.setOnClickListener(v -> typeButtonClicked(drinkType1));
        drinkType2.setOnClickListener(v -> typeButtonClicked(drinkType2));
        drinkType3.setOnClickListener(v -> typeButtonClicked(drinkType3));
        drinkType5.setOnClickListener(v -> typeButtonClicked(drinkType5));
        drinkType6.setOnClickListener(v -> typeButtonClicked(drinkType6));
        drinkType7.setOnClickListener(v -> typeButtonClicked(drinkType7));
        quickAdd.setOnClickListener(v -> {
            if(selectedAmountButton != null && selectedTypeDrinkButton != null){
                String currentTime = getCurrentTime();
                // Get the title and amount
                String title = selectedTypeDrinkButton.getText().toString();
                String amount = selectedAmountButton.getText().toString().split(" ")[0];
                // add new waterLog
                WaterLog waterLog = new WaterLog();
                waterLog.userId = userId;
                waterLog.time = currentTime;
                waterLog.title = title;
                waterLog.amount = amount;
                try {
                    db.waterLogDao().insertAll(waterLog).get();
                } catch (Exception e) {
                    Log.e("WATER_ADD", "Error saving water log", e);
                }
                refreshWaterLogList();
                // update the clicked buttons
                selectedTypeDrinkButton.setBackground(getResources().getDrawable(R.drawable.typedrink_btn_bg));
                selectedAmountButton.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                selectedTypeDrinkButton = null;
                selectedAmountButton = null;
                //update progress bar
                current_total += Double.parseDouble(amount);
                int total = Math.min((int) current_total, 69);
                current_progress = Math.min((int) ((current_total/69.00)*100), 100);
                int remain = 69 - total;
                //set progress bar
                progressBar.setProgress(current_progress);
                progressText.setText(current_progress+"%");
                intake.setText(((int) current_total)+" fl oz");
                remaining.setText("Today I still need to drink " + remain + " oz");
                // Update SharedPreferences for current_total
                SharedPreferences sharedPreferences = getSharedPreferences("HydrationReminder", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //Log.i("Information", "Current total: " + total  + "-" + current_total);
                editor.putInt(sharedkey, total);
                boolean isCommitSuccessful = editor.commit();
                if (!isCommitSuccessful){
                    Log.i("Information", "Commit failed!!!");
                }
                // Check latestTime and set alarm to 2 hours after that
                setDefaultAfterTwoHour();
            }
            else if (selectedAmountButton == null){
                Toast.makeText(ActivityWater.this, "Please select the amount of your drink", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(ActivityWater.this, "Please select the type of your drink", Toast.LENGTH_LONG).show();
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ActivityWater.this, WaterAddActivity.class);
                startActivity(intent);
            }
        });
        // Progress Bar, percentage text
        for (WaterLog waterLogItem : waterLogItems){
            current_total += Double.parseDouble(waterLogItem.getAmount());
        }
        int total = Math.min((int) current_total, 69);
        current_progress = Math.min((int) ((current_total/69.00)*100), 100);
        int remain = 69 - total;
        progressBar.setProgress(current_progress);
        progressText.setText(current_progress+"%");
        intake.setText(((int) current_total)+" fl oz");
        remaining.setText("Today I still need to drink " + remain + " oz");

        // Update SharedPreferences for current_total
        SharedPreferences sharedPreferences = getSharedPreferences("HydrationReminder", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Convert the double to its 'raw long bits' equivalent and store that long.
        // When you're reading the value, convert back to double.
        editor.putInt(sharedkey, total);
        boolean isCommitSuccessful = editor.commit();
        Log.i("Information", String.valueOf(isCommitSuccessful));
        if (!isCommitSuccessful){
            Log.i("Information", "Commit failed!!!");
        }
    }

    private void refreshWaterLogList() {
        try {
            waterLogItems = db.waterLogDao().findAllByUserId(userId).get();
            ArrayAdapter<WaterLog> adapter = new CustomListAdapter(this, waterLogItems);
            recordsListView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("WATER_ADD", "Error fetching water log", e);
        }
    }

    private void createNotificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "hydrationReminderChannel";
            String description = "Channel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("hydration", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.i("Information", "Channel created");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_from_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();
        if (itemId == R.id.main_action_add_water) {
            Intent intent =new Intent(ActivityWater.this, WaterAddActivity.class);
            startActivity(intent);
            return true;
        }
        else if (itemId == R.id.main_set_menu) {
            Intent intent = new Intent(ActivityWater.this, ReminderActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getCurrentTime(){
        Calendar currentDate = Calendar.getInstance();
        int hour = currentDate.get(Calendar.HOUR_OF_DAY);
        int min = currentDate.get(Calendar.MINUTE);
        String current_hour = String.valueOf(hour);
        String current_min = String.valueOf(min);
        if(hour < 10) current_hour = "0"+current_hour;
        if(min < 10) current_min = "0"+current_min;
        return current_hour+" : "+current_min;
    }

    private void requestPermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            // Notification Permission not required till Android 13 (Tiramisu)
            return;
        }
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
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

    private ArrayList<String> findLatestDrinkTime(){
        LocalTime currentLatestTime = LocalTime.parse("00:00");
        String checked = "0";
        for(WaterLog waterLogItem : waterLogItems){
            String timeItem = waterLogItem.getTime().replace(" ", "");
            if(LocalTime.parse(timeItem).isAfter(currentLatestTime)){
                currentLatestTime = LocalTime.parse(timeItem);
                checked = "1";
            }
        }
        ArrayList<String> res = new ArrayList<>();
        res.add(currentLatestTime.toString());
        res.add(checked);
        return res;
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

    private void setDefaultAfterTwoHour() {
        ArrayList<String> lastDrinkTimeList = findLatestDrinkTime();
        String lastDrinkTime = lastDrinkTimeList.get(0);
        String checked = lastDrinkTimeList.get(1);
        if (checked == "1") {
            Integer lastLatestHour = Integer.parseInt(lastDrinkTime.split(":")[0].trim());
            Integer lastLatestMin = Integer.parseInt(lastDrinkTime.split(":")[1].trim());
            Integer toSetHour = lastLatestHour + 2; // if the time is later than 22:00, the alarm needs to set it to the following day
            if (toSetHour > 23) toSetHour = toSetHour - 24;
            cancelAlarm(-2);
            setAlarm(-2, toSetHour, lastLatestMin); // set the alarm
            Log.i("Information", "From ActivityWater, the latestDrinkTime: " + lastDrinkTime);
        }
    }
}