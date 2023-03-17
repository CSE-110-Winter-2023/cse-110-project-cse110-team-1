package com.example.socialcompass;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.widget.ImageView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
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
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
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

import android.graphics.ColorFilter;

import java.lang.reflect.Field;

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
import com.example.socialcompass.utility.Utilities;
import com.example.socialcompass.activity.UserActivity;

import org.junit.Rule;
@RunWith(AndroidJUnit4.class)
public class US6BDDTest {
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void testDisplayNode() throws InterruptedException {
        // add friend "utah"
        ActivityScenario<UserActivity> scenario
                = ActivityScenario.launch(UserActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        // Set test input name as John Doe
        String testInputName = "myname";

        onView(withId(R.id.my_input_name)).perform(typeText(testInputName), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.save_user_name_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.save_user_name_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.add_friend_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.compass_view_button)).perform(click());

        GetColorFilterAction getColorFilterAction = new GetColorFilterAction();
        onView(withId(R.id.status_indicator)).perform(getColorFilterAction);
        ColorFilter currentFilter = getColorFilterAction.getColorFilter();
        Thread.sleep(2000);
        PorterDuffColorFilter expectedFilter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        boolean areFiltersEqual = areColorFiltersEqual(currentFilter, expectedFilter);
        assertTrue(areFiltersEqual);
    }

    public class GetColorFilterAction implements ViewAction {
        private ColorFilter colorFilter;

        @Override
        public Matcher<View> getConstraints() {
            return ViewMatchers.isAssignableFrom(ImageView.class);
        }

        @Override
        public String getDescription() {
            return "Get color filter";
        }

        @Override
        public void perform(UiController uiController, View view) {
            ImageView imageView = (ImageView) view;
            colorFilter = imageView.getColorFilter();
        }

        public ColorFilter getColorFilter() {
            return colorFilter;
        }
    }

    public static boolean areColorFiltersEqual(ColorFilter filter1, ColorFilter filter2) {
        if (filter1 == null || filter2 == null) {
            return false;
        }

        for (Field field : filter1.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value1 = field.get(filter1);
                Object value2 = field.get(filter2);
                if (!value1.equals(value2)) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
