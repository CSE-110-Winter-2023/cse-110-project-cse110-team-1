package com.example.socialcompass.model.Friend;


import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;


import java.util.List;
@Dao
public abstract class FriendDao {

    @Upsert
    public abstract long upsert(Friend friend);

    @Query("SELECT EXISTS(SELECT 1 FROM friends WHERE public_code = :public_code)")
    public abstract boolean exists(String public_code);

    @Query("SELECT * FROM friends WHERE public_code = :public_code")
    public abstract LiveData<Friend> get(String public_code);


//    @Query("DELETE FROM friends WHERE private_code = :private_code")
//    public abstract int delete(String private_code);

//    @VisibleForTesting
//    @Query("SELECT EXISTS(SELECT 1 FROM friends WHERE private_code = :private_code)")
//    public abstract boolean existsPrivateCode(String private_code);

    @VisibleForTesting
    @Query("SELECT * FROM friends WHERE public_code = :public_code")
    public abstract Friend friendGet(String public_code);


}
