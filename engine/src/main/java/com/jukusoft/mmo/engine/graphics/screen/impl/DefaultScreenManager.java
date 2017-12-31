package com.jukusoft.mmo.engine.graphics.screen.impl;

import com.badlogic.gdx.Gdx;
import com.jukusoft.mmo.engine.exception.RequiredServiceNotFoundException;
import com.jukusoft.mmo.engine.exception.ScreenNotFoundException;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;
import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.ServiceManager;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Justin on 06.02.2017.
 */
public class DefaultScreenManager implements ScreenManager<IScreen> {

    protected static final String TAG_INJECT_SERVICE = "inject_service";

    /**
     * map with all initialized screens
     */
    protected Map<String, IScreen> screens = new ConcurrentHashMap<>();

    /**
     * list with all active screens
     */
    protected Deque<IScreen> activeScreens = new ConcurrentLinkedDeque<>();

    /**
     * only for performance improvements!
     *
     * caching list
     */
    protected List<IScreen> cachedScreenList = new ArrayList<>();

    //instance of service manager to inject services
    protected ServiceManager serviceManager = null;

    /**
    * default constructor
    */
    public DefaultScreenManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Override
    public void addScreen(String name, IScreen screen) {
        //inject services first
        this.injectServices(screen);

        // initialize screen first
        screen.onStart();

        this.screens.put(name, screen);

        this.cachedScreenList.add(screen);

        Gdx.app.debug("Screens", "add screen: " + name);
    }

    @Override
    public void removeScreen(String name) {
        if (name == null) {
            throw new NullPointerException("name cannot be null.");
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty.");
        }

        IScreen screen = this.screens.get(name);

        if (screen != null) {
            screen.onStop();

            this.activeScreens.remove(screen);
            this.cachedScreenList.remove(screen);
        }

        this.screens.remove(name);

        Gdx.app.debug("Screens", "remove screen: " + name);
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
        Object value = this.serviceManager.getServiceObject(field.getType());

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

    @Override
    public void push(String name) {
        IScreen screen = this.screens.get(name);

        if (screen == null) {
            throw new ScreenNotFoundException(
                    "Couldnt found initialized screen '" + name + "', add screen with method addScreen() first.");
        }

        screen.onResume();

        this.activeScreens.push(screen);

        Gdx.app.debug("Screens", "push screen: " + name);
    }

    @Override
    public void leaveAllAndEnter(String name) {
        // leave all active game states
        IScreen screen = pop();

        // pop and pause all active screens
        while (this.pop() != null) {
            screen = pop();
        }

        // push new screen
        this.push(name);
    }

    @Override
    public IScreen pop() {
        IScreen screen = this.activeScreens.poll();

        if (screen != null) {
            /*game.runOnUIThread(() -> {
                //pause screen
                screen.onPause();
            });*/
            //pause screen
            screen.onPause();
        }

        Gdx.app.debug("Screens", "pop screen.");

        return screen;
    }

    @Override
    public IScreen getScreenByName(String name) {
        return this.screens.get(name);
    }

    @Override
    public Collection<IScreen> listScreens() {
        return this.cachedScreenList;
    }

    @Override
    public Collection<IScreen> listActiveScreens() {
        return this.activeScreens;
    }

    @Override
    public void dispose() {
        //iterate through all screens
        for (Map.Entry<String,IScreen> entry : this.screens.entrySet()) {
            //remove screen
            this.removeScreen(entry.getKey());
        }
    }

}
