package com.cs407.zoomfoods;


import static java.text.DateFormat.getDateInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.text.DateFormat;


public class CalendarView extends AppCompatActivity {
    android.widget.CalendarView calendar;
    String date;
    TextView dateView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        calendar = findViewById(R.id.calendar); //getting reference to calendar view
        dateView = findViewById(R.id.dateView);

        long today = calendar.getDate();
        date = DateFormat.getDateInstance(DateFormat.LONG).format(today);
        dateView.setText(date);

        // Add Listener in calendar
        calendar

                .setOnDateChangeListener(
                        new android.widget.CalendarView.OnDateChangeListener() {
                            @Override

                            // get the value of DAYS, MONTH, YEARS
                            public void onSelectedDayChange(
                                    @NonNull android.widget.CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {

                                String monthString;

                                switch(month + 1){
                                    // Case
                                    case 1:
                                        monthString = "January";
                                        break;

                                    // Case
                                    case 2:
                                        monthString = "February";
                                        break;

                                    // Case
                                    case 3:
                                        monthString = "March";
                                        break;

                                    // Case
                                    case 4:
                                        monthString = "April";
                                        break;

                                    // Case
                                    case 5:
                                        monthString = "May";
                                        break;

                                    // Case
                                    case 6:
                                        monthString = "June";
                                        break;

                                    // Case
                                    case 7:
                                        monthString = "July";
                                        break;
                                    // Case
                                    case 8:
                                        monthString = "August";
                                        break;
                                    // Case
                                    case 9:
                                        monthString = "September";
                                        break;
                                    // Case
                                    case 10:
                                        monthString = "October";
                                        break;
                                    // Case
                                    case 11:
                                        monthString = "November";
                                        break;
                                    // Case
                                    case 12:
                                        monthString = "December";
                                        break;

                                    // Default case
                                    default:
                                        monthString = "Invalid day";
                                }

                                // Store the value of date with
                                // format in String type Variable
                                // Add 1 in month because month
                                // index is start with 0
                                String Date
                                        = monthString + " " + dayOfMonth + ", " + year;

                                // set this date in TextView for Display
                                dateView.setText(Date);
                            }
                            public void onDoubleTap(MotionEvent e){
                                //TODO: add note taker
                            }


                        });



    }
}