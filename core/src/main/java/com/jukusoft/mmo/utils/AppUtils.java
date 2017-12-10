package com.jukusoft.mmo.utils;

public class AppUtils {

    protected static String appName = "";

    /**
     * protected constructor
     */
    protected AppUtils () {
        //
    }

    public static String getAppName () {
        if (appName.isEmpty()) {
            throw new IllegalStateException("app name wasnt initialized yet, call AppUtils.setAppName() before.");
        }

        return appName;
    }

    public static void setAppName (String appName) {
        if (appName == null) {
            throw new NullPointerException("app name cannot be null.");
        }

        if (appName.isEmpty()) {
            throw new IllegalArgumentException("app name cannot be empty.");
        }

        AppUtils.appName = appName;
    }

}
