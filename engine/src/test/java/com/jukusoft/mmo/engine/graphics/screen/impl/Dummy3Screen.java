package com.jukusoft.mmo.engine.graphics.screen.impl;

import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.InjectScreenManager;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;

public class Dummy3Screen implements IScreen {

    @InjectScreenManager
    protected ScreenManager<IScreen> screenManager;

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public boolean processInput() {
        return false;
    }

    @Override
    public void update() {

    }


    @Override
    public void draw() {

    }
}
