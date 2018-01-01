package com.jukusoft.mmo.utils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Platform {

    //tasks which should be executed in OpenGL context thread
    protected static Queue<Runnable> uiQueue = new ConcurrentLinkedQueue<>();

    /**
    * private constructor
    */
    protected Platform () {
        //
    }

    public static void runOnUIThread (Runnable runnable) {
        uiQueue.add(runnable);
    }

    public static void executeQueue () {
        //execute tasks, which should be executed in OpenGL context thread
        Runnable runnable = uiQueue.poll();

        while (runnable != null) {
            runnable.run();

            runnable = uiQueue.poll();
        }
    }

}
