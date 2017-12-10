package com.jukusoft.mmo.server;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ServerFinderTest {

    @Test
    public void testConstructor () throws IOException {
        new ServerFinder("../data/config/server.cfg");
    }

    @Test (expected = NullPointerException.class)
    public void testNullConstructor () throws IOException {
        new ServerFinder(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testEmptyConstructor () throws IOException {
        new ServerFinder("");
    }

    @Test (expected = IOException.class)
    public void testNotExistentFileConstructor () throws IOException {
        new ServerFinder("not-existent-file.cfg");
    }

    @Test
    public void testServerListURL () throws IOException {
        ServerFinder serverFinder = new ServerFinder("../data/config/junit-server.cfg");

        assertEquals("http://mmo.jukusoft.com/api/serverlist.json", serverFinder.getServerListURL());
    }

}
