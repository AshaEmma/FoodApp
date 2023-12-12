package com.cs407.zoomfoods;

import android.util.Log;

import com.cs407.zoomfoods.database.FoodAppDatabase;
import com.cs407.zoomfoods.database.entities.ReminderItem;

import java.util.List;

public class ReminderDBHelper {
    FoodAppDatabase db;

    public ReminderDBHelper(FoodAppDatabase db) {this.db = db;}

    public List<ReminderItem> readReminder(long userId){
        try{
            return db.reminderItemDao().findAllByUserId(userId).get();
        } catch (Exception e){
            Log.e("REMINDERS", "Error fetching reminders for user", e);
            throw new RuntimeException(e);
        }
    }

    public void saveReminder(long userId, String time, boolean isActive){
        try{
            ReminderItem item = new ReminderItem(userId, time, isActive);
            db.reminderItemDao().insertAll(item).get();
        } catch (Exception e){
            Log.e("REMINDERS", "Error saving reminder for user", e);
            throw new RuntimeException(e);
        }
    }

    public void updateReminder(ReminderItem item ){
        try{
            db.reminderItemDao().updateItem(item).get();
        } catch (Exception e){
            Log.e("REMINDERS", "Error updating reminder for user", e);
            throw new RuntimeException(e);
        }
    }

    public void deleteReminder(ReminderItem item){
        try{
            db.reminderItemDao().delete(item).get();
        } catch (Exception e){
            Log.e("REMINDERS", "Error deleting reminder for user", e);
            throw new RuntimeException(e);
        }
    }
}
