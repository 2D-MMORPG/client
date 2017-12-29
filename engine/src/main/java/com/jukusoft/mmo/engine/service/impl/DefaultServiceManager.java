package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.Gdx;
import com.jukusoft.mmo.engine.exception.RequiredServiceNotFoundException;
import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.ServiceManager;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        Gdx.app.debug("ServiceManager", "check for service injections in class: " + cls.getName());

        //inject services
        this.injectServices(service);

        this.serviceMap.put(cls, service);
    }

    @Override
    public <T extends IService> void removeService(Class<T> cls) {
        IService service = this.serviceMap.get(cls);

        if (service != null) {
            //stop service
            service.onStop();
        }

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

    protected <T> void injectServices (T target) {
        //iterate through all fields in class
        for (Field field : target.getClass().getDeclaredFields()) {
            //get annotation
            InjectService annotation = field.getAnnotation(InjectService.class);

            if (annotation != null && IService.class.isAssignableFrom(field.getType())) {
                Gdx.app.debug("inject_service", "try to inject service '" + field.getType().getSimpleName() + "' in class: " + target.getClass().getSimpleName());
                injectServiceField(target, field, annotation.nullable());
            } else {
                Gdx.app.error("ServiceManager", "InjectService annotation on wrong object: " + field.getName());
            }
        }
    }

    /**
     * Injects value of field in given service
     *
     * @param target
     *            The object whose field should be injected.
     * @param field
     *            The field.
     * @param nullable
     *            Whether the field can be null.
     */
    private void injectServiceField(Object target, Field field, boolean nullable) {
        // check if component present
        if (this.serviceMap.get(field.getType()) != null) {
            //set field accessible, so we can change value
            field.setAccessible(true);

            try {
                Object value = this.serviceMap.get(field.getType());

                if (value == null) {
                    if (nullable) {
                        Gdx.app.debug("inject_service", "Service '" + field.getType().getSimpleName() + "' doesnt exists.");
                    } else {
                        throw new RequiredServiceNotFoundException("injected object cannot be null.");
                    }
                } else {
                    //set value of field
                    field.set(target, value);

                    Gdx.app.debug("inject_service", "set value successfully: " + field.getType());
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Exception was thrown in ServiceManager: ", e);

                throw new RuntimeException("Couldn't inject service '" + field.getType() + "' in '"
                        + field.getDeclaringClass().getName() + "'. Exception: " + e.getLocalizedMessage());
            }
        } else if (!nullable) {
            throw new RequiredServiceNotFoundException("Service '" + field.getType()
                    + "' is required by class '" + field.getDeclaringClass().getName() + "' but does not exist.");
        } else {
            Gdx.app.error("inject_service", "Service doesnt exists: " + field.getType().getSimpleName());
        }
    }

}
