package com.example.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowDialog;


@RunWith(RobolectricTestRunner.class)
public class InputTest {
    /*
     * test how many input field are visible at the start
     * */
    @Test
    public void testInitialFieldCount() {
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
    public void testAddNewInputField() {
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

    /*
     * test the data stored in sharedPreference
     * */
    @Test
    public void testSubmitValidValue() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.onActivity(activity -> {
                Button addLocationButton = activity.findViewById(R.id.add_location_btn);
                Button submitButton = activity.findViewById(R.id.submit_btn);
                SharedPreferences preferences = activity.getSharedPreferences("MS1_PREFS", Context.MODE_PRIVATE);
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
                assertEquals("home", preferences.getString("label_0", "Label"));
                assertEquals("-32.2331558", preferences.getString("longitude_0", "Longitude"));
                assertEquals("36.1651525", preferences.getString("latitude_0", "Latitude"));

                assertEquals("family", preferences.getString("label_1", "Label"));
                assertEquals("-65.26465", preferences.getString("longitude_1", "Longitude"));
                assertEquals("-3.235451", preferences.getString("latitude_1", "Latitude"));


                assertEquals("friend", preferences.getString("label_2", "Label"));
                assertEquals("28.316582", preferences.getString("longitude_2", "Longitude"));
                assertEquals("77.46456516", preferences.getString("latitude_2", "Latitude"));

            });
        }
    }

    @Test
    public void testInvalidFormSubmission() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.onActivity(activity -> {
                Button addLocationButton = activity.findViewById(R.id.add_location_btn);
                Button submitButton = activity.findViewById(R.id.submit_btn);
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

                // FIRST TEST: write only to first and second input field
                label0.setText("home");
                long0.setText("-32.2331558");
                lat0.setText("36.1651525");

                label1.setText("family");
                long1.setText("-65.26465");
                lat1.setText("-3.235451");

                // submit the value
                submitButton.performClick();

                // inspect the popup alert dialog message
                var dialog = ShadowDialog.getLatestDialog();
                assertTrue(dialog.isShowing());
                TextView tv = dialog.findViewById(android.R.id.message);
                assertEquals("Input is not Valid, Data not saved",
                        tv.getText());

                // close the dialog
                dialog.dismiss();


                // SECOND TEST: write without label value
                label0.setText("home");
                long0.setText("-32.2331558");
                lat0.setText("36.1651525");

                label1.setText("family");
                long1.setText("-65.26465");
                lat1.setText("-3.235451");

                label2.setText("");
                long2.setText("28.316582");
                lat2.setText("77.46456516");

                // submit the value
                submitButton.performClick();

                // inspect the popup alert dialog message
                dialog = ShadowDialog.getLatestDialog();
                assertTrue(dialog.isShowing());
                tv = dialog.findViewById(android.R.id.message);
                assertEquals("Input is not Valid, Data not saved",
                        tv.getText());


                // THIRD TEST: write without location value
                label0.setText("home");
                long0.setText("-32.2331558");
                lat0.setText("36.1651525");

                label1.setText("family");
                long1.setText("-65.26465");
                lat1.setText("-3.235451");

                label2.setText("friend");
                long2.setText("");
                lat2.setText("77.46456516");

                // submit the value
                submitButton.performClick();

                // inspect the popup alert dialog message
                dialog = ShadowDialog.getLatestDialog();
                assertTrue(dialog.isShowing());
                tv = dialog.findViewById(android.R.id.message);
                assertEquals("Input is not Valid, Data not saved",
                        tv.getText());
            });
        }
    }


}

