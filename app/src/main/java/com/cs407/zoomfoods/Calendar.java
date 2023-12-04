package com.cs407.zoomfoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

public class Calendar extends AppCompatActivity {
    CalendarView calendarView;
    TextView dateView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = (CalendarView) findViewById(R.id.calendar); //getting reference to calendar view
        dateView = (TextView) findViewById(R.id.dateView);

        //Add listener
        calendarView
                .setOnDateChangeListener(
                        new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                                //Store date
                                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                                //display date in textView
                                dateView.setText(date);
                            }
                        }
                );
    }
}