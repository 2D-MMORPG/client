package com.jukusoft.mmo.game;

import com.jukusoft.mmo.engine.app.BaseApp;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;
import com.jukusoft.mmo.engine.service.ServiceManager;
import com.jukusoft.mmo.engine.service.impl.CameraService;
import com.jukusoft.mmo.engine.service.impl.NetworkService;
import com.jukusoft.mmo.engine.service.impl.SpriteBatchService;
import com.jukusoft.mmo.engine.service.impl.StreamManagerService;
import com.jukusoft.mmo.game.screens.LoginScreen;
import com.jukusoft.mmo.game.screens.ServerListScreen;
import com.jukusoft.mmo.game.screens.SplashScreen;
import com.jukusoft.mmo.game.service.AccountService;

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

        //add network service
        manager.addService(new NetworkService(), NetworkService.class);

        //add stream manager service
        manager.addService(new StreamManagerService(), StreamManagerService.class);

        //add account manager service
        manager.addService(new AccountService(), AccountService.class);
    }

    @Override
    protected void initScreens(ScreenManager<IScreen> manager) {
        //add splash screen
        manager.addScreen("splash_screen", new SplashScreen());

        //add screen to show serverlist, so user can choose game server
        manager.addScreen("serverlist", new ServerListScreen());

        //add login screen
        manager.addScreen("login", new LoginScreen());

        //push screen so screen will be active
        manager.push("splash_screen");
    }

}
