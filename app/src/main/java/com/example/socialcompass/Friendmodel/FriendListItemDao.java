package com.example.socialcompass.Friendmodel;

import androidx.annotation.VisibleForTesting;
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

    @Query("SELECT EXISTS(SELECT 1 FROM friends_list_items WHERE publicCode = :publicCode)")
    public abstract boolean exists(String publicCode);

    @Query("SELECT * FROM friends_list_items WHERE publicCode = :publicCode")
    public abstract LiveData<FriendListItem> get(String publicCode);

    @Query("SELECT * FROM `friends_list_items` ORDER BY `order`")
    List<FriendListItem> getAll();

    @Update
    int update(FriendListItem friendListItem);

    @Delete
    int delete(FriendListItem friendListItem);

    @Query("SELECT * FROM `friends_list_items` ORDER BY `order`")
    LiveData<List<FriendListItem>> getAllLive();

    @Query("SELECT `order` +1  FROM `friends_list_items` ORDER BY `order` DESC LIMIT 1")
    int getOrderForAppend();

    @Insert
    List<Long> insertAll(List<FriendListItem> friendListItem);

    @VisibleForTesting
    @Query("SELECT * FROM friends_list_items WHERE publicCode = :publicCode")
    public abstract FriendListItem friendGet(String publicCode);


}