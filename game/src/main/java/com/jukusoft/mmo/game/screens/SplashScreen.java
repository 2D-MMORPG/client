package com.jukusoft.mmo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.asset.AssetManagerService;
import com.jukusoft.mmo.engine.service.impl.SpriteBatchService;

public class SplashScreen implements IScreen {

    protected static final String BG_TEXTURE_PATH = "./data/splashscreen/logo.png";

    @InjectService
    protected AssetManagerService assetManager;

    @InjectService
    protected SpriteBatchService spriteBatchService;

    //background texture
    protected Texture bgTexture = null;

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
        //
    }

    @Override
    public void draw() {
        SpriteBatch batch = this.spriteBatchService.getBatch();

        //draw background image
        batch.draw(this.bgTexture, 0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
    }

}
