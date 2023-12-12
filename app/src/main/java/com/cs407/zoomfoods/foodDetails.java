package com.cs407.zoomfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.util.Date;

public class foodDetails extends AppCompatActivity {

    private TextView itemNameTextView;
    private TextView itemGrams;
    private EditText gramsText;
    private String itemName;
    private String calories;
    private String protein;
    private String carbs;
    private String fat;
    private String sodium;
    private String potassium;
    private String meal;
    private double grams;
    private String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddetails);
        Button submitButton = findViewById(R.id.submitButton);
        Button editButton = findViewById(R.id.editButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        editButton.setVisibility(View.INVISIBLE);
        submitButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);

        itemNameTextView = findViewById(R.id.indItemName);
        gramsText = findViewById(R.id.gramsEdit);
        gramsText.setVisibility(View.INVISIBLE);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        formattedDate = dateFormat.format(date);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("class").equals("foodSearch")) {
                gramsText.setVisibility(View.VISIBLE);
                itemName = intent.getStringExtra("itemName");
                calories = intent.getStringExtra("calories");
                protein = intent.getStringExtra("protein");
                carbs = intent.getStringExtra("carbs");
                fat = intent.getStringExtra("fat");
                sodium = intent.getStringExtra("sodium");
                potassium = intent.getStringExtra("potassium");
                meal = intent.getStringExtra("meal");

                itemNameTextView.setText(itemName);
                grams = 1;
            }
            else {
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                itemName = intent.getStringExtra("itemName");
                calories = intent.getStringExtra("calories");
                protein = intent.getStringExtra("protein");
                carbs = intent.getStringExtra("carbs");
                fat = intent.getStringExtra("fat");
                sodium = intent.getStringExtra("sodium");
                potassium = intent.getStringExtra("potassium");
                meal = intent.getStringExtra("meal");
                grams = Double.parseDouble(intent.getStringExtra("amount"));
                itemGrams = findViewById(R.id.itemGrams);
                itemGrams.setText("Amount in grams: " + String.format("%.1f", grams * 100));


                itemNameTextView.setText(itemName);
            }
            changeText();
        }

        gramsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submitButton.setVisibility(View.VISIBLE);
                if (!gramsText.getText().toString().isEmpty()) {
                    grams = (Double.parseDouble(gramsText.getText().toString())) / 100;
                }
                changeText();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submitButton.setOnClickListener(this::submitGrams);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButton.setVisibility(View.VISIBLE);
                gramsText.setVisibility(View.VISIBLE);
                itemGrams.setText("Amount in grams:");
                editButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                SQLiteDatabase db = context.openOrCreateDatabase("Zoom Foods Database", Context.MODE_PRIVATE, null);
                foodLogTable log = new foodLogTable(db);
                log.deleteFood(Login.userId, formattedDate, meal, itemName);

                Intent intent = new Intent(foodDetails.this, foodTracking.class);
                startActivity(intent);
            }
        });
    }

    public void submitGrams(View view) {
        Context context = getApplicationContext();
        SQLiteDatabase db = context.openOrCreateDatabase("Zoom Foods Database", Context.MODE_PRIVATE, null);
        foodLogTable log = new foodLogTable(db);
        log.createTable(context);

        log.addFood(Login.userId, formattedDate, meal, itemName, grams, Double.parseDouble(calories) * grams, Double.parseDouble(protein) * grams,
                Double.parseDouble(carbs) * grams, Double.parseDouble(fat) * grams, Double.parseDouble(sodium) * grams, Double.parseDouble(potassium) * grams);
        db.close();

        Intent intent = new Intent(foodDetails.this, foodTracking.class);
        startActivity(intent);
    }

    public void changeText() {
        TextView mealText = findViewById(R.id.meal_TextView);
        TextView caloriesText = findViewById(R.id.calories_TextView);
        TextView proteinText = findViewById(R.id.protein_TextView);
        TextView carbsText = findViewById(R.id.carbs_TextView);
        TextView fatText = findViewById(R.id.fat_TextView);
        TextView sodiumText = findViewById(R.id.sodium_TextView);
        TextView potassiumText = findViewById(R.id.potassium_TextView);

        itemNameTextView.setText(itemName);
        String mealString = "Meal: " + meal;
        String caloriesString = "Calories: " + String.format("%.1f", Double.parseDouble(calories) * grams);
        String proteinString = "Protein: " + String.format("%.1f", Double.parseDouble(protein) * grams);
        String carbsString = "Carbohydrates: " + String.format("%.1f", Double.parseDouble(carbs) * grams);
        String fatString = "Fat: " + String.format("%.1f", Double.parseDouble(fat) * grams);
        String sodiumString = "Sodium: " + String.format("%.1f", Double.parseDouble(sodium) * grams);
        String potassiumString = "Potassium: " + String.format("%.1f", Double.parseDouble(potassium) * grams);
        mealText.setText(mealString);
        caloriesText.setText(caloriesString);
        proteinText.setText(proteinString);
        carbsText.setText(carbsString);
        fatText.setText(fatString);
        sodiumText.setText(sodiumString);
        potassiumText.setText(potassiumString);
    }
}