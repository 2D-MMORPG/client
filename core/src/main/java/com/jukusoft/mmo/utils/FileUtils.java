package com.jukusoft.mmo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utils for file operations
 *
 * Created by Justin on 24.08.2016.
 */
public class FileUtils {

    //constant strings for exceptions
    private static final String PATH_CANNOT_NULL = "path cannot be null.";
    private static final String PATH_CANNOT_EMPTY = "path cannot be empty.";

    /**
    * private constructor, so other classes cannot create an instance of FileUtils
    */
    protected FileUtils () {
        //
    }

    /**
     * read content from file
     *
     * @param path
     *            path to file
     * @param encoding
     *            file encoding
     *
     * @throws IOException
     *
     * @return content of file as string
     */
    public static String readFile(String path, Charset encoding) throws IOException {
        if (path == null) {
            throw new NullPointerException(PATH_CANNOT_NULL);
        }

        if (path.isEmpty()) {
            throw new IllegalArgumentException(PATH_CANNOT_EMPTY);
        }

        if (!new File(path).exists()) {
            throw new IOException("File doesnt exists: " + path);
        }

        // read bytes from file
        byte[] encoded = Files.readAllBytes(Paths.get(path.replace("/./", "/").replace("\\.\\", "\\")));

        // convert bytes to string with specific encoding and return string
        return new String(encoded, encoding);
    }

    /**
     * read lines from file
     *
     * @param path
     *            path to file
     * @param charset
     *            encoding of file
     *
     * @throws IOException
     *
     * @return list of lines from file
     */
    public static List<String> readLines(String path, Charset charset) throws IOException {
        if (path == null) {
            throw new NullPointerException(PATH_CANNOT_NULL);
        }

        if (path.isEmpty()) {
            throw new IllegalArgumentException(PATH_CANNOT_EMPTY);
        }

        if (!(new File(path)).exists()) {
            throw new FileNotFoundException("Couldnt find file: " + path);
        }

        return Files.readAllLines(Paths.get(path), charset);
    }

    /**
     * write text to file
     *
     * @param path
     *            path to file
     * @param content
     *            content of file
     * @param encoding
     *            file encoding
     *
     * @throws IOException
     */
    public static void writeFile(String path, String content, Charset encoding) throws IOException {
        if (path == null) {
            throw new NullPointerException(PATH_CANNOT_NULL);
        }

        if (path.isEmpty()) {
            throw new IllegalArgumentException(PATH_CANNOT_EMPTY);
        }

        if (content == null) {
            throw new NullPointerException("content cannot be null.");
        }

        if (content.isEmpty()) {
            throw new IllegalArgumentException("content cannot be empty.");
        }

        if (encoding == null) {
            throw new NullPointerException("encoding cannot be null.");
        }

        Files.write(Paths.get(path), content.getBytes(encoding), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void recursiveDeleteDirectory (File f) throws IOException {
        if (f == null) {
            throw new NullPointerException("file cannot be null.");
        }

        if (!f.exists()) {
            //we dont have to delete anything
            Logger.getAnonymousLogger().log(Level.INFO, "Dont need to delete directory, because it doesnt exists: " + f.getAbsolutePath());

            return;
        }

        //check, if it is an directory
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                recursiveDeleteDirectory(c);
            }
        }

        Logger.getAnonymousLogger().log(Level.INFO, "delete directory / file: " + f.getAbsolutePath());

        //delete directory / file
        Files.delete(f.toPath());
    }

    /**
    * get path to user.home directory
     *
     * @return path to user.home directory
    */
    public static String getUserHomeDir () {
        return System.getProperty("user.home");
    }

    public static String getAppHomeDir (String appName) {
        return getUserHomeDir() + "/." + appName + "/";
    }

    /**
    * removes ../ from path
    */
    protected static String removeDoubleDotInDir(String path) {
        path = path.replace("\\", "/");

        if (path == null) {
            throw new NullPointerException("path cannot be null.");
        }

        if (path.isEmpty()) {
            throw new IllegalArgumentException("path cannot be empty.");
        }

        if (path.startsWith("../")) {
            throw new IllegalArgumentException("Cannot relativize paths starting with ../");
        }

        if (!path.contains("/")) {
            return path;
        }

        String[] array = path.split("/");

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < array.length; i++) {
            if (array[i].equals("..")) {
                array[i] = null;
                array[i-1] = null;
            }
        }

        for (String entry : array) {
            if (entry != null) {
                sb.append(entry + "/");
            }
        }

        return sb.toString();
    }

}
