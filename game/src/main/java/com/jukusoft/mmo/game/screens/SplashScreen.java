package com.jukusoft.mmo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.InjectScreenManager;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;
import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.asset.AssetManagerService;
import com.jukusoft.mmo.engine.service.impl.SpriteBatchService;
import com.jukusoft.mmo.engine.utils.GameTime;

public class SplashScreen implements IScreen {

    protected static final String BG_TEXTURE_PATH = "./data/splashscreen/logo.png";

    @InjectService
    protected AssetManagerService assetManager;

    @InjectService
    protected SpriteBatchService spriteBatchService;

    //background texture
    protected Texture bgTexture = null;

    @InjectScreenManager
    protected ScreenManager<IScreen> screenManager;

    protected float elapsed = 0;
    protected float screenDuration = 1000;

    @Override
    public void onStart() {
        //we dont need to do anything here
    }

    @Override
    public void onStop() {
        //we dont need to do anything here
    }

    @Override
    public void onResume() {
        //load assets
        this.assetManager.load(BG_TEXTURE_PATH, Texture.class);
        this.assetManager.finishLoading(BG_TEXTURE_PATH);
        this.bgTexture = this.assetManager.get(BG_TEXTURE_PATH, Texture.class);

        //reset elapsed time
        this.elapsed = 0;
    }

    @Override
    public void onPause() {
        //we dont need to do anything here
        this.assetManager.unload(BG_TEXTURE_PATH);
    }

    @Override
    public boolean processInput() {
        return false;
    }

    @Override
    public void update() {
        this.elapsed += GameTime.getInstance().getDeltaTime() * 1000;

        if (this.elapsed >= this.screenDuration) {
            //go to next screen
            screenManager.leaveAllAndEnter("serverlist");
        }
    }

    @Override
    public void draw() {
        SpriteBatch batch = this.spriteBatchService.getBatch();

        //draw background image
        batch.draw(this.bgTexture, 0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
    }

}
