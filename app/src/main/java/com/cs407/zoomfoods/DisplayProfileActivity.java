package com.cs407.zoomfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cs407.zoomfoods.database.DBService;
import com.cs407.zoomfoods.database.FoodAppDatabase;
import com.cs407.zoomfoods.database.entities.UserProfile;
import com.cs407.zoomfoods.utils.Constants;

import java.util.concurrent.ExecutionException;

public class DisplayProfileActivity extends AppCompatActivity {

    TextView customerName;
    TextView birthdate;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent currentIntent = getIntent();
        long userId = currentIntent.getLongExtra(Constants.USER_ID, -1);

        if (userId == -1) {
            throw new IllegalArgumentException("User ID is required");
        }

        FoodAppDatabase db = DBService.getAppDatabase();

        customerName = findViewById(R.id.name);
        birthdate = findViewById(R.id.DOB);
        email = findViewById(R.id.emailAddress);

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
}