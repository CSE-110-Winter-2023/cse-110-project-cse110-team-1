package com.example.socialcompass.old;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TextView;

import com.example.socialcompass.R;
import com.example.socialcompass.utility.Utilities;

public class compass_activity extends AppCompatActivity {

    private GPSLocationHandler locationService;
    private OrientationService orientationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationService = new GPSLocationHandler(this);
        orientationService = new OrientationService(this);

        setContentView(R.layout.activity_compass);
        setNode(null);


        SharedPreferences preferences = getSharedPreferences("MS1_PREFS", Context.MODE_PRIVATE);

        if(preferences.contains("orientation")){
            String ui_orientation = preferences.getString("orientation",null);
            setUiMockOrientation(Float.parseFloat(ui_orientation));
        }
        else{
            orientationService.getOrientation().observe(this,this::setRotation);

        }




        locationService.getLocation().observe(this, this::setNode);

    }





    void setNode(Pair<Double,Double> loc){
        // if there is only one location,
        float gpsLat = loc != null ? loc.first.floatValue() : 32.88074495280559f;
        float gpsLong = loc != null ? loc.second.floatValue() : -117.23403456410483f;

        SharedPreferences preferences = getSharedPreferences("MS1_PREFS", Context.MODE_PRIVATE);
        String label_0 = preferences.getString("label_0","Label");
        float longitude_0 = Float.parseFloat(preferences.getString("longitude_0","0"));
        float latitude_0 = Float.parseFloat(preferences.getString("latitude_0","0"));
        String label_1 = preferences.getString("label_1","Label");
        float longitude_1 = Float.parseFloat( preferences.getString("longitude_1","0"));
        float latitude_1 =  Float.parseFloat(preferences.getString("latitude_1","0"));
        String label_2 = preferences.getString("label_2","Label");
        float longitude_2 =  Float.parseFloat(preferences.getString("longitude_2","0"));
        float latitude_2 =  Float.parseFloat(preferences.getString("latitude_2","0"));


        float angle0 = Utilities.getAngle( gpsLat,  gpsLong, latitude_0,  longitude_0);
        float angle1 = Utilities.getAngle( gpsLat,  gpsLong, latitude_1,  longitude_1);
        float angle2 = Utilities.getAngle( gpsLat,  gpsLong, latitude_2,  longitude_2);



        TextView nodeLabel_0 = findViewById(R.id.label_1);
        nodeLabel_0.setText(label_0);
        TextView nodeLabel_1 = findViewById(R.id.label_2);
        nodeLabel_1.setText(label_1);
        TextView nodeLabel_2 = findViewById(R.id.label_3);
        nodeLabel_2.setText(label_2);


        ConstraintLayout constraintLayout = findViewById(R.id.compass_layout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.constrainCircle(R.id.node_1, R.id.compass_img, 462, angle0);
        constraintSet.constrainCircle(R.id.label_1, R.id.compass_img, 330, angle0);
        constraintSet.constrainCircle(R.id.node_2, R.id.compass_img, 462, angle1);
        constraintSet.constrainCircle(R.id.label_2, R.id.compass_img, 330, angle1);
        constraintSet.constrainCircle(R.id.node_3, R.id.compass_img, 462, angle2);
        constraintSet.constrainCircle(R.id.label_3, R.id.compass_img, 330, angle2);
        constraintSet.applyTo(constraintLayout);

    }

    void setRotation(float rotation){
        float degrees = (float) Math.toDegrees(rotation);
        ConstraintLayout constraintLayout = findViewById(R.id.compass_layout);
        constraintLayout.setRotation(-1 * degrees);
    }


    void setUiMockOrientation(float rotation){

        ConstraintLayout constraintLayout = findViewById(R.id.compass_layout);
        constraintLayout.setRotation(rotation);
    }

    @Override
    public void finish() {
        SharedPreferences preferences = getSharedPreferences("MS1_PREFS", Context.MODE_PRIVATE);
        if(preferences.contains("orientation")){
            preferences.edit().remove("orientation").commit();

        }
        super.finish();
    }

}