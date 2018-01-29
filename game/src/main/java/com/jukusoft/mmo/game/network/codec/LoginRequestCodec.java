package com.jukusoft.mmo.game.network.codec;

import com.jukusoft.mmo.engine.network.SimpleMessageCodec;
import com.jukusoft.mmo.utils.ByteUtils;
import io.vertx.core.buffer.Buffer;

import java.nio.charset.StandardCharsets;

public class LoginRequestCodec implements SimpleMessageCodec<LoginRequestMessage> {

    @Override
    public void encodeToWire(Buffer buffer, LoginRequestMessage msg) {
        //convert username and password to byte arrays
        byte[] usernameBytes = msg.username.getBytes(StandardCharsets.UTF_8);
        byte[] passwordBytes = msg.password.getBytes(StandardCharsets.UTF_8);

        //add username
        buffer.appendShort((short) usernameBytes.length);
        buffer.appendBytes(usernameBytes);

        //add password
        buffer.appendShort((short) passwordBytes.length);
        buffer.appendBytes(passwordBytes);
    }

    @Override
    public LoginRequestMessage decodeFromWire(int pos, Buffer buffer, short version) {
        throw new UnsupportedOperationException("Cannot decode this message");
    }

}
