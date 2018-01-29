package com.jukusoft.mmo.game.network.codec;

import com.jukusoft.mmo.message.Message;

public class LoginResponseMessage extends Message {

    protected boolean loggedIn = false;

    public boolean isLoggedIn () {
        return this.loggedIn;
    }

}
