package com.example.socialcompass.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDatabase;
import com.example.socialcompass.model.repository.Repository;

public class FriendViewModel extends AndroidViewModel {

    private LiveData<Friend> friend;
    private final Repository repo;
    public FriendViewModel(@NonNull Application application, LiveData<Friend> friend, Repository repo) {
        super(application);

        var context = application.getApplicationContext();
        var db = FriendDatabase.provide(context);
        var dao = db.getDao();

        this.repo = new Repository(dao);

    }

    public LiveData<Friend> getFriend(String publicCode){
        if(friend == null){
            friend = repo.getSyncedFriend(publicCode);
        }
        return friend;
    }

}
