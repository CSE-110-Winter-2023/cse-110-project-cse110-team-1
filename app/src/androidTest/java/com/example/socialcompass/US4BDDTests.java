package com.example.socialcompass;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;

import com.example.socialcompass.activity.FriendListActivity;
import com.example.socialcompass.activity.UserActivity;
import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.FriendDatabase;

import org.junit.Before;
import org.junit.Test;

public class US4BDDTests {
    private FriendDao friendDao;
    private FriendDatabase friendDb;

    @Before
    public void preparetDatabase(){
        var context2 = ApplicationProvider.getApplicationContext();
        friendDb = Room.inMemoryDatabaseBuilder(context2, FriendDatabase.class)
                .allowMainThreadQueries().build();
        // indirectly test getDao function
        friendDao = friendDb.getDao();
        Friend testFriend1 = new Friend("1233445", "john", 32, -124);
        Friend testFriend2 = new Friend("1233446", "mary", 32, 124);

//        List<Friend> friends = Friend.toJSON(context,"demo_todos.json");
        //Insert the user into the database
        friendDao.upsert(testFriend1);
        friendDao.upsert(testFriend2);

    }
    @Test
    public void testFriendExists(){
        ActivityScenario<FriendListActivity> scenario
                = ActivityScenario.launch(FriendListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            assertTrue(friendDao.exists("1233445"));
            ViewInteraction viewInteraction = onView(withId(R.id.node_1));

            viewInteraction.check((view, noViewFoundException) -> {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                // Use the radius and angle values as needed
                int radius = layoutParams.circleRadius;
            });
        });
    }



}
