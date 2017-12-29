package com.jukusoft.mmo.engine.service;

public interface ServiceManager {

    /**
    * add an new service
     *
     * @param service instance of service
     * @param cls service class
    */
    public <T extends IService> void addService (T service, Class<T> cls);

    /**
    * remove service
     *
     * @param cls service class
    */
    public <T extends IService> void removeService (Class<T> cls);

    /**
    * get singleton instance of service
     *
     * @param cls service class
     *
     * @throws IllegalStateException if service doesnt exists
     *
     * @return instance of service
    */
    public <T extends IService> T getService (Class<T> cls);

    /**
    * check, if service exists
     *
     * @param cls service class
     *
     * @return true, if service exists
    */
    public <T extends IService> boolean existsService (Class<T> cls);

    public void update ();

    public void draw ();

}
