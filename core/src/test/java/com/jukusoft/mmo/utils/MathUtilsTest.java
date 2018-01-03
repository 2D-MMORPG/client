package com.jukusoft.mmo.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathUtilsTest {

    @Test
    public void testConstructor () {
        new MathUtils();
    }

    @Test
    public void testOverlapping () {
        assertEquals(false, MathUtils.overlapping(1, 2, 3, 4));
        assertEquals(true, MathUtils.overlapping(1, 3, 2, 4));
        assertEquals(false, MathUtils.overlapping(1, 2.99f, 3, 4));
        assertEquals(true, MathUtils.overlapping(1, 2, 2, 4));

        assertEquals(true, MathUtils.overlapping(4, 6, 2, 4));
        assertEquals(true, MathUtils.overlapping(3, 6, 2, 4));

        //swap values
        assertEquals(false, MathUtils.overlapping(2, 1, 4, 3));
        assertEquals(true, MathUtils.overlapping(3, 1, 4, 2));
        assertEquals(false, MathUtils.overlapping(2.99f, 1, 4, 3));
        assertEquals(true, MathUtils.overlapping(2, 1, 4, 2));

        //check equal ranges
        assertEquals(true, MathUtils.overlapping(2, 2, 2, 2));
        assertEquals(true, MathUtils.overlapping(2, 3, 2, 3));

        assertEquals(true, MathUtils.overlapping(2, 3, 1, 5));
        assertEquals(true, MathUtils.overlapping(1, 5, 2, 3));
    }

}
