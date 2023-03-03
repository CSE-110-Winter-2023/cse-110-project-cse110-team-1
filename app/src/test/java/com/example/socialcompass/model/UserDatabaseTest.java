package com.example.socialcompass.model;

import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UserDatabaseTest extends TestCase {

    private UserDao dao;
    private UserDatabase db;

    @Before
    public void initDb() {
        var context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase.class).allowMainThreadQueries().build();
        // indirectly test getDao function

        dao = db.getDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void testUpsert() {
        User testUser1 = new User("7830", "858-135-2467",
                "nini", 32, -124);
        User testUser2 = new User("7831", "858-135-2468",
                "nini2", 32, 124);

        //Insert the user into the database
        long upsert1 = dao.upsert(testUser1);
        long upsert2 = dao.upsert(testUser2);

        assertNotEquals(upsert1,upsert2);

    }

    @Test
    public void testExist()  {
        User testUser1 = new User("7830", "858-135-2467",
                "nini", 32, -124);
        User testUser2 = new User("7831", "858-135-2468",
                "nini2", 32, 124);

        //Insert the user into the database
        dao.upsert(testUser1);
        dao.upsert(testUser2);

        // test exists
        assertTrue(dao.exists("7830"));
        assertTrue(dao.exists("7831"));
        // test not inserted should not exist
        assertFalse(dao.exists("7832"));
    }

    @Test
    public void testGet() throws InterruptedException{
        User testUser1 = new User("7830", "858-135-2467",
                "nini", 32, -124);
        User testUser2 = new User("7831", "858-135-2468",
                "nini2", 32, 124);

        //Insert the user into the database
        dao.upsert(testUser1);
        dao.upsert(testUser2);
        assertTrue(dao.exists("7830"));
        LiveData<User>  liveRetreivedUser = dao.get("7830");
        assertNotNull(liveRetreivedUser);
    }

    @Test
    public void testDelete() {
        User testUser1 = new User("7830", "858-135-2467",
                "nini", 32, -124);
        User testUser2 = new User("7831", "858-135-2468",
                "nini2", 32, 124);

        //Insert the user into the database
        dao.upsert(testUser1);
        dao.upsert(testUser2);
        assertTrue(dao.exists("7830"));
        assertTrue(dao.exists("7831"));

        dao.delete("858-135-2467");
        assertFalse(dao.exists("7830"));
        assertTrue(dao.exists("7831"));
    }


}
