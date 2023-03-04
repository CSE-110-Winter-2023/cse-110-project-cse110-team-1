package com.example.socialcompass;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

public class UtilitiesTest {

    /**
     * test both isLongitudeWithinRange and isLatitudeWithinRange
     * */
    @Test
    public void testIsValueWithinRange() {
        assertTrue(Utilities.isLongitudeWithinRange("35"));
        assertTrue(Utilities.isLongitudeWithinRange("0"));
        assertTrue(Utilities.isLongitudeWithinRange("91"));
        assertFalse(Utilities.isLongitudeWithinRange("35e"));
        assertFalse(Utilities.isLongitudeWithinRange("abc"));

        assertFalse(Utilities.isLatitudeWithinRange(" 180"));
        assertTrue(Utilities.isLatitudeWithinRange("0"));
        assertFalse(Utilities.isLatitudeWithinRange("181"));
        assertFalse(Utilities.isLongitudeWithinRange(""));
    }


    @Test
    public void testIsValidString() {
        assertTrue(Utilities.isValidString("abcs"));
        assertTrue(Utilities.isValidString("a"));
        assertTrue(Utilities.isValidString("125"));
        assertTrue(Utilities.isValidString("-10.6"));


        assertTrue(Utilities.isValidString("10a"));
        assertFalse(Utilities.isValidString((" ")));
        assertFalse(Utilities.isValidString(""));
        assertFalse(Utilities.isValidString("anc;rahn;firamjn;iskdnfc; isjkenf;irkasd"));
        assertFalse(Utilities.isValidString(null));
    }


    @Test
    public void testGeneratedIdIsUnique() {
        String id1 = Utilities.generatePrivateId();
        String id2 = Utilities.generatePrivateId();
        assertNotEquals("Generated IDs should be unique", id1, id2);
    }

    @Test
    public void testGeneratedIdHasCorrectFormat() {
        String id = Utilities.generatePrivateId();
        assertTrue("Generated ID should be a UUID",
                id.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
    }

    @Test
    public void testGeneratedUUIDsAreUnique() {
        int numberOfUUIDsToGenerate = 1000000;
        int count = 0;
        Set<String> uuids = new HashSet<>();
        for (int i = 0; i < numberOfUUIDsToGenerate; i++) {
            uuids.add(Utilities.generatePrivateId());
            count++;
        }
        assertEquals("Expected 1000000 unique UUIDs", numberOfUUIDsToGenerate, uuids.size());
        assertEquals(count,1000000);
    }

}