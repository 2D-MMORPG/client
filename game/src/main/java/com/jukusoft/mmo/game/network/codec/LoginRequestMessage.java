package com.jukusoft.mmo.game.network.codec;

import com.jukusoft.mmo.message.Message;

public class LoginRequestMessage extends Message {

    protected String username = "";
    protected String password = "";

    public LoginRequestMessage (String username, String password) {
        this.username = username;
        this.password = password;
    }

}
