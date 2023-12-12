package com.cs407.zoomfoods.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"user_id"}, unique = true)})
public class UserReminder {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "isActive")//Reminder
    public String isActive;

    @ColumnInfo(name = "drinkNotify")//Reminder
    public String drinkNotify;
}
