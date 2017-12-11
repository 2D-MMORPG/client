package com.jukusoft.mmo.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
        CacheUtils.createCacheDirIfAbsent("my-cache");

        //check if file exists
        assertEquals(true, new File(CacheUtils.getCachePath()).exists());
        assertEquals(true, new File(CacheUtils.getCacheDir("test-app", "my-cache")).exists());
        assertEquals(true, new File(CacheUtils.getCacheDir("my-cache")).exists());
    }

    @Test
    public void testCreateCacheDirIfAbsent2 () {
        AppUtils.setAppName("junit-test");

        if (new File(CacheUtils.getCacheDir("my-cache")).exists()) {
            new File(CacheUtils.getCacheDir("my-cache")).delete();
        }

        CacheUtils.createCacheDirIfAbsent();
        CacheUtils.createCacheDirIfAbsent("my-cache");

        //check if file exists
        assertEquals(true, new File(CacheUtils.getCacheDir("junit-test", "my-cache")).exists());
        assertEquals(true, new File(CacheUtils.getCacheDir("my-cache")).exists());
    }

    @Test
    public void testCreateCacheDirIfAbsent3 () {
        AppUtils.setAppName("junit-test");

        if (new File(CacheUtils.getCacheDir("my-cache")).exists()) {
            new File(CacheUtils.getCacheDir("my-cache")).delete();
        }

        CacheUtils.createCacheDirIfAbsent("my-cache");

        //check if file exists
        assertEquals(true, new File(CacheUtils.getCacheDir("junit-test", "my-cache")).exists());
        assertEquals(true, new File(CacheUtils.getCacheDir("my-cache")).exists());
    }

    @Test
    public void testCreateCacheDirIfAbsent4 () {
        AppUtils.setAppName("junit-test");

        if (new File(CacheUtils.getCacheDir("my-cache")).exists()) {
            new File(CacheUtils.getCacheDir("my-cache")).delete();
        }

        if (new File(CacheUtils.getCachePath()).exists()) {
            new File(CacheUtils.getCachePath()).delete();
        }

        CacheUtils.createCacheDirIfAbsent("my-cache");

        //check if file exists
        assertEquals(true, new File(CacheUtils.getCacheDir("junit-test", "my-cache")).exists());
        assertEquals(true, new File(CacheUtils.getCacheDir("my-cache")).exists());
    }

    @Test
    public void testCreateCacheDirIfAbsent5 () throws IOException {
        AppUtils.setAppName("junit-test");

        String path = CacheUtils.getCachePath(AppUtils.getAppName());

        //delete directory, if exists
        if (new File(path).exists()) {
            FileUtils.recursiveDeleteDirectory(new File(path));
            //Files.delete(new File(path).toPath());
        }

        CacheUtils.createCacheDirIfAbsent();
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

    @Test
    public void testEndsWithSlash () {
        assertEquals(true, CacheUtils.getCacheDir("junit-test", "my-cache").endsWith("/"));
    }

    @Test
    public void testEndsWithSlash1 () {
        assertEquals(true, CacheUtils.getCacheDir("my-cache").endsWith("/"));
    }

}
