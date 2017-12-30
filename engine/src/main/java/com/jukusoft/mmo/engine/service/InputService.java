package com.jukusoft.mmo.engine.service;

public interface InputService extends IService {

    /**
    * process input
     *
     * @return false, if next service should process input or true, if service has processed input
    */
    public boolean processInput ();

}
