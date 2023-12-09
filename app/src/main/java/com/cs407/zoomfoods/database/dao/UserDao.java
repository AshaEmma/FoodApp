package com.cs407.zoomfoods.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cs407.zoomfoods.database.entities.User;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id = :userId")
    User findById(int userId);

    @Query("SELECT * FROM user WHERE username = :userName")
    ListenableFuture<User> findByUsername(String userName);

    @Delete
    void delete(User user);

    @Insert
    ListenableFuture<Long> insertUser(User user);
}