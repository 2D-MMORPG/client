package com.jukusoft.mmo.network;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NetworkResultTest {

    @Test
    public void testCreate () {
        assertNotNull(NetworkResult.create());
    }

    @Test
    public void testComplete () {
        NetworkResult<String> rs = NetworkResult.complete("test");

        assertEquals(false, rs.failed());
        assertEquals(true, rs.succeeded());
        assertEquals("test", rs.result());
    }

    @Test
    public void testFail1 () {
        NetworkResult<String> rs = NetworkResult.fail(new Throwable("test"), String.class);

        assertEquals(true, rs.failed());
        assertEquals(false, rs.succeeded());
    }

    @Test
    public void testFail2 () {
        NetworkResult<String> rs = NetworkResult.fail(new Throwable("test"));

        assertEquals(true, rs.failed());
        assertEquals(false, rs.succeeded());
    }

    @Test
    public void testFail3 () {
        NetworkResult<String> rs = NetworkResult.fail("test");

        assertEquals(true, rs.failed());
        assertEquals(false, rs.succeeded());
    }

}
