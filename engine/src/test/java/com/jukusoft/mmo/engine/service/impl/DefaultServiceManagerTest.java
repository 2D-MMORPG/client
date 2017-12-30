package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.jukusoft.mmo.engine.exception.RequiredServiceNotFoundException;
import com.jukusoft.mmo.engine.service.ServiceManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultServiceManagerTest {

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

        application.setApplicationLogger(new ApplicationLogger() {
            @Override
            public void log(String tag, String message) {
                Logger.getAnonymousLogger().log(Level.SEVERE, tag, message);
            }

            @Override
            public void log(String tag, String message, Throwable exception) {
                Logger.getAnonymousLogger().log(Level.SEVERE, tag  + ": " + message, exception);
            }

            @Override
            public void error(String tag, String message) {
                Logger.getAnonymousLogger().log(Level.SEVERE, tag + ": " + message);
            }

            @Override
            public void error(String tag, String message, Throwable exception) {
                Logger.getAnonymousLogger().log(Level.SEVERE, tag + ": " + message);
            }

            @Override
            public void debug(String tag, String message) {
                Logger.getAnonymousLogger().log(Level.INFO, tag, message);
                System.err.println("DEBUG [" + tag + "] " + message);
            }

            @Override
            public void debug(String tag, String message, Throwable exception) {
                Logger.getAnonymousLogger().log(Level.INFO, tag + ": " + message, exception);
            }
        });

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;

        Gdx.app = application;
    }

    @AfterClass
    public static void afterClass () {
        // Exit the application first
        application.exit();
        application = null;
    }

    @Test
    public void testConstructor () {
        new DefaultServiceManager();
    }

    @Test (expected = IllegalStateException.class)
    public void testAddExistentService () {
        ServiceManager serviceManager = new DefaultServiceManager();

        //add service
        serviceManager.addService(new DummyService(), DummyService.class);

        //add service again and throw IllegalStateException
        serviceManager.addService(new DummyService(), DummyService.class);
    }

    @Test
    public void testAddAndRemove () {
        ServiceManager serviceManager = new DefaultServiceManager();

        //add services
        serviceManager.addService(new WindowService(), WindowService.class);

        assertEquals(true, serviceManager.existsService(WindowService.class));

        assertNotNull(serviceManager.getService(WindowService.class));

        //get service again
        assertNotNull(serviceManager.getService(WindowService.class));

        serviceManager.removeService(WindowService.class);

        assertEquals(false, serviceManager.existsService(WindowService.class));

        //remove service again
        serviceManager.removeService(WindowService.class);
    }

    @Test (expected = IllegalStateException.class)
    public void testGetNotExistentService () {
        ServiceManager serviceManager = new DefaultServiceManager();

        serviceManager.getService(WindowService.class);
    }

    @Test
    public void testProcessInput () {
        ServiceManager serviceManager = new DefaultServiceManager();
        serviceManager.addService(new DummyService(), DummyService.class);

        serviceManager.processInput();
    }

    @Test
    public void testUpdate () {
        ServiceManager serviceManager = new DefaultServiceManager();
        serviceManager.addService(new DummyService(), DummyService.class);

        serviceManager.update();
    }

    @Test
    public void testDraw () {
        ServiceManager serviceManager = new DefaultServiceManager();
        serviceManager.addService(new DummyService(), DummyService.class);

        serviceManager.draw();
    }

    @Test
    public void testAfterDraw () {
        ServiceManager serviceManager = new DefaultServiceManager();
        serviceManager.addService(new DummyService(), DummyService.class);

        serviceManager.afterDraw();
    }

    @Test
    public void testServiceInjection () {
        ServiceManager serviceManager = new DefaultServiceManager();

        //because service isnt required, no exception should be thrown
        serviceManager.addService(new OtherDummyService(), OtherDummyService.class);

        assertNull(serviceManager.getService(OtherDummyService.class).dummyService);
    }

    @Test(expected = RequiredServiceNotFoundException.class)
    public void testServiceInjection1 () {
        Gdx.app.debug("test", "test");

        ServiceManager serviceManager = new DefaultServiceManager();

        //because service isnt required, no exception should be thrown
        serviceManager.addService(new OtherIbjectedDummyService(), OtherIbjectedDummyService.class);
    }

    @Test
    public void testServiceInjection2 () {
        ServiceManager serviceManager = new DefaultServiceManager();

        serviceManager.addService(new DummyService(), DummyService.class);

        //because service isnt required, no exception should be thrown
        serviceManager.addService(new OtherIbjectedDummyService(), OtherIbjectedDummyService.class);

        assertNotNull(serviceManager.getService(OtherIbjectedDummyService.class).dummyService);
    }

    @Test (expected = IllegalStateException.class)
    public void testServiceInjection3 () {
        ServiceManager serviceManager = new DefaultServiceManager();

        serviceManager.addService(new Other1DummyService(), Other1DummyService.class);
    }

    @Test
    public void testServiceInjection4 () {
        ServiceManager serviceManager = new DefaultServiceManager();

        serviceManager.addService(new OtherDummyService(), OtherDummyService.class);
    }

    @Test (expected = RequiredServiceNotFoundException.class)
    public void testServiceInjection5 () {
        ServiceManager serviceManager = new DefaultServiceManager();

        serviceManager.addService(new DummyService(), DummyService.class);

        serviceManager.addService(new FinalOtherDummyService(), FinalOtherDummyService.class);
    }

}
