package com.jukusoft.mmo.engine.network.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.jukusoft.mmo.engine.network.Message;
import com.jukusoft.mmo.engine.network.SimpleMessageCodec;
import com.jukusoft.mmo.engine.network.StreamManager;
import com.jukusoft.mmo.network.Callback;
import com.jukusoft.mmo.network.NetworkManager;
import com.jukusoft.mmo.network.NetworkResult;
import com.jukusoft.mmo.network.message.MessageReceiver;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

public class VertxStreamManager implements StreamManager<Message> {

    //network manager
    protected NetworkManager<Buffer> networkManager = null;

    //message queue
    protected Array<Message> messageQueue = new Array<>();

    //callback map for outstanding messages
    protected IntMap<Callback> callbackMap = new IntMap<>();

    //map with encoder & decoder
    protected IntMap<SimpleMessageCodec> codecMap = new IntMap<>();

    /**
    * default constructor
    */
    public VertxStreamManager (NetworkManager<Buffer> networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public void sendMessage(Message msg) {
        //add message to queue
        this.addMessageToQueue(msg);
    }

    @Override
    public <V extends Message> void sendACKMessage(Message msg, Class<V> cls, Callback<NetworkResult<V>> callback) {
        //add message to callback map
        this.callbackMap.put(msg.getAckID(), callback);

        //add message to queue
        this.addMessageToQueue(msg);
    }

    protected void addMessageToQueue (Message msg) {
        this.messageQueue.add(msg);
    }

    @Override
    public <V extends Message> void addMessageReceiver(MessageReceiver<V> receiver, Class<V> messageType) {

    }

    @Override
    public void update() {
        if (this.messageQueue.size > 0) {
            //sort message queue
            this.messageQueue.sort();

            //send message
            for (int i = 0; i < this.messageQueue.size; i++) {
                //get first item
                Message msg = this.messageQueue.first();

                if (this.networkManager.canSend()) {
                    //remove first item from queue
                    this.messageQueue.removeIndex(i);

                    //convert message to buffer
                    Buffer buffer = this.convertMessageToBuffer(msg);

                    //send message
                    this.networkManager.send(buffer, msg.getProtocol());
                }
            }
        }
    }

    protected <T extends Message> Buffer convertMessageToBuffer (T msg) {
        //get buffer
        Buffer buffer = Buffer.buffer();

        //first get class of message
        Class<? extends Message> cls = msg.getClass();

        if (cls == Message.class) {
            throw new IllegalArgumentException("message has to be a instance of Message, but cannot be of class Message itself. Please create a new class and extend from Message.");
        }

        /**
        * add message header
        */

        int msgType = cls.getSimpleName().hashCode();

        //add message type header
        buffer.appendInt(msgType);

        //add protocol version
        buffer.appendShort(msg.getVersion());

        //add ackID
        buffer.appendInt(msg.getAckID());

        //get codec
        SimpleMessageCodec<T> codec = this.codecMap.get(msgType);

        if (codec == null) {
            throw new IllegalStateException("no codec is specified for message class: " + msg.getClass().getSimpleName());
        }

        //encode message
        codec.encodeToWire(buffer, msg);

        return buffer;
    }

}