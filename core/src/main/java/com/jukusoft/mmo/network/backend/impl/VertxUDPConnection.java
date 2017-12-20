package com.jukusoft.mmo.network.backend.impl;

import com.jukusoft.mmo.network.Callback;
import com.jukusoft.mmo.network.NetworkResult;
import com.jukusoft.mmo.network.backend.UDPConnection;
import com.jukusoft.mmo.network.message.MessageReceiver;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VertxUDPConnection implements UDPConnection<Buffer> {

    //vertx options
    protected VertxOptions options = null;

    //instance of vertx
    protected Vertx vertx = null;

    //datagram socket options
    protected DatagramSocketOptions socketOptions = null;

    //udp socket
    protected DatagramSocket socket = null;

    //message receiver
    protected MessageReceiver<Buffer> messageReceiver = null;

    protected AtomicBoolean connected = new AtomicBoolean(false);

    protected static final Logger LOGGER = Logger.getLogger("VertxUDPConnection");

    protected String ip = "";
    protected int port = 0;

    public VertxUDPConnection(Vertx vertx) {
        this.vertx = vertx;

        //create datagram socket options
        this.socketOptions = new DatagramSocketOptions();
    }

    public VertxUDPConnection () {
        this.options = new VertxOptions();

        //create new vertx.io instance
        this.vertx = Vertx.vertx(this.options);

        //create datagram socket options
        this.socketOptions = new DatagramSocketOptions();
    }

    @Override
    public void connect(String server, int port, Callback<NetworkResult<Boolean>> callback) {
        //open UDP socket
        this.socket = vertx.createDatagramSocket(new DatagramSocketOptions());

        this.ip = server;
        this.port = port;

        socket.listen(port, server, asyncResult -> {
            if (asyncResult.succeeded()) {
                LOGGER.log(Level.INFO, "UDP connected!");

                this.socket = asyncResult.result();

                //init socket
                this.initSocket(asyncResult.result());

                //set flag
                connected.set(true);

                callback.handle(NetworkResult.complete(true));
            } else {
                LOGGER.log(Level.WARNING, "Listen failed: {0}", asyncResult.cause());

                //set flag
                connected.set(false);

                callback.handle(NetworkResult.fail(asyncResult.cause()));
            }
        });
    }

    @Override
    public void disconnect() {
        if (!this.connected.get()) {
            throw new IllegalStateException("Cannot disconnect, because client isnt connected.");
        }

        //close UDP socket
        this.socket.close();

        //reset flag
        this.connected.set(false);
    }

    protected void initSocket (DatagramSocket socket) {
        //set connection close handler
        socket.exceptionHandler(cause -> {
            LOGGER.log(Level.WARNING, "Exception on UDP channel: " + cause.getLocalizedMessage(), cause);
        });

        socket.handler(packet -> {
            LOGGER.log(Level.INFO, "RECEIVE: " + packet.data().toString("UTF-8"));

            //check, if it is from sender
            if (!this.ip.equals(packet.sender().host()) || this.port != packet.sender().port()) {
                LOGGER.log(Level.WARNING, "received UDP packet from unknown sender: {0}:{1}", new String[]{packet.sender().host(), packet.sender().port() + ""});
            }

            this.messageReceiver.onReceive(packet.data());
        });
    }

    @Override
    public boolean isConnected() {
        return this.connected.get();
    }

    @Override
    public void send(Buffer msg) {
        if (!msg.toString().contains("password")) {
            //log
            LOGGER.log(Level.INFO, "WRITE: {0}", msg);
        } else {
            LOGGER.log(Level.INFO, "WRITE: ******** (contains password)");
        }

        // Send a Buffer
        this.socket.send(msg, this.port, this.ip, asyncResult -> {
            LOGGER.log(Level.SEVERE, "Send succeeded? {0}", asyncResult.succeeded());
        });
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
