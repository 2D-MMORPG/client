package com.jukusoft.mmo.engine.service.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ThreadPoolServiceTest {

    protected boolean executed = false;

    @Test
    public void testConstructor () {
        new ThreadPoolService();
    }

    @Test
    public void testStartAndStop () {
        //create new threadpool service
        ThreadPoolService service = new ThreadPoolService();

        //start service
        service.onStart();

        //stop service
        service.onStop();
    }

    @Test
    public void testExecute () throws InterruptedException {
        this.executed = false;

        //create new threadpool service
        ThreadPoolService service = new ThreadPoolService();

        //start service
        service.onStart();

        service.execute(() -> {
            //set flag
            this.executed = true;
        });

        //wait 200ms so thread pool can execute task
        Thread.sleep(200);

        assertEquals(true, this.executed);

        //stop service
        service.onStop();
    }

}
