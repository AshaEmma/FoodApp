package com.cs407.zoomfoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.cs407.zoomfoods.services.UserSessionService;
import com.cs407.zoomfoods.ActivityWater;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        checkLoggedIn();
        // initialize views
        Button profile = findViewById(R.id.btnGotoViewProfile);
        Button waterIntake = findViewById(R.id.btnGotoWaterIntake);
        Button fridge = findViewById(R.id.btnGoToFridge);
        Button foodIntake = findViewById(R.id.btnGoToFoodIntake);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        //set up tool bar
        setSupportActionBar(toolbar);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        profile.setOnClickListener(v -> openCreateProfileActivity());
        waterIntake.setOnClickListener(v -> openWaterActivity());
        fridge.setOnClickListener(v -> openFridgeActivity());
        foodIntake.setOnClickListener(v -> openFoodActivity());
        //logout.setOnClickListener(v -> logoutDashboard());
    }

    private void checkLoggedIn() {
        UserSessionService userSessionService = UserSessionService.getInstance();
        long userId = userSessionService.getUserId();
        if (userId == -1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void logoutDashboard(){
        UserSessionService userSessionService = UserSessionService.getInstance();
        long userId = userSessionService.getUserId();
        if(userId != -1){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }
    }

    public void openCreateProfileActivity(){
        Intent viewProfileIntent = new Intent(DashboardActivity.this, CreateProfileActivity.class);
        startActivity(viewProfileIntent);
    }

    public void openDisplayProfileActivity(){
        Intent viewProfileIntent = new Intent(DashboardActivity.this, DisplayProfileActivity.class);
        startActivity(viewProfileIntent);
    }

    public void openWaterActivity(){
        //TODO: navigate to water activity
        Intent foodActivity = new Intent(DashboardActivity.this, ActivityWater.class);
        startActivity(foodActivity);
    }


    public void openFridgeActivity(){
       //TODO: navigate to fridge activity
    }

    public void openFoodActivity(){
        Intent foodActivity = new Intent(DashboardActivity.this, foodTracking.class);
        startActivity(foodActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();
        if(itemId == R.id.profile){
            Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
            return true;
        }else if(itemId == R.id.viewProfile){
            openDisplayProfileActivity();
            return true;
        } else if(itemId == R.id.updateProfile){
            Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
            openCreateProfileActivity();
            return true;
        }else if (itemId == R.id.waterIntake){
            Toast.makeText(this, "Item 3 selected", Toast.LENGTH_SHORT).show();
            openWaterActivity();
            return true;
        }else if (itemId == R.id.fridge){
            Toast.makeText(this, "Sub item 1 selected", Toast.LENGTH_SHORT).show();
            openFridgeActivity();
            return true;
        }else if(itemId == R.id.foodRecord){
            Toast.makeText(this, "Sub item 2 selected", Toast.LENGTH_SHORT).show();
            openFoodActivity();
            return true;
        }
        else if(itemId == R.id.logout){
            Toast.makeText(this, "Logging out ...", Toast.LENGTH_SHORT).show();
            logoutDashboard();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}