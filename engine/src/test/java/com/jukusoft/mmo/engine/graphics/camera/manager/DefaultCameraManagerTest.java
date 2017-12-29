package com.jukusoft.mmo.engine.graphics.camera.manager;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.jukusoft.mmo.engine.utils.TestGameTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultCameraManagerTest {

    protected static Application application = null;

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
        DefaultCameraManager manager = new DefaultCameraManager(800, 600);

        assertNotNull(manager.getMainCamera());
        assertNotNull(manager.getUICamera());
    }

    @Test
    public void testGetCustomCamera () {
        DefaultCameraManager manager = new DefaultCameraManager(800, 600);

        assertEquals(0, manager.countCustomCameras());

        assertNotNull(manager.getCustomCamera(0));

        assertEquals(1, manager.countCustomCameras());

        assertNotNull(manager.getCustomCamera(0));

        assertEquals(1, manager.countCustomCameras());
    }

    @Test
    public void testMaxCustomCameras () {
        DefaultCameraManager manager = new DefaultCameraManager(800, 600);

        assertEquals(true, manager.maxCustomCameras() > 0);
    }

    @Test
    public void testUpdate () {
        DefaultCameraManager manager = new DefaultCameraManager(800, 600);

        //create new custom camera
        manager.getCustomCamera(0);

        TestGameTime time = new TestGameTime();
        time.setDelta(0.2f);

        for (int i = 0; i < 10; i++) {
            manager.update(time);
        }
    }

}
