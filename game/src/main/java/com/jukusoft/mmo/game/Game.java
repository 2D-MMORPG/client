package com.jukusoft.mmo.game;

import com.jukusoft.mmo.engine.app.BaseApp;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;
import com.jukusoft.mmo.engine.service.ServiceManager;
import com.jukusoft.mmo.engine.service.impl.CameraService;
import com.jukusoft.mmo.engine.service.impl.SpriteBatchService;
import com.jukusoft.mmo.game.screens.ServerListScreen;
import com.jukusoft.mmo.game.screens.SplashScreen;

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
        //add splash screen
        manager.addScreen("splash_screen", new SplashScreen());

        //add screen to show serverlist, so user can choose game server
        manager.addScreen("serverlist", new ServerListScreen());

        //push screen so screen will be active
        manager.push("splash_screen");
    }

}
