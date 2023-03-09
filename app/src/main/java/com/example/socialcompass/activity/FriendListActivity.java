package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.viewmodel.FriendListViewModel;
import com.example.socialcompass.R;
import com.example.socialcompass.view.FriendListAdapter;

import org.w3c.dom.Text;

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
//        String publicCode = getIntent().getStringExtra("publicCode");
//        myPublicCode = this.findViewById(R.id.my_public_code);
//        myPublicCode.setText(publicCode);


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
        this.newFriendPublicCode = this.findViewById(R.id.new_friend_public_code);
        this.addFriendButton = this.findViewById(R.id.add_friend_btn);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friend_public_code = newFriendPublicCode.getText().toString();
                newFriendPublicCode.setText("");
                viewModel.createFriend(friend_public_code);
            }
        });
    }


    @SuppressLint("RestrictedApi")
    private void setupRecycler(FriendListAdapter adapter) {
        recyclerView = this.findViewById(R.id.friend_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

//    private void onAddFriendClicked(View view) {
//        String friend_public_code = newFriendPublicCode.getText().toString();
//        newFriendPublicCode.setText("");
//        viewModel.createFriend(friend_public_code);
////        friend.observe(this,this::onSetFriend);
//
//    }
}