package com.example.socialcompass.model;
import static org.junit.Assert.assertNotEquals;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.socialcompass.Friendmodel.FriendDatabase;
import com.example.socialcompass.Friendmodel.FriendListItem;
import com.example.socialcompass.Friendmodel.FriendListItemDao;


import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FriendDatabaseTest extends TestCase {

    private FriendListItemDao friend_dao;
    private FriendDatabase friend_db;

    @Before
    public void initDb() {
        var context2 = ApplicationProvider.getApplicationContext();
        friend_db = Room.inMemoryDatabaseBuilder(context2, FriendDatabase.class)
                .allowMainThreadQueries().build();
        // indirectly test getDao function
        friend_dao = friend_db.friendListItemDao();
    }

    @After
    public void closeDb() {

        friend_db.close();
    }

    @Test
    public void testExist()  {
        FriendListItem testFriend1 = new FriendListItem("Eva", "858-135-2467",
                "23", "32", 40);
        FriendListItem testFriend2 = new FriendListItem("Julia", "858-135-2468",
                "-23", "32", 124);

//        Insert the user into the database
        friend_dao.insert(testFriend1);
        friend_dao.insert(testFriend2);
//
        // test exists
        assertTrue(friend_dao.exists("858-135-2467"));
        assertTrue(friend_dao.exists("858-135-2468"));

        // test not inserted should not exist
        assertFalse(friend_dao.exists("7832"));
    }

    @Test
    public void testGet(){
        FriendListItem testFriend1 = new FriendListItem("Eva", "858-135-2467",
                "23", "32", 40);
        FriendListItem testFriend2 = new FriendListItem("Julia", "858-135-2468",
                "-23", "32", 124);

        //Insert the friend into the database
        friend_dao.insert(testFriend1);
        friend_dao.insert(testFriend2);

        // retrieve live data for the use in repository
        LiveData<FriendListItem>  liveRetreivedFriend = friend_dao.get("858-135-2467");
        // test livedata is not null
        assertNotNull(liveRetreivedFriend);

        // retrieve user object to test specific information
        FriendListItem retreivedFriend = friend_dao.friendGet("858-135-2468");

        // transform to json, to check there is no "private code" information
        String actualFriendJson = retreivedFriend.toJSON();
        // truncate the string so time stamp are not included
        String friendJsonEl1 = actualFriendJson.split(",")[1];
        String friendJsonEl2 = actualFriendJson.split(",")[2];
        String friendJsonEl3 = actualFriendJson.split(",")[3];
        String friendJsonEl4 = actualFriendJson.split(",")[4];
        assertEquals("\"public_code\":\"858-135-2468\"", friendJsonEl1);
        assertEquals("\"label\":\"Julia\"", friendJsonEl2);
        assertEquals("\"latitude\":\"-23\"", friendJsonEl3);
        assertEquals("\"longitude\":\"32\"", friendJsonEl4);

    }



}