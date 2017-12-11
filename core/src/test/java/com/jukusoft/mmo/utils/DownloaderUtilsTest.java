package com.jukusoft.mmo.utils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class DownloaderUtilsTest {

    @BeforeClass
    public static void beforeClass () {
        AppUtils.setAppName("junit-test");
    }

    @AfterClass
    public static void afterClass () {
        //
    }

    @Test
    public void testConstructor () {
        new DownloaderUtils();
    }

    @Test (expected = NullPointerException.class)
    public void testGetNullFileExtension () {
        DownloaderUtils.getFileExtension(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetEmptyFileExtension () {
        DownloaderUtils.getFileExtension("");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetFileExtensionWithoutDot () {
        DownloaderUtils.getFileExtension("my-file");
    }

    @Test
    public void testGetFileExtension () {
        assertEquals("txt", DownloaderUtils.getFileExtension("my-text-file.txt"));
        assertEquals("txt", DownloaderUtils.getFileExtension("my-text.file.txt"));
        assertEquals("png", DownloaderUtils.getFileExtension("http://my-example-domain.com/images/image.png"));
    }

    @Test (expected = NullPointerException.class)
    public void testGenerateNullFilePath () {
        DownloaderUtils.generateFilePath(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGenerateEmptyFilePath () {
        DownloaderUtils.generateFilePath("");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGenerateFilePathWithoutDot () {
        DownloaderUtils.generateFilePath("my-file");
    }

    @Test
    public void testGenerateFilePath () {
        assertEquals(true, DownloaderUtils.generateFilePath("my-file.txt").endsWith(".txt"));
        assertEquals(true, DownloaderUtils.generateFilePath("my-file.txt").contains("/downloaded-files/"));

        assertEquals(true, DownloaderUtils.generateFilePath("http://my-example-domain.com/images/image.png").endsWith("/cache/downloaded-files/5600bf88a076afda4ac7641739f0bde7.png"));
    }

}
