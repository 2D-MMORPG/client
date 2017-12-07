package com.jukusoft.mmo.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class LogUtilsTest {

    @Test
    public void testCreateLogDirIfAbsent () {
        LogUtils.createLogDirIfAbsent("junit-test");

        //check, if directory exists
        assertEquals(true, new File(LogUtils.getLogPath("junit-test")).exists());

    }

}
