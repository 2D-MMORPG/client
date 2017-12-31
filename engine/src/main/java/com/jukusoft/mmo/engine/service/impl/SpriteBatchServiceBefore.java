package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jukusoft.mmo.engine.service.AfterDrawService;
import com.jukusoft.mmo.engine.service.BeforeDrawService;
import com.jukusoft.mmo.engine.service.IService;

public class SpriteBatchServiceBefore implements IService, BeforeDrawService, AfterDrawService {

    //sprite batch
    protected SpriteBatch batch = null;

    public static boolean isJUnitTest = false;

    /**
    * default constructor
    */
    public SpriteBatchServiceBefore() {
        //
    }

    @Override
    public void onStart() {
        Gdx.app.debug("SpriteBatchServiceBefore", "create sprite batch.");

        // create sprite batcher
        if (!isJUnitTest) {
            this.batch = new SpriteBatch();
        }
    }

    @Override
    public void onStop() {
        this.batch.dispose();
        this.batch = null;
    }

    @Override
    public void beforeDraw() {
        //beginn rendering process
        this.batch.begin();
    }

    @Override
    public void afterDraw() {
        //flush rendering
        this.batch.end();
    }

    /**
    * get instance of sprite batch
    */
    public SpriteBatch getBatch() {
        return this.batch;
    }
}
