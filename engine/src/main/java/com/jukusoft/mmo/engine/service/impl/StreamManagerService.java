package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.network.Message;
import com.jukusoft.mmo.engine.network.SimpleMessageCodec;
import com.jukusoft.mmo.engine.network.StreamManager;
import com.jukusoft.mmo.engine.network.impl.VertxStreamManager;
import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.UpdateService;
import com.jukusoft.mmo.network.Callback;
import com.jukusoft.mmo.network.NetworkResult;
import com.jukusoft.mmo.network.message.MessageReceiver;

public class StreamManagerService implements IService, UpdateService, StreamManager<Message> {

    //instance of stream manager
    protected StreamManager<Message> streamManager;

    @InjectService
    protected NetworkService networkService;

    /**
     * default constructor
     */
    public StreamManagerService () {
        //
    }

    @Override
    public void onStart() {
        //create stream manager
        this.streamManager = new VertxStreamManager(this.networkService.getNetworkManager());
    }

    @Override
    public void onStop() {
        //close vertx
        this.streamManager = null;
    }

    @Override
    public void update() {
        if (this.streamManager != null) {
            //send messages from queue to client
            this.streamManager.update();
        }
    }

    /**
     * send message without return value / ack response
     *
     * @param msg instance of message
     */
    @Override
    public void sendMessage (Message msg) {
        if (!this.networkService.isConnected()) {
            throw new IllegalStateException("Cannot send messages, because connection wasnt established yet.");
        }

        this.streamManager.sendMessage(msg);
    }

    @Override
    public <V extends Message> void sendACKMessage(Message msg, Class<V> cls, Callback<NetworkResult<V>> callback) {
        if (!this.networkService.isConnected()) {
            throw new IllegalStateException("Cannot send messages, because connection wasnt established yet.");
        }

        this.streamManager.sendACKMessage(msg, cls, callback);
    }

    @Override
    public <V extends Message>  void addMessageReceiver (Class<V> messageType, MessageReceiver<V> receiver) {
        this.streamManager.addMessageReceiver(messageType, receiver);
    }

    @Override
    public <V extends Message> void addCodec(SimpleMessageCodec<V> codec, Class<V> cls) {
        this.streamManager.addCodec(codec, cls);
    }

}
