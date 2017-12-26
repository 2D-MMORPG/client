package com.jukusoft.mmo.network.config;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NetConfigTest {

    @Test (expected = NullPointerException.class)
    public void testNullConstructor () throws IOException {
        new NetConfig(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testEmptyConstructor () throws IOException {
        new NetConfig("");
    }

    @Test (expected = IOException.class)
    public void testConstructorWithNotExistentFile () throws IOException {
        new NetConfig("./not-existent-file.cfg");
    }

    @Test
    public void testConstructor () throws IOException {
        NetConfig config = new NetConfig("../data/config/junit-network.cfg");

        assertEquals(50, config.getReceiveDelay());
        assertEquals(50, config.getSendDelay());
    }

}
