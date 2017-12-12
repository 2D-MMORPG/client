package com.jukusoft.mmo.server;

import org.junit.Test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        assertEquals("http://mmo.jukusoft.com/api/junit-serverlist.json", serverFinder.getServerListURL());
    }

    @Test
    public void testGetContent () throws IOException {
        ServerFinder serverFinder = new ServerFinder("../data/config/junit-server.cfg");

        String content = serverFinder.getContent(serverFinder.getServerListURL());

        Logger.getAnonymousLogger().log(Level.INFO, "website content: " + content);

        //check json format
        assertEquals(true, content.startsWith("{"));
        assertEquals(true, content.endsWith("}"));
    }

    @Test
    public void testGetContent1 () throws IOException {
        ServerFinder serverFinder = new ServerFinder("../data/config/junit-server2.cfg");

        String content = serverFinder.getContent(serverFinder.getServerListURL());

        Logger.getAnonymousLogger().log(Level.INFO, "website content: " + content);

        //check json format
        assertEquals(true, content.startsWith("{"));
        assertEquals(true, content.endsWith("}"));
    }

    @Test
    public void testLoad () throws IOException {
        ServerFinder serverFinder = new ServerFinder("../data/config/junit-server.cfg");
        serverFinder.load();
    }

    @Test
    public void testListServer () throws IOException {
        ServerFinder serverFinder = new ServerFinder("../data/config/junit-server.cfg");
        serverFinder.load();

        assertEquals(3, serverFinder.listServer().size());
    }

}
