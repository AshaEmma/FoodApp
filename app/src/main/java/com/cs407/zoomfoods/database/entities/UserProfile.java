package com.cs407.zoomfoods.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"user_id"}, unique = true)})
public class UserProfile {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "user_id")
    @NonNull
    public Long userId;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "profile_image_url")
    public String profileImageUrl;

    @ColumnInfo(name = "dob")
    public String birthdate;

    @ColumnInfo(name = "email")
    public String email;
}
