package com.example.socialcompass.model;
import static org.junit.Assert.assertNotEquals;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.socialcompass.Friendmodel.Friend;
import com.example.socialcompass.Friendmodel.FriendDao;
import com.example.socialcompass.Friendmodel.FriendDatabase;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FriendDatabaseTest extends TestCase {

    private FriendDao friend_dao;
    private FriendDatabase friend_db;

    @Before
    public void initDb() {
        var context2 = ApplicationProvider.getApplicationContext();
        friend_db = Room.inMemoryDatabaseBuilder(context2, FriendDatabase.class)
                .allowMainThreadQueries().build();
        // indirectly test getDao function
        friend_dao = friend_db.getDao();
    }

    @After
    public void closeDb() {

        friend_db.close();
    }

    @Test
    public void testExist()  {
        Friend testFriend1 = new Friend("7830",  "nini", 32, -124,12);
        Friend testFriend2 = new Friend("7831",
                "nini2", 32, 124,13);

        //Insert the user into the database
        friend_dao.upsert(testFriend1);
        friend_dao.upsert(testFriend2);

        // test exists
        assertTrue(friend_dao.exists("7830"));
        assertTrue(friend_dao.exists("7831"));

        // test not inserted should not exist
        assertFalse(friend_dao.exists("7832"));
    }

    @Test
    public void testGet(){
        Friend testFriend1 = new Friend("7830",
                "nini", 32, -124,12);
        Friend testFriend2 = new Friend("7831",
                "nini2", 32, 124,22);

        //Insert the friend into the database
        friend_dao.upsert(testFriend1);
        friend_dao.upsert(testFriend2);

        // retrieve live data for the use in repository
        LiveData<Friend>  liveRetreivedFriend = friend_dao.get("7830");
        // test livedata is not null
        assertNotNull(liveRetreivedFriend);

        // retrieve user object to test specific information
        Friend retreivedFriend = friend_dao.friendGet("7830");

        // transform to json, to check there is no "private code" information
        String actualFriendJson = retreivedFriend.toJSON();
        // truncate the string so time stamp are not included
        String friendJsonEl1 = actualFriendJson.split(",")[0];
        String friendJsonEl2 = actualFriendJson.split(",")[1];
        String friendJsonEl3 = actualFriendJson.split(",")[2];
        String friendJsonEl4 = actualFriendJson.split(",")[3];
        assertEquals("{\"publicCode\":\"7830\"", friendJsonEl1);
        assertEquals("\"label\":\"nini\"", friendJsonEl2);
        assertEquals("\"latitude\":32.0", friendJsonEl3);
        assertEquals("\"longitude\":-124.0", friendJsonEl4);

    }



}
