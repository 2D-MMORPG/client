package com.jukusoft.mmo.downloader;

import org.junit.Test;

public class DummyDownloadListenerTest {

    @Test
    public void testConstructor () {
        new DummyDownloadListener();
    }

    @Test
    public void testOnProgress () {
        DummyDownloadListener listener = new DummyDownloadListener();
        listener.onProgress(Downloader.STATE.COMPLETED, 10, 20, 0.5f);
    }

}
