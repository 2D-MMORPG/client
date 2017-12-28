package com.jukusoft.mmo.engine.graphics.camera;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TempCameraParamsTest {

    @Test
    public void testConstructor () {
        new TempCameraParams(1, 2, 3);
    }

    @Test
    public void testGetterAndSetter () {
        TempCameraParams params = new TempCameraParams(1, 2, 3);

        assertEquals(true, params.getX() == 1);
        assertEquals(true, params.getY() == 2);
        assertEquals(true, params.getZoom() == 3);

        params.setX(4);
        params.setY(5);
        params.setZoom(1);

        assertEquals(true, params.getX() == 4);
        assertEquals(true, params.getY() == 5);
        assertEquals(true, params.getZoom() == 1);

        params.setPosition(6, 7);

        assertEquals(true, params.getX() == 6);
        assertEquals(true, params.getY() == 7);
        assertEquals(true, params.getZoom() == 1);

        params.setZoom(2);

        assertEquals(true, params.getX() == 6);
        assertEquals(true, params.getY() == 7);
        assertEquals(true, params.getZoom() == 2);

        params.setZoom(1);

        assertEquals(true, params.getX() == 6);
        assertEquals(true, params.getY() == 7);
        assertEquals(true, params.getZoom() == 1);

        params.translate(10, 20);

        assertEquals(true, params.getX() == 16);
        assertEquals(true, params.getY() == 27);
        assertEquals(true, params.getZoom() == 1);

        params.reset(3, 2, 1);

        assertEquals(true, params.getX() == 3);
        assertEquals(true, params.getY() == 2);
        assertEquals(true, params.getZoom() == 1);
    }

}
