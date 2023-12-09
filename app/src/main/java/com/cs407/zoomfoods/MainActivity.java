package com.cs407.zoomfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cs407.zoomfoods.database.DBService;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MAIN", "Initializing DB...");
        DBService.initAppDb(getApplicationContext());
        Log.i("MAIN", "Finished initializing DB");

        setContentView(R.layout.activity_main);
        Button loginBtn = findViewById(R.id.login);
        Button signUpBtn = findViewById(R.id.signup);

        loginBtn.setOnClickListener(v -> openLoginActivity());
        signUpBtn.setOnClickListener(v -> openRegistrationActivity());

        Log.i("MAIN", "====== App Started ======");
    }
    public void openLoginActivity(){
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    public void openRegistrationActivity(){
        Intent signupIntent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(signupIntent);
    }
}