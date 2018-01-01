package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.jukusoft.mmo.engine.service.event.DummyEvent;
import com.jukusoft.mmo.engine.service.event.EventHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class EventBusServiceTest {

    protected static Application application = null;

    protected boolean executed = false;

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
        new EventBus();
    }

    @Test
    public void testStartAndStop () {
        EventBus eventBus = new EventBus();

        //start event bus
        eventBus.onStart();

        //stop event bus
        eventBus.onStop();
    }

    @Test
    public void testSubscribe () {
        EventBus eventBus = new EventBus();

        eventBus.subscribe(DummyEvent.class, new EventHandler<DummyEvent>() {
            @Override
            public void handle(DummyEvent event) {
                //
            }
        });

        //subscribe again
        eventBus.subscribe(DummyEvent.class, new EventHandler<DummyEvent>() {
            @Override
            public void handle(DummyEvent event) {
                //
            }
        });
    }

    @Test
    public void testUnsubscribe () {
        EventBus eventBus = new EventBus();

        eventBus.unsubscribe(DummyEvent.class, new EventHandler<DummyEvent>() {
            @Override
            public void handle(DummyEvent event) {
                //
            }
        });
    }

    @Test
    public void testUnsubscribe1 () {
        EventBus eventBus = new EventBus();

        //subscribe event
        eventBus.subscribe(DummyEvent.class, new EventHandler<DummyEvent>() {
            @Override
            public void handle(DummyEvent event) {
                //
            }
        });

        //unsubscribe event
        eventBus.unsubscribe(DummyEvent.class, new EventHandler<DummyEvent>() {
            @Override
            public void handle(DummyEvent event) {
                //
            }
        });
    }

    @Test (expected = NullPointerException.class)
    public void testDispatchNullEvent () {
        EventBus eventBus = new EventBus();
        eventBus.dispatch(null);
    }

    @Test
    public void testDispatchEventWithoutHandler () {
        EventBus eventBus = new EventBus();
        eventBus.dispatch(new DummyEvent());
    }

    @Test
    public void testDispatchEvent () {
        this.executed = false;

        EventBus eventBus = new EventBus();

        //subscribe event
        eventBus.subscribe(DummyEvent.class, new EventHandler<DummyEvent>() {
            @Override
            public void handle(DummyEvent event) {
                executed = true;
            }
        });

        eventBus.dispatch(new DummyEvent());

        assertEquals(true, this.executed);
    }

}
