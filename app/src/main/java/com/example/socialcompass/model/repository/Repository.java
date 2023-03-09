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
    private MediatorLiveData<Long> timeData;

    public Repository(FriendDao dao) {
        this.dao = dao;
    }

    // Synced Methods
    // ==============

    //Used to get observe changes in single friend location remotely
    public LiveData<Friend> getSyncedFriend(String public_code) {
        var friend = new MediatorLiveData<Friend>();
        var stuff = new MediatorLiveData<String>();

        Observer<Friend> updateFromRemote = theirFriend -> {
            var ourFriend = friend.getValue();
            Log.d("SYNC_FRIEND", "get sync called");
            if (theirFriend == null) return; // do nothing
            if (ourFriend == null || ourFriend.updatedAt < theirFriend.updatedAt) {
                upsertLocal(theirFriend);
            }
        };

        Observer<Long> superDummy = theirFriend -> {
            Log.d("SYNC_DUMMY", "Super dummy called with " + theirFriend.toString());
        };


        // If we get a local update, pass it on.
        friend.addSource(getLocalFriend(public_code), friend::postValue);
        // If we get a remote update, update the local version (triggering the above observer)
        friend.addSource(getRemote(public_code), updateFromRemote);

        dummy();
        stuff.addSource(timeData, superDummy);

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

//        if (this.poller != null && !this.poller.isCancelled()) {
//            poller.cancel(true);
//        }

//        API api = new API();
//        MutableLiveData<Friend> friendData = new MutableLiveData<>();
//
////        AsyncTask.execute(new Runnable() {
////            @Override
////            public void run() {
////                Log.d("GET_REMOTE", "get remote is called");
////                var friend = api.getFriend(public_code);
////                Log.d("GET_REMOTE", friend.toString());
////                friendData.postValue(friend);
////            }
////        });
//
//        // Update every 3 seconds with executor
//        ScheduledExecutorService btExec = Executors.newScheduledThreadPool(1);
//
//        Runnable getFriends = () -> friendData.postValue(api.getFriend(public_code));
//        this.poller = btExec.scheduleAtFixedRate(getFriends, (long) 3, (long) 3, TimeUnit.SECONDS);
//
//        return friendData;

        API api = new API();
        var executor = Executors.newSingleThreadScheduledExecutor();
        MutableLiveData<Friend> friend = new MutableLiveData<>();


        executor.scheduleAtFixedRate(() -> {
            Friend fetchedFriend = api.getFriend(public_code);
            friend.postValue(fetchedFriend);
        }, 0, 3000, TimeUnit.MILLISECONDS);


        return friend;
    }

    //Used to update Self location
    public void upsertRemote(Friend user, String privateCode) {

        API api = new API();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                api.putUser(user, privateCode);
            }
        });
    }

    public void dummy() {
        var executor = Executors.newSingleThreadScheduledExecutor();
//        MutableLiveData<Long> dumm = new MutableLiveData<>();


        executor.scheduleAtFixedRate(() -> {
            Log.d("DUMMY_CALL", "DUMMY 1");
            timeData.postValue(System.currentTimeMillis());
            Log.d("DUMMY_CALL", "DUMMY 2");
        }, 0, 3000, TimeUnit.MILLISECONDS);

    }
}