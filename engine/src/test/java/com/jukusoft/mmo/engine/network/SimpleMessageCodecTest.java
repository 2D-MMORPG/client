package com.jukusoft.mmo.engine.network;

import io.vertx.core.buffer.Buffer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleMessageCodecTest {

    @Test
    public void testEncode () {
        DummyMessageCodec codec = new DummyMessageCodec();

        //encode message
        Message msg = new Message();
        codec.encodeToWire(Buffer.buffer(), msg);
    }

    @Test
    public void testDecode () {
        DummyMessageCodec codec = new DummyMessageCodec();

        //create new buffer
        Buffer buffer = Buffer.buffer();

        //encode message
        Message msg = new Message();
        codec.encodeToWire(buffer, msg);

        //decode message
        Message receivedMessage = codec.decodeFromWire(0, buffer, (short) 1);
    }

    @Test
    public void testGetName () {
        DummyMessageCodec codec = new DummyMessageCodec();
        codec.name();
    }

    @Test
    public void testTransform () {
        DummyMessageCodec codec = new DummyMessageCodec();
        Message msg = new Message();

        Message msg1 = codec.transform(msg);

        assertEquals(msg, msg1);
    }

    @Test
    public void testSystemCodeID () {
        DummyMessageCodec codec = new DummyMessageCodec();
        assertEquals(0, codec.systemCodecID());
    }

}
