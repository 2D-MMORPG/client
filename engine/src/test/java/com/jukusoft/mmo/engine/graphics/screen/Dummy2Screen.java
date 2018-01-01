package com.jukusoft.mmo.engine.graphics.screen;

import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.impl.DummyService;

public class Dummy2Screen implements IScreen {

    @InjectService
    protected static final DummyService dummyService = null;

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

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
