package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        * TODO: keep track of what view should be displayed/hide
        *  (and) display data that we already have on those fields
        * This can be determine by checking which data we have available
        * */

    }

    public void onAddLocationClick(View view) {
        /*
        * TODO: display additional form for user to fill in
        * Each form has 3 input fields: label, longitude and latitude
        * Store these data in storage
        *  */
        ScrollView parentLayout = findViewById(R.id.scroll_view);
    }

    public void onSubmitClick(View view){
        /*
        * TODO: submit user's input and store them to storage
        *  */
    }


}