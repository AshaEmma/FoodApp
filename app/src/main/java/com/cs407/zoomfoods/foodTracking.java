package com.cs407.zoomfoods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.cs407.zoomfoods.utils.Constants;

public class foodTracking extends AppCompatActivity {
    private ListView breakfastListView, lunchListView, dinnerListView;
    private Button addBreakfastButton, addLunchButton, addDinnerButton;
    private TextView dateTextView, breakfastCaloriesText, lunchCaloriesText, dinnerCaloriesText;
    private double totalCalories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodtracking);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = dateFormat.format(date);

        dateTextView = findViewById(R.id.dateTextView);
        breakfastListView = findViewById(R.id.breakfastListView);
        lunchListView = findViewById(R.id.lunchListView);
        dinnerListView = findViewById(R.id.dinnerListView);
        addBreakfastButton = findViewById(R.id.addBreakfastButton);
        addLunchButton = findViewById(R.id.addLunchButton);
        addDinnerButton = findViewById(R.id.addDinnerButton);
        TextView totalCaloriesText = findViewById(R.id.totalCaloriesText);
        breakfastCaloriesText = findViewById(R.id.breakfastCaloriesText);
        lunchCaloriesText = findViewById(R.id.lunchCaloriesText);
        dinnerCaloriesText = findViewById(R.id.dinnerCaloriesText);

        loadAndDisplayData("Breakfast", formattedDate, breakfastListView);
        loadAndDisplayData("Lunch", formattedDate, lunchListView);
        loadAndDisplayData("Dinner", formattedDate, dinnerListView);

        dateTextView.setText(formattedDate);
        totalCaloriesText.setText("Total Calories: " + String.format("%.1f", totalCalories));

        addBreakfastButton.setOnClickListener(view -> openSearchActivity("Breakfast"));
        addLunchButton.setOnClickListener(view -> openSearchActivity("Lunch"));
        addDinnerButton.setOnClickListener(view -> openSearchActivity("Dinner"));

        Context context = getApplicationContext();
        SQLiteDatabase dbHelper = context.openOrCreateDatabase("Zoom Foods Database", Context.MODE_PRIVATE, null);
        foodLogTable logTable = new foodLogTable(dbHelper);
        logTable.createTable(context);
        breakfastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                itemClicked("Breakfast", formattedDate, name);
            }
        });
        lunchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                itemClicked("Lunch", formattedDate, name);
            }
        });
        dinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                itemClicked("Dinner", formattedDate, name);
            }
        });
    }

    private void openSearchActivity(String meal) {
        Intent intent = new Intent(foodTracking.this, foodSearch.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }

    private void loadAndDisplayData(String meal, String date, ListView listView) {
        Context context = getApplicationContext();
        SQLiteDatabase db = context.openOrCreateDatabase("Zoom Foods Database", Context.MODE_PRIVATE, null);
        db = context.openOrCreateDatabase("Zoom Foods Database", Context.MODE_PRIVATE, null);
        foodLogTable logTable = new foodLogTable(db);
        logTable.createTable(context);
        Cursor cursor = logTable.getMealFoods(Integer.parseInt(Constants.USER_ID), date, meal);

        double mealCalories = 0;
        if (cursor != null && cursor.moveToFirst()) {
            List<String> foodNames = new ArrayList<>();
            do {
                @SuppressLint("Range") String foodName = cursor.getString(cursor.getColumnIndex("name"));
                foodNames.add(foodName);
                @SuppressLint("Range") double foodCalories = cursor.getDouble(cursor.getColumnIndex("calories"));
                mealCalories += foodCalories;
            } while (cursor.moveToNext());
            totalCalories += mealCalories;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            adapter.addAll(foodNames);
            listView.setAdapter(adapter);
        }

        if (meal.equals("Breakfast")) {
            breakfastCaloriesText.setText(String.format("%.1f", mealCalories));
        }
        else if (meal.equals("Lunch")) {
            lunchCaloriesText.setText(String.format("%.1f", mealCalories));
        }
        else {
            dinnerCaloriesText.setText(String.format("%.1f", mealCalories));
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    @SuppressLint("Range")
    private void itemClicked(String meal, String date, String name) {
        Context context = getApplicationContext();
        SQLiteDatabase dbHelper = context.openOrCreateDatabase("Zoom Foods Database", Context.MODE_PRIVATE, null);
        foodLogTable logTable = new foodLogTable(dbHelper);
        logTable.createTable(context);
        Intent intent = new Intent(foodTracking.this, foodDetails.class);
        intent.putExtra("itemName", name);
        Cursor cursor = dbHelper.rawQuery("SELECT * FROM foodLog WHERE user_id=? AND date=? AND meal=?", new String[]{"2", date, meal});
        while (cursor.moveToNext()) {
            intent.putExtra("calories", cursor.getString(cursor.getColumnIndex("calories")));
            intent.putExtra("protein", cursor.getString(cursor.getColumnIndex("protein")));
            intent.putExtra("carbs", cursor.getString(cursor.getColumnIndex("carbs")));
            intent.putExtra("fat", cursor.getString(cursor.getColumnIndex("fat")));
            intent.putExtra("sodium", cursor.getString(cursor.getColumnIndex("sodium")));
            intent.putExtra("potassium", cursor.getString(cursor.getColumnIndex("potassium")));
            intent.putExtra("meal", meal);
            intent.putExtra("amount", cursor.getString(cursor.getColumnIndex("amount")));
            intent.putExtra("class", "foodTracking");
        }
        if (cursor != null) {
            cursor.close();
        }
        startActivity(intent);
    }

}