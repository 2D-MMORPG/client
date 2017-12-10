package com.jukusoft.mmo.utils;

import java.io.File;

public class CacheUtils {

    /**
    * protected constructor
    */
    protected CacheUtils () {
        //
    }

    /**
     * get path to cache directory
     *
     * @param appName name of application
     *
     * @return cache directory path, in most cases user.home/.APPNAME/map-cache/
     */
    public static String getCachePath (String appName) {
        return FileUtils.getAppHomeDir(appName) + "/cache/";
    }

    /**
    * get path to cache directory
    */
    public static String getCachePath () {
        return getCachePath(AppUtils.getAppName());
    }

    /**
     * create maps cache directory, if directory doesnt exists
     */
    public static void createCacheDirIfAbsent () {
        String path = getCachePath();

        //create directory, if not exists
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
    }

    public static String getCacheDir (String appName, String cacheDir) {
        return getCachePath(appName) + "" + cacheDir + "/";
    }

    public static String getCacheDir (String cacheDir) {
        return getCacheDir(AppUtils.getAppName(), cacheDir);
    }

}
