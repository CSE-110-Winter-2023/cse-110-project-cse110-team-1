package com.example.socialcompass.Friendmodel;


import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;


import com.example.socialcompass.Friendmodel.Friend;

import java.util.List;
@Dao
public abstract class FriendDao {

    @Upsert
    public abstract long upsert(Friend friend);

    @Insert
    public abstract long insert(Friend friend);

    @Query("SELECT EXISTS(SELECT 1 FROM friends WHERE publicCode = :publicCode)")
    public abstract boolean exists(String publicCode);

    @Query("SELECT * FROM friends WHERE publicCode = :publicCode")
    public abstract LiveData<Friend> get(String publicCode);

    @Query("SELECT * FROM `friends` ORDER BY `order`")
    public abstract List<Friend> getAll();

    @Delete
    public abstract int delete(Friend friend);

    @Query("SELECT * FROM `friends` ORDER BY `order`")
    public abstract LiveData<List<Friend>> getAllLive();

    @Query("SELECT `order` +1  FROM `friends` ORDER BY `order` DESC LIMIT 1")
    public abstract int getOrderForAppend();

    @Insert
    public abstract List<Long> insertAll(List<Friend> friendListItem);
    @VisibleForTesting
    @Query("SELECT * FROM friends WHERE publicCode = :publicCode")
    public abstract Friend friendGet(String publicCode);


}
