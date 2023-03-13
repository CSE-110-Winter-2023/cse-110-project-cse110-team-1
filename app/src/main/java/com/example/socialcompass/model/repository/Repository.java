package com.example.socialcompass.model.repository;

import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.api.API;


import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Repository {

    private final FriendDao dao;
    private ScheduledFuture<?> poller;
    private final MutableLiveData<Friend> realNoteData;
    private MediatorLiveData<Long> timeData;

    public Repository(FriendDao dao) {
        this.dao = dao;
        realNoteData = new MutableLiveData<>();
    }

    // Synced Methods
    // ==============

    //Used to get observe changes in single friend location remotely
    public LiveData<Friend> getSyncedFriend(String public_code) {
        var friend = new MediatorLiveData<Friend>();

        Observer<Friend> updateFromRemote = theirFriend -> {
            var ourFriend = friend.getValue();
            Log.d("SYNC_FRIEND", "get sync called");
            if (theirFriend == null) return; // do nothing
            if (ourFriend == null || ourFriend.updatedAt < theirFriend.updatedAt) {
                upsertLocal(theirFriend);
            }
        };




        // If we get a local update, pass it on.
        friend.addSource(getLocalFriend(public_code), friend::postValue);
        // If we get a remote update, update the local version (triggering the above observer)
        friend.addSource(getRemote(public_code), updateFromRemote);


        return friend;
    }


    // Local Methods
    // =============

    public LiveData<Friend> getLocalFriend(String public_code) {
        return dao.get(public_code);
    }

    public LiveData<List<Friend>> getAllLiveLocalFriends() {
        return dao.getAllLive();
    }

    public List<Friend> getAllLocalFriends() {
        return dao.getAll();
    }

    public void upsertLocal(Friend friend) {
        dao.upsert(friend);
    }

    public boolean existsLocal(String public_code) {
        return dao.exists(public_code);
    }

    // Remote Methods
    // ==============

    public LiveData<Friend> getRemote(String public_code) {

        API api = API.provide();
        var executor = Executors.newSingleThreadScheduledExecutor();


        executor.scheduleAtFixedRate(() -> {
            Friend fetchedNote = api.getFriend(public_code);
            realNoteData.postValue(fetchedNote);
        }, 0, 3000, TimeUnit.MILLISECONDS);
        return realNoteData;
    }

    //Used to update Self location
    public void upsertRemote(Friend user, String privateCode) {

        API api = API.provide();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                api.putUser(user, privateCode);
            }
        });
    }


}