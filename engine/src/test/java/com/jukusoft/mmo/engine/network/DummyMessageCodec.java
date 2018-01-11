package com.jukusoft.mmo.engine.network;

import io.vertx.core.buffer.Buffer;

public class DummyMessageCodec extends SimpleMessageCodec<Message> {
    @Override
    public void encodeToWire(Buffer buffer, Message message) {

    }

    @Override
    public Message decodeFromWire(int pos, Buffer buffer) {
        return null;
    }
}
