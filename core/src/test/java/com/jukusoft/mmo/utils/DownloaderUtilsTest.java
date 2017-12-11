package com.jukusoft.mmo.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DownloaderUtilsTest {

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

}
