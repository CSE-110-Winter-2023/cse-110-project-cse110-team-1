package com.example.socialcompass.FriendViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.Friendmodel.FriendDatabase;
import com.example.socialcompass.Friendmodel.FriendListItem;
import com.example.socialcompass.Friendmodel.FriendListItemDao;

import java.util.List;

public class FriendListViewModel extends AndroidViewModel {
    private LiveData<List<FriendListItem>> friendListItems;
    private final FriendListItemDao friendListItemDao;


    public FriendListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        FriendDatabase db = FriendDatabase.getSingleton(context);
        friendListItemDao = db.friendListItemDao();
    }

    public LiveData<List<FriendListItem>> getFriendListItems(){
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
        FriendListItem newItem = new FriendListItem("default name",public_code,"231","312",endOfListOrder);
        friendListItemDao.insert( newItem);
    }

    public void toggleDelete(FriendListItem friendListItem){
        friendListItemDao.delete(friendListItem);
    }


}
