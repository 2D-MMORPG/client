package com.jukusoft.mmo.network;

/**
* network layer on top of NetworkManager, priorizes messages, check if messages can be sended and so on (with queue)
*/
public interface StreamManager<T> {

    /**
    * initialize stream manager
    */
    public void init (NetworkManager<T> networkManager);

}
