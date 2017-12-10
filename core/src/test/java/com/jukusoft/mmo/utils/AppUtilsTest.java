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

}
