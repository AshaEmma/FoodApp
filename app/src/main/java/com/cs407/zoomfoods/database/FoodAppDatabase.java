package com.cs407.zoomfoods.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.cs407.zoomfoods.database.dao.UserDao;
import com.cs407.zoomfoods.database.dao.UserProfileDao;
import com.cs407.zoomfoods.database.entities.User;
import com.cs407.zoomfoods.database.entities.UserProfile;

@Database(entities = {User.class, UserProfile.class}, version = 1)
public abstract class FoodAppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract UserProfileDao userProfileDao();
}