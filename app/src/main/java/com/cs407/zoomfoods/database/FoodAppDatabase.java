package com.cs407.zoomfoods.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.cs407.zoomfoods.database.dao.ReminderItemDao;
import com.cs407.zoomfoods.database.dao.UserDao;
import com.cs407.zoomfoods.database.dao.UserProfileDao;
import com.cs407.zoomfoods.database.dao.WaterLogDao;
import com.cs407.zoomfoods.database.entities.ReminderItem;
import com.cs407.zoomfoods.database.entities.User;
import com.cs407.zoomfoods.database.entities.UserProfile;
import com.cs407.zoomfoods.database.entities.WaterLog;

@Database(entities = {User.class, UserProfile.class, WaterLog.class, ReminderItem.class}, version = 1)
public abstract class FoodAppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract UserProfileDao userProfileDao();

    public abstract WaterLogDao waterLogDao();

    public abstract ReminderItemDao reminderItemDao();


}