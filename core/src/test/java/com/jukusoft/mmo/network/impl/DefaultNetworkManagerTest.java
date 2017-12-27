package com.jukusoft.mmo.network.impl;

import org.junit.Test;

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

}
