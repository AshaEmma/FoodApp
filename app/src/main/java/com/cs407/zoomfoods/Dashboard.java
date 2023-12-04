package com.cs407.zoomfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button profile = findViewById(R.id.btnGotoViewProfile);
        Button waterIntake = findViewById(R.id.btnGotoWaterIntake);
        Button calendar = findViewById(R.id.btnGoToCalendar);
        Button fridge = findViewById(R.id.btnGoToFridge);
        Button foodIntake = findViewById(R.id.btnGoToFoodIntake);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileActivity();
            }
        });

        waterIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWaterActivity();
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendarActivity();
            }
        });

        fridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFridgeActivity();
            }
        });

        foodIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFoodActivity();
            }
        });
    }

    public void openProfileActivity(){
        Intent loginIntent = new Intent(Dashboard.this, Profile.class);
        startActivity(loginIntent);
    }

    public void openWaterActivity(){
        //TODO: navigate to water activity
    }

    public void openCalendarActivity(){
        Intent loginIntent = new Intent(Dashboard.this, Calendar.class);
        startActivity(loginIntent);
    }

    public void openFridgeActivity(){
       //TODO: navigate to fridge activity
    }

    public void openFoodActivity(){
        //TODO: navigate to food activity
    }
}