package com.jukusoft.mmo.engine.network;

import com.jukusoft.mmo.message.Message;
import io.vertx.core.buffer.Buffer;

public interface SimpleMessageCodec<T extends Message> {

    /**
     * Called by Vert.x when marshalling a message to the wire.
     *
     * @param buffer  the message should be written into this buffer
     * @param s  the message that is being sent
     */
    public void encodeToWire(Buffer buffer, T s);

    /**
     * Called by Vert.x when a message is decoded from the wire.
     *
     * @param pos  the position in the buffer where the message should be read from.
     * @param buffer  the buffer to read the message from
     * @return  the read message
     */
    public T decodeFromWire(int pos, Buffer buffer, short version);

    /**
     * If a message is sent <i>locally</i> across the event bus, this method is called to transform the message from
     * the sent type S to the received type R
     *
     * @param s  the sent message
     * @return  the transformed message
     */
    default T transform(T s) {
        return s;
    }

    /**
     * The codec name. Each codec must have a unique name. This is used to identify a codec when sending a message and
     * for unregistering codecs.
     *
     * @return the name
     */
    default String name() {
        return getClass().getSimpleName();
    }

    /**
     * Used to identify system codecs. Should always return -1 for a user codec.
     *
     * @return -1 for a user codec.
     */
    default byte systemCodecID() {
        return 0;
    }

}
