package com.jukusoft.mmo.engine.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.jukusoft.mmo.engine.service.ServiceManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

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

}
