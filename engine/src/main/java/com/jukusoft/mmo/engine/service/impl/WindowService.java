package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.graphics.camera.ResizeListener;
import com.jukusoft.mmo.engine.service.IService;

import java.util.ArrayList;
import java.util.List;

public class WindowService implements IService {

    //list with resize listeners
    protected List<ResizeListener> resizeListeners = new ArrayList<>();

    /**
    * default constructor
    */
    public WindowService () {
        //
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    /**
     * add resize listener
     *
     * @param listener resize listener which is called, if window was resized
     */
    public void addResizeListener(ResizeListener listener) {
        this.resizeListeners.add(listener);
    }

    /**
     * remove resize listener
     *
     * @param listener resize listener which is called, if window was resized
     */
    public void removeResizeListener(ResizeListener listener) {
        this.resizeListeners.remove(listener);
    }

    /**
    * resize window
     *
     * @param width new window width
     * @param height new window height
    */
    public void resize (final int width, final int height) {
        //call resize listeners
        this.resizeListeners.stream().forEach(consumer -> {
            consumer.onResize(width, height);
        });
    }

}
