package com.jukusoft.mmo.downloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Downloader class to download files
 *
 * @link https://www.java-tips.org/java-se-tips-100019/15-javax-swing/1391-how-to-create-a-download-manager-in-java.html
*/
public class Downloader extends Observable implements Runnable {

    // Max size of download buffer in bytes
    protected static final int MAX_BUFFER_SIZE = 1024;

    //source URL
    protected URL url = null;

    //file size in bytes
    protected volatile int fileSize = 0;

    //number of downloaded bytes
    protected volatile int downloadedBytes = 0;

    protected File targetFile = null;

    public enum STATE {
        DOWNLOADING,

        PAUSED,

        COMPLETED,

        CANCELLED,

        ERROR
    };

    protected STATE currentState = STATE.DOWNLOADING;

    /**
    * default constructor
    */
    public Downloader () {
        //
    }

    public void startDownload (String fileURL, File targetFile) throws IOException {
        this.url = new URL(fileURL);
        this.targetFile = targetFile;

        this.fileSize = -1;
        this.downloadedBytes = 0;

        //reset state
        this.currentState = STATE.DOWNLOADING;

        //check, if file already exists
        if (targetFile.exists()) {
            throw new FileAlreadyExistsException("file aready exists: " + targetFile.getAbsolutePath());
        }

        //create target file
        Files.createFile(targetFile.toPath());

        //create new thread and start download
        this.download();
    }

    protected void download () {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
    * get source download URL
     *
     * @return download url
    */
    public String getDownloadUrl() {
        return url.toString();
    }

    public File getTargetFile () {
        return this.targetFile;
    }

    public String getTargetPath () {
        return this.targetFile.getAbsolutePath();
    }

    public int getDownloadedBytes () {
        return this.downloadedBytes;
    }

    /**
    * get file size in bytes
     *
     * @return file size in bytes
    */
    public int getFileSize () {
        return this.fileSize;
    }

    /**
    * get download progress
     *
     * @return get download progress in range 0 .. 1
    */
    public float getProgress() {
        return ((float) this.downloadedBytes / this.fileSize);
    }

    public STATE getState () {
        return this.currentState;
    }

    public boolean isDownloading () {
        return this.currentState == STATE.DOWNLOADING;
    }

    public void pause () {
        this.currentState = STATE.PAUSED;
        stateChanged();
    }

    public void resume () {
        this.currentState = STATE.DOWNLOADING;
        stateChanged();
        download();
    }

    public void cancel () {
        this.currentState = STATE.CANCELLED;
        stateChanged();
    }

    public void error () {
        this.currentState = STATE.ERROR;
        stateChanged();
    }

    /**
    * Get file name of URL
     *
     * @param url source url
     *
     * @return file name splitted from url
    */
    protected String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    @Override
    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;

        try {
            //Open connection to URL
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            //Specify what portion of file to download
            connection.setRequestProperty("Range",
                    "bytes=" + this.downloadedBytes + "-");

            // Connect to server.
            connection.connect();

            // Make sure response code is in the 200 range
            if (connection.getResponseCode() / 100 != 2) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "bad connection response code: " + connection.getResponseCode());
                error();
            }

            // Check for valid content length
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "bad content length: " + contentLength);
                error();
            }

            //Set the size for this download if it hasn't been already set
            if (this.fileSize == -1) {
                fileSize = contentLength;
                stateChanged();
            }

            // Open file and seek to the end of it.
            file = new RandomAccessFile(/*getFileName(url)*/this.targetFile.getAbsolutePath(), "rw");
            file.seek(this.downloadedBytes);

            stream = connection.getInputStream();

            while (this.currentState == STATE.DOWNLOADING) {
                Logger.getAnonymousLogger().log(Level.FINEST, "Downloading... " + (getProgress() * 100f) + "%");

                //Size buffer according to how much of the file is left to download.
                byte buffer[];

                if (this.fileSize - this.downloadedBytes > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[this.fileSize - this.downloadedBytes];
                }

                // Read from server into buffer.
                int read = stream.read(buffer);
                if (read == -1) {
                    break;
                }

                // Write buffer to file.
                file.write(buffer, 0, read);
                this.downloadedBytes += read;
                stateChanged();
            }

            //Change status to complete if this point was reached because downloading has finished
            if (this.currentState == STATE.DOWNLOADING) {
                this.currentState = STATE.COMPLETED;
                stateChanged();
            }
        } catch (Exception e) {
            error();
        } finally {
            // Close file.
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e) {}
            }

            // Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {}
            }
        }
    }

    /**
    * Notify observers that this download's status has changed
    */
    protected void stateChanged() {
        setChanged();
        notifyObservers();
    }

}
