package com.cs407.zoomfoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cs407.zoomfoods.database.DBService;
import com.cs407.zoomfoods.database.FoodAppDatabase;
import com.cs407.zoomfoods.database.entities.UserProfile;
import com.cs407.zoomfoods.services.UserSessionService;
import com.cs407.zoomfoods.utils.Constants;

import java.util.concurrent.ExecutionException;

public class DisplayProfileActivity extends AppCompatActivity {

    TextView customerName;
    TextView birthdate;
    TextView email;
    Button logout;
    private long userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UserSessionService userSessionService = UserSessionService.getInstance();
        // Check if userId is valid (not -1 or any other invalid value)
        userId = userSessionService.getUserId();
        checkLoggedIn();

        FoodAppDatabase db = DBService.getAppDatabase();

        customerName = findViewById(R.id.name);
        birthdate = findViewById(R.id.DOB);
        email = findViewById(R.id.emailAddress);
        logout = findViewById(R.id.btnLogoutFromProfile);


        try{
            UserProfile profile = db.userProfileDao().findByUserId(userId).get();
            Log.i("TEST", "Profile = "+profile.firstName);
            customerName.setText(profile.firstName);
            birthdate.setText(profile.lastName);
            email.setText(profile.email);
        } catch (Exception e) {
            Log.e("DISPLAY_USER_PROFILE", "Error loading user profile", e);
        }
    }

    public void openDashboardActivity(){
        Intent viewProfileIntent = new Intent(DisplayProfileActivity.this,DashboardActivity.class);
        startActivity(viewProfileIntent);
    }

    public void openCreateProfileActivity(){
        Intent viewProfileIntent = new Intent(DisplayProfileActivity.this, CreateProfileActivity.class);
        startActivity(viewProfileIntent);
    }

    public void openWaterActivity(){
        //TODO: navigate to water activity
    }


    public void openFridgeActivity(){
        //TODO: navigate to fridge activity
    }

    public void openFoodActivity(){
        //TODO: navigate to food activity
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
        if(itemId == R.id.dashboard){
            Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
            return true;
        }else if(itemId == R.id.profile){
            //openCreateProfileActivity();
            return true;

        } else if(itemId == R.id.viewProfile){
            Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
            //openDashboardActivity();

            return true;
        }else if (itemId == R.id.updateProfile){
            Toast.makeText(this, "Item 3 selected", Toast.LENGTH_SHORT).show();
            //openWaterActivity();
            return true;
        }else if (itemId == R.id.waterIntake){
            Toast.makeText(this, "Sub item 1 selected", Toast.LENGTH_SHORT).show();
            //openFridgeActivity();
            return true;
        }else if(itemId == R.id.fridge){
            Toast.makeText(this, "Sub item 2 selected", Toast.LENGTH_SHORT).show();
            //openFoodActivity();
            return true;
        }else if(itemId == R.id.foodRecord){
            Toast.makeText(this, "Sub item 2 selected", Toast.LENGTH_SHORT).show();
            //openFoodActivity();
            return true;
        }else if(itemId == R.id.logout){
            Toast.makeText(this, "Sub item 2 selected", Toast.LENGTH_SHORT).show();
            //openFoodActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkLoggedIn() {
        if (userId == -1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}