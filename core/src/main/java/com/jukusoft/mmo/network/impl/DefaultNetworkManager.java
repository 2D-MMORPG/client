package com.jukusoft.mmo.network.impl;

import com.jukusoft.mmo.network.NetworkManager;
import com.jukusoft.mmo.network.message.MessageReceiver;
import com.jukusoft.mmo.network.traffic.TrafficCounter;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;

public class DefaultNetworkManager implements NetworkManager<Buffer> {

    //vertx options
    protected VertxOptions options = null;

    //vertx instance
    protected Vertx vertx = null;

    //traffic counter
    protected TrafficCounter counter = null;

    //singleton instance
    protected static DefaultNetworkManager instance = null;

    public DefaultNetworkManager () {
        this.options = new VertxOptions();

        //create new vertx.io instance
        this.vertx = Vertx.vertx(this.options);

        //create new traffic counter
    }


    @Override
    public void send(Buffer msg, PROTOCOL protocol) {
        //TODO: send message to specific network backend

        //count traffic
        this.counter.addSendBytes(msg.length(), protocol);
    }

    @Override
    public void setMessageReceiver(MessageReceiver<Buffer> receiver) {

    }

    @Override
    public void executeBlocking(Runnable runnable) {

    }

    @Override
    public long startPeriodicTimer(long time, Runnable runnable) {
        return 0;
    }

    @Override
    public void stopPeriodicTimer(long timerID) {

    }

    @Override
    public long executeDelayed(long time, Runnable runnable) {
        return 0;
    }

    public static DefaultNetworkManager getManagerInstance () {
        if (instance == null) {
            instance = new DefaultNetworkManager();
        }

        return instance;
    }

}
