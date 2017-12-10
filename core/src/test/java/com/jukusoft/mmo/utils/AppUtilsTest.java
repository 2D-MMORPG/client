package com.jukusoft.mmo.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppUtilsTest {

    @Test
    public void testConstructor () {
        new AppUtils();
    }

    @Test
    public void testGetterAndSetter () {
        AppUtils.setAppName("test-app");

        assertEquals("test-app", AppUtils.getAppName());
    }

    @Test (expected = IllegalStateException.class)
    public void testEmptyAppName () {
        AppUtils.appName = "";

        AppUtils.getAppName();
    }

    @Test (expected = NullPointerException.class)
    public void testNullSetter () {
        AppUtils.setAppName(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testEmptySetter () {
        AppUtils.setAppName("");
    }

}
