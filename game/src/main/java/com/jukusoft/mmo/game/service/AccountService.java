package com.jukusoft.mmo.game.service;

import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.impl.StreamManagerService;
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

        //TODO: add code here

        throw new UnsupportedOperationException("this method isnt implemented yet.");
    }

    /**
    * check, if user is logged in
    */
    public boolean isLoggedIn () {
        return this.loggedIn;
    }

}
