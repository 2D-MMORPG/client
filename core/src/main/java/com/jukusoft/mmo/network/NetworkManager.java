package com.jukusoft.mmo.network;

import com.jukusoft.mmo.network.impl.DefaultNetworkManager;
import com.jukusoft.mmo.network.message.MessageReceiver;

public interface NetworkManager<T> extends IThreadPool {

    public void send (T msg, Protocol protocol);

    public void setMessageReceiver (MessageReceiver<T> receiver);

    public void shutdown ();

    public static NetworkManager getInstance () {
        return DefaultNetworkManager.getManagerInstance();
    }

}
