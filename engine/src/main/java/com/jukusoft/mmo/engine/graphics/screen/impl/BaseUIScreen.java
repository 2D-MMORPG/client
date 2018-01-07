package com.jukusoft.mmo.engine.graphics.screen.impl;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.impl.InputProcessorService;
import com.jukusoft.mmo.engine.service.impl.WindowService;
import com.jukusoft.mmo.engine.utils.GameTime;
import com.kotcrab.vis.ui.VisUI;

public abstract class BaseUIScreen implements IScreen {

    @InjectService
    protected InputProcessorService inputService;

    @InjectService
    protected WindowService windowService;

    //ui stage
    protected Stage stage = null;

    @Override
    public void onStart() {
        VisUI.load();
    }

    @Override
    public void onStop() {
        VisUI.dispose();
    }

    @Override
    public void onResume() {
        //create new stage
        this.stage = new Stage(new ScreenViewport());

        this.initStage(this.stage);

        //add input handling
        this.inputService.addInputProcessor(this.stage);

        this.windowService.addResizeListener(((width, height) ->
                // See below for what true means.
                stage.getViewport().update(width, height, true)
        ));
    }

    @Override
    public void onPause() {
        this.inputService.removeInputProcessor(this.stage);

        this.cleanUpStage(this.stage);

        this.stage.dispose();
        this.stage = null;
    }

    @Override
    public boolean processInput() {
        return false;
    }

    @Override
    public void update() {
        this.stage.act(GameTime.getInstance().getDeltaTime());
    }

    @Override
    public void draw() {
        this.stage.draw();
    }

    public abstract void initStage (Stage stage);

    public abstract void cleanUpStage (Stage stage);

}
