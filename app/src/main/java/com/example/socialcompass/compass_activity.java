package com.example.socialcompass;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;

public class compass_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        setAngle();
    }



    float getAngle(float gpsLat, float gpsLong,float addressLat, float addressLong){
        double len_long = gpsLong - addressLong;
        double len_lat =  gpsLat - addressLat;
        double angle_rad =  Math.atan2(len_lat,len_long);

        double angle_deg =  angle_rad*180.0/Math.PI;
        float degree = (float) angle_deg;

        return degree;
    }

    void setAngle(){
        // if there is only one location,
        float gpsLat = (float) 32.8511987628294;
        float gpsLong = (float) -117.23676356032297;
        float addressLat = (float) 32.859057060490336;
        float addressLong = (float) -117.24266799416866;
        float angle1 = getAngle( 1,  1, 2,  2);

        ConstraintLayout constraintLayout = findViewById(R.id.compass_layout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.constrainCircle(R.id.node_1, R.id.compass_img, 462, angle1);
        constraintSet.constrainCircle(R.id.label_1, R.id.compass_img, 330, angle1);
        constraintSet.applyTo(constraintLayout);

    }








}