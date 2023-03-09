package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.FriendDatabase;

import com.example.socialcompass.activity.FriendListActivity;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

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

//        List<Friend> friends = Friend.loadJSON(context,"demo_friends.json");
        friendListItemDao = testDb.getDao();
//        friendListItemDao.insertAll(friends);
    }

    @Test
    public void testAddFriend(){
        String newText = "20238026";
        ActivityScenario<FriendListActivity> scenario
                = ActivityScenario.launch(FriendListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            List<Friend> beforeFriendList = friendListItemDao.getAll();

            EditText newPublicCode = activity.findViewById(R.id.new_friend_public_code);
            Button addTodoButton = activity.findViewById(R.id.add_friend_btn);

            newPublicCode.setText(newText);
            addTodoButton.performClick();

            List<Friend> afterFriendList = friendListItemDao.getAll();
            assertEquals(beforeFriendList.size()+1,afterFriendList.size());
            assertEquals(newText,afterFriendList.get(afterFriendList.size()-1).publicCode);

        });
    }
}
