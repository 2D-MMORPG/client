package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.jukusoft.mmo.engine.service.IService;

public class InputProcessorService implements IService {

    protected InputMultiplexer inputMultiplexer = null;

    @Override
    public void onStart() {
        //create new input multiplexer
        this.inputMultiplexer = new InputMultiplexer();

        //set input processor
        Gdx.input.setInputProcessor(this.inputMultiplexer);
    }

    @Override
    public void onStop() {

    }

    public void addInputProcessor (InputProcessor inputProcessor) {
        this.inputMultiplexer.addProcessor(inputProcessor);
    }

    public void removeInputProcessor (InputProcessor inputProcessor) {
        this.inputMultiplexer.removeProcessor(inputProcessor);
    }

}
