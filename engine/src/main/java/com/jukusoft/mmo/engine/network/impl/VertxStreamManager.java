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
import com.jukusoft.mmo.utils.MessageIDGenerator;
import io.vertx.core.buffer.Buffer;

public class VertxStreamManager implements StreamManager<Message>, MessageReceiver<Buffer> {

    //network manager
    protected NetworkManager<Buffer> networkManager = null;

    //message queue
    protected Array<Message> messageQueue = new Array<>();

    //callback map for outstanding messages
    protected IntMap<Callback> callbackMap = new IntMap<>();

    //map with encoder & decoder
    protected IntMap<SimpleMessageCodec> codecMap = new IntMap<>();

    //map for message receivers
    protected IntMap<MessageReceiver> receiverMap = new IntMap<>();

    /**
    * default constructor
    */
    public VertxStreamManager (NetworkManager<Buffer> networkManager) {
        this.networkManager = networkManager;

        //set message receiver
        this.networkManager.setMessageReceiver(this);
    }

    @Override
    public void sendMessage(Message msg) {
        //add message to queue
        this.addMessageToQueue(msg);
    }

    @Override
    public <V extends Message> void sendACKMessage(Message msg, Class<V> cls, Callback<NetworkResult<V>> callback) {
        //generate ackID
        msg.setAckID(MessageIDGenerator.generateID());

        //add message to callback map
        this.callbackMap.put(msg.getAckID(), callback);

        //add message to queue
        this.addMessageToQueue(msg);
    }

    protected void addMessageToQueue (Message msg) {
        this.messageQueue.add(msg);
    }

    @Override
    public <V extends Message> void addMessageReceiver(Class<V> messageType, MessageReceiver<V> receiver) {
        this.receiverMap.put(messageType.getSimpleName().hashCode(), receiver);
    }

    @Override
    public <V extends Message> void addCodec(SimpleMessageCodec<V> codec, Class<V> cls) {
        this.codecMap.put(cls.getSimpleName().hashCode(), codec);
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

        //convert message to extra buffer
        Buffer content = Buffer.buffer();
        int startLength = content.length();

        int msgType = cls.getSimpleName().hashCode();

        //get codec
        SimpleMessageCodec<T> codec = this.codecMap.get(msgType);

        if (codec == null) {
            throw new IllegalStateException("no codec is specified for message class: " + msg.getClass().getSimpleName());
        }

        //encode message
        codec.encodeToWire(content, msg);

        /**
        * add message header
        */

        buffer.appendInt(content.length() - startLength);

        //add message type header
        buffer.appendInt(msgType);

        //add protocol version
        buffer.appendShort(msg.getVersion());

        //add ackID
        buffer.appendInt(msg.getAckID());

        //add message content
        buffer.appendBuffer(content);

        return buffer;
    }

    protected <T extends Message> T decodeBufferToMessage (Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = 0;

        // Length of JSON
        int length = buffer.getInt(_pos);

        //get message type
        int msgType = buffer.getInt(_pos + 4);

        //get message version
        short version = buffer.getShort(_pos + 8);

        //get ackID
        int ackID = buffer.getInt(_pos + 10);

        //get codec
        SimpleMessageCodec<T> codec = this.codecMap.get(msgType);

        if (codec == null) {
            throw new IllegalStateException("no codec is specified for received message type: " + msgType);
        }

        return codec.decodeFromWire(_pos + 14, buffer, version);
    }

    @Override
    public void onReceive(Buffer msg) {
        //TODO: add code here
    }

    @Override
    public void onStart() {
        //
    }

    @Override
    public void onStop() {
        //
    }

}
