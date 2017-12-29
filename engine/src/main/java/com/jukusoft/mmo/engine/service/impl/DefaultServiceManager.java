package com.jukusoft.mmo.engine.service.impl;

import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.ServiceManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServiceManager implements ServiceManager {

    //map with all services
    protected Map<Class<?>,IService> serviceMap = new ConcurrentHashMap<>();

    /**
    * default service manager
    */
    public DefaultServiceManager () {
        //
    }

    @Override
    public <T extends IService> void addService(T service, Class<T> cls) {
        //check, if service already exists
        if (this.serviceMap.get(cls) != null) {
            throw new IllegalStateException("service '" + cls.getName() + "' already exists.");
        }

        this.serviceMap.put(cls, service);
    }

    @Override
    public <T extends IService> void removeService(Class<T> cls) {
        this.serviceMap.remove(cls);
    }

    @Override
    public <T extends IService> T getService(Class<T> cls) {
        IService service = this.serviceMap.get(cls);

        if (service == null) {
            throw new IllegalStateException("service " + cls.getName() + " isnt registered yet. Add with addService() first.");
        }

        return cls.cast(service);
    }

    @Override
    public <T extends IService> boolean existsService(Class<T> cls) {
        return this.serviceMap.get(cls) != null;
     }

    @Override
    public void update() {

    }

    @Override
    public void draw() {

    }

}
