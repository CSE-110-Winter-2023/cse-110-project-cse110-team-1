package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.repository.Repository;

import com.example.socialcompass.R;
import com.example.socialcompass.old.GPSLocationHandler;
import com.example.socialcompass.utility.Utilities;

public class UserActivity extends AppCompatActivity {
    Button saveUserNameButton;
    private GPSLocationHandler locationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        locationService = new GPSLocationHandler(this);

        //Click SAVE starts FriendList activity
        this.saveUserNameButton = this.findViewById(R.id.save_user_name_btn);
        saveUserNameButton.setOnClickListener(this::onSaveUserNameClicked);

        //get user information: name and block EditText
        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userName = preferences.getString("name", null);
        if(userName != null){
            EditText input_name = this.findViewById(R.id.my_input_name);
            input_name.setText(userName);
            input_name.setFocusable(false);
        }

    }

    private void onSaveUserNameClicked(View view) {
        EditText input_name = this.findViewById(R.id.my_input_name);

        if(input_name.getText().toString().length() == 0){
            Utilities.showAlert(this,"Please enter your name");
        }
        else{
            //store user information: name
            SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String userPublicCode = Utilities.generatePrivateId();
            String userPrivateCode = Utilities.generatePrivateId();
            String label = input_name.getText().toString();
            float currentLatitude = locationService.getLocation().getValue().first.floatValue();
            float currentLongitude = locationService.getLocation().getValue().second.floatValue();
            editor.putString("publicCode", userPublicCode);
            editor.putString("privateCode", userPrivateCode);
            editor.putString("label", label);
            editor.putString("latitude", String.valueOf(currentLatitude));
            editor.putString("longitude", String.valueOf(currentLongitude));
            //TODO: Add created_at updated_at strings
            editor.apply();
            Friend user = new Friend(userPublicCode, label, currentLatitude, currentLongitude);
            // TODO: Add repository object (repo) to this class.
            // repo.upsertRemote(user, userPrivateCode);
            Intent intent = new Intent(this, FriendListActivity.class);
            intent.putExtra("inputName", input_name.getText().toString());
            startActivity(intent);
        }

    }
}