package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.EditText;

import com.example.socialcompass.R;
import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.viewmodel.FriendViewModel;

public class FriendActivity extends AppCompatActivity {


    private LiveData<Friend> friend;
    private FriendDao dao;
    private EditText contentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        var viewModel = setupViewModel();

        var intent = getIntent();
        var publicCode = intent.getStringExtra("friendPublicCode");

        friend = viewModel.getFriend(publicCode);
    }

    private FriendViewModel setupViewModel() {
        return new ViewModelProvider(this).get(FriendViewModel.class);

    }
}