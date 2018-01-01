package com.jukusoft.mmo.game;

import com.jukusoft.mmo.engine.app.BaseApp;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;
import com.jukusoft.mmo.engine.service.ServiceManager;
import com.jukusoft.mmo.engine.service.impl.CameraService;
import com.jukusoft.mmo.engine.service.impl.SpriteBatchService;

public class Game extends BaseApp {

    public Game () {
        super();
    }

    @Override
    protected void createServices(ServiceManager manager) {
        //add service which holds sprite batch
        manager.addService(new SpriteBatchService(), SpriteBatchService.class);

        //add service for camera
        manager.addService(new CameraService(), CameraService.class);
    }

    @Override
    protected void initScreens(ScreenManager<IScreen> manager) {
        //
    }

}
