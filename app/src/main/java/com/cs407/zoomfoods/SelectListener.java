package com.cs407.zoomfoods;

import com.cs407.zoomfoods.database.entities.ReminderItem;

public interface SelectListener {
    void onItemClicked(ReminderItem reminderItem, int position);
    void onSwitchClicked(ReminderItem reminderItem, int position);
    void onItemLongClicked(ReminderItem reminderItem, int position);
}
