package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.Gdx;
import com.jukusoft.mmo.engine.exception.RequiredServiceNotFoundException;
import com.jukusoft.mmo.engine.service.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultServiceManager implements ServiceManager {

    protected static final String TAG_INJECT_SERVICE = "inject_service";

    //map with all services
    protected Map<Class<?>,IService> serviceMap = new ConcurrentHashMap<>();

    //list with input processors
    protected List<InputService> inputProcessors = new ArrayList<>();

    //services which can be updated
    protected List<UpdateService> updateServices = new ArrayList<>();

    //services which can be drawn
    protected List<BeforeDrawService> beforeDrawServices = new ArrayList<>();

    //services which can be executed after beforeDraw
    protected List<AfterDrawService> afterDrawServices = new ArrayList<>();

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

        //check, if service can process input
        if (canProcessInput(service)) {
            this.inputProcessors.add((InputService) service);
        }

        //check, if service can update
        if (canUpdate(service)) {
            this.updateServices.add((UpdateService) service);
        }

        //check, if service can beforeDraw
        if (canDraw(service)) {
            this.beforeDrawServices.add((BeforeDrawService) service);
        }

        //check, if service can after beforeDraw
        if (canAfterDraw(service)) {
            this.afterDrawServices.add((AfterDrawService) service);
        }

        //start service
        service.onStart();
    }

    @Override
    public <T extends IService> void removeService(Class<T> cls) {
        IService service = this.serviceMap.get(cls);

        if (service != null) {
            //stop service
            service.onStop();

            //remove service from processor list
            this.inputProcessors.remove(service);
            this.updateServices.remove(service);
            this.beforeDrawServices.remove(service);
            this.afterDrawServices.remove(service);
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
    public IService getServiceObject (Class<?> type) {
        IService service = this.serviceMap.get(type);

        if (service == null) {
            throw new IllegalStateException("service " + type.getName() + " isnt registered yet. Add with addService() first.");
        }

        return service;
    }

    @Override
    public <T extends IService> boolean existsService(Class<T> cls) {
        return this.serviceMap.get(cls) != null;
     }

    @Override
    public void processInput() {
        for (InputService service : this.inputProcessors) {
            if (service.processInput()) {
                //if service returns true, input was already processed
                break;
            }
        }
    }

    @Override
    public void update() {
        for (UpdateService service : this.updateServices) {
            service.update();
        }
    }

    @Override
    public void draw() {
        for (BeforeDrawService service : this.beforeDrawServices) {
            service.beforeDraw();
        }
    }

    @Override
    public void afterDraw() {
        for (AfterDrawService service : this.afterDrawServices) {
            service.afterDraw();
        }
    }

    protected <T> void injectServices (T target) {
        //iterate through all fields in class
        for (Field field : target.getClass().getDeclaredFields()) {
            //get annotation
            InjectService annotation = field.getAnnotation(InjectService.class);

            if (annotation != null) {
                if (IService.class.isAssignableFrom(field.getType())) {
                    Gdx.app.debug(TAG_INJECT_SERVICE, "try to inject service '" + field.getType().getSimpleName() + "' in class: " + target.getClass().getSimpleName());
                    injectServiceField(target, field, annotation.nullable());
                } else {
                    throw new IllegalStateException("annotation " + annotation.getClass().getName() + " was set on wrong attribute, which doesnt extends IService: " + field.getName());
                }
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
        Object value = this.serviceMap.get(field.getType());

        // check if component is present
        if (value != null) {
            //set field accessible, so we can change value
            field.setAccessible(true);

            try {
                //set value of field
                field.set(target, value);

                Gdx.app.debug(TAG_INJECT_SERVICE, "set value successfully: " + field.getType());
            } catch (IllegalArgumentException | IllegalAccessException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Exception was thrown in ServiceManager: ", e);

                throw new RequiredServiceNotFoundException("Couldn't inject service '" + field.getType() + "' in '"
                        + field.getDeclaringClass().getName() + "'. Exception: " + e.getLocalizedMessage());
            }
        } else if (!nullable) {
            throw new RequiredServiceNotFoundException("Service '" + field.getType()
                    + "' is required by class '" + field.getDeclaringClass().getName() + "' but does not exist.");
        } else {
            Gdx.app.error(TAG_INJECT_SERVICE, "Service doesnt exists: " + field.getType().getSimpleName());
        }
    }

    protected <T extends IService> boolean canProcessInput (T service) {
        return service instanceof InputService;
    }

    protected <T extends IService> boolean canUpdate (T service) {
        return service instanceof UpdateService;
    }

    protected <T extends IService> boolean canDraw (T service) {
        return service instanceof BeforeDrawService;
    }

    protected <T extends IService> boolean canAfterDraw (T service) {
        return service instanceof AfterDrawService;
    }

}
