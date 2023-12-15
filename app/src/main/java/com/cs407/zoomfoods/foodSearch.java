package com.cs407.zoomfoods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class foodSearch extends AppCompatActivity {
    private EditText searchEditText;
    private ListView resultsListView;
    private ArrayAdapter<String> adapter;
    private SQLiteDatabase dbHelper;
    private String meal;
    private boolean submitClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodsearch);
        Intent intent = getIntent();
        if (intent != null) {
            meal = intent.getStringExtra("meal");
        }

        Context context = getApplicationContext();
        dbHelper = context.openOrCreateDatabase("Foods Database", Context.MODE_PRIVATE, null);
        foodNutritionTable table = new foodNutritionTable(dbHelper);
        table.createTable(context);
        startImportThread();

        searchEditText = findViewById(R.id.searchEditText);
        resultsListView = findViewById(R.id.resultsListView);
        Toolbar toolbar = findViewById(R.id.foodSearch_toolbar);

        setSupportActionBar(toolbar);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        resultsListView.setAdapter(adapter);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch(searchEditText.getText().toString());
                submitClicked = true;
            }
        });

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(foodSearch.this, foodDetails.class);
                intent.putExtra("itemName", name);
                Cursor cursor = dbHelper.rawQuery("SELECT * FROM foodNutrition WHERE name = ?", new String[]{name});
                while (cursor.moveToNext()) {
                    intent.putExtra("calories", cursor.getString(cursor.getColumnIndex("calories")));
                    intent.putExtra("protein", cursor.getString(cursor.getColumnIndex("protein")));
                    intent.putExtra("carbs", cursor.getString(cursor.getColumnIndex("carbs")));
                    intent.putExtra("fat", cursor.getString(cursor.getColumnIndex("fat")));
                    intent.putExtra("sodium", cursor.getString(cursor.getColumnIndex("sodium")));
                    intent.putExtra("potassium", cursor.getString(cursor.getColumnIndex("potassium")));
                    intent.putExtra("meal", meal);
                    intent.putExtra("class", "foodSearch");
                }
                if (cursor != null) {
                    cursor.close();
                }
                startActivity(intent);
            }
        });
    }

    private void performSearch(String query) {
        List<String> searchResults = new ArrayList<>();
        Context context = getApplicationContext();
        dbHelper = context.openOrCreateDatabase("Foods Database", Context.MODE_PRIVATE, null);
        Cursor cursor = dbHelper.rawQuery("SELECT * FROM foodNutrition WHERE name LIKE ?", new String[]{"%" + query + "%"});

        try {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String foodName = cursor.getString(cursor.getColumnIndex("name"));
                searchResults.add(foodName);
            }
        } finally {
            cursor.close();
        }

        adapter.clear();

        if (searchResults != null && !searchResults.isEmpty()) {
            adapter.addAll(searchResults);
        } else {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void startImportThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                SQLiteDatabase db = context.openOrCreateDatabase("Foods Database", Context.MODE_PRIVATE, null);
                foodNutritionTable table = new foodNutritionTable(db);
                table.importData(context,"nutrition.txt");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (submitClicked) {
                            performSearch(searchEditText.getText().toString());
                        }
                        db.close();
                    }
                });
            }
        }).start();
    }
}