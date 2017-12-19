package com.jukusoft.mmo.network.backend;

import com.jukusoft.mmo.network.Callback;
import com.jukusoft.mmo.network.NetworkResult;
import com.jukusoft.mmo.network.message.MessageReceiver;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

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

        System.out.println("try to connect to server: " + server + ":" + port);

        //connect to server
        this.netClient.connect(port, server, res -> {
            if (res.succeeded()) {
                System.out.println("Connected!");
                this.socket = res.result();

                //initialize socket
                initSocket(socket);

                //set flag
                connected.set(true);

                callback.handle(NetworkResult.complete(true));
            } else {
                System.out.println("Failed to connect: " + res.cause().getMessage());

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
    }

    @Override
    public boolean isConnected() {
        return this.connected.get();
    }

    protected void initSocket (NetSocket socket) {
        //set connection close handler
        socket.closeHandler(res -> {
            System.err.println("Server connection was closed by server.");

            //reset flag
            this.connected.set(false);

            System.exit(0);
        });

        //add message handler
        socket.handler(buffer -> {
            //convert to string and json object
            //String str = buffer.toString(StandardCharsets.UTF_8);

            //System.out.println("message received: " + str);

            System.out.println("RECEIVE: " + buffer.toString("UTF-8"));

            this.messageReceiver.onReceive(buffer);
        });
    }

    @Override
    public void send(Buffer msg) {
        if (!msg.toString().contains("password")) {
            //log
            System.out.println("WRITE: " + msg);
        } else {
            System.out.println("WRITE: ******** (contains password)");
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
