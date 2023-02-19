package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Layout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        * This can be determine by checking which data we have available
        * */
        View groupView_1 = findViewById(R.id.input_group_1);
        View groupView_2 = findViewById(R.id.input_group_2);
        SharedPreferences preferences = getSharedPreferences("MS1_PREFS", Context.MODE_PRIVATE);
        String label_0 = preferences.getString("label_0","Label");
        String longitude_0 = preferences.getString("longitude_0","Longitude");
        String latitude_0 = preferences.getString("latitude_0","Latitude");

        TextView labelView_0 = findViewById(R.id.label_0);
        labelView_0.setText(label_0);
        TextView longitudeView_0 = findViewById(R.id.longitude_0);
        longitudeView_0.setText(longitude_0);
        TextView latitudeView_0 = findViewById(R.id.latitude_0);
        latitudeView_0.setText(latitude_0);


        String label_1 = preferences.getString("label_1","Label");
        String longitude_1 = preferences.getString("longitude_1","Longitude");
        String latitude_1 = preferences.getString("latitude_1","Latitude");
        String label_2 = preferences.getString("label_2","Label");
        String longitude_2 = preferences.getString("longitude_2","Longitude");
        String latitude_2 = preferences.getString("latitude_2","Latitude");

        if (!label_1.equals("Label")) {
            groupView_1.setVisibility(View.VISIBLE);

            TextView labelView_1 = findViewById(R.id.label_1);
            labelView_1.setText(label_1);
            TextView longitudeView_1 = findViewById(R.id.longitude_1);
            longitudeView_1.setText(longitude_1);
            TextView latitudeView_1 = findViewById(R.id.latitude_1);
            latitudeView_1.setText(latitude_1);
        }
        if (!label_2.equals("Label")) {
            groupView_2.setVisibility(View.VISIBLE);

            TextView labelView_2 = findViewById(R.id.label_2);
            labelView_2.setText(label_2);
            TextView longitudeView_2 = findViewById(R.id.longitude_2);
            longitudeView_2.setText(longitude_2);
            TextView latitudeView_2 = findViewById(R.id.latitude_2);
            latitudeView_2.setText(latitude_2);
        }

    }

    public void onAddLocationClick(View view) {
        /*
        * Each form has 3 input fields: label, longitude and latitude
        * Store these data in storage
        * */
        View groupView_1 = findViewById(R.id.input_group_1);
        View groupView_2 = findViewById(R.id.input_group_2);
        if (groupView_1.getVisibility() == View.GONE) {
            groupView_1.setVisibility(View.VISIBLE);
        }
        else if (groupView_2.getVisibility() == View.GONE) {
            groupView_2.setVisibility(View.VISIBLE);
        }
        else if (groupView_1.getVisibility() == View.VISIBLE
                && groupView_2.getVisibility() == View.VISIBLE){
            Utilities.showAlert(this,"All three fields are visible");
        }
    }

    public void onSubmitButtonClick(View view) {

        //Get all label's text store in strings
        TextView labelView_0 = findViewById(R.id.label_0);
        String labelStr_0 = labelView_0.getText().toString().trim();
        TextView longitudeView_0 = findViewById(R.id.longitude_0);
        String longitudeStr_0 = longitudeView_0.getText().toString().trim();
        TextView latitudeView_0 = findViewById(R.id.latitude_0);
        String latitudeStr_0 = latitudeView_0.getText().toString().trim();

        TextView labelView_1 = findViewById(R.id.label_1);
        String labelStr_1 = labelView_1.getText().toString().trim();
        TextView longitudeView_1 = findViewById(R.id.longitude_1);
        String longitudeStr_1 = longitudeView_1.getText().toString().trim();
        TextView latitudeView_1 = findViewById(R.id.latitude_1);
        String latitudeStr_1 = latitudeView_1.getText().toString().trim();

        TextView labelView_2 = findViewById(R.id.label_2);
        String labelStr_2 = labelView_2.getText().toString().trim();
        TextView longitudeView_2 = findViewById(R.id.longitude_2);
        String longitudeStr_2 = longitudeView_2.getText().toString().trim();
        TextView latitudeView_2 = findViewById(R.id.latitude_2);
        String latitudeStr_2 = latitudeView_2.getText().toString().trim();

        //determine all three group of fields are available and store together
        if (Utilities.isValidAll(labelStr_0,longitudeStr_0,latitudeStr_0)
                && Utilities.isValidAll(labelStr_1,longitudeStr_1,latitudeStr_1)
                && Utilities.isValidAll(labelStr_2,longitudeStr_2,latitudeStr_2)) {

            SharedPreferences preferences = getSharedPreferences("MS1_PREFS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("label_0",labelStr_0);
            editor.putString("longitude_0",longitudeStr_0);
            editor.putString("latitude_0",latitudeStr_0);

            editor.putString("label_1",labelStr_1);
            editor.putString("longitude_1",longitudeStr_1);
            editor.putString("latitude_1",latitudeStr_1);

            editor.putString("label_2",labelStr_2);
            editor.putString("longitude_2",longitudeStr_2);
            editor.putString("latitude_2",latitudeStr_2);
            editor.apply();
            Utilities.showAlert(this,"Data Saved");

            //link to new page
            Intent intent = new Intent(this, compass_activity.class);
            startActivity(intent);
        }
        else {
            Utilities.showAlert(this,
                    "Input is not Valid, Data not saved");
        }
    }
}