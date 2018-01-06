package com.jukusoft.mmo.engine.graphics.screen.impl;

import com.badlogic.gdx.Gdx;
import com.jukusoft.mmo.engine.exception.RequiredServiceNotFoundException;
import com.jukusoft.mmo.engine.exception.ScreenNotFoundException;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.InjectScreenManager;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;
import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.ServiceManager;
import com.jukusoft.mmo.utils.Platform;

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
    protected static final String TAG_NAME_CANNOT_NULL = "name cannot be null.";
    protected static final String TAG_NAME_CANNOT_EMPTY = "name cannot be empty.";
    protected static final String TAG_SCREENS = "Screens";

    /**
     * map with all initialized screens
     */
    protected Map<String, IScreen> screens = new ConcurrentHashMap<>();

    /**
     * list with all active screens
     */
    protected Deque<IScreen> activeScreens = new ConcurrentLinkedDeque<>();

    /**
    * backward active screens list
    */
    protected List<IScreen> backActiveScreens = new ArrayList<>();

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
        if (name == null) {
            throw new NullPointerException(TAG_NAME_CANNOT_NULL);
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException(TAG_NAME_CANNOT_EMPTY);
        }

        if (screen == null) {
            throw new NullPointerException("screen cannot be null.");
        }

        //check, if screen already exists
        if (this.screens.get(name) != null) {
            throw new IllegalStateException("screen '" + name + "' already exists!");
        }

        //inject services first
        this.injectServices(screen);

        //inject screen manager
        this.injectScreenManager(screen);

        // initialize screen first
        screen.onStart();

        this.screens.put(name, screen);

        this.cachedScreenList.add(screen);

        Gdx.app.debug(TAG_SCREENS, "add screen: " + name);
    }

    @Override
    public void removeScreen(String name) {
        if (name == null) {
            throw new NullPointerException(TAG_NAME_CANNOT_NULL);
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException(TAG_NAME_CANNOT_EMPTY);
        }

        IScreen screen = this.screens.get(name);

        if (screen != null) {
            screen.onStop();

            this.activeScreens.remove(screen);
            this.cachedScreenList.remove(screen);
        }

        this.screens.remove(name);

        Gdx.app.debug(TAG_SCREENS, "remove screen: " + name);
    }

    protected <T> void injectServices (T target) {
        //iterate through all fields in class
        /*for (Field field : target.getClass().getDeclaredFields()) {
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
        }*/

        Class<?> cls = target.getClass();

        while (cls != null) {
            this.injectServicesInClass(target, cls);

            //get next upper class
            cls = cls.getSuperclass();
        }
    }

    protected <T> void injectServicesInClass (T target, Class<?> cls) {
        //iterate through all fields in class
        for (Field field : cls.getDeclaredFields()) {
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

    protected void injectScreenManager (Object target) {
        //iterate through all fields in class
        for (Field field : target.getClass().getDeclaredFields()) {
            //get annotation
            InjectScreenManager annotation = field.getAnnotation(InjectScreenManager.class);

            if (annotation != null && ScreenManager.class.isAssignableFrom(field.getType())) {
                //set field accessible, so we can change value
                field.setAccessible(true);

                //set value of field
                try {
                    field.set(target, this);
                } catch (IllegalAccessException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "IllegalAccessException while inject screen manager: ", e);

                    throw new IllegalStateException("Could not inject screen manager for field '" + field.getName() + "' in class: " + target.getClass().getName());
                }
            }
        }
    }

    @Override
    public void push(String name) {
        if (name == null) {
            throw new NullPointerException(TAG_NAME_CANNOT_NULL);
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException(TAG_NAME_CANNOT_EMPTY);
        }

        IScreen screen = this.screens.get(name);

        if (screen == null) {
            throw new ScreenNotFoundException(
                    "Couldnt found initialized screen '" + name + "', add screen with method addScreen() first.");
        }

        screen.onResume();

        this.activeScreens.push(screen);

        this.backActiveScreens.add(0, screen);

        Gdx.app.debug(TAG_SCREENS, "push screen: " + name);
    }

    @Override
    public void leaveAllAndEnter(String name) {
        // leave all active game states
        IScreen screen = pop();

        // pop and pause all active screens
        while (screen != null) {
            screen = pop();
        }

        // push new screen
        this.push(name);
    }

    @Override
    public IScreen pop() {
        IScreen screen = this.activeScreens.poll();

        if (screen != null) {
            Platform.runOnUIThread(() -> {
                //pause screen
                screen.onPause();
            });

            this.backActiveScreens.remove(screen);
        }

        Gdx.app.debug(TAG_SCREENS, "pop screen.");

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
    public boolean processInput() {
        for (IScreen screen : this.backActiveScreens) {
            if (screen.processInput()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void update() {
        for (IScreen screen : this.activeScreens) {
            screen.update();
        }
    }

    @Override
    public void draw() {
        for (IScreen screen : this.activeScreens) {
            screen.draw();
        }
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
