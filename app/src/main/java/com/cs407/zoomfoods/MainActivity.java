package com.cs407.zoomfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("APP_STARTED", "====== App Started");
        setContentView(R.layout.activity_main);

        Button login = findViewById(R.id.login);
        Button signUp = findViewById(R.id.signup);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openRegistrationActivity();
            }
        });


    }
    public void openLoginActivity(){
        Intent loginIntent = new Intent(MainActivity.this, Login.class);
        startActivity(loginIntent);
    }

    public void openRegistrationActivity(){
        Intent signupIntent = new Intent(MainActivity.this, Registration.class);
        startActivity(signupIntent);
    }

}