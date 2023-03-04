package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialcompass.R;
import com.example.socialcompass.model.FriendListAdapter;
import com.example.socialcompass.model.FriendListViewModel;

public class FriendListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    FriendListViewModel viewModel;
    private EditText newFriendPublicCode;
    private Button addFriendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        viewModel = new ViewModelProvider(this)
                .get(FriendListViewModel.class);
        FriendListAdapter adapter = new FriendListAdapter();
        adapter.setHasStableIds(true);
        adapter.setOnDeleteClickedHandler(viewModel::toggleDelete);
        viewModel.getFriendListItems().observe(this,adapter::setFriendListItems);

        recyclerView = this.findViewById(R.id.friend_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        this.newFriendPublicCode = this.findViewById(R.id.new_friend_public_code);
        this.addFriendButton = this.findViewById(R.id.add_friend_btn);
        addFriendButton.setOnClickListener(this::onAddFriendClicked);
    }

    private void onAddFriendClicked(View view) {
        String friend_public_code = newFriendPublicCode.getText().toString();
        newFriendPublicCode.setText("");
        viewModel.createFriend(friend_public_code);
    }
}