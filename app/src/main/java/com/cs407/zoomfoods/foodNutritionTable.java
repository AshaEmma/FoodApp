package com.cs407.zoomfoods;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class foodNutritionTable {
    private static final String databaseName = "Foods Database";
    static SQLiteDatabase database;
    public foodNutritionTable(SQLiteDatabase sqLiteDatabase)
    {
        this.database = sqLiteDatabase;
    }

    private static final String CREATE_TABLE_FOODNUTRITION = "CREATE TABLE IF NOT EXISTS foodNutrition ("
            + "name TEXT,"
            + "calories REAL,"
            + "protein REAL,"
            + "fat REAL,"
            + "carbs REAL,"
            + "sodium REAL,"
            + "potassium REAL);";


    public static void createTable(Context context) {
        SQLiteDatabase db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS foodNutrition");
        db.execSQL(CREATE_TABLE_FOODNUTRITION);
        db.close();
    }

    public void importData(Context context, String filePath) {
        Cursor cursor = database.rawQuery("SELECT EXISTS (SELECT 1 FROM foodNutrition LIMIT 1)", null);
        boolean isImported = false;
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getInt(0) == 1) {
                isImported = true;
            }
        }
        if (!isImported) {
            try {
                InputStream inputStream = context.getAssets().open(filePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] values = line.split("\\|");
                    String name = values[0].trim();
                    double calories = Double.parseDouble(values[1].trim());
                    double protein = Double.parseDouble(values[2].trim());
                    double carbohydrates = Double.parseDouble(values[3].trim());
                    double fat = Double.parseDouble(values[4].trim());
                    double sodium = Double.parseDouble(values[5].trim());
                    double potassium = Double.parseDouble(values[6].trim());

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", name);
                    contentValues.put("calories", calories);
                    contentValues.put("protein", protein);
                    contentValues.put("carbs", carbohydrates);
                    contentValues.put("fat", fat);
                    contentValues.put("sodium", sodium);
                    contentValues.put("potassium", potassium);

                    database.insert("foodNutrition", null, contentValues);
                }

                reader.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                database.close();
            }
        }
    }
}