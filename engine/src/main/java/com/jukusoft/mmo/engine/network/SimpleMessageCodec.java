package com.jukusoft.mmo.engine.network;

import io.vertx.core.eventbus.MessageCodec;

public abstract class SimpleMessageCodec<T> implements MessageCodec<T, T> {

    @Override
    public T transform(T t) {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public byte systemCodecID() {
        return 0;
    }

}
