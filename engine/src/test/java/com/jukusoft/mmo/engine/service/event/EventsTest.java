package com.jukusoft.mmo.engine.service.event;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventsTest {

    @Test
    public void testConstructor () {
        new Events();
    }

    @Test
    public void testCreate () {
        DummyEvent event = Events.create(DummyEvent.class);

        assertEquals(true, event != null);

        Events.free(event);
    }

}
