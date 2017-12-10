package com.jukusoft.mmo.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class MapCacheUtilsTest {

    @Test
    public void testConstructor () {
        new MapCacheUtils();
    }

    @Test
    public void testEndsWithSlash () {
        assertEquals(true, MapCacheUtils.getMapCachePath("test-app").endsWith("/"));
    }

    @Test
    public void testEndsWithSlash1 () {
        assertEquals(true, MapCacheUtils.getMapCachePath().endsWith("/"));
    }

    @Test
    public void testCreateMapCacheDirIfAbsent () {
        AppUtils.setAppName("test-app");

        MapCacheUtils.createMapCacheDirIfAbsent();

        //check if file exists
        assertEquals(true, new File(MapCacheUtils.getMapCachePath("test-app")).exists());
    }

    @Test
    public void testCreateMapCacheDirIfAbsent2 () {
        AppUtils.setAppName("junit-test");

        if (new File(MapCacheUtils.getMapCachePath("junit-test")).exists()) {
            new File(MapCacheUtils.getMapCachePath("junit-test")).delete();
        }

        MapCacheUtils.createMapCacheDirIfAbsent();

        //check if file exists
        assertEquals(true, new File(MapCacheUtils.getMapCachePath("junit-test")).exists());
    }

}
