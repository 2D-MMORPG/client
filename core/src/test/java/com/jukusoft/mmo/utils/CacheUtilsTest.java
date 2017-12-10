package com.jukusoft.mmo.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class CacheUtilsTest {

    @Test
    public void testConstructor () {
        new CacheUtils();
    }

    @Test
    public void testGetCachePath () {
        AppUtils.setAppName("junit-test");

        assertEquals(true, CacheUtils.getCachePath().endsWith("/cache/"));
    }

    @Test
    public void testCreateCacheDirIfAbsent () {
        AppUtils.setAppName("test-app");

        CacheUtils.createCacheDirIfAbsent();

        //check if file exists
        assertEquals(true, new File(CacheUtils.getCachePath()).exists());
    }

    @Test
    public void testCreateCacheDirIfAbsent2 () {
        AppUtils.setAppName("junit-test");

        if (new File(CacheUtils.getCachePath("junit-test")).exists()) {
            new File(CacheUtils.getCachePath("junit-test")).delete();
        }

        CacheUtils.createCacheDirIfAbsent();

        //check if file exists
        assertEquals(true, new File(CacheUtils.getCachePath("junit-test")).exists());
    }

    @Test
    public void testGetCacheDir () {
        AppUtils.setAppName("junit-test");

        assertEquals(true, CacheUtils.getCacheDir("junit-test", "my-cache").endsWith("/my-cache/"));
    }

    @Test
    public void testGetCacheDir1 () {
        AppUtils.setAppName("junit-test");

        assertEquals(true, CacheUtils.getCacheDir("my-cache").endsWith("/my-cache/"));
    }

}
