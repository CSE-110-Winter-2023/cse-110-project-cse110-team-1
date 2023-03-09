package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LiveData;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.socialcompass.R;
import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.FriendDatabase;
import com.example.socialcompass.old.GPSLocationHandler;
import com.example.socialcompass.old.OrientationService;
import com.example.socialcompass.utility.Utilities;

import java.util.List;

public class CompassActivity extends AppCompatActivity {
    private Icon nodeIcon;

    public synchronized void redrawAllFriends() {
        if(locationService.getLocation().getValue() == null
        || orientationService.getOrientation().getValue() == null
        || friendsList == null) return;

        // TODO synchronize against the activity drawing?
        ConstraintLayout layout = findViewById(R.id.compass_layout);
//        layout.removeAllViews();
        ConstraintSet cs = new ConstraintSet();
        cs.clone(layout);

        Log.d("CompassView","Redraw "+friendsList.getValue());

        Pair<Double, Double> loc = locationService.getLocation().getValue();

        final float gpsLat = loc.first.floatValue(),
                    gpsLon = loc.second.floatValue();

        for(Friend f : friendsList.getValue()) {
            ImageView node = new ImageView(getApplicationContext());
            node.setImageIcon(nodeIcon);
            node.setLayoutParams(new LinearLayout.LayoutParams(20,20));
            node.setId(View.generateViewId());
            layout.addView(node);

            cs.constrainCircle(node.getId(), R.id.compass_img, 462, Utilities.getAngle(gpsLat, gpsLon, f.latitude, f.longitude));
        }

        cs.applyTo(layout);

//        Log.d("Layout", String.valueOf(layout.getChildCount()));
    }

    private GPSLocationHandler locationService;
    private OrientationService orientationService;

    private LiveData<List<Friend>> friendsList;

    private FriendDao friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass_new);

        nodeIcon = Icon.createWithResource(getApplicationContext(), R.drawable.address_node);

        friends = FriendDatabase.provide(getApplicationContext()).getDao();
        friendsList = friends.getAllLive();
        friendsList.observe(this, (a) -> { this.redrawAllFriends(); });

        locationService = new GPSLocationHandler(this);
        orientationService = new OrientationService(this);

        orientationService.getOrientation().observe(this, (a) -> { this.redrawAllFriends(); });
        locationService.getLocation().observe(this, (a) -> { this.redrawAllFriends(); });
    }
}