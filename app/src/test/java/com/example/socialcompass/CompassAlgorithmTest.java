package com.example.socialcompass;

import org.junit.Test;
import static org.junit.Assert.*;

public class CompassAlgorithmTest {
    private static final float MAX_DELTA = 0.5f; // maximum floating-point deviation from expected answer
    private static final float long30 = (float)Math.sqrt(3)/2;

    /**
     * Test if algorithm works at basic angles when gpsLat & gpsLong are at (0,0)
     */
    @Test
    public void testAlgorithmValuesBasic1() {
        assertEquals(45, Utilities.getAngle(0,0,1,1), MAX_DELTA);
        assertEquals(0, Utilities.getAngle(0,0,0.0001f,0), MAX_DELTA);
    }

    /**
     * Test for all quadrants
     */
    @Test
    public void testAlgorithmValuesBasic2() {
        assertEquals(135,Utilities.getAngle(0,0,-4,4),MAX_DELTA);
        assertEquals(225,Utilities.getAngle(0,0,-12,-12),MAX_DELTA);
        assertEquals(315,Utilities.getAngle(0,0,5,-5),MAX_DELTA);
    }

    /**
     * Test for values in increments of 30
     */
    @Test
    public void testAlgorithmValuesBasic3() {
        assertEquals(30,Utilities.getAngle(0,0,long30,0.5f),MAX_DELTA);
        assertEquals(240,Utilities.getAngle(0,0,-0.5f,-long30),MAX_DELTA);
    }

    /**
     * Test if algorithm works at more angles than just intervals of 30 or 45 deg
     * Also at (0,0)
     */
    @Test
    public void testAlgorithmValuesAtDiverseAngles() {
        for(float i = 0; i < 2*Math.PI; i+=0.01) {
            assertEquals(i*180/Math.PI,Utilities.getAngle(0,0,(float)Math.cos(i),(float)Math.sin(i)),MAX_DELTA);
        }
    }

    /**
     * Test when gpsLat and gpsLong are not zero.
     * Basically just tests if addition and subtraction work for the algorithms.
     */
    @Test
    public void testAlgorithmValuesAtOtherSpots() {
        assertEquals(135,Utilities.getAngle(2,1,-3,6),MAX_DELTA);
        assertEquals(120,Utilities.getAngle(-15,4.002f,-15.5f,4.002f+long30),MAX_DELTA);
    }
}
