package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.service.IService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadPoolService implements IService {

    //executor service
    protected ScheduledExecutorService executorService = null;

    protected static final int WORKER_THREADS = 2;

    /**
    * default constructor
    */
    public ThreadPoolService () {
        //
    }

    /**
    * execute runnable in threadpool thread asynchronous
    */
    public void execute (Runnable command) {
        this.executorService.execute(command);
    }

    @Override
    public void onStart() {
        this.executorService = Executors.newScheduledThreadPool(WORKER_THREADS);
    }

    @Override
    public void onStop() {
        this.executorService.shutdown();
    }

}
