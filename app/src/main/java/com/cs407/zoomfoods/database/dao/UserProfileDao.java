package com.cs407.zoomfoods.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cs407.zoomfoods.database.entities.UserProfile;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface UserProfileDao {

    @Query("SELECT * FROM userProfile WHERE id = :profileId")
    ListenableFuture<UserProfile> findById(int profileId);

    @Query("SELECT * FROM userProfile WHERE user_id = :userId")
    ListenableFuture<UserProfile> findByUserId(long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> insertAll(UserProfile... userProfiles);

    @Delete
    ListenableFuture<Void> delete(UserProfile userProfile);
}