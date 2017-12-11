package com.jukusoft.mmo.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DownloaderUtils {

    /**
    * protected constructor
    */
    protected DownloaderUtils () {
        //
    }

    /**
    * download file from server and save it into cache
     *
     * @param fileURL http url to file on server
     * @param overrideOnExists flag, if file should be override, if file already exists
     *
     * @return path to cached file
    */
    public static String downloadFileToCache (String fileURL, boolean overrideOnExists) throws IOException {
        //generate file path
        String destPath = generateFilePath(fileURL);

        File destFile = new File(destPath);

        //check, if file already exists
        if (destFile.exists()) {
            if (overrideOnExists) {
                //delete and create new file
                Files.delete(destFile.toPath());
                Files.createFile(destFile.toPath());
            } else {
                throw new FileAlreadyExistsException("cache file '" + destPath + "' already exists.");
            }
        } else {
            //create new file
            Files.createFile(destFile.toPath());
        }

        //https://stackoverflow.com/questions/921262/how-to-download-and-save-a-file-from-internet-using-java

        //download file
        URL website = new URL(fileURL);
        try (InputStream in = website.openStream()) {
            Files.copy(in, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return destPath;
    }

    public static String downloadFileToCache (String fileURL) throws IOException {
        return downloadFileToCache(fileURL, false);
    }

    protected static String getFileExtension (String filename) {
        if (filename == null) {
            throw new NullPointerException("filename cannot be null.");
        }

        if (filename.isEmpty()) {
            throw new IllegalArgumentException("filename cannot be empty.");
        }

        if (!filename.contains(".")) {
            throw new IllegalArgumentException("filename doesnt contains a dot: " + filename);
        }

        String[] array = filename.split("\\.");

        return array[array.length - 1];
    }

    protected static String generateFilePath (String filename) {
        //create cache directory, if not exists
        CacheUtils.createCacheDirIfAbsent("downloaded-files");

        //generate cache file name
        String fileName = HashUtils.computeMD5Hash(filename);

        //get file name extension
        String extension = getFileExtension(filename);

        if (extension.isEmpty()) {
            throw new IllegalArgumentException("Unknown file extension: " + filename);
        }

        //generate file path
        return CacheUtils.getCacheDir("downloaded-files") + "" + fileName + "." + extension;
    }

}
