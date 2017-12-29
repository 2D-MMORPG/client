package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;

public class OtherDummyService implements IService {

    @InjectService (nullable = true)
    protected DummyService dummyService;

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

}
