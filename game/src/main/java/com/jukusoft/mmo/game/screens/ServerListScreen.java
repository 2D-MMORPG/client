package com.jukusoft.mmo.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.jukusoft.mmo.EngineVersion;
import com.jukusoft.mmo.engine.graphics.screen.impl.BaseUIScreen;
import com.kotcrab.vis.ui.widget.VisLabel;

public class ServerListScreen extends BaseUIScreen {

    protected VisLabel versionLabel = null;

    protected Image bgImage = null;

    @Override
    public void initStage(Stage stage) {
        //https://github.com/kotcrab/vis-editor/wiki/VisUI

        //TODO: add background

        //add versionLabel
        this.versionLabel = new VisLabel("2D MMORPG Build version: " + EngineVersion.BUILD_NUMBER, Color.WHITE);
        this.versionLabel.setBounds(0, 0, 100, 20);
        stage.addActor(this.versionLabel);
    }

    @Override
    public void cleanUpStage(Stage stage) {
        this.versionLabel = null;
    }

}
