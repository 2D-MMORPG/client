package com.jukusoft.mmo.network.impl;

import com.jukusoft.mmo.network.message.MessageReceiver;
import org.junit.Test;

import java.nio.Buffer;

import static org.junit.Assert.assertEquals;

public class DefaultNetworkManagerTest {

    @Test
    public void testConstructor () {
        new DefaultNetworkManager();
    }

    @Test
    public void testGetInstance () {
        //create new manager instance
        DefaultNetworkManager.getManagerInstance();

        //get instance again
        DefaultNetworkManager.getManagerInstance();

        //shutdown network manager
        DefaultNetworkManager.getManagerInstance().shutdown();
    }

    @Test (expected = NullPointerException.class)
    public void testSetNullMessageReceiver () {
        DefaultNetworkManager.getManagerInstance().setMessageReceiver(null);
    }

    @Test
    public void testSetMessageReceiver () {
        DefaultNetworkManager.getManagerInstance().setMessageReceiver(msg -> {
            //
        });
    }

    @Test
    public void testIsJUnitTest () {
        assertEquals(true, DefaultNetworkManager.isJUnitTest());
    }

    @Test
    public void testExecuteBlocking () {
        new DefaultNetworkManager().executeBlocking(() -> {
            //
        });
    }

    @Test
    public void testTimer () {
        DefaultNetworkManager networkManager = new DefaultNetworkManager();

        long id = networkManager.startPeriodicTimer(50l, () -> {});

        networkManager.stopPeriodicTimer(id);
    }

}
