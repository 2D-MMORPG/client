package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.network.Callback;
import com.jukusoft.mmo.network.NetworkResult;
import com.jukusoft.mmo.server.Server;
import com.jukusoft.mmo.utils.SocketUtils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkService implements IService {

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

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

}
