package com.example.socialcompass.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.socialcompass.model.api.API;
import com.example.socialcompass.model.friend.Friend;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Helper class
 * Will execute the script file
 */
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

@RunWith(AndroidJUnit4.class)
public class APITest extends TestCase {
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
    }

    @After
    public void dismantle() {
        // remove the test friend from server
        String script = "./src/test/java/com/example/socialcompass/model/delete_script";
        ExecuteShellCommand.executeCommand(script);
    }


    @Test
    public void testGetFriend() {
        Friend friend = api.getFriend(testFriend.publicCode);
        assertEquals(testFriend.publicCode, friend.publicCode);
        assertEquals(testFriend.label, friend.label);
        assertEquals(testFriend.latitude, friend.latitude);
        assertEquals(testFriend.longitude, friend.longitude);
    }

    @Test
    public void testPutUser() throws InterruptedException {
        testFriend.longitude = (float)-69.424242;
        testFriend.latitude = (float)24.242424;

        api.putUser(testFriend, privateCode);
        // because putUser run asynchronously, need to wait for the action to finish
        TimeUnit.SECONDS.sleep(3);

        Friend friend = api.getFriend(testFriend.publicCode);

        assertEquals(testFriend.publicCode, friend.publicCode);
        assertEquals(testFriend.label, friend.label);
        assertEquals(testFriend.latitude, friend.latitude);
        assertEquals(testFriend.longitude, friend.longitude);
    }
}
