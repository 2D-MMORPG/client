package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.service.ServiceManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DefaultServiceManagerTest {

    @Test
    public void testConstructor () {
        new DefaultServiceManager();
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
    }

    @Test (expected = IllegalStateException.class)
    public void testGetNotExistentService () {
        ServiceManager serviceManager = new DefaultServiceManager();

        serviceManager.getService(WindowService.class);
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

}
