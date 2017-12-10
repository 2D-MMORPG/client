package com.jukusoft.mmo.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RandomUtilsTest {

    @Test
    public void testConstructor () {
        new RandomUtils();
    }

    @Test
    public void testGetRandomNumber () {
        //define range
        int min = 1;
        int max = 6;

        //generate random number
        int n = RandomUtils.getRandomNumber(min, max);

        assertEquals(true, n >= min && n <= max);
    }

    @Test
    public void testRollTheDice () {
        RandomUtils.rollTheDice(6);
    }

    @Test
    public void testGetRandomFloat () {
        //define range
        float min = 1;
        float max = 6;

        //generate random number
        float n = RandomUtils.randomFloat(min, max);

        assertEquals(true, n >= min && n <= max);
    }

}
