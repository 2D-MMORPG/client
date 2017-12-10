package com.jukusoft.mmo.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathUtilsTest {

    @Test
    public void testOverlapping () {
        assertEquals(false, MathUtils.overlapping(1, 2, 3, 4));
        assertEquals(true, MathUtils.overlapping(1, 3, 2, 4));
        assertEquals(false, MathUtils.overlapping(1, 2.99f, 3, 4));
        assertEquals(true, MathUtils.overlapping(1, 2, 2, 4));
    }

}
