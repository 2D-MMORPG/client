package com.jukusoft.mmo.game.screens;

import com.badlogic.gdx.Gdx;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;
import com.jukusoft.mmo.engine.graphics.screen.impl.DefaultScreenManager;
import com.jukusoft.mmo.engine.service.ServiceManager;
import com.jukusoft.mmo.engine.service.asset.AssetManagerService;
import com.jukusoft.mmo.engine.service.asset.JUnitAssetManagerService;
import com.jukusoft.mmo.engine.service.impl.DefaultServiceManager;
import com.jukusoft.mmo.engine.service.impl.SpriteBatchService;
import com.jukusoft.mmo.game.GameUnitTest;
import com.jukusoft.mmo.game.service.JUnitSpriteBatchService;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SpashScreenTest extends GameUnitTest {

    @Test
    public void testConstructor () {
        new SplashScreen();
    }

    @Test
    public void testStartAndStop () {
        ServiceManager serviceManager = new DefaultServiceManager();
        SpriteBatchService.isJUnitTest = true;
        SpriteBatchService spriteBatchService = new JUnitSpriteBatchService();
        serviceManager.addService(spriteBatchService, SpriteBatchService.class);

        AssetManagerService assetManagerService = new JUnitAssetManagerService();
        serviceManager.addService(assetManagerService, AssetManagerService.class);
        ScreenManager<IScreen> screenManager = new DefaultScreenManager(serviceManager);

        SplashScreen screen = new SplashScreen();
        screenManager.addScreen("spash_screen", screen);

        //start screen
        screen.onStart();

        screen.onResume();

        screen.onPause();

        //stop screen
        screen.onStop();

        SpriteBatchService.isJUnitTest = false;
    }

    @Test
    public void testProcessInput () {
        SplashScreen screen = new SplashScreen();
        screen.processInput();
    }

    @Test
    public void testUpdate () {
        SplashScreen screen = new SplashScreen();

        screen.update();

        screen.elapsed = screen.screenDuration + 100;

        screen.update();
    }

    @Test
    public void testDraw () {
        SplashScreen screen = createScreen();
        screen.draw();
    }

    protected SplashScreen createScreen () {
        ServiceManager serviceManager = new DefaultServiceManager();
        SpriteBatchService.isJUnitTest = true;
        SpriteBatchService spriteBatchService = new JUnitSpriteBatchService();
        serviceManager.addService(spriteBatchService, SpriteBatchService.class);

        AssetManagerService assetManagerService = Mockito.mock(AssetManagerService.class);
        serviceManager.addService(assetManagerService, AssetManagerService.class);
        ScreenManager<IScreen> screenManager = new DefaultScreenManager(serviceManager);

        SplashScreen screen = new SplashScreen();
        screenManager.addScreen("spash_screen", screen);

        return screen;
    }

}
