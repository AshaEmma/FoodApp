package com.cs407.zoomfoods;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cs407.zoomfoods.database.DBService;
import com.cs407.zoomfoods.database.FoodAppDatabase;
import com.cs407.zoomfoods.database.entities.UserProfile;
import com.cs407.zoomfoods.services.UserSessionService;
import com.cs407.zoomfoods.utils.Constants;
import com.cs407.zoomfoods.utils.StringUtils;

import java.util.Objects;

public class CreateProfileActivity extends AppCompatActivity {
    //TODO: add menu to navigate back to places
    TextView firstName;
    TextView lastName;
    TextView birthdate;
    TextView email;
    Button confirm;
    private FoodAppDatabase db;

    long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        db = DBService.getAppDatabase();

        UserSessionService userSessionService = UserSessionService.getInstance();
        userId = userSessionService.getUserId();
        checkLoggedIn();

        firstName = findViewById(R.id.FirstName);
        lastName = findViewById(R.id.LastName);
        birthdate = findViewById(R.id.txtDOB);
        email = findViewById(R.id.txtEmail);
        confirm = findViewById(R.id.btnEditProfileSubmit);

        confirm.setOnClickListener(this::onProfileSaved);
    }

    public void showProfileActivity() {
        Intent viewProfileIntent = new Intent(CreateProfileActivity.this, DisplayProfileActivity.class);
        viewProfileIntent.putExtra(Constants.USER_ID, userId);
        startActivity(viewProfileIntent);
    }

    private void onProfileSaved(View v) {
        Toast.makeText(getApplicationContext(), "Button Clicked here", Toast.LENGTH_SHORT).show();
        String first = firstName.getText().toString();
        String last = lastName.getText().toString();
        String birthDate = birthdate.getText().toString();
        String emailAddress = email.getText().toString();

        if(!isValidData(first, last, birthDate, emailAddress)) {
            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        createOrUpdateProfile(first, last, birthDate, emailAddress);
    }

    private void createOrUpdateProfile(String regFirstName, String regLastName, String regbirthdate, String regEmail) {
        UserProfile userProfile = new UserProfile();
        userProfile.firstName = regFirstName;
        userProfile.lastName = regLastName;
        userProfile.birthdate = regbirthdate;
        userProfile.email = regEmail;
        userProfile.userId = userId;

        try {
            db.userProfileDao().insertAll(userProfile).get();
            Log.i("CREATE_PROFILE", "Created use profile");
            Toast.makeText(getApplicationContext(), "Profile saved", Toast.LENGTH_SHORT).show();
            showProfileActivity();
        } catch (Exception e) {
            Log.e("CREATE_PROFILE", "Error creating/updating user profile", e);
            Toast.makeText(getApplicationContext(), "Ooops! We had trouble creating that user. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isValidFields(String regFirstName, String regLastName, String dob, String email) {
        return StringUtils.isNotBlank(regFirstName)
                && StringUtils.isNotBlank(regLastName)
                && StringUtils.isNotBlank(dob)
                && StringUtils.isNotBlank(email);
    }

    private boolean isValidData(String regFirstName, String regLastName, String dob, String email) {
        if (!isValidFields(regFirstName, regLastName, dob, email)) {
            Toast.makeText(getApplicationContext(), "Fields Required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void checkLoggedIn() {
        if (userId == -1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}