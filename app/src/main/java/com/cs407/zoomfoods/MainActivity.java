package com.cs407.zoomfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("APP_STARTED", "====== App Started");
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signup);
    }
}