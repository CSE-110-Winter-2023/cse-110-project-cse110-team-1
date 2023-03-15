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
import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.UserActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class MS2DeveloperStoryTest {
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    @Test
    public void developerStoryTest() throws InterruptedException {
        ActivityScenario<UserActivity> scenario
                = ActivityScenario.launch(UserActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        // Set test input name as John Doe
        String testInputName = "John Doe";
        AtomicReference<String> actualDisplayName = new AtomicReference<>("");
        AtomicBoolean case_allready_exist = new AtomicBoolean(false);
        AtomicBoolean atomicFalse = new AtomicBoolean(false);
        AtomicBoolean atomicTrue = new AtomicBoolean(true);
        // start testing on this scenario
        scenario.onActivity(activity -> {
            EditText newInputName = activity.findViewById(R.id.my_input_name);
            Button saveButton = activity.findViewById(R.id.save_user_name_btn);
            if(newInputName.getText()==null){
                newInputName.setText(testInputName);
            }else{
                case_allready_exist.set(true);
                actualDisplayName.set(String.valueOf(newInputName.getText()));
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            saveButton.performClick();


        });

        onView(withId(R.id.new_friend_public_code)).perform(typeText("313757"), closeSoftKeyboard());
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
}
