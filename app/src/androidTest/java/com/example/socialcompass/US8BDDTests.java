package com.example.socialcompass;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalMatchers.not;

import android.Manifest;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.FriendListActivity;
import com.example.socialcompass.activity.UserActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class US8BDDTests {
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityScenarioRule<UserActivity> activityScenarioRule =
            new ActivityScenarioRule<>(UserActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void teardown() {
        Intents.release();
    }

    /**
     * Test whether a friend within range has their label displayed
     * @throws InterruptedException
     */
    @Test
    public void testCloseNodeDisplay() throws InterruptedException {
        // enter username and pass to the friend list
        String testInputName = "John Doe";
        onView(withId(R.id.my_input_name)).perform(typeText(testInputName), closeSoftKeyboard());
        onView(withId(R.id.save_user_name_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.save_user_name_btn)).perform(click());

        // add a friend to the friend list
        onView(withId(R.id.new_friend_public_code)).perform(typeText("ucsdclose0"), closeSoftKeyboard());
        onView(withId(R.id.add_friend_btn)).perform(click());
        Thread.sleep(1000);

        // move to the compass view
        onView(withId(R.id.compass_view_button)).perform(click());
        Thread.sleep(5000);

        // check whether we have the node label displayed or not
        // at default zoom level, we should see the label and no node
        onView(withTagValue(is("label_0"))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withTagValue(is("label_0"))).check(matches(isDisplayed()));
        onView(withTagValue(is("label_0"))).check((matches((withText("very close ucsd 0")))));
    }

    /**
     * test whether a friend very far away is displayed as a node without text
     * @throws InterruptedException
     */
    @Test
    public void testFarNodeDisplay() throws InterruptedException{
        // enter username and pass to the friend list
        String testInputName = "John Doe";
        onView(withId(R.id.my_input_name)).perform(typeText(testInputName), closeSoftKeyboard());
        onView(withId(R.id.save_user_name_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.save_user_name_btn)).perform(click());

        // add a friend to the friend list
        onView(withId(R.id.new_friend_public_code)).perform(typeText("ucsdsuperfar"), closeSoftKeyboard());
        onView(withId(R.id.add_friend_btn)).perform(click());
        Thread.sleep(1000);

        // move to the compass view
        onView(withId(R.id.compass_view_button)).perform(click());
        Thread.sleep(5000);

        // check whether we have the node label displayed or not
        // at default zoom level, we should only see a node, and no label
        onView(withTagValue(is("node_0"))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withTagValue(is("node_0"))).check(matches(isDisplayed()));
    }

    /**
     * test when a friend change from node to label when zoom out
     * @throws InterruptedException
     */
    @Test
    public void testNodeDisplayOnChange() throws InterruptedException{
        // enter username and pass to the friend list
        String testInputName = "John Doe";
        onView(withId(R.id.my_input_name)).perform(typeText(testInputName), closeSoftKeyboard());
        onView(withId(R.id.save_user_name_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.save_user_name_btn)).perform(click());

        // add a friend to the friend list
        onView(withId(R.id.new_friend_public_code)).perform(typeText("ucsdclose2"), closeSoftKeyboard());
        onView(withId(R.id.add_friend_btn)).perform(click());
        Thread.sleep(1000);

        // move to the compass view
        onView(withId(R.id.compass_view_button)).perform(click());
        Thread.sleep(5000);

        // at default zoom level, we should only see a node, and no label
        onView(withTagValue(is("node_0"))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withTagValue(is("node_0"))).check(matches(isDisplayed()));

        // zoom out one level
        onView(withId(R.id.zoom_out_button)).perform(click());
        Thread.sleep(1000);
        // at bigger zoom level, we should see the label being display here
        onView(withTagValue(is("label_0"))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withTagValue(is("label_0"))).check(matches(isDisplayed()));
        onView(withTagValue(is("label_0"))).check((matches((withText("relatively close ucsd 2")))));

    }


}
