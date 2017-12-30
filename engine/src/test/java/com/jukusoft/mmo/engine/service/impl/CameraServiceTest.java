package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.jukusoft.mmo.engine.exception.RequiredServiceNotFoundException;
import com.jukusoft.mmo.engine.service.ServiceManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class CameraServiceTest {

    protected static Application application = null;

    @BeforeClass
    public static void beforeClass () {
        //http://manabreak.eu/java/2016/10/21/unittesting-libgdx.html

        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(new ApplicationListener() {
            @Override public void create() {}
            @Override public void resize(int width, int height) {}
            @Override public void render() {}
            @Override public void pause() {}
            @Override public void resume() {}
            @Override public void dispose() {}
        });

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;
    }

    @AfterClass
    public static void afterClass () {
        // Exit the application first
        application.exit();
        application = null;
    }

    @Test
    public void testConstructor () {
        new CameraService();
    }

    @Test (expected = RequiredServiceNotFoundException.class)
    public void testStartAndStop () {
        ServiceManager serviceManager = new DefaultServiceManager();

        CameraService service = new CameraService();
        serviceManager.addService(service, CameraService.class);

        serviceManager.removeService(WindowService.class);
    }

    @Test
    public void testStartAndStop1 () {
        ServiceManager serviceManager = new DefaultServiceManager();

        serviceManager.addService(new WindowService(), WindowService.class);

        CameraService service = new CameraService();
        serviceManager.addService(service, CameraService.class);

        serviceManager.removeService(WindowService.class);
    }

    @Test
    public void testGetter () {
        ServiceManager serviceManager = new DefaultServiceManager();

        serviceManager.addService(new WindowService(), WindowService.class);

        CameraService service = new CameraService();
        serviceManager.addService(service, CameraService.class);

        assertEquals(true, service.getCameraManager() != null);
        assertEquals(true, service.getMainCamera() != null);
        assertEquals(true, service.getUICamera() != null);
    }

}
