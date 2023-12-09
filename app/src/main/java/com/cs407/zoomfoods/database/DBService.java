package com.cs407.zoomfoods.database;

import android.content.Context;

import androidx.room.Room;

import java.util.UUID;

public class DBService {
    public static final String DATABASE_NAME = "FoodAppDb";
    private static FoodAppDatabase appDatabase;

    private DBService() {}

    public static void initAppDb(Context context) {
        if(appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, FoodAppDatabase.class, getDBName())
                    .fallbackToDestructiveMigration()
                    .build();
        }
    }

    public static FoodAppDatabase getAppDatabase() {
        return appDatabase;
    }

    private static String getDBName() {
        return DATABASE_NAME + "_" + UUID.randomUUID();
    }
}