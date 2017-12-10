package com.jukusoft.mmo.utils;

import java.io.File;

/**
* Utility class to manage map cache
*/
public class MapCacheUtils {

    /**
     * protected constructor
     */
    protected MapCacheUtils () {
        //
    }

    /**
     * get path to maps cache directory
     *
     * @param appName name of application
     *
     * @return maps cache directory path, in most cases user.home/.APPNAME/map-cache/
     */
    public static String getMapCachePath (String appName) {
        return FileUtils.getAppHomeDir(appName) + "/map-cache/";
    }

    /**
     * create maps cache directory, if directory doesnt exists
     */
    public static void createMapCacheDirIfAbsent () {
        String path = getMapCachePath(AppUtils.getAppName());

        //create directory, if not exists
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
    }

}
