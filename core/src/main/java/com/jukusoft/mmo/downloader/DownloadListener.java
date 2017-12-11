package com.jukusoft.mmo.downloader;

@FunctionalInterface
public interface DownloadListener {

    /**
    * on progress observer
     *
     * @param state current download state
     * @param downloadedSize downloaded bytes
     * @param fileSize file size
     * @param progress current progress
    */
    public void onProgress (Downloader.STATE state, int downloadedSize, int fileSize, float progress);

}
