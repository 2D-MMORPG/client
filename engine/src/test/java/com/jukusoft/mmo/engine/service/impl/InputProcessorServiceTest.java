package com.jukusoft.mmo.engine.service.impl;

import com.badlogic.gdx.InputProcessor;
import com.jukusoft.mmo.engine.GameUnitTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InputProcessorServiceTest extends GameUnitTest {

    @Test
    public void testConstructor () {
        new InputProcessorService();
    }

    @Test
    public void testStartAndStop () {
        InputProcessorService service = new InputProcessorService();

        //start service
        service.onStart();

        //stop service
        service.onStop();
    }

    @Test
    public void testAddInputProcessor () {
        InputProcessorService service = new InputProcessorService();

        service.onStart();

        InputProcessor processor = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        };

        service.addInputProcessor(processor);

        assertEquals(true, service.inputMultiplexer.getProcessors().contains(processor, true));

        service.removeInputProcessor(processor);

        assertEquals(false, service.inputMultiplexer.getProcessors().contains(processor, true));

        service.onStop();
    }

}
