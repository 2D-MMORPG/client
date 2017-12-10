package com.jukusoft.mmo.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class LogUtilsTest {

    @Test
    public void testConstructor () {
        new LogUtils();
    }

    @Test
    public void testCreateLogDirIfAbsent () {
        AppUtils.setAppName("junit-test");

        if (new File(LogUtils.getLogPath()).exists()) {
            new File(LogUtils.getLogPath()).delete();
        }

        LogUtils.createLogDirIfAbsent();

        //check, if directory exists
        assertEquals(true, new File(LogUtils.getLogPath("junit-test")).exists());

    }

    @Test
    public void testEndsWithSlash () {
        AppUtils.setAppName("junit-test");

        assertEquals(true, LogUtils.getLogPath().endsWith("/"));
        assertEquals(true, LogUtils.getLogPath("junit-test2").endsWith("/"));
    }

}
