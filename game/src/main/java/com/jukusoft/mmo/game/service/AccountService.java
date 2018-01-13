package com.jukusoft.mmo.game.service;

import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.impl.StreamManagerService;
import com.jukusoft.mmo.game.network.codec.LoginRequestMessage;
import com.jukusoft.mmo.game.network.codec.LoginResponseMessage;
import com.jukusoft.mmo.network.Callback;
import com.jukusoft.mmo.network.NetworkResult;

public class AccountService implements IService {

    //user information
    protected String username = "";

    //flag, if user is logged in
    protected volatile boolean loggedIn = false;

    @InjectService
    protected StreamManagerService streamManagerService;

    @Override
    public void onStart() {
        //
    }

    @Override
    public void onStop() {
        //
    }

    public void login (String username, String password, Callback<NetworkResult<Boolean>> callback) {
        if (this.loggedIn) {
            throw new IllegalStateException("User is already logged in.");
        }

        //create new message
        LoginRequestMessage msg = new LoginRequestMessage(username, password);

        //send login request message to server
        this.streamManagerService.sendACKMessage(msg, LoginResponseMessage.class, res -> {
            if (res.succeeded()) {
                //check, if login was successful
                if (res.result().isLoggedIn()) {
                    callback.handle(NetworkResult.complete(true));
                } else {
                    callback.handle(NetworkResult.complete(false));
                }
            } else {
                callback.handle(NetworkResult.fail(res.cause()));
            }
        });
    }

    /**
    * check, if user is logged in
    */
    public boolean isLoggedIn () {
        return this.loggedIn;
    }

}
