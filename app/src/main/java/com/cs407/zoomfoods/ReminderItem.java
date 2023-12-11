package com.cs407.zoomfoods;

public class ReminderItem {
    private String time;
    private boolean isActive;
    private int id;

    public ReminderItem(String time, boolean isActive, int id){
        this.time = time;
        this.isActive = isActive;
        this.id = id;
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
}
