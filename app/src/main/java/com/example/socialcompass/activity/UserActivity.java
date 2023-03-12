package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialcompass.model.friend.Friend;

import com.example.socialcompass.R;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.FriendDatabase;
import com.example.socialcompass.model.repository.Repository;
import com.example.socialcompass.old.GPSLocationHandler;
import com.example.socialcompass.utility.Utilities;

import java.util.concurrent.atomic.AtomicReference;

public class UserActivity extends AppCompatActivity {
    Button saveUserNameButton;
    private GPSLocationHandler locationService;
    private FriendDao friendListItemDao;

    private Repository repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        FriendDatabase db = FriendDatabase.provide(this);
        friendListItemDao = db.getDao();
        repo = new Repository(friendListItemDao);

        locationService = new GPSLocationHandler(this);

        //Click SAVE starts FriendList activity
        this.saveUserNameButton = this.findViewById(R.id.save_user_name_btn);
        saveUserNameButton.setOnClickListener(this::onSaveUserNameClicked);

        //get user information: name and block EditText
        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userName = preferences.getString("label", null);
        if(userName != null){
            EditText input_name = this.findViewById(R.id.my_input_name);
            input_name.setText(userName);
            input_name.setFocusable(false);
        }

    }

    private void onSaveUserNameClicked(View view) {
        EditText input_name = this.findViewById(R.id.my_input_name);
        //store user information: name
        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String userName = preferences.getString("label", null);

        if(userName == null){
            if(input_name.getText().toString().length() == 0 ){
                Utilities.showAlert(this,"Please enter your name");

            }
            String userPublicCode = Utilities.generatePublicId();
            String userPrivateCode = Utilities.generatePrivateId();
            String label = input_name.getText().toString();
            //TODO: location service return error
            AtomicReference<Float> currentLatitude = new AtomicReference<>((float) 0);
            AtomicReference<Float> currentLongitude = new AtomicReference<>((float) 0);
            var locations = locationService.getLocation();
            locations.observe(this, locationValue->{
                locations.removeObservers(this);
                currentLatitude.set(locationValue.first.floatValue());
                currentLongitude.set(locationValue.second.floatValue());

            });

            editor.putString("publicCode", userPublicCode);
            editor.putString("privateCode", userPrivateCode);
            editor.putString("label", label);
            editor.putString("latitude", String.valueOf(currentLatitude));
            editor.putString("longitude", String.valueOf(currentLongitude));
            //TODO: Add created_at updated_at strings
            editor.apply();
            Friend user = new Friend(userPublicCode, label, currentLatitude.get(),currentLongitude.get() );
            // TODO: Add repository object (repo) to this class.
             repo.upsertRemote(user, userPrivateCode);
        }
        else{
            userName = preferences.getString("label", null);
            String userPublicCode = preferences.getString("publicCode", null);
            Intent intent = new Intent(this, FriendListActivity.class);
            intent.putExtra("inputName", userName);
            intent.putExtra("publicCode",userPublicCode);
            startActivity(intent);

        }

    }
}