package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jukusoft.mmo.engine.GameUnitTest;
import com.jukusoft.mmo.engine.service.ServiceManager;
import org.junit.Test;
import org.mockito.Mockito;

public class SpriteBatchServiceTest extends GameUnitTest {

    @Test
    public void testConstructor () {
        new SpriteBatchServiceBefore();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testStart () {
        ServiceManager serviceManager = new DefaultServiceManager();

        SpriteBatchServiceBefore service = new SpriteBatchServiceBefore();
        service.batch = Mockito.mock(SpriteBatch.class);
        serviceManager.addService(service, SpriteBatchServiceBefore.class);

        serviceManager.removeService(SpriteBatchServiceBefore.class);
    }

    @Test
    public void testStop () {
        SpriteBatchServiceBefore service = new SpriteBatchServiceBefore();
        service.batch = Mockito.mock(SpriteBatch.class);

        service.onStop();
    }

    @Test
    public void testDraw () {
        SpriteBatchServiceBefore service = new SpriteBatchServiceBefore();
        service.batch = Mockito.mock(SpriteBatch.class);

        service.beforeDraw();
    }

    @Test
    public void testAfterDraw () {
        SpriteBatchServiceBefore service = new SpriteBatchServiceBefore();
        service.batch = Mockito.mock(SpriteBatch.class);

        service.afterDraw();
    }

}
