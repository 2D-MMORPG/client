package com.jukusoft.mmo.engine.service.event;

import com.badlogic.gdx.utils.Pools;

public class Events {

    /**
    * private constructor
    */
    protected Events () {
        //
    }

    public static <T extends Event> T create (Class<T> cls) {
        return Pools.obtain(cls);
    }

    public static void free (Object obj) {
        Pools.free(obj);
    }

}
