package com.jukusoft.mmo.engine.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;
import com.jukusoft.mmo.engine.graphics.screen.impl.DefaultScreenManager;
import com.jukusoft.mmo.engine.service.ServiceManager;
import com.jukusoft.mmo.engine.service.impl.DefaultServiceManager;
import com.jukusoft.mmo.engine.service.impl.WindowService;
import com.jukusoft.mmo.engine.utils.GameTime;
import com.jukusoft.mmo.utils.Platform;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseApp implements ApplicationListener, IApp {

    //instance of game time
    protected GameTime time = GameTime.getInstance();

    //last second of FPS warning
    protected long lastFPSWarning = 0;
    protected int lastFPS = 0;

    //window background (clear) color
    protected Color bgColor = Color.BLACK;

    //service manager
    protected ServiceManager serviceManager = null;

    //services
    protected WindowService windowService = null;

    protected ScreenManager<IScreen> screenManager = null;

    /**
    * default constructor
    */
    public BaseApp () {
        //
    }

    @Override
    public void create() {
        //create service manager
        this.serviceManager = new DefaultServiceManager();

        //add window service
        this.serviceManager.addService(new WindowService(), WindowService.class);

        this.createServices(this.serviceManager);

        //get window service
        this.windowService = this.serviceManager.getService(WindowService.class);

        //create screen manager
        this.screenManager = new DefaultScreenManager(this.serviceManager);

        //create and initialize screens
        this.initScreens(this.screenManager);
    }

    protected abstract void createServices (ServiceManager serviceManager);

    protected abstract void initScreens (ScreenManager<IScreen> manager);

    @Override
    public void resize(int width, int height) {
        Gdx.app.debug("Window", "new window size: " + width + "x" + height + "");

        //notify window manager
        this.windowService.resize(width, height);
    }

    @Override
    public void render() {
        //update game time
        this.time.update();

        //check for FPS warning
        int fps = getFPS();
        if ((fps <= 59 && fps != 0) || this.lastFPS <= 59) {
            // check if warning was already logged this second
            long now = System.currentTimeMillis();
            long nowWarnSecond = now / 1000;
            long lastWarnSecond = lastFPSWarning / 1000;

            if (nowWarnSecond != lastWarnSecond) {
                Gdx.app.log("FPS", "Warning! FPS is <= 59, FPS: " + fps);

                lastFPSWarning = System.currentTimeMillis();

                lastFPS = fps;
            }
        }

        //execute tasks, which should be executed in OpenGL context thread
        Platform.executeQueue();

        try {
            //process input
            if (!this.screenManager.processInput()) {
                this.serviceManager.processInput();
            }

            //update game
            this.serviceManager.update();

            //update screens
            this.screenManager.update();

            //clear all color buffer bits and clear screen
            Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            //beforeDraw game
            this.serviceManager.beforeDraw();

            //draw screens
            this.screenManager.draw();

            //after beforeDraw
            this.serviceManager.afterDraw();
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Exception was thrown in gameloop: ", e);

            Gdx.app.error("BaseGame", "exception thrown while updating game: " + e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    /**
     * get frames per second count
     *
     * @return FPS count
     */
    @Override
    public int getFPS() {
        return Gdx.graphics.getFramesPerSecond();
    }

}
