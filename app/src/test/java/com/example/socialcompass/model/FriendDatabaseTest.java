package com.example.socialcompass.model;

import static org.junit.Assert.*;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.FriendDatabase;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FriendDatabaseTest extends TestCase {

    private FriendDao friendDao;
    private FriendDatabase friendDb;

    @Before
    public void initDb() {
        var context2 = ApplicationProvider.getApplicationContext();
        friendDb = Room.inMemoryDatabaseBuilder(context2, FriendDatabase.class)
                .allowMainThreadQueries().build();
        // indirectly test getDao function
        friendDao = friendDb.getDao();
    }

    @After
    public void closeDb() {
        friendDb.close();
    }

    @Test
    public void testExist() {
        Friend testFriend1 = new Friend("7830", "nini", 32, -124);
        Friend testFriend2 = new Friend("7831", "nini2", 32, 124);

        //Insert the user into the database
        friendDao.upsert(testFriend1);
        friendDao.upsert(testFriend2);

        // test exists
        assertTrue(friendDao.exists("7830"));
        assertTrue(friendDao.exists("7831"));

        // test not inserted should not exist
        assertFalse(friendDao.exists("7832"));
    }

    @Test
    public void testGet() {
        Friend testFriend1 = new Friend("7830",
                "nini", 32, -124);
        Friend testFriend2 = new Friend("7831",
                "nini2", 32, 124);

        //Insert the friend into the database
        friendDao.upsert(testFriend1);
        friendDao.upsert(testFriend2);

        // retrieve live data for the use in repository
        LiveData<Friend> liveRetrievedFriend = friendDao.get("7830");
        // test livedata is not null
        assertNotNull(liveRetrievedFriend);

        // retrieve user object to test specific information
        Friend retrievedFriend = friendDao.friendGet("7830");

        // transform to json, to check there is no "private code" information
        String actualFriendJson = retrievedFriend.toJSON();
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
