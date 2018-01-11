package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.network.Message;
import com.jukusoft.mmo.engine.network.StreamManager;
import com.jukusoft.mmo.engine.network.impl.VertxStreamManager;
import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.UpdateService;
import com.jukusoft.mmo.network.Callback;
import com.jukusoft.mmo.network.NetworkManager;
import com.jukusoft.mmo.network.NetworkResult;
import com.jukusoft.mmo.network.impl.DefaultNetworkManager;
import com.jukusoft.mmo.server.Server;
import com.jukusoft.mmo.utils.SocketUtils;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkService implements IService, UpdateService {

    protected NetworkManager<Buffer> networkManager = null;

    protected StreamManager<Message> streamManager = null;

    /**
    * default constructor
    */
    public NetworkService () {
        //
    }

    @Override
    public void onStart() {
        //create network manager
        this.networkManager = DefaultNetworkManager.getManagerInstance();

        //create stream manager
        this.streamManager = new VertxStreamManager(this.networkManager);
    }

    @Override
    public void onStop() {
        //close vertx
        this.streamManager = null;

        //shutdown network manager and close all open connections
        this.networkManager.shutdown();
    }

    /**
    * check, if proxy server is online
     *
     * @param server proxy server
     *
     * @return true, if server is online
    */
    public boolean isOnline (Server server) {
        try {
            return SocketUtils.checkRemoteTCPPort(server.getProxyIP(), server.getProxyPort(), 500);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Couldnt check, if server is online: ", e);

            return false;
        }
    }

    public void connect (Server proxyServer, Callback<NetworkResult<Boolean>> callback) {
        //
    }

    public void disconnect () {
        //
    }

    @Override
    public void update() {
        if (this.streamManager != null) {
            //send messages from queue to client
            this.streamManager.update();
        }
    }
}
