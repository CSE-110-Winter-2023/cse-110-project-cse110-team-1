package com.example.socialcompass.activity;

import static java.lang.String.valueOf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.example.socialcompass.model.repository.Repository;
import com.example.socialcompass.old.GPSLocationHandler;
import com.example.socialcompass.old.OrientationService;
import com.example.socialcompass.utility.Utilities;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kotlin.Triple;

public class CompassActivity extends AppCompatActivity {
    private Icon nodeIcon;
    private int displayCompass;

//    private final List<ImageView> nodes = new ArrayList<>();
//    private final List<TextView> labels = new ArrayList<>();


    public synchronized void redrawAllFriends() {
        if(locationService.getLocation().getValue() == null
        || orientationService.getOrientation().getValue() == null
        || friendsList == null) return;

        // TODO synchronize against the activity drawing?
        ConstraintLayout layout = findViewById(R.id.compass_layout);
        layout.removeAllViews();

        Log.d("CompassView","Redraw "+friendsList.getValue());

        Pair<Double, Double> loc = locationService.getLocation().getValue();

        final float gpsLat = loc.first.floatValue(),
                    gpsLon = loc.second.floatValue();

        List<Friend> friends = friendsList.getValue();
        List<View> nodes = new ArrayList<>(friends.size());
        List<Triple<String,Float,Integer>> overlapCheckList = new ArrayList<>();
        List<Double> dists = new ArrayList<>(friends.size());

        for(int i = 0; i < friends.size(); i++) {
            Friend f = friends.get(i);
            double actual_dist = Utilities.calculateDistanceInMiles(gpsLat, gpsLon, f.latitude, f.longitude);
            dists.add(actual_dist);
            int radius_dist = Utilities.calculateRadius( displayCompass, actual_dist);
            float angle = Utilities.getAngle(gpsLat, gpsLon, f.latitude, f.longitude);
            overlapCheckList.add(new Triple<>(f.label,angle,radius_dist));

        }

        for(int i = 0; i < friends.size(); i++) {
            Friend f = friends.get(i);
            double actual_dist = Utilities.calculateDistanceInMiles(gpsLat, gpsLon, f.latitude, f.longitude);
            int radius_dist = Utilities.calculateRadius( displayCompass, actual_dist);


            float angle = Utilities.getAngle(gpsLat, gpsLon, f.latitude, f.longitude);

            if(radius_dist >= 450) {
                ImageView node = new ImageView(getApplicationContext());
                node.setImageIcon(nodeIcon);
                node.setId(View.generateViewId());
                node.setLayoutParams(new LinearLayout.LayoutParams(50,50));
                node.setTag("node_"+i);
                layout.addView(node);
                nodes.add(node);
            } else {
                TextView text = new TextView(getApplicationContext());
                text.setId(View.generateViewId());
//                text.setText( String.format("%s\n%.0fmi",f.label,
//                        Utilities.calculateDistanceInMiles(gpsLat, gpsLon, f.latitude, f.longitude)));



                //if the current textview has similar radius and distance like other existing textview
                //change the textview Width to 50px
                Boolean checkOverlap = checkOverlapTextView(f.label,angle,radius_dist,overlapCheckList,displayCompass, i);

                if(checkOverlap){
                    int r = checkSameRing(radius_dist, dists, i);
                    if(r != -1) {
                        Log.d("Dists", "match");

                        text = (TextView) nodes.get(r);

//                        text.setWidth(-1);
//                        text.setMaxLines(-1);
                        text.append(String.format("%s\n", f.label));
                        nodes.add(null);
                        continue;
                    }

                    text.setMaxLines(1);
                    text.setWidth(70);

                }

                text.setText( String.format("%s\n",f.label));
                text.setTag("label_" + i);
                layout.addView(text);
                nodes.add(text);
            }
        }

        ConstraintSet cs = new ConstraintSet();
        cs.clone(layout);

        int i;
        for(i = 0; i < friends.size(); i++) {
            Friend f = friends.get(i);
            if(nodes.get(i) == null) continue;
            float angle = Utilities.getAngle(gpsLat, gpsLon, f.latitude, f.longitude);
            double actual_dist = Utilities.calculateDistanceInMiles(gpsLat, gpsLon, f.latitude, f.longitude);
            int radius_dist = Utilities.calculateRadius(displayCompass, actual_dist);
            nodes.get(i).setVisibility(View.VISIBLE);
            cs.constrainCircle(nodes.get(i).getId(), R.id.compass_layout, radius_dist, angle);


            cs.applyTo(layout);
        }
    }

    private static int checkSameRing(double dist1, List<Double> dists, int prio) {
        if(dist1 >= 450) return -1;

        for (int i = 0; i < prio; i++) {
            double dist2 = dists.get(i);
            if (dist1 < 1) {
                if (dist2 < 1) return i;
            } else if (dist1 < 10) {
                if (dist2 >= 1 && dist2 < 10) return i;
            } else if (dist1 < 500) {
                if (dist2 >= 10 && dist2 < 500) return i;
            } else {
                if (dist2 >= 500) return i;
            }
        }
        return -1;
    }

    private static boolean checkOverlapTextView(String label,float angle, int radiusDist, List<Triple<String,Float,Integer>> overlapCheckList, int displayCompass, int prio) {
        for(int i = 0; i < prio; i++){
            Triple<String,Float,Integer> pairs = overlapCheckList.get(i);
            if (label != pairs.getFirst()){
                //level 1
                if(displayCompass == 1){
                    if ((Math.abs(pairs.getSecond() - angle) <10) && (Math.abs(pairs.getThird() - radiusDist) < 450)){
                        return true;
                    }
                }
                else if(displayCompass ==2){
                    //level 2
                    if ((Math.abs(pairs.getSecond() - angle) <10) && (Math.abs(pairs.getThird() - radiusDist) < 225)){
                        return true;
                    }
                }
                else if(displayCompass ==3){
                    //level 3
                    if ((Math.abs(pairs.getSecond() - angle) <10) && (Math.abs(pairs.getThird() - radiusDist) < 150)){
                        return true;
                    }
                }
                else if(displayCompass ==4){
                    //level 4
                    if ((Math.abs(pairs.getSecond() - angle) <10) && (Math.abs(pairs.getThird() - radiusDist) < 100)){
                        return true;
                    }
                }


            }


        }
        return false;
    }


    private GPSLocationHandler locationService;
    private OrientationService orientationService;

    private LiveData<List<Friend>> friendsList;
    private FriendDao friendDao;
    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass_new);

        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String apiURL = preferences.getString("apiURL", null);
        nodeIcon = Icon.createWithResource(getApplicationContext(), R.drawable.address_node);

        friendDao = FriendDatabase.provide(getApplicationContext()).getDao();
        locationService = new GPSLocationHandler(this);
        orientationService = new OrientationService(this);
        this.repo = new Repository(friendDao, apiURL);


        friendsList = friendDao.getAllLive();
        friendsList.observe(this, (allFriends) -> {
            this.redrawAllFriends();

        });
        orientationService.getOrientation().observe(this, (rotation) -> {
//            this.redrawAllFriends();
            float degrees = (float) Math.toDegrees(rotation);
            ConstraintLayout constraintLayout = findViewById(R.id.compass_screen_layout);
            constraintLayout.setRotation(-1 * degrees);
        });
        locationService.getLocation().observe(this, (a) -> {
            this.updateUserLocation();
            this.redrawAllFriends();
        });
        List<Friend> allFriends = friendDao.getAll();
        /**
         * TODO: fix this ASAP
         * This is a terrible way to fix this
         * Basically, we observe every single nodes and spawn a thread for each of those
         */
        for (Friend friend : allFriends) {
            Log.d("COMPASS_LOG", "DAO friend list updated");
            repo.getSyncedFriend(friend.publicCode).observe(this, (a) -> {
                this.redrawAllFriends();

            });

        }

        //Update GPS status
        TextView last_connected = findViewById(R.id.last_connected);
        ImageView status_indicator = findViewById(R.id.status_indicator);
        var executor = Executors.newSingleThreadScheduledExecutor();
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        PorterDuffColorFilter redFilter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        PorterDuffColorFilter greenFilter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);

        executor.scheduleAtFixedRate(() -> {
            long currentTimeMillis = System.currentTimeMillis();

            if (locationService.isGPSOn()) {
                Log.d("Status", "Online");
                editor.putLong("lastConnectedTime", currentTimeMillis);
                editor.apply();
                last_connected.setText("Online");
                status_indicator.setColorFilter(greenFilter);
            } else {
                Log.d("Status", "Offline");
                long timeOffline = currentTimeMillis - sharedPref.getLong("lastConnectedTime", (long) 0.0);
                String timeOfflineString = statusTimeFormatter(timeOffline);
                last_connected.setText(timeOfflineString);
                status_indicator.setColorFilter(redFilter);
            }
        }, 0, 3000, TimeUnit.MILLISECONDS);






        //Handle Zoom In and Zoom out
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
    public String statusTimeFormatter(long timeOffline) {
        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(timeOffline);
        int hours = (int) TimeUnit.MILLISECONDS.toHours(timeOffline);
        String returnString = "";
        if (hours > 0) {
            returnString = returnString.concat(hours + "h ");
        }
        returnString = returnString.concat(minutes + "m");
        return returnString;
    }
    public void toFriendsList(View v) {
        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        Intent intent = new Intent(this, FriendListActivity.class);
        String userName = preferences.getString("label", null);
        String userPublicCode = preferences.getString("publicCode", null);

        intent.putExtra("inputName", userName);
        intent.putExtra("publicCode", userPublicCode);
        startActivity(intent);
    }

    public void updateUserLocation(){
        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        Pair<Double, Double> loc = locationService.getLocation().getValue();
        final float gpsLat = loc.first.floatValue(),
                gpsLon = loc.second.floatValue();

        //Update user location in shared preferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("latitude", String.valueOf(gpsLat));
        editor.putString("longitude", String.valueOf(gpsLon));
        editor.apply();
        //Update user location in remote server
        String label = preferences.getString("label", null);
        String userPublicCode = preferences.getString("publicCode", null);
        String userPrivateCode = preferences.getString("privateCode",null);
        Friend user = new Friend(userPublicCode,label,gpsLat,gpsLon);
        repo.upsertRemote(user, userPrivateCode);
    }
}