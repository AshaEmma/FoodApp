package com.cs407.zoomfoods.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WaterLog {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    @NonNull
    public Long userId;

    @ColumnInfo(name = "time") //Water
    public String time;

    @ColumnInfo(name = "amount") // Water
    public String amount;

    @ColumnInfo(name = "title")// Water
    public String title;

    public int getId() {
        return id;
    }

    @NonNull
    public Long getUserId() {
        return userId;
    }

    public String getTime() {
        return time;
    }

    public String getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

}
