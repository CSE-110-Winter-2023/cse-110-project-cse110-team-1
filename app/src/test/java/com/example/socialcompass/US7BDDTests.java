package com.example.socialcompass;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import android.location.LocationManager;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import org.robolectric.shadows.ShadowLocationManager;

import static org.junit.Assert.*;

import com.example.socialcompass.activity.CompassActivity;
import com.example.socialcompass.model.api.API;
import com.example.socialcompass.model.friend.Friend;
import com.example.socialcompass.model.friend.FriendDao;
import com.example.socialcompass.model.friend.FriendDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Config(
        instrumentedPackages = {
                // required to access final members on androidx.loader.content.ModernAsyncTask
                "androidx.loader.content"
        })
class ExecuteShellCommand {
    public static void executeCommand(String filePath) {
        try {
            // set permission to the file to executable
            File file = new File(filePath);
            boolean result = file.setExecutable(true);
            // if the file can be set as executable
            if(result){
                // execute the file
                Process process = Runtime.getRuntime().exec(filePath);
                // print the result
                BufferedReader read = new BufferedReader(new InputStreamReader(
                        process.getInputStream())
                );
                process.waitFor();
                while (read.ready()) {
                    System.out.println(read.readLine());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

@RunWith(RobolectricTestRunner.class)
public class US7BDDTests {
    private FriendDao friendDao;
    private FriendDatabase friendDb;
    private LocationManager myLocationManager;
    private ShadowLocationManager shadowLocationManager;

    private API api;
    private String privateCode;
    private Friend testFriend;

    @Before
    public void setup() {
        api = API.provide(null);
        privateCode = "some random string here";
        testFriend = new Friend("the-world", "The world", (float) -25.326356, (float) 33.25622);
        // put the test friend to server
        String script = "./src/test/java/com/example/socialcompass/model/create_script";
        ExecuteShellCommand.executeCommand(script);


        // clear all pre-existing friends
        var context2 = ApplicationProvider.getApplicationContext();
        friendDb = Room.inMemoryDatabaseBuilder(context2, FriendDatabase.class)
                .allowMainThreadQueries().build();
        // indirectly test getDao function
        friendDao = friendDb.getDao();
    }

    @After
    public void dismantle() {
        // remove the test friend from server
        String script = "./src/test/java/com/example/socialcompass/model/delete_script";
        ExecuteShellCommand.executeCommand(script);
    }


    @Test
    public void US3andDeveloperStoryTest() {
        // Context of the app under test.
        try(ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class)) {
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.onActivity(activity -> {


                Friend friend = api.getFriend(testFriend.publicCode);
                assertEquals(testFriend.publicCode, friend.publicCode);
                assertEquals(testFriend.label, friend.label);
                assertEquals(0,Float.compare(testFriend.longitude, friend.longitude));
                assertEquals(0,Float.compare(testFriend.latitude, friend.latitude));


                //turn off gps service
                LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                ShadowLocationManager shadowLocationManager = Shadows.shadowOf(locationManager);
                shadowLocationManager.setProviderEnabled(LocationManager.GPS_PROVIDER, false);

                assertFalse(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
                testFriend.longitude = (float)-69.424242;
                testFriend.latitude = (float)24.242424;

                api.putUser(testFriend, privateCode);
                // because putUser run asynchronously, need to wait for the action to finish
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Friend afterFriend = api.getFriend(testFriend.publicCode);

                assertEquals(testFriend.publicCode, afterFriend.publicCode);
                assertEquals(testFriend.label, afterFriend.label);
                assertEquals(0,Float.compare(testFriend.latitude, afterFriend.latitude));
                assertEquals(0,Float.compare(testFriend.longitude, afterFriend.longitude));

            });
        }
    }
}