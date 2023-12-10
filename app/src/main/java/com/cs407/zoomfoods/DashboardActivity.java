package com.cs407.zoomfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button profile = findViewById(R.id.btnGotoViewProfile);
        Button waterIntake = findViewById(R.id.btnGotoWaterIntake);

        Button fridge = findViewById(R.id.btnGoToFridge);
        Button foodIntake = findViewById(R.id.btnGoToFoodIntake);

        profile.setOnClickListener(v -> openProfileActivity());
        waterIntake.setOnClickListener(v -> openWaterActivity());
        fridge.setOnClickListener(v -> openFridgeActivity());
        foodIntake.setOnClickListener(v -> openFoodActivity());
    }

    public void openProfileActivity(){
        Intent viewProfileIntent = new Intent(DashboardActivity.this, CreateProfileActivity.class);
        startActivity(viewProfileIntent);
    }

    public void openWaterActivity(){
        //TODO: navigate to water activity
    }


    public void openFridgeActivity(){
       //TODO: navigate to fridge activity
    }

    public void openFoodActivity(){
        Intent foodActivity = new Intent(DashboardActivity.this, foodTracking.class);
        startActivity(foodActivity);
    }
}