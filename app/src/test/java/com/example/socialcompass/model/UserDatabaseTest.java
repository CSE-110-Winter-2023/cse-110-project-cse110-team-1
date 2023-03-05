//package com.example.socialcompass.model;
//
//import static org.junit.Assert.assertNotEquals;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Room;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//
//import com.example.socialcompass.Friendmodel.Friend;
//import com.example.socialcompass.model.User.User;
//import com.example.socialcompass.model.User.UserDao;
//import com.example.socialcompass.model.User.UserDatabase;
//
//import junit.framework.TestCase;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class UserDatabaseTest extends TestCase {
//
//    private UserDao dao;
//    private UserDatabase db;
//
//    @Before
//    public void initDb() {
//        var context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, UserDatabase.class).allowMainThreadQueries().build();
//        // indirectly test getDao function
//
//        dao = db.getDao();
//    }
//
//    @After
//    public void closeDb() {
//        db.close();
//    }
//
//    @Test
//    public void testUpsert() {
//        User testUser1 = new User("7830", "858-135-2467",
//                "nini", 32, -124);
//        User testUser2 = new User("7831", "858-135-2468",
//                "nini2", 32, 124);
//
//        //Insert the user into the database
//        long upsert1 = dao.upsert(testUser1);
//        long upsert2 = dao.upsert(testUser2);
//
//        assertNotEquals(upsert1,upsert2);
//
//    }
//
//    @Test
//    public void testExist()  {
//        User testUser1 = new User("7830", "858-135-2467",
//                "nini", 32, -124);
//        User testUser2 = new User("7831", "858-135-2468",
//                "nini2", 32, 124);
//
//        //Insert the user into the database
//        dao.upsert(testUser1);
//        dao.upsert(testUser2);
//
//        // test exists
//        assertTrue(dao.exists("7830"));
//        assertTrue(dao.exists("7831"));
//        // test not inserted should not exist
//        assertFalse(dao.exists("7832"));
//    }
//
//    @Test
//    public void testGet() throws InterruptedException{
//        User testUser1 = new User("7830", "858-135-2467",
//                "nini", 32, -124);
//        User testUser2 = new User("7831", "858-135-2468",
//                "nini2", 32, 124);
//
//        //Insert the user into the database
//        dao.upsert(testUser1);
//        dao.upsert(testUser2);
//
//        // retrieve live data for the use in repository
//        LiveData<User>  liveRetreivedUser = dao.get("7830");
//        // test livedata is not null
//        assertNotNull(liveRetreivedUser);
//
//        // retrieve user object to test specific information
//        User retreivedUser = dao.userGet("7830");
//        assertNotNull(retreivedUser);
//        assertEquals(testUser1.public_code, retreivedUser.public_code);
//        assertEquals(testUser1.private_code, retreivedUser.private_code);
//        assertEquals(testUser1.label, retreivedUser.label);
//        assertEquals(testUser1.longitude, retreivedUser.longitude);
//        assertEquals(testUser1.latitude, retreivedUser.latitude);
//    }
//
//
//    @Test
//    public void testDelete() {
//        User testUser1 = new User("7830", "858-135-2467",
//                "nini", 32, -124);
//        User testUser2 = new User("7831", "858-135-2468",
//                "nini2", 32, 124);
//
//        //Insert the user into the database
//        dao.upsert(testUser1);
//        dao.upsert(testUser2);
//
//        // check they exists
//        assertTrue(dao.exists("7830"));
//        assertTrue(dao.exists("7831"));
//
//        // perform deletion
//        dao.delete("858-135-2467");
//
//        // check if public code 7830 no longer exists, but public code 7831 still exists
//        assertFalse(dao.exists("7830"));
//        assertTrue(dao.exists("7831"));
//    }
//
//
//}
