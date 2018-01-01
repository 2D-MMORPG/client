package com.jukusoft.mmo.engine.service.event;

import com.badlogic.gdx.utils.Pools;
import com.sun.xml.internal.stream.events.DummyEvent;

public class Events {

    public static <T extends Event> T create (Class<T> cls) {
        return Pools.obtain(cls);
    }

    public static void free (Object obj) {
        Pools.free(obj);
    }

}
