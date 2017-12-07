package com.jukusoft.mmo.utils;

import org.apache.commons.lang.NullArgumentException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class ReportUtilsTest {

    @Test(expected = NullPointerException.class)
    public void testSendExceptionToServer () {
        ReportUtils.sendExceptionToServer(null);
    }

    @Test
    public void testAddSystemInformationToMap () {
        Map<String,Object> params = new HashMap();

        ReportUtils.addSystemInformationToMap(params);

        assertNotNull(params.get("os_name"));
        assertNotNull(params.get("os_arch"));
        assertNotNull(params.get("os_version"));
    }

}
