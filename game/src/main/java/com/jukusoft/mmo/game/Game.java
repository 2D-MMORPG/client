package com.jukusoft.mmo.game;

import com.jukusoft.mmo.engine.app.BaseApp;
import com.jukusoft.mmo.engine.service.ServiceManager;
import com.jukusoft.mmo.engine.service.impl.CameraServiceBefore;
import com.jukusoft.mmo.engine.service.impl.SpriteBatchServiceBefore;

public class Game extends BaseApp {

    public Game () {
        super();
    }

    @Override
    protected void createServices(ServiceManager manager) {
        //add service which holds sprite batch
        manager.addService(new SpriteBatchServiceBefore(), SpriteBatchServiceBefore.class);

        //add service for camera
        manager.addService(new CameraServiceBefore(), CameraServiceBefore.class);
    }

}
