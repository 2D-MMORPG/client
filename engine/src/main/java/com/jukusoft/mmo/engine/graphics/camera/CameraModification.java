package com.jukusoft.mmo.engine.graphics.camera;

import com.jukusoft.mmo.engine.utils.GameTime;

/**
 * Created by Justin on 11.02.2017.
 */
public interface CameraModification {

    /**
    * update camera
     *
     * @param time current game time
     * @param position temporary position which will be affected
     * @param listener modification listener, which is called if mod has finished
    */
    public void onUpdate(GameTime time, TempCameraParams position, ModificationFinishedListener listener);

    /**
    * dispose mod and cleanup memory
    */
    public void dispose();

}
