package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.jukusoft.mmo.engine.graphics.camera.ResizeListener;
import com.jukusoft.mmo.engine.service.ServiceManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class WindowServiceTest {

    protected static Application application = null;

    protected boolean resized = false;

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
        new WindowService();
    }

    @Test
    public void testStartAndStop () {
        ServiceManager serviceManager = new DefaultServiceManager();

        WindowService service = new WindowService();
        serviceManager.addService(service, WindowService.class);

        serviceManager.removeService(WindowService.class);
    }

    @Test
    public void testResizeListener () {
        ServiceManager serviceManager = new DefaultServiceManager();

        WindowService service = new WindowService();
        serviceManager.addService(service, WindowService.class);

        DummyResizeListener resizeListener = new DummyResizeListener();

        service.addResizeListener(resizeListener);

        assertEquals(true, service.resizeListeners.contains(resizeListener));

        service.removeResizeListener(resizeListener);

        assertEquals(false, service.resizeListeners.contains(resizeListener));
    }

    @Test
    public void testExecuteResizeListener () {
        ServiceManager serviceManager = new DefaultServiceManager();

        WindowService service = new WindowService();
        serviceManager.addService(service, WindowService.class);

        assertEquals(false, this.resized);

        service.addResizeListener(new ResizeListener() {
            @Override
            public void onResize(int width, int height) {
                resized = true;
            }
        });

        assertEquals(false, this.resized);

        service.resize(800, 600);

        //check, if method was executed
        assertEquals(true, this.resized);
    }

}
