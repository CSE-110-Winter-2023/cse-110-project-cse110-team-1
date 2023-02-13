package com.example.socialcompass;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testIsWithinRange() {
        assertTrue(Utilities.isLongitudeWithinRange("   35"));
        assertTrue(Utilities.isLatitudeWithinRange(" 180"));
        assertTrue(Utilities.isLongitudeWithinRange("    0"));
        assertTrue(Utilities.isLatitudeWithinRange("       0"));
        assertFalse(Utilities.isLongitudeWithinRange("91"));
        assertFalse(Utilities.isLatitudeWithinRange("181"));
        assertFalse(Utilities.isLongitudeWithinRange("35e"));
        assertFalse(Utilities.isLongitudeWithinRange("abc"));
        assertFalse(Utilities.isLongitudeWithinRange(""));
    }
}