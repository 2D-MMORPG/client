package com.jukusoft.mmo.downloader;

import com.jukusoft.mmo.utils.AppUtils;
import com.jukusoft.mmo.utils.CacheUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DownloaderTest {

    @Test
    public void testConstructor () {
        new Downloader();
    }

    @Test (timeout = 3000)
    public void testDownload () throws IOException {
        AppUtils.setAppName("junit-test");

        Downloader downloader = new Downloader();

        CacheUtils.createCacheDirIfAbsent("my-download-cache");

        //delete file, if already exists
        Files.deleteIfExists(new File(CacheUtils.getCacheDir("my-download-cache") + "download.png").toPath());

        downloader.startDownload("http://mmo.jukusoft.com/api/junit-test-image-32.png", new File(CacheUtils.getCacheDir("my-download-cache") + "download.png"));

        //wait, while download is completed
        while (downloader.isDownloading()) {
            Thread.currentThread().yield();
        }

        System.out.println("state: " + downloader.getStatus().name());
        System.out.println("target path: " + downloader.getTargetPath());
    }

}
