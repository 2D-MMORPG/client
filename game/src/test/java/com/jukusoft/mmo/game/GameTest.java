package com.jukusoft.mmo.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.jukusoft.mmo.engine.service.impl.SpriteBatchService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class GameTest extends GameUnitTest {

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
        new Game();
    }

    //@Test
    public void testGameStartUp () throws InterruptedException {
        SpriteBatchService.isJUnitTest = true;

        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(new Game());

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;

        Thread.sleep(200);

        SpriteBatchService.isJUnitTest = false;
    }

}
