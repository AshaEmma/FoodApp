package com.cs407.zoomfoods.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cs407.zoomfoods.database.entities.ReminderItem;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface ReminderItemDao {


    @Query("SELECT * FROM ReminderItem WHERE id = :reminderId")
    ListenableFuture<ReminderItem> findById(int reminderId);

    @Query("SELECT * FROM ReminderItem WHERE user_id = :userId")
    ListenableFuture<List<ReminderItem>> findAllByUserId(long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> insertAll(ReminderItem... items);

    @Update
    ListenableFuture<Void> updateItem(ReminderItem item);

    @Delete
    ListenableFuture<Void> delete(ReminderItem item);


}
