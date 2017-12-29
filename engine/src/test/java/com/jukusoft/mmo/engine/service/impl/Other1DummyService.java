package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;

public class Other1DummyService implements IService {

    @InjectService
    protected String testString;

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

}
