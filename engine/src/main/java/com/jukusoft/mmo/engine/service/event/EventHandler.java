package com.jukusoft.mmo.engine.service.event;

public interface EventHandler<T extends Event> {

    /**
    * handle event
     *
     * @param event instance of event to handle
    */
    public void handle (T event);

}
