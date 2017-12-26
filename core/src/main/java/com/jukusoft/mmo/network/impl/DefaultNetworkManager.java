package com.jukusoft.mmo.network.impl;

import com.jukusoft.mmo.network.NetworkManager;
import com.jukusoft.mmo.network.config.NetConfig;
import com.jukusoft.mmo.network.message.MessageReceiver;
import com.jukusoft.mmo.network.traffic.TrafficCounter;
import com.jukusoft.mmo.utils.ReportUtils;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultNetworkManager implements NetworkManager<Buffer> {

    //vertx options
    protected VertxOptions options = null;

    //vertx instance
    protected Vertx vertx = null;

    //network config
    protected NetConfig config = null;

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

    public void load (String configFile) throws IOException {
        this.config = new NetConfig(configFile);
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

            //load configuration
            try {
                if (!new File("./data/config/network.cfg").exists()) {
                    //its an junit test
                    instance.load("../data/config/junit-network.cfg");
                } else {
                    instance.load("./data/config/junit-network.cfg");
                }
            } catch (IOException e) {
                ReportUtils.sendExceptionToServer(e);

                Logger.getAnonymousLogger().log(Level.SEVERE, "Exception occurred while loading network configuration file: ", e);
            }
        }

        return instance;
    }

}
