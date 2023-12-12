package com.cs407.zoomfoods.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.cs407.zoomfoods.database.entities.WaterLog;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface WaterLogDao {


    @Query("SELECT * FROM WaterLog WHERE id = :recordId")
    ListenableFuture<WaterLog> findById(int recordId);

    @Query("SELECT * FROM WaterLog WHERE user_id = :userId")
    ListenableFuture<WaterLog> findByUserId(long userId);

    @Query("SELECT * FROM WaterLog WHERE user_id = :userId")
    ListenableFuture<List<WaterLog>> findAllByUserId(long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> insertAll(WaterLog... waterLogs);

    @Update
    ListenableFuture<Void> updateLog(WaterLog waterLog);

    @Delete
    ListenableFuture<Void> delete(WaterLog waterLog);


}
