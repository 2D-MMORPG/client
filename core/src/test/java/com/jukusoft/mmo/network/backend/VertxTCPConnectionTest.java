package com.jukusoft.mmo.network.backend;

import com.jukusoft.mmo.network.Callback;
import com.jukusoft.mmo.network.NetworkResult;
import com.jukusoft.mmo.network.message.MessageReceiver;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import org.junit.Test;

public class VertxTCPConnectionTest {

    @Test
    public void testConstructor () {
        VertxTCPConnection conn = new VertxTCPConnection();
        conn.shutdown();
    }

    @Test
    public void testConstructor1 () {
        //create new vertx instance
        Vertx vertx = Vertx.vertx();

        VertxTCPConnection conn = new VertxTCPConnection(vertx);
        conn.shutdown();

        //close vertx instance
        vertx.close();
    }

    @Test (expected = IllegalStateException.class)
    public void testConnectWithoutReceiver () {
        VertxTCPConnection conn = new VertxTCPConnection();

        conn.connect("127.0.0.1", 7, new Callback<NetworkResult<Boolean>>() {
            public void handle(NetworkResult<Boolean> param) {
                //
            }
        });

        conn.shutdown();
    }

    @Test
    public void testConnect () {
        VertxTCPConnection conn = new VertxTCPConnection();

        conn.setMessageReceiver(new MessageReceiver<Buffer>() {
            @Override
            public void onReceive(Buffer msg) {
                //
            }
        });

        conn.connect("127.0.0.1", 7, new Callback<NetworkResult<Boolean>>() {
            @Override
            public void handle(NetworkResult<Boolean> param) {
                //
            }
        });

        conn.shutdown();
    }

}
