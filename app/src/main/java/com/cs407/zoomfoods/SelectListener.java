package com.cs407.zoomfoods;

public interface SelectListener {
    void onItemClicked(ReminderItem reminderItem, int position);
    void onSwitchClicked(ReminderItem reminderItem, int position);
    void onItemLongClicked(ReminderItem reminderItem, int position);
}
