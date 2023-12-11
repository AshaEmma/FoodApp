package com.cs407.zoomfoods;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/*
 * Perform all the CRUD operations related to database required for our application
 * */
public class WaterDBHelper {
    static SQLiteDatabase sqLiteDatabase;
    private Context context;
    public WaterDBHelper (SQLiteDatabase sqLiteDatabase, Context context) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.context = context;
    }

    public static void createTable(){
        //use integer column to set username
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS records "+
                "(id INTEGER PRIMARY KEY, username TEXT, time TEXT, amount TEXT, title TEXT)");
    }

    public ArrayList<Record> readRecords(String username){
        createTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM records WHERE username=?", new String[]{username});
        try {
            int timeIndex = c.getColumnIndex("time");
            int amountIndex = c.getColumnIndex("amount");
            int titleIndex = c.getColumnIndex("title");
            int idIndex = c.getColumnIndex("id");

            c.moveToFirst();
            ArrayList<Record> recordList = new ArrayList<>();
            while (!c.isAfterLast()) {
                int id = c.getInt(idIndex);
                //Log.i("Information", "From DB, id: " + id);
                String time = c.getString(timeIndex);
                String title = c.getString(titleIndex);
                String amount = c.getString(amountIndex);
                Record record = new Record(id, time, amount, title);
                recordList.add(record);
                c.moveToNext();
            }
            return recordList;
        }
        finally {
            if(c != null){
                c.close();
            }
        }


    }

    public void saveRecord(String username, String time, String amount, String title){
        createTable();
        sqLiteDatabase.execSQL("INSERT INTO records (username, time, amount, title) VALUES (?, ?, ?, ?)",
                new String[]{username, time, amount, title});
    }

    public void updateRecord(String username, String time, String amount, String title, int id){
        createTable();
        sqLiteDatabase.execSQL("UPDATE records set time=?, amount=?, title=? where username=? AND id=?",
                new String[]{time, amount, title, username, String.valueOf(id)});
    }

    public void deleteRecord(String username, int id){
        createTable();
        sqLiteDatabase.execSQL("DELETE FROM records WHERE username=? AND id=?",
                new String[]{username, String.valueOf(id)});
    }
}
