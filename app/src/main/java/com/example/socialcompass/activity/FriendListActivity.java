package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDatabase;
import com.example.socialcompass.model.repository.Repository;
import com.example.socialcompass.old.GPSLocationHandler;
import com.example.socialcompass.old.OrientationService;
import com.example.socialcompass.utility.Utilities;
import com.example.socialcompass.viewmodel.FriendListViewModel;
import com.example.socialcompass.R;
import com.example.socialcompass.view.FriendListAdapter;

import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    private EditText newFriendPublicCode;
    private Button addFriendButton;
    TextView myName;
    TextView myPublicCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        var viewModel = setupViewModel();
        var adapter = setupAdapter(viewModel);
        setupViews(viewModel,adapter);

        //get the input user name from user activity
        // Get the text from the intent
        String text = getIntent().getStringExtra("inputName");
        myName = this.findViewById(R.id.my_name_from_friendlist);
        myName.setText(text);
        String publicCode = getIntent().getStringExtra("publicCode");
        myPublicCode = this.findViewById(R.id.my_public_code);
        myPublicCode.setText(publicCode);


        Button compassViewButton = this.findViewById(R.id.compass_view_button);

        compassViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendListActivity.this, CompassActivity.class);
                startActivity(intent);
            }
        });
    }

    private FriendListAdapter setupAdapter(FriendListViewModel viewModel) {
        FriendListAdapter adapter = new FriendListAdapter();
        adapter.setHasStableIds(true);
        //handle delete item
        adapter.setOnDeleteClickedHandler(viewModel::toggleDelete);
        viewModel.getFriendListItems().observe(this,adapter::setFriendListItems);
        return adapter;
    }

    private FriendListViewModel setupViewModel() {
        return new ViewModelProvider(this).get(FriendListViewModel.class);

    }

    private void setupViews(FriendListViewModel viewModel, FriendListAdapter adapter) {
        setupRecycler(adapter);
        setupInput(viewModel);
    }



    private void setupInput(FriendListViewModel viewModel) {
        this.newFriendPublicCode = (EditText) this.findViewById(R.id.new_friend_public_code);
        this.addFriendButton = this.findViewById(R.id.add_friend_btn);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friend_public_code = newFriendPublicCode.getText().toString();
                newFriendPublicCode.setText("");
                var friend = viewModel.getOrcreateFriend(friend_public_code);
                friend.observe(FriendListActivity.this,friendEntity->{
                });
            }
        });

    }


    @SuppressLint("RestrictedApi")
    private void setupRecycler(FriendListAdapter adapter) {
        recyclerView = this.findViewById(R.id.friend_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}