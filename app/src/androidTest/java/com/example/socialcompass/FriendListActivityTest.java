package com.example.socialcompass;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.socialcompass.activity.FriendListActivity;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.FriendDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
@RunWith(AndroidJUnit4.class)
public class FriendListActivityTest {
    FriendDatabase testDb;
    FriendDao friendListItemDao;

    @Before
    public void resetDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context,FriendDatabase.class)
                .allowMainThreadQueries()
                .build();
        FriendDatabase.inject(testDb);
        friendListItemDao = testDb.getDao();
    }

    @Test
    public void testAddFriend(){
        String newText = "forTeamOneTesting";
        ActivityScenario<FriendListActivity> scenario
                = ActivityScenario.launch(FriendListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            EditText newPublicCode = activity.findViewById(R.id.new_friend_public_code);
            Button addTodoButton = activity.findViewById(R.id.add_friend_btn);

            //the before database
            var beforeDao = friendListItemDao.getAll();


            newPublicCode.requestFocus();
            //enter a new public code
            newPublicCode.setText(newText);
            //click on ADD
            addTodoButton.performClick();

            var afterFriend = friendListItemDao.getAllLive();
            afterFriend.observe(activity,(a)->{});

            //the new Friend should be added to the database
            CountDownLatch latch = new CountDownLatch(10);
            try {
                if (!latch.await(6, TimeUnit.SECONDS)) {
                    // we manually stop all threads and resume activity
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("something is extremely wrong with this");
            }
            newPublicCode.requestFocus();
            String currentText = String.valueOf(newPublicCode.getText());

            Log.d("AFTER_COUNTDOWN", currentText);
        });
    }
}
