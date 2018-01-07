package com.jukusoft.mmo.engine.graphics.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
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

    //screen dimension
    protected float width = 0;
    protected float height = 0;
    protected boolean stretch = true;

    @Override
    public void onStart() {
        VisUI.load();
    }

    @Override
    public void onStop() {
        VisUI.dispose();
    }

    /**
    * default constructor
     *
     * @param width width of window / ui screen
     * @param height width of window / ui screen
    */
    public BaseUIScreen (float width, float height) {
        this.width = width;
        this.height = height;
    }

    public BaseUIScreen () {
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
    }

    @Override
    public void onResume() {
        //create new stage
        if (this.stretch) {
            this.stage = new Stage(new ScalingViewport(Scaling.stretch, this.width, this.height));
        } else {
            this.stage = new Stage(new ScreenViewport());
        }

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
