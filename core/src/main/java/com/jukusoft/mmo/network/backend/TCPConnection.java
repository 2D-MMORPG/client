package com.jukusoft.mmo.network.backend;

import com.jukusoft.mmo.network.Callback;
import com.jukusoft.mmo.network.NetworkResult;
import com.jukusoft.mmo.network.message.MessageReceiver;

public interface TCPConnection<T> {

    /**
     * try to connect to network server asynchronous
     *
     * @param server ip of server
     * @param port port of server
     * @param callback callback to execute, if connection has established or failed
     */
    public void connect (String server, int port, Callback<NetworkResult<Boolean>> callback);

    /**
    * disconnect connection
    */
    public void disconnect ();

    /**
    * check, if connection is established and open
     *
     * @return true, if client is connected
    */
    public boolean isConnected ();

    /**
     * send message to server
     *
     * @param msg instance of message
     */
    public void send (T msg);

    /**
     * set message receiver to listen to messages and handle them
     *
     * @param receiver instance of message receiver
     */
    public void setMessageReceiver (MessageReceiver<T> receiver);

    /**
     * shutdown network backend
     */
    public void shutdown ();

}
