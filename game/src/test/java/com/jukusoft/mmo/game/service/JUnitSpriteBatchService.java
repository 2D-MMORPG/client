package com.jukusoft.mmo.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jukusoft.mmo.engine.service.impl.SpriteBatchService;
import org.mockito.Mockito;

public class JUnitSpriteBatchService extends SpriteBatchService {

    @Override
    public void onStart() {
        Gdx.app.debug("SpriteBatchService", "create sprite batch.");

        this.batch = Mockito.mock(SpriteBatch.class);
    }

}
