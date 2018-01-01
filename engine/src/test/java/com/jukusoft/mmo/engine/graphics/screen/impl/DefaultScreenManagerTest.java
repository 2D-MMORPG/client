package com.jukusoft.mmo.engine.graphics.screen.impl;

import com.jukusoft.mmo.engine.GameUnitTest;
import com.jukusoft.mmo.engine.exception.ScreenNotFoundException;
import com.jukusoft.mmo.engine.graphics.screen.DummyScreen;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.OtherDummyScreen;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;
import com.jukusoft.mmo.engine.service.ServiceManager;
import com.jukusoft.mmo.engine.service.impl.DefaultServiceManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultScreenManagerTest extends GameUnitTest {

    @Test
    public void testConstructor () {
        ServiceManager serviceManager = new DefaultServiceManager();
        new DefaultScreenManager(serviceManager);
    }

    @Test (expected = NullPointerException.class)
    public void testAddNullScreen () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.addScreen(null, new DummyScreen());
    }

    @Test (expected = NullPointerException.class)
    public void testAddNullScreen1 () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.addScreen("dummy_screen", null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAddEmptyScreen () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.addScreen("", new DummyScreen());
    }

    @Test (expected = IllegalStateException.class)
    public void testAddExistentScreen () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.addScreen("dummy_screen", new DummyScreen());
        manager.addScreen("dummy_screen", new DummyScreen());
    }

    @Test
    public void testAddScreen () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.addScreen("dummy_screen", new DummyScreen());
    }

    @Test
    public void testAddScreen1 () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.addScreen("dummy_screen", new OtherDummyScreen());
    }

    @Test (expected = NullPointerException.class)
    public void testRemoveNullScreen () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.removeScreen(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRemoveEmptyScreen () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.removeScreen("");
    }

    @Test
    public void testRemoveScreen () {
        ScreenManager<IScreen> manager = this.createScreenManager();

        manager.removeScreen("not-existent-screen");

        assertNull(manager.getScreenByName("not-existent-screen"));

        manager.addScreen("dummy_screen", new DummyScreen());

        assertNotNull(manager.getScreenByName("dummy_screen"));

        manager.removeScreen("dummy_screen");

        assertEquals(true, manager.getScreenByName("dummy_screen") == null);
    }

    @Test (expected = NullPointerException.class)
    public void testPushNullScreen () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.push(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testPushEmptyScreen () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.push("");
    }

    @Test (expected = ScreenNotFoundException.class)
    public void testPushNotExistentScreen () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.push("not-existent-screen");
    }

    @Test
    public void testPush () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.addScreen("dummy_screen", new DummyScreen());
        manager.push("dummy_screen");
    }

    protected ScreenManager<IScreen> createScreenManager () {
        ServiceManager serviceManager = new DefaultServiceManager();
        return new DefaultScreenManager(serviceManager);
    }

}
