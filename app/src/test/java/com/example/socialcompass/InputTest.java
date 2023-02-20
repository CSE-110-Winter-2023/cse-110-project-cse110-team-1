package com.example.socialcompass;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class InputTest {
    /*
     * test how many input field are visible at the start
     * */
    @Test
    public void test_initial_field_count() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.onActivity(activity -> {
                View inputGroup0 = activity.findViewById(R.id.input_group_0);
                View inputGroup1 = activity.findViewById(R.id.input_group_1);
                View inputGroup2 = activity.findViewById(R.id.input_group_2);

                assertEquals(View.VISIBLE, inputGroup0.getVisibility());
                assertEquals(View.GONE, inputGroup1.getVisibility());
                assertEquals(View.GONE, inputGroup2.getVisibility());
            });
        }
    }

    /*
    * test add new input fields by click on "Add location" button
    * */
    @Test
    public void test_add_new_input_field(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.onActivity(activity -> {
                View inputGroup0 = activity.findViewById(R.id.input_group_0);
                View inputGroup1 = activity.findViewById(R.id.input_group_1);
                View inputGroup2 = activity.findViewById(R.id.input_group_2);
                Button addLocationButton = activity.findViewById(R.id.add_location_btn);

                // button clicked once
                addLocationButton.performClick();
                assertEquals(View.VISIBLE, inputGroup0.getVisibility());
                assertEquals(View.VISIBLE, inputGroup1.getVisibility());
                assertEquals(View.GONE, inputGroup2.getVisibility());

                // button clicked twice
                addLocationButton.performClick();
                assertEquals(View.VISIBLE, inputGroup0.getVisibility());
                assertEquals(View.VISIBLE, inputGroup1.getVisibility());
                assertEquals(View.VISIBLE, inputGroup2.getVisibility());
            });
        }
    }

    @Test
    public void test_submit_valid_value() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.onActivity(activity -> {
                Button addLocationButton = activity.findViewById(R.id.add_location_btn);
                Button submitButton = activity.findViewById(R.id.submit_btn);
                SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
                // button clicked twice to display all fields
                addLocationButton.performClick();
                addLocationButton.performClick();

                // first input field
                TextView label0 = activity.findViewById(R.id.label_0);
                TextView long0 = activity.findViewById(R.id.longitude_0);
                TextView lat0 = activity.findViewById(R.id.latitude_0);

                // second input field
                TextView label1 = activity.findViewById(R.id.label_1);
                TextView long1 = activity.findViewById(R.id.longitude_1);
                TextView lat1 = activity.findViewById(R.id.latitude_1);

                // third, and the last input field
                TextView label2 = activity.findViewById(R.id.label_2);
                TextView long2 = activity.findViewById(R.id.longitude_2);
                TextView lat2 = activity.findViewById(R.id.latitude_2);

                // write to first input field
                label0.setText("home");
                long0.setText("-32.2331558");
                lat0.setText("36.1651525");

                // write to second input field
                label1.setText("family");
                long1.setText("-65.26465");
                lat1.setText("-3.235451");

                // write to last input field
                label2.setText("friend");
                long2.setText("28.316582");
                lat2.setText("77.46456516");

                // submit the value
                submitButton.performClick();

                // test the values stored in storage
                String firstLabel = preferences.getString("label_0", "Label");
                String secondLabel = preferences.getString("longitude_0", "longitude");



            });
        }
    }



}
