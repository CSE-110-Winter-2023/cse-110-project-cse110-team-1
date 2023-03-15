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

        for(int i = 0; i < friends.size(); i++) {
            Friend f = friends.get(i);
            double actual_dist = Utilities.calculateDistanceInMiles(gpsLat, gpsLon, f.latitude, f.longitude);
            int radius_dist = Utilities.calculateRadius( displayCompass, actual_dist);

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
                text.setText( String.format("%s",f.label));
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
            float angle = Utilities.getAngle(gpsLat, gpsLon, f.latitude, f.longitude);
            double actual_dist = Utilities.calculateDistanceInMiles(gpsLat, gpsLon, f.latitude, f.longitude);
            int radius_dist = Utilities.calculateRadius(displayCompass, actual_dist);
            nodes.get(i).setVisibility(View.VISIBLE);
            cs.constrainCircle(nodes.get(i).getId(), R.id.compass_layout, radius_dist, angle);


            cs.applyTo(layout);
            truncateTextView(layout);}
    }


    private void truncateTextView(ConstraintLayout layout) {
        int childCount = layout.getChildCount();
        ArrayList<TextView> textViewList = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View view = layout.getChildAt(i);
            if (view instanceof TextView) {
                textViewList.add((TextView) view);
            }
        }
        for (TextView textView : textViewList) {
            // Set up a listener for layout changes
            ViewTreeObserver observer = textView.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Get the global bounds of textView1 and textView2
                    Rect rect1 = new Rect();
                    textView.getGlobalVisibleRect(rect1);

                    // Loop through the other TextViews to find the one that overlaps with textView
                    for (TextView otherTextView : textViewList) {
                        if (otherTextView == textView) {
                            continue;
                        }
                        Rect rect2 = new Rect();
                        otherTextView.getGlobalVisibleRect(rect2);

                        // Check if the TextViews intersect
                        if (Rect.intersects(rect1, rect2)) {
                            textView.setMaxLines(1);
                            textView.setWidth(70);

                            // Remove the listener to avoid redundant calculations
                            textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        }
                    }
                }
            });
        }

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