package com.jukusoft.mmo.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WebUtilsTest {

    @Test
    public void testConstructor () {
        new WebUtils();
    }

    //@Test
    public void testReadContentFromWebsite () {
        assertEquals("my-test-content", WebUtils.readContentFromWebsite("http://mmo.jukusoft.com/api/junit-test-file.txt"));
    }

}
