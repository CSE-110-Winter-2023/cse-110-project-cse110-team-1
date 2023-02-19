package com.example.socialcompass;

import org.junit.Test;

import static org.junit.Assert.*;
public class UtilitiesTest {

    /**
     * test both isLongitudeWithinRange and isLatitudeWithinRange
     * */
    @Test
    public void testIsValueWithinRange() {
        assertTrue(Utilities.isLongitudeWithinRange("   35"));
        assertTrue(Utilities.isLongitudeWithinRange("    0"));
        assertFalse(Utilities.isLongitudeWithinRange("91"));
        assertFalse(Utilities.isLongitudeWithinRange("35e"));
        assertFalse(Utilities.isLongitudeWithinRange("abc"));

        assertTrue(Utilities.isLatitudeWithinRange(" 180"));
        assertTrue(Utilities.isLatitudeWithinRange("       0"));
        assertFalse(Utilities.isLatitudeWithinRange("181"));
        assertFalse(Utilities.isLongitudeWithinRange(""));
    }


    @Test
    public void testIsValidString() {
        assertTrue(Utilities.isValidString("abcs"));
        assertTrue(Utilities.isValidString("a"));
        assertTrue(Utilities.isValidString("125"));
        assertTrue(Utilities.isValidString("-10.6"));




        assertFalse(Utilities.isValidString("10a"));
        assertFalse(Utilities.isValidString((" ")));
        assertFalse(Utilities.isValidString(""));
        assertFalse(Utilities.isValidString("anc;rahn;firamjn;iskdnfc; isjkenf;irkasd"));
        assertFalse(Utilities.isValidString(null));
    }
}