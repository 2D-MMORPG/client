package com.jukusoft.mmo.engine.graphics.screen.impl;

import com.jukusoft.mmo.engine.GameUnitTest;
import com.jukusoft.mmo.engine.graphics.screen.DummyScreen;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
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

    @Test
    public void testAddScreen () {
        ScreenManager<IScreen> manager = this.createScreenManager();
        manager.addScreen("dummy_screen", new DummyScreen());
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

    protected ScreenManager<IScreen> createScreenManager () {
        ServiceManager serviceManager = new DefaultServiceManager();
        return new DefaultScreenManager(serviceManager);
    }

}
