package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.Gdx;
import com.jukusoft.mmo.engine.graphics.camera.CameraHelper;
import com.jukusoft.mmo.engine.graphics.camera.manager.CameraManager;
import com.jukusoft.mmo.engine.graphics.camera.manager.DefaultCameraManager;
import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;

/**
* this service is reponsible for holding the camera and updating them
*/
public class CameraService implements IService {

    //camera manager
    protected CameraManager cameraManager = null;

    protected boolean updateCameraOnResize = true;

    @InjectService (nullable = false)
    protected WindowService windowService = null;

    @Override
    public void onStart() {
        //create new camera manager and set viewport of current window dimensions
        this.cameraManager = new DefaultCameraManager(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.windowService.addResizeListener(this::updateCamera);
    }

    protected void updateCamera (int width, int height) {
        if (updateCameraOnResize) {
            //resize main camera & ui camera
            this.cameraManager.getMainCamera().resize(width, height);
            this.cameraManager.getUICamera().resize(width, height);
        }
    }

    @Override
    public void onStop() {

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

}
