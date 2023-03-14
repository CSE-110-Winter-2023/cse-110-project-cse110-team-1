package com.example.socialcompass.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDatabase;
import com.example.socialcompass.model.repository.Repository;

import java.util.List;

public class FriendListViewModel extends AndroidViewModel {
//    private LiveData<List<Friend>> friendListItems;
    private LiveData<List<Friend>> friendListItems;

    private final FriendDao friendListItemDao;

    private final Repository repo;


    public FriendListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        FriendDatabase db = FriendDatabase.provide(context);
        friendListItemDao = db.getDao();

        this.repo = new Repository(friendListItemDao);
    }

    public LiveData<List<Friend>> getFriendListItems() {
        if (friendListItems == null) {
            loadFriends();
        }
        return friendListItems;
    }

    private void loadFriends() {
        friendListItems = friendListItemDao.getAllLive();
    }

    public LiveData<Friend> getOrcreateFriend(String public_code) {

        return repo.getSyncedFriend(public_code);

    }
    public void toggleDelete(Friend friendListItem) {
        friendListItemDao.delete(friendListItem);
    }


}
