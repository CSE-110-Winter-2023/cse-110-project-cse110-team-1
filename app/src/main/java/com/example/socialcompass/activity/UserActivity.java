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

import com.example.socialcompass.R;
import com.example.socialcompass.Utilities;

public class UserActivity extends AppCompatActivity {
    Button saveUserNameButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

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
            editor.putString("name", input_name.getText().toString());
            editor.apply();


            Intent intent = new Intent(this, FriendListActivity.class);
            intent.putExtra("inputName", input_name.getText().toString());
            startActivity(intent);
        }

    }
}