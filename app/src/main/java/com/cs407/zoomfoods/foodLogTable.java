package com.cs407.zoomfoods;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class foodLogTable {
    private static final String databaseName = "Zoom Foods Database";
    static SQLiteDatabase database;
    public foodLogTable(SQLiteDatabase sqLiteDatabase) {
        this.database = sqLiteDatabase;
    }

    private static final String CREATE_TABLE_FOODLOG = "CREATE TABLE IF NOT EXISTS foodLog ("
            + "user_id INTEGER,"
            + "date TEXT,"
            + "meal TEXT,"
            + "name TEXT,"
            + "amount REAL,"
            + "calories REAL,"
            + "protein REAL,"
            + "fat REAL,"
            + "carbs REAL,"
            + "sodium REAL,"
            + "potassium REAL,"
            + "PRIMARY KEY (user_id, date, meal, name));";

    public void createTable(Context context) {
        SQLiteDatabase db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        db.execSQL(CREATE_TABLE_FOODLOG);
        db.close();
    }

    public Cursor getMealFoods(int user_id, String date, String meal) {
        if (database.isOpen()) {
            String[] columns = {"name", "calories"};
            String selection = "user_id=? AND date=? AND meal=?";
            String[] selectionArgs = {String.valueOf(user_id), date, meal};

            return database.query("foodLog", columns, selection, selectionArgs, null, null, null);
        }
        return null;
    }

    public void addFood(int user_id, String date, String meal, String name, double amount, double calories, double protein, double carbs,
                    double fat, double sodium, double potassium) {
        if (database.isOpen()) {
            Cursor cursor = database.rawQuery("SELECT * FROM foodLog WHERE user_id=? AND date=? AND meal=? AND name=?",
                    new String[]{String.valueOf(user_id), date, meal, name});

            if (cursor.moveToFirst()) {
                updateFood(user_id, date, meal, name, amount, calories, protein, carbs, fat, sodium, potassium);
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("user_id", user_id);
                contentValues.put("date", date);
                contentValues.put("meal", meal);
                contentValues.put("name", name);
                contentValues.put("amount", amount);
                contentValues.put("calories", calories);
                contentValues.put("protein", protein);
                contentValues.put("carbs", carbs);
                contentValues.put("fat", fat);
                contentValues.put("sodium", sodium);
                contentValues.put("potassium", potassium);

                database.insert("foodLog", null, contentValues);
            }
            cursor.close();
            database.close();
        }
    }
    public void updateFood(int user_id, String date, String meal, String name, double amount, double calories, double protein, double carbs,
                           double fat, double sodium, double potassium) {
        if (database.isOpen()) {
            database.execSQL("UPDATE foodLog SET amount=?, calories=?, protein=?, carbs=?, fat=?, sodium=?, potassium=? WHERE user_id=? AND date=? AND name=? AND meal=?",
                    new Object[]{amount, calories, protein, carbs, fat, sodium, potassium, user_id, date, name, meal});
        }
    }

    public void deleteFood(int user_id, String date, String meal, String name){
        String whereClause = "user_id=? AND date=? AND meal=? AND name=?";
        String[] whereArgs = {String.valueOf(user_id), date, meal, name};
        database.delete("foodLog", whereClause, whereArgs);
    }


}