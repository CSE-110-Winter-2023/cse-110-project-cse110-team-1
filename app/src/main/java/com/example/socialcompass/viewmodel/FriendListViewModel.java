package com.example.socialcompass.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDatabase;

import java.util.List;

public class FriendListViewModel extends AndroidViewModel {
    private LiveData<List<Friend>> friendListItems;
    private final FriendDao friendListItemDao;


    public FriendListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        FriendDatabase db = FriendDatabase.provide(context);
        friendListItemDao = db.getDao();
    }

    public LiveData<List<Friend>> getFriendListItems(){
        if(friendListItems == null){
            loadFriends();
        }
        return friendListItems;
    }

    private void loadFriends() {
        friendListItems = friendListItemDao.getAllLive();
    }

    public void createFriend(String public_code){
        int endOfListOrder = friendListItemDao.getOrderForAppend();
        Friend newItem = new Friend(public_code,"default name",12,13,endOfListOrder);
        friendListItemDao.upsert( newItem);
    }

    public void toggleDelete(Friend friendListItem){
        friendListItemDao.delete(friendListItem);
    }


}
