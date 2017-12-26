package com.jukusoft.mmo.network;

public interface IThreadPool {

    /**
     * execute blocking task
     *
     * @param runnable blocking task
     */
    public void executeBlocking (Runnable runnable);

    /**
     * create and start new timer
     *
     * @param time delay time in ms
     * @param runnable runnable to execute
     *
     * @return timer ID
     */
    public long startPeriodicTimer (long time, Runnable runnable);

    /**
     * stop existing timer
     *
     * @param timerID timer ID
     */
    public void stopPeriodicTimer (long timerID);

    /**
     * execute runnable after an given time
     *
     * @param time delay time in ms
     * @param runnable runnable to execute
     *
     * @return timer ID
     */
    public long executeDelayed (long time, Runnable runnable);

}
