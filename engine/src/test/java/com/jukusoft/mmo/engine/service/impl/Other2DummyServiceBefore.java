package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.service.*;

public class Other2DummyServiceBefore implements IService, InputService, UpdateService, BeforeDrawService, AfterDrawService {

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean processInput() {
        return false;
    }

    @Override
    public void update() {

    }

    @Override
    public void beforeDraw() {

    }

    @Override
    public void afterDraw() {

    }

}
