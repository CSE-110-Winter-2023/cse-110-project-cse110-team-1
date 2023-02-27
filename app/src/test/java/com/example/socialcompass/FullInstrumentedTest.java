package com.example.socialcompass;

import android.app.AlertDialog;
import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowDisplay;
import org.robolectric.shadows.ShadowLocationManager;
import org.robolectric.shadows.ShadowLooper;

import static org.junit.Assert.*;

@Config(
        instrumentedPackages = {
                // required to access final members on androidx.loader.content.ModernAsyncTask
                "androidx.loader.content"
        })

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class FullInstrumentedTest {

    @Test
    public void US3andDeveloperStoryTest() {
        // Context of the app under test.
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.onActivity(activity -> {

                //Set device location to Carlsbad Flower fields
                LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                Location newLocation = new Location(LocationManager.GPS_PROVIDER);
                newLocation.setLatitude(33.1229);
                newLocation.setLongitude(-117.3174);
                ShadowLocationManager shadowLocationManager = Shadows.shadowOf(locationManager);
                shadowLocationManager.simulateLocation(newLocation);

                //Set device orientation to facing north
                ShadowDisplay shadowDisplay = Shadows.shadowOf(activity.getWindowManager().getDefaultDisplay());
                shadowDisplay.setRotation(0);

                TextView label_0 = activity.findViewById(R.id.label_0);
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                label_0.requestFocus();
                label_0.setText("LA Home");

                Button add_location_btn = activity.findViewById(R.id.add_location_btn);
                add_location_btn.performClick();

                TextView label_1 = activity.findViewById(R.id.label_1);
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                label_1.requestFocus();
                label_1.setText("SD Home");

                Button submit_btn = activity.findViewById(R.id.submit_btn);
                submit_btn.performClick();

                // Verify that an AlertDialog is shown with the correct message
                AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
                assertNotNull(alertDialog);

                TextView messageTextView = alertDialog.findViewById(android.R.id.message);
                String actualMessage = messageTextView.getText().toString();

                String expectedMessage = "Input is not Valid, Data not saved";
                assertEquals(expectedMessage, actualMessage);

                alertDialog.dismiss();

                //LA Home is set to Westfield Topanga
                TextView longitude_0 = activity.findViewById(R.id.longitude_0);
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                longitude_0.requestFocus();
                longitude_0.setText("-118.6041");

                TextView latitude_0 = activity.findViewById(R.id.latitude_0);
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                latitude_0.requestFocus();
                latitude_0.setText("34.1903");

                //LA Home is set to Westfield UTC
                TextView longitude_1 = activity.findViewById(R.id.longitude_1);
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                longitude_1.requestFocus();
                longitude_1.setText("-117.2117");

                TextView latitude_1 = activity.findViewById(R.id.latitude_1);
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                latitude_1.requestFocus();
                latitude_1.setText("32.8711");

                add_location_btn.performClick();

                TextView label_2 = activity.findViewById(R.id.label_2);
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                label_2.requestFocus();
                label_2.setText("PH Home");

                //PH Home is set to Divisoria in the Philippines
                TextView longitude_2 = activity.findViewById(R.id.longitude_2);
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                longitude_2.requestFocus();
                longitude_2.setText("120.9732");

                TextView latitude_2 = activity.findViewById(R.id.latitude_2);
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                latitude_2.requestFocus();
                latitude_2.setText("14.6061");

                submit_btn.performClick();

                //Test if Data saved dialog is shown
                AlertDialog alertDialog2 = ShadowAlertDialog.getLatestAlertDialog();
                assertNotNull(alertDialog2);
                TextView messageTextView2 = alertDialog2.findViewById(android.R.id.message);
                String actualMessage2 = messageTextView2.getText().toString();
                String expectedMessage2 = "Data Saved";
                assertEquals(expectedMessage2, actualMessage2);
                alertDialog2.dismiss();

                //Test if compass_activity was started after submit
                Intent expectedIntent = new Intent(activity, compass_activity.class);
                ShadowActivity shadowActivity = Shadows.shadowOf(activity);
                Intent actualIntent = shadowActivity.getNextStartedActivity();
                assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());

                //Simulate device facing North
                TextView orientation_ui_mock = activity.findViewById(R.id.orientation_ui_mock);
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                orientation_ui_mock.requestFocus();
                orientation_ui_mock.setText("0");
                Button test_ui_btn = activity.findViewById(R.id.test_ui_btn);
                test_ui_btn.performClick();

                //Test Orientation and Node angle values
                compass_activity compassActivity = Robolectric.buildActivity(compass_activity.class).create().start().resume().get();
                ConstraintLayout compass_layout = compassActivity.findViewById(R.id.compass_layout);

                //Test Orientation values when device is facing North
                float rotation = compass_layout.getRotation();
                assertEquals(0f, rotation, 0.1f);

                //Test Node angle values when device is facing North
                ConstraintLayout constraintLayout = (ConstraintLayout) compassActivity.findViewById(R.id.compass_layout);
                ConstraintLayout.LayoutParams layoutParamsNode1_1 = (ConstraintLayout.LayoutParams) constraintLayout.findViewById(R.id.node_1).getLayoutParams();
                float angle1_1 = layoutParamsNode1_1.circleAngle;
                ConstraintLayout.LayoutParams layoutParamsNode1_2 = (ConstraintLayout.LayoutParams) constraintLayout.findViewById(R.id.node_2).getLayoutParams();
                float angle1_2 = layoutParamsNode1_2.circleAngle;
                ConstraintLayout.LayoutParams layoutParamsNode1_3 = (ConstraintLayout.LayoutParams) constraintLayout.findViewById(R.id.node_3).getLayoutParams();
                float angle1_3 = layoutParamsNode1_3.circleAngle;

                assertEquals(313.7062683105469, angle1_1, 0.1f);
                assertEquals(113.356689453125, angle1_2, 0.1f);
                //changed expected to correct computation, because the absolute value between the longitude of
                // Philippines and San Diego is greater than 180, which going to the opposite direction is closer
                //the original expected value was 94.38699340820312
                assertEquals(261.4665832519531, angle1_3, 0.1f);

                //Simulate turning and now the device is facing South
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                orientation_ui_mock.requestFocus();
                orientation_ui_mock.setText("180");
                test_ui_btn.performClick();

                //Test Orientation values when device is facing South

                ActivityController<compass_activity> compassActivity2Controller;
                compassActivity2Controller = Robolectric.buildActivity(compass_activity.class);
                compass_activity compassActivity2 = compassActivity2Controller.create().start().resume().get();
                ConstraintLayout compass_layout2 = compassActivity2.findViewById(R.id.compass_layout);
                float rotation2 = compass_layout2.getRotation();
                assertEquals(180f, rotation2, 0.1f);

                //Test Node angle values when device is facing South (should be the same as North)
                ConstraintLayout constraintLayout2 = (ConstraintLayout) compassActivity2.findViewById(R.id.compass_layout);
                ConstraintLayout.LayoutParams layoutParamsNode2_1 = (ConstraintLayout.LayoutParams) constraintLayout2.findViewById(R.id.node_1).getLayoutParams();
                float angle2_1 = layoutParamsNode2_1.circleAngle;
                ConstraintLayout.LayoutParams layoutParamsNode2_2 = (ConstraintLayout.LayoutParams) constraintLayout2.findViewById(R.id.node_2).getLayoutParams();
                float angle2_2 = layoutParamsNode2_2.circleAngle;
                ConstraintLayout.LayoutParams layoutParamsNode2_3 = (ConstraintLayout.LayoutParams) constraintLayout2.findViewById(R.id.node_3).getLayoutParams();
                float angle2_3 = layoutParamsNode2_3.circleAngle;

                assertEquals(313.7062683105469, angle2_1, 0.1f);
                assertEquals(113.356689453125, angle2_2, 0.1f);
                //changed expected to correct computation, because the absolute value between the longitude of
                // Philippines and San Diego is greater than 180, which going to the opposite direction is closer
                //the original expected value was 94.38699340820312
                assertEquals(261.4665832519531, angle2_3, 0.1f);


                //Simulate closing and restarting the app from the compass activity.
                compassActivity2Controller.pause().stop().destroy();
                compassActivity2Controller = Robolectric.buildActivity(compass_activity.class);
                compassActivity2 = compassActivity2Controller.create().start().resume().get();

                //Test the node angles after restarting
                ConstraintLayout constraintLayout3 = (ConstraintLayout) compassActivity2.findViewById(R.id.compass_layout);
                ConstraintLayout.LayoutParams layoutParamsNode3_1 = (ConstraintLayout.LayoutParams) constraintLayout2.findViewById(R.id.node_1).getLayoutParams();
                float angle3_1 = layoutParamsNode3_1.circleAngle;
                ConstraintLayout.LayoutParams layoutParamsNode3_2 = (ConstraintLayout.LayoutParams) constraintLayout2.findViewById(R.id.node_2).getLayoutParams();
                float angle3_2 = layoutParamsNode3_2.circleAngle;
                ConstraintLayout.LayoutParams layoutParamsNode3_3 = (ConstraintLayout.LayoutParams) constraintLayout2.findViewById(R.id.node_3).getLayoutParams();
                float angle3_3 = layoutParamsNode3_3.circleAngle;

                assertEquals(313.7062683105469, angle3_1, 0.1f);
                assertEquals(113.356689453125, angle3_2, 0.1f);
                //changed expected to correct computation, because the absolute value between the longitude of
                // Philippines and San Diego is greater than 180, which going to the opposite direction is closer
                //the original expected value was 94.38699340820312
                assertEquals(261.4665832519531, angle3_3, 0.1f);

            });
        }
    }
}