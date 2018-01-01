package com.jukusoft.mmo.engine.graphics.screen;

import com.jukusoft.mmo.engine.service.InjectService;

public class Dummy1Screen implements IScreen {

    @InjectService
    protected String someVar = "";

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
