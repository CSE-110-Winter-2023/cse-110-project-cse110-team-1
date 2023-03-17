package com.example.socialcompass;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.socialcompass.activity.FriendListActivity;
import com.example.socialcompass.utility.Utilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FriendListActivityTest {
    String newText = "forTeamOneTesting";

    @Rule
    public ActivityScenarioRule<FriendListActivity> activityScenarioRule =
            new ActivityScenarioRule<>(FriendListActivity.class);

    @Before
    public void resetDatabase() {
        Intents.init();
    }

    @After
    public void teardown() {
        Intents.release();
    }

    @Test
    public void testAddFriend() {
        onView(withId(R.id.new_friend_public_code)).perform(typeText(newText), closeSoftKeyboard());
        onView(withId(R.id.add_friend_btn)).perform(click());
        // sleep for a bit, waiting for the data from API
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // test whether the first item on the list is the correct friend
        onView(withId(R.id.friend_items))
                .check(matches(Utilities.atPosition(0, hasDescendant(withText("forTeamOneTesting")))));
    }



}
