package com.jukusoft.mmo.engine.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;
import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.ServiceManager;
import com.jukusoft.mmo.utils.Platform;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class BaseAppTest {

    protected static Application application = null;

    protected boolean created = false;

    @BeforeClass
    public static void beforeClass () {
        //http://manabreak.eu/java/2016/10/21/unittesting-libgdx.html

        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(new ApplicationListener() {
            @Override public void create() {}
            @Override public void resize(int width, int height) {}
            @Override public void render() {}
            @Override public void pause() {}
            @Override public void resume() {}
            @Override public void dispose() {}
        });

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;
    }

    @AfterClass
    public static void afterClass () {
        // Exit the application first
        application.exit();
        application = null;
    }

    @Test
    public void testConstructor () {
        new BaseApp() {
            @Override
            protected void createServices(ServiceManager serviceManager) {
                //
            }

            @Override
            protected void initScreens(ScreenManager<IScreen> manager) {

            }
        };
    }

    @Test
    public void testStartUp () throws InterruptedException {
        assertEquals(false, this.created);

        BaseApp app = new BaseApp() {
            @Override
            protected void createServices(ServiceManager serviceManager) {
                created = true;
            }

            @Override
            protected void initScreens(ScreenManager<IScreen> manager) {

            }
        };

        assertEquals(false, this.created);

        Platform.runOnUIThread(() -> {
            //
        });

        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(app);

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;

        //wait some ms
        Thread.sleep(200);

        assertEquals(true, this.created);

        application.exit();
    }

    @Test
    public void testResize () throws InterruptedException {
        this.created = false;

        assertEquals(false, this.created);

        BaseApp app = new BaseApp() {
            @Override
            protected void createServices(ServiceManager serviceManager) {
                created = true;
            }

            @Override
            protected void initScreens(ScreenManager<IScreen> manager) {

            }
        };

        assertEquals(false, this.created);

        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(app);

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;

        //wait some ms
        Thread.sleep(200);

        assertEquals(true, this.created);

        app.resize(800, 600);

        app.pause();

        app.resume();

        application.exit();
    }

    @Test
    public void testGetFPS () {
        BaseApp app = new BaseApp() {
            @Override
            protected void createServices(ServiceManager serviceManager) {
                created = true;
            }

            @Override
            protected void initScreens(ScreenManager<IScreen> manager) {
                //
            }
        };

        assertEquals(true, app.getFPS() >= 0);
    }

    @Test
    public void testRender () throws InterruptedException {
        BaseApp app = new BaseApp() {
            @Override
            protected void createServices(ServiceManager serviceManager) {
                created = true;
            }

            @Override
            protected void initScreens(ScreenManager<IScreen> manager) {
                //
            }
        };

        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(app);

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;

        app.lastFPS = 40;

        Thread.sleep(100);

        app.lastFPS = 60;

        Thread.sleep(100);

        application.exit();
    }

    @Test
    public void testRender1 () throws InterruptedException {
        BaseApp app = new BaseApp() {
            @Override
            protected void createServices(ServiceManager serviceManager) {
                created = true;
            }

            @Override
            protected void initScreens(ScreenManager<IScreen> manager) {
                //
            }

            @Override
            public int getFPS () {
                return 50;
            }
        };

        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(app);

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;

        app.lastFPS = 40;

        Thread.sleep(100);

        app.lastFPS = 60;

        Thread.sleep(100);

        application.exit();
    }

    @Test
    public void testRender2 () throws InterruptedException {
        BaseApp app = new BaseApp() {
            @Override
            protected void createServices(ServiceManager serviceManager) {
                created = true;
            }

            @Override
            protected void initScreens(ScreenManager<IScreen> manager) {
                //
            }

            @Override
            public int getFPS () {
                return 50;
            }
        };

        app.serviceManager = new ServiceManager() {
            @Override
            public <T extends IService> void addService(T service, Class<T> cls) {
                //
            }

            @Override
            public <T extends IService> void removeService(Class<T> cls) {
                //
            }

            @Override
            public <T extends IService> T getService(Class<T> cls) {
                return null;
            }

            @Override
            public Object getServiceObject(Class<?> type) {
                return null;
            }

            @Override
            public <T extends IService> boolean existsService(Class<T> cls) {
                return false;
            }

            @Override
            public void processInput() {
                //
            }

            @Override
            public void update() {
                //
            }

            @Override
            public void beforeDraw() {
                throw new RuntimeException("test exception");
            }

            @Override
            public void afterDraw() {
                //
            }

            @Override
            public void shutdown() {

            }
        };

        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(app);

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;

        Thread.sleep(100);

        app.lastFPS = 60;

        Thread.sleep(100);

        application.exit();
    }

    @Test
    public void testRender3 () throws InterruptedException {
        BaseApp app = new BaseApp() {
            @Override
            protected void createServices(ServiceManager serviceManager) {
                created = true;
            }

            @Override
            protected void initScreens(ScreenManager<IScreen> manager) {
                //
            }

            @Override
            public int getFPS () {
                return 50;
            }
        };

        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(app);

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;

        Thread.sleep(100);

        app.lastFPS = 60;

        Thread.sleep(100);

        application.exit();
    }

    @Test
    public void testRender4 () throws InterruptedException {
        BaseApp app = new BaseApp() {
            @Override
            protected void createServices(ServiceManager serviceManager) {
                created = true;
            }

            @Override
            protected void initScreens(ScreenManager<IScreen> manager) {
                //
            }

            @Override
            public int getFPS () {
                return 50;
            }
        };

        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(app);

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;

        Thread.sleep(100);

        app.screenManager = new ScreenManager<IScreen>() {

            protected int i = 0;

            @Override
            public void addScreen(String name, IScreen screen) {

            }

            @Override
            public void removeScreen(String name) {

            }

            @Override
            public void push(String name) {

            }

            @Override
            public void leaveAllAndEnter(String name) {

            }

            @Override
            public IScreen pop() {
                return null;
            }

            @Override
            public IScreen getScreenByName(String name) {
                return null;
            }

            @Override
            public Collection<IScreen> listScreens() {
                return null;
            }

            @Override
            public Collection<IScreen> listActiveScreens() {
                return null;
            }

            @Override
            public boolean processInput() {
                return (i++) % 2 == 0;
            }

            @Override
            public void update() {

            }

            @Override
            public void draw() {

            }

            @Override
            public void dispose() {

            }

        };

        app.lastFPS = 60;

        Thread.sleep(100);

        application.exit();
    }

}
