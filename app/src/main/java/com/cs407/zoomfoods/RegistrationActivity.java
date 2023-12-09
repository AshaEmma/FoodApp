package com.cs407.zoomfoods;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cs407.zoomfoods.database.DBService;
import com.cs407.zoomfoods.database.FoodAppDatabase;
import com.cs407.zoomfoods.database.entities.User;
import com.cs407.zoomfoods.utils.Constants;
import com.cs407.zoomfoods.utils.StringUtils;

public class RegistrationActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText confirmation;
    Button signUp;
    Button login;
    private FoodAppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        db = DBService.getAppDatabase();

        username = findViewById(R.id.emailAddress);
        password = findViewById(R.id.Sign_up_Password);
        confirmation = findViewById(R.id.confirm_password);
        signUp = findViewById(R.id.signup);
        login = findViewById(R.id.login);

        login.setOnClickListener(v -> {
            Intent loginIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });

        signUp.setOnClickListener(this::onSignupClick);
    }

    public void showCreateProfileActivity(Long userId) {
        Intent intent = new Intent(RegistrationActivity.this, CreateProfileActivity.class);
        intent.putExtra(Constants.USER_ID, userId);
        startActivity(intent);
    }

    private void onSignupClick(View v) {
        String regUsername = username.getText().toString();
        String regPassword = password.getText().toString();
        String confirmPassword = confirmation.getText().toString();

        if (!isValidData(regUsername, regPassword, confirmPassword)) {
            return;
        }

        if (isExistingUsername(regUsername)) {
            Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_SHORT).show();
            return;
        }

        createAndSaveUser(regUsername, regPassword);
    }

    private boolean isExistingUsername(String regUsername) {
        User existingUser;
        try {
            existingUser = db.userDao().findByUsername(regUsername).get();
        } catch (Exception e) {
            Log.e("REGISTER", "Unexpected error finding user", e);
            return false;
        }

        return existingUser != null;
    }

    private void createAndSaveUser(String regUsername, String regPassword) {
        User user = new User();
        user.userName = regUsername;
        user.password = regPassword;
        try {
            Long userId = db.userDao().insertUser(user).get();
            Log.i("REGISTER", "Created new user with userId=" + userId);
            Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
            resetFields();
            showCreateProfileActivity(userId);
        } catch (Exception e) {
            Log.e("REGISTER", "Error creating user", e);
            Toast.makeText(getApplicationContext(), "Ooops! We had trouble creating that user. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetFields() {
        username.setText("");
        password.setText("");
        confirmation.setText("");
    }

    private static boolean isValidFields(String regUsername, String regPassword, String confirmPassword) {
        return StringUtils.isNotBlank(regUsername)
                && StringUtils.isNotBlank(regPassword)
                && StringUtils.isNotBlank(confirmPassword);
    }

    private boolean isValidData(String regUsername, String regPassword, String confirmPassword) {
        if (!isValidFields(regUsername, regPassword, confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Fields Required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!regPassword.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}