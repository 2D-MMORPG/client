package com.jukusoft.mmo.utils;

import com.jukusoft.mmo.downloader.DummyDownloadListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

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

    @Test (expected = IOException.class)
    public void testDownloadNotExistentFileToCache () throws IOException {
        DownloaderUtils.downloadFileToCache("http://mmo.jukusoft.com/api/not-existent-file.png", true);
    }

    @Test
    public void testDownloadFileToCache () throws IOException {
        DownloaderUtils.downloadFileToCache("http://mmo.jukusoft.com/api/junit-test-image-32.png", true);
    }

    @Test
    public void testDownloadFileToCache1 () throws IOException {
        Files.deleteIfExists(new File(DownloaderUtils.generateFilePath("http://mmo.jukusoft.com/api/junit-test-image-32.png")).toPath());

        DownloaderUtils.downloadFileToCache("http://mmo.jukusoft.com/api/junit-test-image-32.png");
    }

    @Test
    public void testDownloadFileToCacheWithoutExistentFile () throws IOException {
        Files.deleteIfExists(new File(DownloaderUtils.generateFilePath("http://mmo.jukusoft.com/api/junit-test-image-32.png")).toPath());

        DownloaderUtils.downloadFileToCache("http://mmo.jukusoft.com/api/junit-test-image-32.png", true);
    }

    @Test
    public void testDownloadFileToCacheOverride () throws IOException {
        DownloaderUtils.downloadFileToCache("http://mmo.jukusoft.com/api/junit-test-image-32.png", true);
        DownloaderUtils.downloadFileToCache("http://mmo.jukusoft.com/api/junit-test-image-32.png", true);
    }

    @Test (expected = FileAlreadyExistsException.class)
    public void testDownloadFileToCacheNoOverride () throws IOException {
        DownloaderUtils.downloadFileToCache("http://mmo.jukusoft.com/api/junit-test-image-32.png", true);
        DownloaderUtils.downloadFileToCache("http://mmo.jukusoft.com/api/junit-test-image-32.png", false);
    }

    @Test (expected = FileAlreadyExistsException.class)
    public void testDownloadFileToCacheNoOverride1 () throws IOException {
        DownloaderUtils.downloadFileToCache("http://mmo.jukusoft.com/api/junit-test-image-32.png", true);
        DownloaderUtils.downloadFileToCache("http://mmo.jukusoft.com/api/junit-test-image-32.png");
    }

    @Test (expected = NullPointerException.class)
    public void testDownloadNullURL () throws IOException {
        DownloaderUtils.download(null, "", null, false);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testDownloadEmptyURL () throws IOException {
        DownloaderUtils.download("", "", null, false);
    }

    @Test (expected = NullPointerException.class)
    public void testDownloadNullTargetPath () throws IOException {
        DownloaderUtils.download("http://my-domain.de", null, null, false);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testDownloadEmptyTargetPath () throws IOException {
        DownloaderUtils.download("http://my-domain.de", "", null, false);
    }

    @Test (expected = NullPointerException.class)
    public void testDownloadNullListener () throws IOException {
        DownloaderUtils.download("http://my-domain.de", "my-file.png", null, false);
    }

    @Test (expected = FileAlreadyExistsException.class)
    public void testDownloadExistentFile () throws IOException {
        CacheUtils.createCacheDirIfAbsent("my-cache-dir");

        File file = new File(CacheUtils.getCacheDir("my-cache-dir") + "downloaded-file.txt");

        if (!file.exists()) {
            Files.createFile(file.toPath());
        }

        DownloaderUtils.download("http://my-domain.de", file.getAbsolutePath(), new DummyDownloadListener(), false);
    }

    @Test
    public void testDownload () throws IOException {
        CacheUtils.createCacheDirIfAbsent("my-cache-dir");

        File file = new File(CacheUtils.getCacheDir("my-cache-dir") + "downloaded-file2.txt");

        DownloaderUtils.download("http://mmo.jukusoft.com/api/junit-test-image-32.png", file.getAbsolutePath(), new DummyDownloadListener(), true);
    }

    @Test
    public void testDownloadOverride () throws IOException {
        CacheUtils.createCacheDirIfAbsent("my-cache-dir");

        File file = new File(CacheUtils.getCacheDir("my-cache-dir") + "downloaded-file.txt");

        if (!file.exists()) {
            Files.createFile(file.toPath());
        }

        DownloaderUtils.download("http://mmo.jukusoft.com/api/junit-test-image-32.png", file.getAbsolutePath(), new DummyDownloadListener(), true);
    }

}
