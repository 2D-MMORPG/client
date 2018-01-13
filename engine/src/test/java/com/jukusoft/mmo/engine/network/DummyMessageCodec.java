package com.jukusoft.mmo.engine.network;

import io.vertx.core.buffer.Buffer;

public class DummyMessageCodec implements SimpleMessageCodec<Message> {

    @Override
    public void encodeToWire(Buffer buffer, Message message) {

    }

    @Override
    public Message decodeFromWire(int pos, Buffer buffer, short version) {
        return null;
    }

}
