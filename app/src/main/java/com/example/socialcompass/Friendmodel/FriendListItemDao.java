package com.example.socialcompass.Friendmodel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface FriendListItemDao {

    @Insert
    Long insert(FriendListItem friendListItem);

    @Query("SELECT * FROM friends_list_items WHERE public_code = :public_code")
    public abstract LiveData<FriendListItem> get(String public_code);

    @Query("SELECT * FROM `friends_list_items` ORDER BY `order`")
    List<FriendListItem> getAll();

    @Update
    int update(FriendListItem friendListItem);

    @Delete
    int delete(FriendListItem friendListItem);

    @Query("SELECT * FROM `friends_list_items` ORDER BY `public_code`")
    LiveData<List<FriendListItem>> getAllLive();

    @Query("SELECT `order` +1  FROM `friends_list_items` ORDER BY `order` DESC LIMIT 1")
    int getOrderForAppend();

    @Insert
    List<Long> insertAll(List<FriendListItem> LocationListItem);




}
