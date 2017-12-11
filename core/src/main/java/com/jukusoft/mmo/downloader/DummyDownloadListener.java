package com.jukusoft.mmo.downloader;

public class DummyDownloadListener implements DownloadListener {

    @Override
    public void onProgress(Downloader.STATE state, int downloadedSize, int fileSize, float progress) {
        //
    }

}
