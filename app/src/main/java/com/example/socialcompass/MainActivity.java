package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAddLocationClick(View view) {
        ScrollView parentLayout = findViewById(R.id.scroll_view);



    }
    public void submitButtonClick(View view) {

        TextView labelView_0 = findViewById(R.id.label_0);
        String labelStr_0 = labelView_0.getText().toString().trim();
        TextView longitudeView_0 = findViewById(R.id.longitude_0);
        String longitudeStr_0 = longitudeView_0.getText().toString().trim();
        TextView latitudeView_0 = findViewById(R.id.latitude_0);
        String latitudeStr_0 = latitudeView_0.getText().toString().trim();
        if (Utilities.isValidAll(labelStr_0,longitudeStr_0,latitudeStr_0)) {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("label_0",labelStr_0);
            editor.putString("longitude_0",longitudeStr_0);
            editor.putString("latitude_0",latitudeStr_0);
            editor.apply();

            String label = preferences.getString("label_0","default");
            String longitude = preferences.getString("longitude_0","default");
            String latitude = preferences.getString("latitude_0","default");
            Utilities.showAlert(this,label+" "+longitude+" "+latitude);

        }
        else {
            Utilities.showAlert(this,"Input is not Valid, Please check");
        }
        //Intent intent = new Intent(this, CompassActivity.class);
        //link to new page
    }

}