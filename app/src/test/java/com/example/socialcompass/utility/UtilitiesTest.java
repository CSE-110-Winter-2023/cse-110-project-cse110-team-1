package com.example.socialcompass.utility;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.socialcompass.utility.Utilities;

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

    @Test
    public void testGeneratePublicID() {
        Set<String> idSet = new HashSet<>();
        for (int i = 0; i < 1000000; i++) {
            String id = Utilities.generatePublicId();
            assertFalse(idSet.contains(id)); // Check if ID is unique
            idSet.add(id);
        }
    }


    @Test
    public void testCalculateDistance() {
        double lat1 = 37.7749; // San Francisco
        double lon1 = -122.4194;
        double lat2 = 40.7128; // New York City
        double lon2 = -74.0060;

        double expectedDistance = 2571.1;
        double actualDistance = Utilities.calculateDistanceInMiles(lat1, lon1, lat2, lon2);

        double delta = 15; // allow for some rounding error
        assertEquals(expectedDistance, actualDistance, delta);
    }

    @Test
    public void testCalculateDistance_LosAngelesToChicago() {
        double lat1 = 34.0522; // Los Angeles
        double lon1 = -118.2437;
        double lat2 = 41.8781; // Chicago
        double lon2 = -87.6298;

        double expectedDistance = 1744.4;
        double actualDistance = Utilities.calculateDistanceInMiles(lat1, lon1, lat2, lon2);

        double delta = 15;
        assertEquals(expectedDistance, actualDistance, delta);
    }

    @Test
    public void testCalculateDistance_MiamiToSeattle() {
        double lat1 = 25.7617; // Miami
        double lon1 = -80.1918;
        double lat2 = 47.6062; // Seattle
        double lon2 = -122.3321;

        double expectedDistance = 2728.7;
        double actualDistance = Utilities.calculateDistanceInMiles(lat1, lon1, lat2, lon2);

        double delta = 15;
        assertEquals(expectedDistance, actualDistance, delta);
    }

    @Test
    public void testCalculateDistance_SydneyToTokyo() {
        double lat1 = -33.8651; // Sydney
        double lon1 = 151.2094;
        double lat2 = 35.6762; // Tokyo
        double lon2 = 139.6503;

        double expectedDistance = 4863.10;
        double actualDistance = Utilities.calculateDistanceInMiles(lat1, lon1, lat2, lon2);

        double delta = 15;
        assertEquals(expectedDistance, actualDistance, delta);
    }

    @Test
    public void testCalculateDistance_LondonToDubai() {
        double lat1 = 51.5074; // London
        double lon1 = -0.1278;
        double lat2 = 25.2048; // Dubai
        double lon2 = 55.2708;

        double expectedDistance = 3412.2;
        double actualDistance = Utilities.calculateDistanceInMiles(lat1, lon1, lat2, lon2);

        double delta = 15;
        assertEquals(expectedDistance, actualDistance, delta);
    }

    @Test
    public void testCalculateDistance_SydneyToLosAngeles() {
        double lat1 = -33.8651; // Sydney
        double lon1 = 151.2094;
        double lat2 = 34.0522; // Los Angeles
        double lon2 = -118.2437;

        double expectedDistance = 7491.5;
        double actualDistance = Utilities.calculateDistanceInMiles(lat1, lon1, lat2, lon2);

        double delta = 15;
        assertEquals(expectedDistance, actualDistance, delta);

    }

    @Test
    public void testCalculateDistance_NorthPoleToSouthPole() {
        double lat1 = 90; // North pole
        double lon1 = 180;
        double lat2 = -90; // South pole
        double lon2 = 123;

        double expectedDistance = 12436.81;
        double actualDistance = Utilities.calculateDistanceInMiles(lat1, lon1, lat2, lon2);

        double delta = 15;
        assertEquals(expectedDistance, actualDistance, delta);

    }
    @Test
    public void testCalculateDistance_NorthPoleToNorthPole() {
        double lat1 = 90; // Sydney
        double lon1 = 180;
        double lat2 = 90; // Los Angeles
        double lon2 = 123;

        double expectedDistance = 0;
        double actualDistance = Utilities.calculateDistanceInMiles(lat1, lon1, lat2, lon2);

        double delta = 1;
        assertEquals(expectedDistance, actualDistance, delta);

    }
    @Test
    public void testCalculateDistance_ucsd() {
        double lat1 = 32.867240; // Vons
        double lon1 = -117.219376;
        double lat2 = 32.8565867; // Kernymesa
        double lon2 = -117.1722693;

        double expectedDistance = 3;
        double actualDistance = Utilities.calculateDistanceInMiles(lat1, lon1, lat2, lon2);

        double delta = 0.5;
        assertEquals(expectedDistance, actualDistance, delta);

    }

}