package com.jukusoft.mmo.network.backend;

import com.jukusoft.mmo.network.backend.impl.VertxTCPConnection;
import com.jukusoft.mmo.network.backend.impl.VertxUDPConnection;
import io.vertx.core.Vertx;
import org.junit.Test;

public class VertxUDPConnectionTest {

    @Test
    public void testConstructor () {
        VertxUDPConnection conn = new VertxUDPConnection();
        conn.shutdown();
    }

    @Test
    public void testConstructor1 () {
        //create new vertx instance
        Vertx vertx = Vertx.vertx();

        VertxUDPConnection conn = new VertxUDPConnection();
        conn.shutdown();

        //close vertx instance
        vertx.close();
    }

}
