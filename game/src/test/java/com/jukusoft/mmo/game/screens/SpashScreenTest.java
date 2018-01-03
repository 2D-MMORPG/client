package com.jukusoft.mmo.game.screens;

import com.jukusoft.mmo.game.GameUnitTest;
import org.junit.Test;

public class SpashScreenTest extends GameUnitTest {

    @Test
    public void testConstructor () {
        new SplashScreen();
    }

    @Test
    public void testStartAndStop () {
        SplashScreen screen = new SplashScreen();

        screen.onStart();

        screen.onResume();

        screen.onPause();

        screen.onStop();
    }

    @Test
    public void testProcessInput () {
        SplashScreen screen = new SplashScreen();
        screen.processInput();
    }

    @Test
    public void testUpdate () {
        SplashScreen screen = new SplashScreen();
        screen.update();
    }

    @Test
    public void testDraw () {
        SplashScreen screen = new SplashScreen();
        screen.draw();
    }

}
