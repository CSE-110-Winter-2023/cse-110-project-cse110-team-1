package com.example.socialcompass;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.socialcompass.activity.FriendListActivity;
import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.FriendDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
@RunWith(AndroidJUnit4.class)
public class US2BDDTests {
    private FriendDao friendDao;
    private FriendDatabase friendDb;

    @Before
    public void clearFriendList(){
        // clear all pre-existing friends
        var context2 = ApplicationProvider.getApplicationContext();
        friendDb = Room.inMemoryDatabaseBuilder(context2, FriendDatabase.class)
                .allowMainThreadQueries().build();
        // indirectly test getDao function
        friendDao = friendDb.getDao();


    }

    /*Test valid friend insertion*/
    @Test
    public void testAddValidFriend(){
        ActivityScenario<FriendListActivity> scenario
                = ActivityScenario.launch(FriendListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            // first, clear the list
            View listItemView = activity.findViewById(R.id.friend_items);
            TextView deleteButton = listItemView.findViewById(R.id.delete_btn);
            if(deleteButton!=null){
                deleteButton.performClick();
            }

            EditText newFriendPCode = activity.findViewById(R.id.new_friend_public_code);
            Button addButton = activity.findViewById(R.id.add_friend_btn);

            var beforeDao = friendDao.getAll();
            newFriendPCode.setText("20238026");
            addButton.performClick();
            var afterDao = friendDao.getAllLive();
            afterDao.observe(activity, (a) -> {
                if (a.size() != beforeDao.size()) {
                    assertEquals(beforeDao.size(),a.size());
                }
            });

            TextView thisFriend = listItemView.findViewById(R.id.friend_name);
            assertEquals("Point Nemo",thisFriend.getText().toString());
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
