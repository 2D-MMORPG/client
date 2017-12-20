package com.jukusoft.mmo.network.backend.impl;

import com.jukusoft.mmo.network.Callback;
import com.jukusoft.mmo.network.NetworkResult;
import com.jukusoft.mmo.network.backend.TCPConnection;
import com.jukusoft.mmo.network.message.MessageReceiver;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VertxTCPConnection implements TCPConnection<Buffer> {

    //vertx options
    protected VertxOptions options = null;

    //instance of vertx
    protected Vertx vertx = null;

    /**
     * network client options
     */
    protected NetClientOptions netClientOptions = new NetClientOptions();

    /**
     * network client
     */
    protected NetClient netClient = null;

    /**
     * network socket
     */
    protected NetSocket socket = null;

    //message receiver
    protected MessageReceiver<Buffer> messageReceiver = null;

    protected AtomicBoolean connected = new AtomicBoolean(false);

    protected static final Logger LOGGER = Logger.getLogger("VertxTCPConnection");

    public VertxTCPConnection(Vertx vertx) {
        this.vertx = vertx;
    }

    public VertxTCPConnection() {
        this.options = new VertxOptions();

        //create new vertx.io instance
        this.vertx = Vertx.vertx(this.options);

        //set connection timeout of 1 seconds
        this.netClientOptions.setConnectTimeout(1000);
    }

    @Override
    public void connect(String server, int port, Callback<NetworkResult<Boolean>> callback) {
        if (this.messageReceiver == null) {
            throw new IllegalStateException("You have to set an message receiver first.");
        }

        //create new network client
        this.netClient = this.vertx.createNetClient(this.netClientOptions);

        LOGGER.log(Level.INFO, "try to connect to server: {0}: {1}", new String[]{server, "" + port});

        //connect to server
        this.netClient.connect(port, server, res -> {
            if (res.succeeded()) {
                LOGGER.log(Level.INFO, "Connected!");
                this.socket = res.result();

                //initialize socket
                initSocket(socket);

                //set flag
                connected.set(true);

                callback.handle(NetworkResult.complete(true));
            } else {
                LOGGER.log(Level.INFO, "Failed to connect: " + res.cause().getMessage());

                //set flag
                connected.set(false);

                callback.handle(NetworkResult.fail(res.cause()));
            }
        });
    }

    @Override
    public void disconnect() {
        if (!this.connected.get()) {
            throw new IllegalStateException("Cannot disconnect, because client isnt connected.");
        }

        //close socket
        this.socket.close();

        //reset flag
        this.connected.set(false);
    }

    @Override
    public boolean isConnected() {
        return this.connected.get();
    }

    protected void initSocket (NetSocket socket) {
        //set connection close handler
        socket.closeHandler(res -> {
            LOGGER.log(Level.INFO, "Server connection was closed by server.");

            //reset flag
            this.connected.set(false);

            System.exit(0);
        });

        //add message handler
        socket.handler(buffer -> {
            LOGGER.log(Level.INFO, "RECEIVE: " + buffer.toString("UTF-8"));

            this.messageReceiver.onReceive(buffer);
        });
    }

    @Override
    public void send(Buffer msg) {
        if (!msg.toString().contains("password")) {
            //log
            LOGGER.log(Level.INFO, "WRITE: {0}", msg);
        } else {
            LOGGER.log(Level.INFO, "WRITE: ******** (contains password)");
        }

        //write message to stream
        this.socket.write(msg);
    }

    @Override
    public void setMessageReceiver(MessageReceiver<Buffer> receiver) {
        this.messageReceiver = receiver;
    }

    @Override
    public void shutdown() {
        //check, if vertx instance was created by VertxTCPConnection constructor
        if (this.options != null) {
            this.vertx.close();
        }
    }

}
