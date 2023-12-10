package com.cs407.zoomfoods.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.ArrayList;
import java.util.Calendar;

public class WaterAdd extends AppCompatActivity {
    private static final String databaseName = "Zoom Foods Database";
    private String username = "username1";
    AppCompatSeekBar seekBar;
    EditText amountNumb;
    EditText drink_title;
    ImageView pencil_btn;
    TextView time_view;
    ImageView add_btn;
    ImageView delete_btn;
    Toolbar toolbar;
    private int recordId = -1;
    private int recordIndex = -1;
    /*
     *  Update the seekbar based on users's input
     * */
    private void updateSeekBarBasedOnInput(String input){
        double enteredValue = Double.parseDouble(input);
        if (enteredValue > 32.0) enteredValue = 32.0;
        int checkentered = (int) enteredValue;
        int progress = (int) (enteredValue / 0.32);
        seekBar.setProgress(progress);
        if (enteredValue != (double) checkentered){
            String text_toset = String.format("%.2f",enteredValue);
            amountNumb.setText(text_toset);
        }
        else amountNumb.setText(checkentered+"");
    }
    private void initializeView(){
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

    private String getCurrentTime(){
        Calendar currentDate = Calendar.getInstance();
        int hour = currentDate.get(Calendar.HOUR_OF_DAY);
        int min = currentDate.get(Calendar.MINUTE);
        String current_hour = ""+hour;
        String current_min = ""+min;
        if(hour < 10) current_hour = "0"+current_hour;
        if(min < 10) current_min = "0"+current_min;
        return current_hour+" : "+current_min;
    }
    private void openDialog(){
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
                time_view.setText(hour_string+" : "+minute_string);
            }
        }, hour, min, true);

        dialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(WaterAdd.this, ActivityWater.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.add_water);
        // find view
        initializeView();
        // toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //initialize database
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        WaterDBHelper dbHelper = new WaterDBHelper(sqLiteDatabase, context);
        ArrayList<Record> records = dbHelper.readRecords(username);
        // Initialize class variable noteid with the value from intent
        recordId = getIntent().getIntExtra("recordId", -1);
        recordIndex = getIntent().getIntExtra("recordIndex", -1);
        //Log.i("Information", "recordId" + recordId);
        if (recordId != -1){
            Record record = records.get(recordId);
            String time = record.getTime();
            String amount = record.getAmount();
            String title = record.getTitle();

            time_view.setText(time);
            drink_title.setText(title);
            amountNumb.setText(amount);
            Log.i("Information", amount);
            updateSeekBarBasedOnInput(amount);
        }
        else{
            seekBar.setProgress(100);
            seekBar.setMax(100);
            seekBar.setProgress(0);
            Log.i("Information", "current: "+ seekBar.getProgress());
        }
        // Add button
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountNumb.getText().toString();
                String time = time_view.getText().toString();
                String title = drink_title.getText().toString();
                if(recordId == -1){
                    dbHelper.saveRecord(username, time, amount, title);
                }
                else {
                    //Log.i("Information update time", time);
                    //Log.i("Information update amount", amount);
                    //Log.i("Information update title", title);
                    //Log.i("Information update recordId", String.valueOf(recordIndex));
                    dbHelper.updateRecord(username, time, amount, title, recordIndex);
                }
                Intent intent = new Intent(WaterAdd.this, ActivityWater.class);
                startActivity(intent);

            }
        });
        // delete button
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("Information", username);
                //Log.i("Information", (recordIndex)+"");
                dbHelper.deleteRecord(username, recordIndex);
                Intent intent = new Intent(WaterAdd.this, ActivityWater.class);
                startActivity(intent);
            }
        });
        //seek bar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amountNumb.setVisibility(View.VISIBLE);
                double true_progress = progress * 0.32;
                if ((int)true_progress == (progress*0.32)){
                    amountNumb.setText(((int) true_progress)+"");
                }
                else{
                    amountNumb.setText(String.format("%.2f", true_progress)+"");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        //
        amountNumb.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    updateSeekBarBasedOnInput(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
        //amountNumb onClick function
        amountNumb.setOnClickListener(new View.OnClickListener(){
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
}
