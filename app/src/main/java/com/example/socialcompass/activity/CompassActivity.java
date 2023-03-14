package com.example.socialcompass.activity;

import static java.lang.String.valueOf;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.R;
import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.FriendDatabase;
import com.example.socialcompass.old.GPSLocationHandler;
import com.example.socialcompass.old.OrientationService;
import com.example.socialcompass.utility.Utilities;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CompassActivity extends AppCompatActivity {
    private Icon nodeIcon;
    private int displayCompass;

    public synchronized void redrawAllFriends() {
        if(locationService.getLocation().getValue() == null
        || orientationService.getOrientation().getValue() == null
        || friendsList == null) return;

        // TODO synchronize against the activity drawing?
        ConstraintLayout layout = findViewById(R.id.compass_layout);
        layout.removeAllViews();

        ImageView compassimg = findViewById(R.id.compass_img);

        Log.d("CompassView","Redraw "+friendsList.getValue());

        Pair<Double, Double> loc = locationService.getLocation().getValue();

        final float gpsLat = loc.first.floatValue(),
                    gpsLon = loc.second.floatValue();

        List<ImageView> nodes = new ArrayList<>();
        List<TextView> labels = new ArrayList<>();
        List<Friend> friends = friendsList.getValue();

        for(int i = nodes.size(); i < friends.size(); i++) {
            ImageView node = new ImageView(getApplicationContext());
            node.setImageIcon(nodeIcon);
            node.setId(View.generateViewId());
            node.setLayoutParams(new LinearLayout.LayoutParams(50,50));
            layout.addView(node);

            TextView text = new TextView(getApplicationContext());
            text.setId(View.generateViewId());
            layout.addView(text);

            labels.add(text);
            nodes.add(node);
        }

        ConstraintSet cs = new ConstraintSet();
        cs.clone(layout);

        int i;
        for(i = 0; i < friends.size(); i++) {
            Friend f = friends.get(i);
            float angle = Utilities.getAngle(gpsLat, gpsLon, f.latitude, f.longitude);

            labels.get(i).setText( String.format("%s\n%.0fmi",f.label, Utilities.calculateDistanceInMiles(gpsLat, gpsLon, f.latitude, f.longitude)));
            double dist_in_mile = Utilities.calculateDistanceInMiles(gpsLat, gpsLon, f.latitude, f.longitude);
            int dist_in_scale = Utilities.calculateRadius(displayCompass,dist_in_mile);


//            cs.constrainCircle(labels.get(i).getId(), R.id.compass_layout, (int) dist_in_scale, angle);
            cs.constrainCircle(nodes.get(i).getId(), R.id.compass_layout, dist_in_scale, angle);
//            cs.constrainCircle(nodes.get(i).getId(), R.id.compass_layout, 462, angle);
            cs.constrainCircle(labels.get(i).getId(), R.id.compass_layout, dist_in_scale, angle);

            nodes.get(i).setVisibility(View.VISIBLE);
            labels.get(i).setVisibility(View.VISIBLE);

//            if(dist_in_scale==462){
//                nodes.get(i).setVisibility(View.VISIBLE);
//                labels.get(i).setVisibility(View.INVISIBLE);
//            }else{
//                nodes.get(i).setVisibility(View.INVISIBLE);
//                labels.get(i).setVisibility(View.VISIBLE);
//            }

        }
        for(; i < nodes.size(); i++) {
            nodes.get(i).setVisibility(View.INVISIBLE);
            labels.get(i).setVisibility(View.INVISIBLE);
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

        // default start at compass view 2
        displayCompass = 2;
        visibleCompass(displayCompass);
        Button zoomInButton = this.findViewById(R.id.zoom_in_button);
        Button zoomOutButton = this.findViewById(R.id.zoom_out_button);

        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(displayCompass<4){
                    displayCompass = displayCompass+1;
                    visibleCompass(displayCompass);
                }else{
                    Toast.makeText(CompassActivity.this,
                            "Cannot zoom out further", Toast.LENGTH_SHORT).show();
                }
            }
        });

        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(displayCompass>1){
                    displayCompass = displayCompass-1;
                    visibleCompass(displayCompass);
                }else{
                    Toast.makeText(CompassActivity.this,
                            "Cannot zoom in further", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    /*
    * Function to control only one compass view is displayed
    * */
    private void visibleCompass(int compass_no){
        for(int i = 1; i<=4; i++){
            if( i != compass_no){
                String compass_id = "imageView"+ valueOf(i);
                int id = getResources().getIdentifier(compass_id, "id", getPackageName());
                ImageView asVisible = findViewById(id);
                asVisible.setVisibility(View.INVISIBLE);
            }else{
                String compass_id = "imageView"+ valueOf(compass_no);
                int id = getResources().getIdentifier(compass_id, "id", getPackageName());
                ImageView asVisible = findViewById(id);
                asVisible.setVisibility(View.VISIBLE);

            }
        }
    }

    public void toFriendsList(View v) {
        Intent intent = new Intent(this, FriendListActivity.class);
        startActivity(intent);
    }

}