package com.jukusoft.mmo.network;

import com.jukusoft.mmo.network.impl.DefaultNetworkManager;
import com.jukusoft.mmo.network.message.MessageReceiver;

public interface NetworkManager<T> extends IThreadPool {

    public enum PROTOCOL {
        TCP, UDP
    }

    public void send (T msg, PROTOCOL protocol);

    public void setMessageReceiver (MessageReceiver<T> receiver);

    public static NetworkManager getInstance () {
        return DefaultNetworkManager.getManagerInstance();
    }

}
