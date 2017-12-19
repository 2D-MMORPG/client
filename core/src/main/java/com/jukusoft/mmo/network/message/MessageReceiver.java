package com.jukusoft.mmo.network.message;

public interface MessageReceiver<T> {

    /**
    * receive message
     *
     * @param msg instance of message
    */
    public void onReceive(T msg);

}
