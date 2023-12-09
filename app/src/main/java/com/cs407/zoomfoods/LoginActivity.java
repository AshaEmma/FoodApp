package com.cs407.zoomfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cs407.zoomfoods.database.DBService;
import com.cs407.zoomfoods.database.FoodAppDatabase;
import com.cs407.zoomfoods.database.entities.User;
import com.cs407.zoomfoods.utils.Constants;
import com.cs407.zoomfoods.utils.StringUtils;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    Button newUserBtn;
    EditText usernameEditText;
    EditText passwordEditText;
    private FoodAppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = DBService.getAppDatabase();

        loginBtn = findViewById(R.id.btn_login);
        newUserBtn = findViewById(R.id.btn_newUser);
        usernameEditText = findViewById(R.id.text_username);
        passwordEditText = findViewById(R.id.text_password);

        loginBtn.setOnClickListener(this::onLoginBnClicked);

        newUserBtn.setOnClickListener(v -> openRegistrationActivity());
    }

    private void onLoginBnClicked(View ignored) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            User user = db.userDao().findByUsername(username).get();

            if (!password.equals(user.password)) {
                Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
            showDashboardActivity(user.id);
            // TODO: Cache login user so we don't have to pass userId via all intents
        } catch (Exception e) {
            Log.e("LOGIN", "Error fetching user for userName=" + username, e);
            Toast.makeText(getApplicationContext(), "Ooops! We had trouble creating that user. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDashboardActivity(long userId) {
        Intent loginIntent = new Intent(LoginActivity.this, DashboardActivity.class);
        loginIntent.putExtra(Constants.USER_ID, userId);
        startActivity(loginIntent);
    }

    public void openRegistrationActivity() {
        Intent signupIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(signupIntent);
    }
}