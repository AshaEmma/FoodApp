package com.cs407.zoomfoods;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ReminderDBHelper {
    static SQLiteDatabase sqLiteDatabase;
    public ReminderDBHelper(SQLiteDatabase sqLiteDatabase) {this.sqLiteDatabase = sqLiteDatabase;}

    public static void createTable(){
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS reminders " +
                "(id INTEGER PRIMARY KEY, username TEXT, time TEXT, isActive INTEGER)");
    }

    public ArrayList<ReminderItem> readReminder(String username){
        createTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM reminders WHERE username=?", new String[]{username});
        try{
            int timeIndex = c.getColumnIndex("time");
            int idIndex = c.getColumnIndex("id");
            int activeIndex = c.getColumnIndex("isActive");

            c.moveToFirst();
            ArrayList<ReminderItem> ReminderItemList = new ArrayList<>();
            while(!c.isAfterLast()){
                int id = c.getInt(idIndex);
                String time = c.getString(timeIndex);
                boolean isActive = c.getInt(activeIndex) != 0;
                ReminderItem item = new ReminderItem(time, isActive, id);
                ReminderItemList.add(item);
                c.moveToNext();
            }
            return ReminderItemList;
        }
        finally {
            if(c != null){
                c.close();
            }
        }


    }

    public void saveReminder(String username, String time, boolean isActive){
        createTable();
        int checkActive;
        if (isActive) checkActive = 1;
        else checkActive = 0;
        sqLiteDatabase.execSQL("INSERT INTO reminders (username, time, isActive) VALUES (?,?,?)",
                new String[]{username, time, String.valueOf(checkActive)});
    }

    public void updateReminder(String username, String time, boolean isActive, int id){
        createTable();
        int checkActive;
        if (isActive) checkActive = 1;
        else checkActive = 0;
        sqLiteDatabase.execSQL("UPDATE reminders set time=?, isActive=? where username=? and id=?",
                new String[]{time, String.valueOf(checkActive), username, String.valueOf(id)});
    }

    public void deleteReminder(String username, int id){
        createTable();
        sqLiteDatabase.execSQL("DELETE FROM reminders where username =? and id=?",
                new String[]{username, String.valueOf(id)});
    }
}
