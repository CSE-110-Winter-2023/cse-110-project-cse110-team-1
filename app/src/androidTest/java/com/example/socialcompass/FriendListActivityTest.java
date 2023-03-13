//package com.example.socialcompass;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.lifecycle.Lifecycle;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.room.Room;
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.core.app.ApplicationProvider;
//
//import com.example.socialcompass.model.friend.Friend;
//import com.example.socialcompass.model.friend.FriendDao;
//import com.example.socialcompass.model.friend.FriendDatabase;
//
//import com.example.socialcompass.activity.FriendListActivity;
//import com.example.socialcompass.viewmodel.FriendListViewModel;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicReference;
//
//public class FriendListActivityTest {
//    FriendDatabase testDb;
//    FriendDao friendListItemDao;
//
//    @Before
//    public void resetDatabase(){
//        Context context = ApplicationProvider.getApplicationContext();
//        testDb = Room.inMemoryDatabaseBuilder(context,FriendDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        FriendDatabase.inject(testDb);
//
//        friendListItemDao = testDb.getDao();
//
//    }
//
//    @Test
//    public void testAddFriend(){
//        String newText = "forTeamOneTesting";
//        ActivityScenario<FriendListActivity> scenario
//                = ActivityScenario.launch(FriendListActivity.class);
//        scenario.moveToState(Lifecycle.State.CREATED);
//        scenario.moveToState(Lifecycle.State.STARTED);
//        scenario.moveToState(Lifecycle.State.RESUMED);
//
//        scenario.onActivity(activity -> {
//            EditText newPublicCode = activity.findViewById(R.id.new_friend_public_code);
//            Button addTodoButton = activity.findViewById(R.id.add_friend_btn);
//
//            //the before database
//            var testLiveData = friendListItemDao.getAll();
//
//            //enter a new public code
//            newPublicCode.setText(newText);
//            //click on ADD
//            addTodoButton.performClick();
//
//            FriendListViewModel viewModel = new FriendListViewModel(activity.getApplication());
//
//            //the new Friend should be added to the database
//            var friend = viewModel.getOrcreateFriend(newText);
//
//            // Observe changes to the test LiveData object
//            assertEquals(testLiveData.size()+1,friendListItemDao.getAll().size());
//
//        });
//    }
//}
