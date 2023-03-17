package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.Button;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import com.example.socialcompass.activity.CompassActivity;
import com.example.socialcompass.activity.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ZoomTest {
    @Test
    public void testZoomButtons() {
        try (ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class)) {
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.onActivity(activity -> {


                //check oncreate zoom view is the second layer (with only two rings)
                View imageView1 = activity.findViewById(R.id.imageView1);
                View imageView2 = activity.findViewById(R.id.imageView2);
                View imageView3 = activity.findViewById(R.id.imageView3);
                View imageView4 = activity.findViewById(R.id.imageView4);


                assertEquals(View.INVISIBLE, imageView1.getVisibility());
                assertEquals(View.VISIBLE, imageView2.getVisibility());
                assertEquals(View.INVISIBLE, imageView3.getVisibility());
                assertEquals(View.INVISIBLE, imageView4.getVisibility());

                //check when zoom in once, view is the first layer (with only one ring)
                Button zoom_in_button = activity.findViewById(R.id.zoom_in_button);
                zoom_in_button.performClick();

                assertEquals(View.VISIBLE, imageView1.getVisibility());
                assertEquals(View.INVISIBLE, imageView2.getVisibility());
                assertEquals(View.INVISIBLE, imageView3.getVisibility());
                assertEquals(View.INVISIBLE, imageView4.getVisibility());

                //check zoom in again, nothing changes(button disabled)
                zoom_in_button.performClick();

                assertEquals(View.VISIBLE, imageView1.getVisibility());
                assertEquals(View.INVISIBLE, imageView2.getVisibility());
                assertEquals(View.INVISIBLE, imageView3.getVisibility());
                assertEquals(View.INVISIBLE, imageView4.getVisibility());

                //check zoom out twice, on third layer (with three rings)
                Button zoom_out_button = activity.findViewById(R.id.zoom_out_button);
                zoom_out_button.performClick();
                zoom_out_button.performClick();

                assertEquals(View.INVISIBLE, imageView1.getVisibility());
                assertEquals(View.INVISIBLE, imageView2.getVisibility());
                assertEquals(View.VISIBLE, imageView3.getVisibility());
                assertEquals(View.INVISIBLE, imageView4.getVisibility());

                // should be on forth layer (with four rings)
                zoom_out_button.performClick();

                assertEquals(View.INVISIBLE, imageView1.getVisibility());
                assertEquals(View.INVISIBLE, imageView2.getVisibility());
                assertEquals(View.INVISIBLE, imageView3.getVisibility());
                assertEquals(View.VISIBLE, imageView4.getVisibility());

                // click zoom out again, should be no change (button disabled)
                assertEquals(View.INVISIBLE, imageView1.getVisibility());
                assertEquals(View.INVISIBLE, imageView2.getVisibility());
                assertEquals(View.INVISIBLE, imageView3.getVisibility());
                assertEquals(View.VISIBLE, imageView4.getVisibility());
            });
        }
    }
}
