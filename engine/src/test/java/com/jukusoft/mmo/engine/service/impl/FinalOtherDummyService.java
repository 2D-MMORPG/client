package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;

public class FinalOtherDummyService implements IService {

    @InjectService
    protected static final DummyService dummyService = null;

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

}
