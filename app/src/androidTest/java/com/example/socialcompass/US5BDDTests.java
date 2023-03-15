package com.example.socialcompass;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.*;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.CompassActivity;
import com.example.socialcompass.activity.FriendListActivity;
import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.FriendDatabase;
import com.example.socialcompass.utility.Utilities;

@RunWith(AndroidJUnit4.class)
public class US5BDDTests {
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Test
    public void testDisplayNode() throws InterruptedException {
        // add friend "utah"
        ActivityScenario<FriendListActivity> scenario
                = ActivityScenario.launch(FriendListActivity.class);

        //onView(withId(R.id.delete_btn)).perform(click());
        onView(withId(R.id.new_friend_public_code)).perform(typeText("utah"), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.add_friend_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.compass_view_button)).perform(click());
        // link to compass activity

//        ActivityScenario<CompassActivity> scenario1
//                = ActivityScenario.launch(CompassActivity.class);
//        scenario1.moveToState(Lifecycle.State.RESUMED);

        // Verify that the correct number of friends is displayed on the compass view
        Thread.sleep(5000);
        int numberOfFriends = 1;
        for (int i = 1; i <= numberOfFriends; i++) {
            ViewInteraction friendViewInteraction = onView(
                    allOf(withId(R.id.compass_layout)));
            friendViewInteraction.check(matches(isDisplayed()));
            onView(withTagValue(is((Object) "node_0"))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withTagValue(is((Object) "node_0"))).check(matches(isDisplayed()));
        }

    }



    @Test
    public void testDisplayLabel() throws InterruptedException {
        ActivityScenario<FriendListActivity> scenario
                = ActivityScenario.launch(FriendListActivity.class);

        onView(withId(R.id.new_friend_public_code)).perform(typeText("utah"), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.add_friend_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.compass_view_button)).perform(click());
        Thread.sleep(5000);

        onView(withId(R.id.zoom_out_button)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.zoom_out_button)).perform(click());
        Thread.sleep(1000);

        int numberOfFriends = 1;
        for (int i = 1; i <= numberOfFriends; i++) {
            ViewInteraction friendViewInteraction = onView(
                    allOf(withId(R.id.compass_layout)));
            friendViewInteraction.check(matches(isDisplayed()));
            onView(withTagValue(is((Object) "label_0"))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withTagValue(is((Object) "label_0"))).check(matches(isDisplayed()));

            ViewInteraction labelInteraction = onView(withTagValue(is((Object) "label_0")));
            CharSequence labelText = getTextFromTextView(labelInteraction);
            assertEquals("Utah\n534mi",labelText);
        }
    }

    private CharSequence getTextFromTextView(ViewInteraction viewInteraction) {
        final CharSequence[] textHolder = new CharSequence[1];

        viewInteraction.perform(actionWithAssertions(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView) view;
                textHolder[0] = textView.getText();
            }
        }));

        return textHolder[0];
    }
}
