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

import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.UserActivity;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class US1BDDTests {

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    /*
    * BDD scenario 1:
    * when user enter valid name "john doe",
    * then "john doe should appear in the top of another page"
    * */
    @Test
    public void testValidInput_shouldSaveUserInformation() {
        // evoke UserActivity.class
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
        // check the view displayed
        if(case_allready_exist==atomicFalse){
            onView(withId(R.id.my_name_from_friendlist)).check(matches(withText(testInputName)));
        }
    }

    @Test
    public void testInvalidInput_shouldShowAlert() {
        // Arrange
        ActivityScenario<UserActivity> scenario
                = ActivityScenario.launch(UserActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        // Set test input name as blank
        String testInputName = "";

        scenario.onActivity(activity -> {
            EditText newInputName = activity.findViewById(R.id.my_input_name);
            Button saveButton = activity.findViewById(R.id.save_user_name_btn);
            newInputName.setText(testInputName);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            saveButton.performClick();
        });

        // Verify that the alert dialog is displayed with the correct message
        onView(withText("Please enter your name"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }


}
