package com.jukusoft.mmo.utils;

import org.apache.commons.lang.NullArgumentException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class ReportUtilsTest {

    @BeforeClass
    public static void beforeClass () {
        //
    }

    @AfterClass
    public static void afterClass () {
        if (new File("null").exists()) {
            new File("null").delete();
        }
    }

    @Test
    public void testConstructor () {
        new ReportUtils();
    }

    @Test(expected = NullPointerException.class)
    public void testSendExceptionToServer () {
        ReportUtils.sendExceptionToServer(null);
    }

    @Test
    public void testSendExceptionToServer1 () {
        ReportUtils.sendExceptionToServer(new RuntimeException("test"), false);
    }

    @Test
    public void testAddSystemInformationToMap () {
        Map<String,Object> params = new HashMap();

        ReportUtils.addSystemInformationToMap(params);

        assertNotNull(params.get("os_name"));
        assertNotNull(params.get("os_arch"));
        assertNotNull(params.get("os_version"));
    }

    @Test
    public void testLogSendedInformation () {
        ReportUtils.logSendedInformation(new HashMap<>());
    }

}
