package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pools;
import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.event.Event;
import com.jukusoft.mmo.engine.service.event.EventHandler;
import com.jukusoft.mmo.engine.service.event.Events;
import com.sun.xml.internal.stream.events.DummyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus implements IService {

    //map with all event handlers
    protected Map<Class<?>,List<EventHandler>> handlerMap = new ConcurrentHashMap<>();

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        //
    }

    /**
    * subscribe to event
     *
     * @param cls event class
     * @param handler event handler
    */
    public <T extends Event> void subscribe (Class<T> cls, EventHandler<T> handler) {
        //create list if not exists
        createListIfAbsent(cls);

        //add handler to list
        this.handlerMap.get(cls).add(handler);
    }

    /**
    * unsubscribe from event
     *
     * @param cls event class
     * @param handler event handler
    */
    public <T extends Event> void unsubscribe (Class<T> cls, EventHandler<T> handler) {
        //list
        List<EventHandler> list = this.handlerMap.get(cls);

        if (list == null) {
            //no handler is registered for this event
            return;
        }

        list.remove(handler);
    }

    protected void createListIfAbsent (Class<?> cls) {
        if (this.handlerMap.get(cls) == null) {
            this.handlerMap.put(cls, new ArrayList<>());
        }
    }

    /**
    * dispatch event so event can be handled
    */
    public <T extends Event> void dispatch (T event) {
        this.process(event);
    }

    protected <T extends Event> void process (T event) {
        //get listeners
        List<EventHandler> list = this.handlerMap.get(event.getClass());

        if (list == null) {
            Gdx.app.debug("EventBus", "no event listener found for event '" + event + "'.");

            //free event
            Events.free(event);

            //we dont need to handle event
            return;
        }

        //call handlers
        for (EventHandler handler : list) {
            handler.handle(event);
        }

        //free memory and add event to event pool
        Events.free(event);
    }

}
