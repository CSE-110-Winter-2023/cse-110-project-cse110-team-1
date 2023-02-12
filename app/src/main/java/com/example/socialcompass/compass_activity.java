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
        float lenA = Math.abs(addressLong-gpsLong);
        float lenB = Math.abs(addressLat - gpsLat);

        double angle_rad =  Math.atan2(lenB,lenA);

        double angle_deg =  angle_rad*180.0/Math.PI;
        float degree = (float) angle_deg;

        if(gpsLat<=addressLat && gpsLong<=addressLong){
            // first quadrant
            degree = 90-degree;
        }else if(gpsLat<=addressLat && gpsLong>=addressLong){
            // second quadrant
            degree = degree+270;
        }else if(gpsLat>=addressLat && gpsLong>=addressLong){
            // third quadrant
            degree = 270-degree;
        }else if(gpsLat>=addressLat && gpsLong<=addressLong){
            // forth quadrant
            degree = degree+90;
        }

        return degree;
    }

    void setAngle(){
        // if there is only one location,
        float gpsLat = (float) 32.88074495280559;
        float gpsLong = (float) -117.23403456410483;
        float addressLat = (float) 32.87774383077887;
        float addressLong = (float)  -117.23034561391084;
        float angle1 = getAngle( gpsLat,  gpsLong, addressLat,  addressLong);

        ConstraintLayout constraintLayout = findViewById(R.id.compass_layout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.constrainCircle(R.id.node_1, R.id.compass_img, 462, angle1);
        constraintSet.constrainCircle(R.id.label_1, R.id.compass_img, 330, angle1);



        constraintSet.applyTo(constraintLayout);

    }








}