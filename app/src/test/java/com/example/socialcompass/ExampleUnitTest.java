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
        assertTrue(Utilities.isLongitudeWithinRange("   35N"));
        assertTrue(Utilities.isLatitudeWithinRange(" 179E"));
        assertTrue(Utilities.isLongitudeWithinRange("    0N"));
        assertTrue(Utilities.isLatitudeWithinRange("       0E"));
        assertFalse(Utilities.isLongitudeWithinRange("91N"));
        assertFalse(Utilities.isLatitudeWithinRange("181E"));
        assertFalse(Utilities.isLongitudeWithinRange("35X"));
        assertFalse(Utilities.isLongitudeWithinRange("abc"));
        assertFalse(Utilities.isLongitudeWithinRange(""));
    }


}