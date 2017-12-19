package com.jukusoft.mmo.network;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class WritableNetworkResultTest {

    @Test
    public void testConstructor () {
        new WritableNetworkResult<String>();
    }

    @Test (expected = IllegalStateException.class)
    public void testNullResult () {
        WritableNetworkResult<String> result = new WritableNetworkResult<>();
        result.result();
    }

    @Test
    public void testComplete () {
        WritableNetworkResult<String> result = new WritableNetworkResult<>();

        assertNull(result.result);
        assertEquals(false, result.failed());
        assertEquals(false, result.succeeded());
        assertEquals("Unknown", result.causeMessage());

        result.complete("test");

        assertNotNull(result.result);
        assertNotNull(result.result());
        assertEquals(false, result.failed());
        assertEquals(true, result.succeeded());

        assertEquals("test", result.result());
    }

    @Test
    public void testFail1 () {
        WritableNetworkResult<String> result = new WritableNetworkResult<>();

        result.fail(new Throwable("test"));

        assertNotNull(result.cause());
        assertEquals(true, result.failed());
        assertEquals("test", result.causeMessage());
    }

    @Test
    public void testFail () {
        WritableNetworkResult<String> result = new WritableNetworkResult<>();

        result.fail("test");

        assertNotNull(result.cause());
        assertEquals(true, result.failed());
        assertNotNull(result.causeMessage());
        assertEquals("test", result.causeMessage());
    }

}
