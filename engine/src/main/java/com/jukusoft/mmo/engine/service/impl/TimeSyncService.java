package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;

public class TimeSyncService implements IService {

    //tick length in milliseconds
    public static final long TICK_LENGTH_MS = 50;

    //timestamp of first tick
    protected long firstTickTimestamp = System.currentTimeMillis();

    @InjectService
    protected NetworkService networkService;

    @Override
    public void onStart() {
        //TODO: add network listeners to synchronize server timestamp
    }

    @Override
    public void onStop() {
        //
    }

    /**
    * get server timestamp
     *
     * @return server timestamp
    */
    public long getTimestamp () {
        return System.currentTimeMillis();
    }

    /**
    * get current network tick
    */
    public int getCurrentTick () {
        long time = getTimestamp() - this.firstTickTimestamp;

        return (int) (time / TICK_LENGTH_MS);
    }

}
