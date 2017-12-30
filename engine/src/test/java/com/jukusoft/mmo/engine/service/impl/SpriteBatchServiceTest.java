package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jukusoft.mmo.engine.GameTest;
import com.jukusoft.mmo.engine.service.ServiceManager;
import org.junit.Test;
import org.mockito.Mockito;

public class SpriteBatchServiceTest extends GameTest {

    @Test
    public void testConstructor () {
        new SpriteBatchService();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testStart () {
        ServiceManager serviceManager = new DefaultServiceManager();

        SpriteBatchService service = new SpriteBatchService();
        service.batch = Mockito.mock(SpriteBatch.class);
        serviceManager.addService(service, SpriteBatchService.class);

        serviceManager.removeService(SpriteBatchService.class);
    }

    @Test
    public void testStop () {
        SpriteBatchService service = new SpriteBatchService();
        service.batch = Mockito.mock(SpriteBatch.class);

        service.onStop();
    }

    @Test
    public void testDraw () {
        SpriteBatchService service = new SpriteBatchService();
        service.batch = Mockito.mock(SpriteBatch.class);

        service.draw();
    }

    @Test
    public void testAfterDraw () {
        SpriteBatchService service = new SpriteBatchService();
        service.batch = Mockito.mock(SpriteBatch.class);

        service.afterDraw();
    }

}
