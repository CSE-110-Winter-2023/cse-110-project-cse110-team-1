package com.example.socialcompass;

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

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.FriendListActivity;
import com.example.socialcompass.activity.UserActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class MS2DeveloperStoryTest {
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void resetDatabase() {
        Intents.init();
    }

    @After
    public void teardown() {
        Intents.release();
    }


    @Test
    public void developerStoryTest() throws InterruptedException {
        ActivityScenario<UserActivity> scenario
                = ActivityScenario.launch(UserActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        // Set test input name as John Doe
        String testInputName = "John Doe";

        onView(withId(R.id.my_input_name)).perform(typeText(testInputName), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.save_user_name_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.save_user_name_btn)).perform(click());
        onView(withId(R.id.my_name_from_friendlist)).check(matches(withText(testInputName)));


        onView(withId(R.id.new_friend_public_code)).perform(typeText("mydream"), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.add_friend_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.new_friend_public_code)).perform(typeText("337891"), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.add_friend_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.new_friend_public_code)).perform(typeText("273530"), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.add_friend_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.compass_view_button)).perform(click());
        Thread.sleep(10000);
        onView(withId(R.id.imageView1)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.imageView2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.imageView3)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.imageView4)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));


        int numberOfFriends = 3;
        for (int i = 0; i < numberOfFriends; i++) {
            String nodeName = "node_"+i;
            ViewInteraction friendViewInteraction = onView(
                    allOf(withId(R.id.compass_layout)));
            friendViewInteraction.check(matches(isDisplayed()));
            onView(withTagValue(is((Object) nodeName))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withTagValue(is((Object) nodeName))).check(matches(isDisplayed()));
        }
        onView(withId(R.id.zoom_in_button)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.imageView1)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.imageView2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.imageView3)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.imageView4)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        for (int i = 0; i < numberOfFriends; i++) {
            String nodeName = "node_"+i;
            ViewInteraction friendViewInteraction = onView(
                    allOf(withId(R.id.compass_layout)));
            friendViewInteraction.check(matches(isDisplayed()));
            onView(withTagValue(is((Object) nodeName))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withTagValue(is((Object) nodeName))).check(matches(isDisplayed()));
        }
        onView(withId(R.id.zoom_out_button)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.zoom_out_button)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.imageView1)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.imageView2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.imageView3)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.imageView4)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        for (int i = 1; i <= numberOfFriends; i++) {
            ViewInteraction friendViewInteraction = onView(
                    allOf(withId(R.id.compass_layout)));
            friendViewInteraction.check(matches(isDisplayed()));
            onView(withTagValue(is((Object) "node_0"))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withTagValue(is((Object) "node_0"))).check(matches(isDisplayed()));


            onView(withTagValue(is((Object) "label_1"))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withTagValue(is((Object) "label_1"))).check(matches(isDisplayed()));
            onView(withTagValue(is((Object) "label_2"))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withTagValue(is((Object) "label_2"))).check(matches(isDisplayed()));
        }

        onView(withId(R.id.zoom_out_button)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.imageView1)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.imageView2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.imageView3)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.imageView4)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        for (int i = 0; i < numberOfFriends; i++) {
            String labelName = "label_"+i;
            ViewInteraction friendViewInteraction = onView(
                    allOf(withId(R.id.compass_layout)));
            friendViewInteraction.check(matches(isDisplayed()));
            onView(withTagValue(is((Object) labelName))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withTagValue(is((Object) labelName))).check(matches(isDisplayed()));
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
