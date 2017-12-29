package com.jukusoft.mmo.engine.app;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jukusoft.mmo.engine.graphics.camera.CameraHelper;
import com.jukusoft.mmo.engine.graphics.camera.ResizeListener;
import com.jukusoft.mmo.engine.graphics.camera.manager.CameraManager;

import java.util.concurrent.ExecutorService;

/**
 * Created by Justin on 11.09.2017.
 */
public interface IApp {

    /**
     * get frames per second count
     *
     * @return FPS count
     */
    public int getFPS();

}
