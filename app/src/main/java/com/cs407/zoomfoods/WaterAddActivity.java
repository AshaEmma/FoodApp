package com.cs407.zoomfoods;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.Toolbar;

import com.cs407.zoomfoods.R.layout;

import java.util.Calendar;
import java.util.Objects;

import com.cs407.zoomfoods.database.DBService;
import com.cs407.zoomfoods.database.FoodAppDatabase;
import com.cs407.zoomfoods.database.entities.WaterLog;
import com.cs407.zoomfoods.services.UserSessionService;

public class WaterAddActivity extends AppCompatActivity {
    AppCompatSeekBar seekBar;
    EditText amountNumb; //"amount"
    EditText drink_title; //"title"
    ImageView pencil_btn;
    TextView time_view; //"replenished"
    ImageView add_btn;
    ImageView delete_btn;
    Toolbar toolbar;
    private int recordId = -1;
    private FoodAppDatabase db;
    long userId;
    private WaterLog currentWaterLog;

    /*
     *  Update the seekbar based on users's input
     * */
    private void updateSeekBarBasedOnInput(String input) {
        double enteredValue = Double.parseDouble(input);
        if (enteredValue > 32.0) enteredValue = 32.0;
        int checkentered = (int) enteredValue;
        int progress = (int) (enteredValue / 0.32);
        seekBar.setProgress(progress);
        if (enteredValue != (double) checkentered) {
            String text_toset = String.format("%.2f", enteredValue);
            amountNumb.setText(text_toset);
        } else amountNumb.setText(checkentered + "");
    }

    private void initializeView() {
        seekBar = findViewById(R.id.slider);
        amountNumb = findViewById(R.id.number);
        pencil_btn = findViewById(R.id.pencil);
        time_view = findViewById(R.id.time_button);
        add_btn = findViewById(R.id.add_btn);
        delete_btn = findViewById(R.id.delete_btn);
        drink_title = findViewById(R.id.input_text);
        toolbar = findViewById(R.id.toolbar_add_water);
    }

    /*
     * Then the soft keyboard is explicitly shown, allowing the user to type in a value.
     * */
    private void showKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private String getCurrentTime() {
        Calendar currentDate = Calendar.getInstance();
        int hour = currentDate.get(Calendar.HOUR_OF_DAY);
        int min = currentDate.get(Calendar.MINUTE);
        String current_hour = "" + hour;
        String current_min = "" + min;
        if (hour < 10) current_hour = "0" + current_hour;
        if (min < 10) current_min = "0" + current_min;
        return current_hour + " : " + current_min;
    }

    private void openDialog() {
        //Get the current time
        Calendar currentDate = Calendar.getInstance();
        int hour = currentDate.get(Calendar.HOUR_OF_DAY);
        int min = currentDate.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            String hour_string = "";
            String minute_string = "";

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay < 10) hour_string = "0" + hourOfDay;
                else hour_string = "" + hourOfDay;
                if (minute < 10) minute_string = "0" + minute;
                else minute_string = "" + minute;
                time_view.setText(hour_string + " : " + minute_string);
            }
        }, hour, min, true);

        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(WaterAddActivity.this, ActivityWater.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkLoggedIn() {
        UserSessionService userSessionService = UserSessionService.getInstance();
        if (userId == -1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_water_add);
        UserSessionService userSessionService = UserSessionService.getInstance();
        userId = userSessionService.getUserId();
        checkLoggedIn();
        db = DBService.getAppDatabase();

        // find view
        initializeView();
        // toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //initialize database
        Context context = getApplicationContext();

        // Initialize class variable noteid with the value from intent
        recordId = getIntent().getIntExtra("recordId", -1);
        //Log.i("Information", "recordId" + recordId);
        if (recordId != -1) {
            try {
                currentWaterLog = db.waterLogDao().findById(recordId).get();
                String time = currentWaterLog.time;
                String amount = currentWaterLog.amount;
                String title = currentWaterLog.title;

                time_view.setText(time);
                drink_title.setText(title);
                amountNumb.setText(amount);
                Log.i("Information", amount);
                updateSeekBarBasedOnInput(amount);
            } catch (Exception e) {
                Log.e("WATER_ADD", "Error loading water log", e);
            }
        } else {
            currentWaterLog = new WaterLog();
            seekBar.setProgress(100);
            seekBar.setMax(100);
            seekBar.setProgress(0);
            delete_btn.setEnabled(false);
            Log.i("Information", "current: " + seekBar.getProgress());
        }

        // Add button
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountNumb.getText().toString();
                String time = time_view.getText().toString();
                String title = drink_title.getText().toString();
                currentWaterLog.userId = userId;
                currentWaterLog.time = time;
                currentWaterLog.title = title;
                currentWaterLog.amount = amount;

                try {
                    if (recordId == -1) {
                        db.waterLogDao().insertAll(currentWaterLog).get();
                    } else {
                        //Log.i("Information update time", time);
                        //Log.i("Information update amount", amount);
                        //Log.i("Information update title", title);
                        //Log.i("Information update recordId", String.valueOf(recordIndex));
                        db.waterLogDao().updateLog(currentWaterLog).get();
                    }
                } catch (Exception e) {
                    Log.e("WATER_ADD", "Error saving water log", e);
                }

                Intent intent = new Intent(WaterAddActivity.this, ActivityWater.class);
                startActivity(intent);

            }
        });
        // delete button
        delete_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.waterLogDao().delete(currentWaterLog);
                Intent intent = new Intent(WaterAddActivity.this, ActivityWater.class);
                startActivity(intent);
            }
        });
        //seek bar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amountNumb.setVisibility(View.VISIBLE);
                double true_progress = progress * 0.32;
                if ((int) true_progress == (progress * 0.32)) {
                    amountNumb.setText(((int) true_progress) + "");
                } else {
                    amountNumb.setText(String.format("%.2f", true_progress) + "");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //
        amountNumb.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updateSeekBarBasedOnInput(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
        //amountNumb onClick function
        amountNumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open keyboard when user taps on the amountNumb
                //amountNumb.requestFocus();
                showKeyboard(amountNumb);
            }
        });

        //pencil_btn onClick function
        pencil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountNumb.requestFocus(); //The EditText is given focus using requestFocus().
                showKeyboard(amountNumb);
            }
        });

        // Time setting
        time_view.setText(getCurrentTime());
        time_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void resetSeekBar() {
        seekBar.setProgress(100);
        seekBar.setMax(100);
        seekBar.setProgress(0);
        Log.i("Information", "current: " + seekBar.getProgress());
    }
}
