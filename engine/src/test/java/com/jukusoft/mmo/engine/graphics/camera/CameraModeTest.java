package com.jukusoft.mmo.engine.graphics.camera;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CameraModeTest {

    @Test
    public void testEnum () {
        CameraMode mode = CameraMode.DIRECT_CAMERA;
        mode = CameraMode.FIXED_CAMERA;
        mode = CameraMode.MOUSE_CAMERA;
        mode = CameraMode.SCROLL_CAMERA_WITH_MAX_DISTANCE;
        mode = CameraMode.SMOOTH_CAMERA;

        mode = CameraMode.valueOf("DIRECT_CAMERA");
        assertEquals(true, mode != null);
    }

}
