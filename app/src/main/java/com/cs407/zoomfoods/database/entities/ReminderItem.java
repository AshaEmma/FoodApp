package com.cs407.zoomfoods.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ReminderItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    @NonNull
    public Long userId;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "is_active")
    public boolean isActive;

    public ReminderItem(@NonNull Long userId, String time, boolean isActive) {
        this.userId = userId;
        this.time = time;
        this.isActive = isActive;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean getIsActive(){
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getId(){
        return id;
    }

    @NonNull
    public Long getUserId() {
        return userId;
    }

    public void setUserId(@NonNull Long userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return isActive;
    }
}
