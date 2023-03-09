package com.example.socialcompass.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.activity.FriendListActivity;
import com.example.socialcompass.model.api.API;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDatabase;
import com.example.socialcompass.model.repository.Repository;
import com.example.socialcompass.utility.Utilities;

import java.util.List;

public class FriendListViewModel extends AndroidViewModel {
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

    public LiveData<Friend> createFriend(String public_code) {
          return repo.getSyncedFriend(public_code);
//        Log.d("friend returnd by api",friend.getValue().label);

//        Log.d("friend object returnd by server",friend.getValue().label);
//        if(friend != null){
//            String friendLabel = friend.getValue().label;
//            Friend newItem = new Friend(public_code, "friendLabel", 12, 13);
//            friendListItemDao.upsert(newItem);
//        }


    }
    public void toggleDelete(Friend friendListItem) {
        friendListItemDao.delete(friendListItem);
    }


}
