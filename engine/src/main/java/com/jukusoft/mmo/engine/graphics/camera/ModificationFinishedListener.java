package com.jukusoft.mmo.engine.graphics.camera;

/**
 * Created by Justin on 11.02.2017.
 */
public interface ModificationFinishedListener {

    public <T extends CameraModification> void onModificationFinished(T mod, Class<T> cls);

}
