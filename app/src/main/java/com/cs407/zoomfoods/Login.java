package com.cs407.zoomfoods;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.btn_login);
        Button newUser= findViewById(R.id.btn_newUser);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openRegistrationActivity();
            }
        });
    }

    public void login(){
     //TODO: Handle Login
        Intent loginIntent = new Intent(Login.this, Dashboard.class);
        startActivity(loginIntent);
    }

    public void keepMeLogged(){
        //TODO: Handle Keep me Logged in
    }

    public void openRegistrationActivity(){
        Intent signupIntent = new Intent(Login.this, Registration.class);
        startActivity(signupIntent);
    }
}