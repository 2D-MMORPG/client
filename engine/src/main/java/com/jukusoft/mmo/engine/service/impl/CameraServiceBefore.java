package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.Gdx;
import com.jukusoft.mmo.engine.graphics.camera.CameraHelper;
import com.jukusoft.mmo.engine.graphics.camera.manager.CameraManager;
import com.jukusoft.mmo.engine.graphics.camera.manager.DefaultCameraManager;
import com.jukusoft.mmo.engine.service.BeforeDrawService;
import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.UpdateService;
import com.jukusoft.mmo.engine.utils.GameTime;

/**
* this service is reponsible for holding the camera and updating them
*/
public class CameraServiceBefore implements IService, UpdateService, BeforeDrawService {

    //camera manager
    protected CameraManager cameraManager = null;

    protected boolean updateCameraOnResize = true;

    @InjectService (nullable = false)
    protected WindowService windowService = null;

    @InjectService (nullable = true)
    protected SpriteBatchServiceBefore spriteBatchService = null;

    //game time (local only)
    protected GameTime time = null;

    /**
    * default constructor
    */
    public CameraServiceBefore() {
        //
    }

    @Override
    public void onStart() {
        //create new camera manager and set viewport of current window dimensions
        this.cameraManager = new DefaultCameraManager(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.windowService.addResizeListener(this::resizeCamera);

        this.time = GameTime.getInstance();
    }

    protected void resizeCamera (int width, int height) {
        if (updateCameraOnResize) {
            //resize main camera & ui camera
            this.cameraManager.getMainCamera().resize(width, height);
            this.cameraManager.getUICamera().resize(width, height);
        }
    }

    @Override
    public void onStop() {
        //we dont need to cleanup memory
    }

    /**
    * get camera manager
    */
    public CameraManager getCameraManager() {
        return this.cameraManager;
    }

    public CameraHelper getMainCamera () {
        return this.cameraManager.getMainCamera();
    }

    public CameraHelper getUICamera () {
        return this.cameraManager.getUICamera();
    }

    @Override
    public void update() {
        //update cameras
        this.cameraManager.update(this.time);
    }

    @Override
    public void beforeDraw() {
        if (this.spriteBatchService != null) {
            //set camera projection matrix to sprite batch
            this.spriteBatchService.getBatch().setProjectionMatrix(this.getCameraManager().getMainCamera().getCombined());
        }
    }
}
