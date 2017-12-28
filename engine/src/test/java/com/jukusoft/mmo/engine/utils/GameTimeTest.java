package com.jukusoft.mmo.engine.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameTimeTest {

    @Test
    public void testConstructor () {
        new GameTime();
    }

    @Test
    public void testGetInstance () {
        assertNotNull(GameTime.getInstance());

        //get instance again
        assertNotNull(GameTime.getInstance());
    }

    @Test (expected = NullPointerException.class)
    public void testUpdate () {
        GameTime gameTime = new GameTime();

        //update
        gameTime.update(true);
    }

    @Test
    public void testUpdate1 () {
        GameTime gameTime = new GameTime();

        //check startup time
        assertEquals(true, gameTime.getStartUpTime() <= System.currentTimeMillis());

        long currentTime = System.currentTimeMillis();

        //update
        gameTime.update(false);

        assertEquals(true, currentTime <= gameTime.getTime());
        assertEquals(true, System.currentTimeMillis() >= gameTime.getTime());
    }

    @Test
    public void testGetDeltaTime () {
        GameTime gameTime = new GameTime();

        //because libGDX hasnt rendered yet, delta time should be 0
        assertEquals(0f, gameTime.getDeltaTime(), 0);
    }

}
