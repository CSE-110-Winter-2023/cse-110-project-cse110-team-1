package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socialcompass.R;

public class UserActivity extends AppCompatActivity {
    Button saveUserNameButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        this.saveUserNameButton = this.findViewById(R.id.save_user_name_btn);
        saveUserNameButton.setOnClickListener(this::onSaveUserNameClicked);
    }

    private void onSaveUserNameClicked(View view) {
        EditText input_name = this.findViewById(R.id.my_input_name);
        input_name.setFocusable(false);

        Intent intent = new Intent(this, FriendListActivity.class);
        intent.putExtra("input_name", input_name.getText().toString());
        startActivity(intent);
    }
}