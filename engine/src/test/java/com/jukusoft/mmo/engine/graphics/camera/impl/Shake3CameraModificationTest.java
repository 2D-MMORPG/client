package com.jukusoft.mmo.engine.graphics.camera.impl;

import com.jukusoft.mmo.engine.graphics.camera.CameraModification;
import com.jukusoft.mmo.engine.graphics.camera.ModificationFinishedListener;
import com.jukusoft.mmo.engine.graphics.camera.TempCameraParams;
import com.jukusoft.mmo.engine.utils.GameTime;
import com.jukusoft.mmo.engine.utils.TestGameTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Shake3CameraModificationTest {

    @Test
    public void testConstructor () {
        new Shake3CameraModification();
    }

    @Test
    public void testUpdate () throws InterruptedException {
        Shake3CameraModification mod = new Shake3CameraModification();

        assertEquals(false, mod.isShaking());

        mod.onUpdate(GameTime.getInstance(), null, null);

        //start shaking camera
        mod.shake(10, 200);

        assertEquals(true, mod.isShaking());

        TestGameTime time = new TestGameTime();
        time.setDelta(0.2f);

        TempCameraParams params = new TempCameraParams(0, 0, 1);
        mod.onUpdate(time, params, new ModificationFinishedListener() {
            @Override
            public <T extends CameraModification> void onModificationFinished(T mod, Class<T> cls) {

            }
        });

        Thread.sleep(500);

        //update again
        mod.onUpdate(time, params, new ModificationFinishedListener() {
            @Override
            public <T extends CameraModification> void onModificationFinished(T mod, Class<T> cls) {

            }
        });

        assertEquals("elapsed time is < 200: " + mod.elapsed, true, mod.elapsed >= 200);

        assertEquals(false, mod.isShaking());

        mod.startPermantentShake(5);

        assertEquals(true, mod.isShaking());

        //update again
        mod.onUpdate(GameTime.getInstance(), params, new ModificationFinishedListener() {
            @Override
            public <T extends CameraModification> void onModificationFinished(T mod, Class<T> cls) {

            }
        });

        assertEquals(true, mod.isShaking());

        mod.stopPermanentShake();

        //update again
        mod.onUpdate(GameTime.getInstance(), params, new ModificationFinishedListener() {
            @Override
            public <T extends CameraModification> void onModificationFinished(T mod, Class<T> cls) {

            }
        });

        assertEquals("camera mod is already shaking, elapsed: " + mod.elapsed + ", duration: " + mod.duration, false, mod.isShaking());

        mod.isActive = true;
        mod.elapsed = 200;
        mod.duration = 100;

        //update again
        mod.onUpdate(GameTime.getInstance(), params, new ModificationFinishedListener() {
            @Override
            public <T extends CameraModification> void onModificationFinished(T mod, Class<T> cls) {

            }
        });

        mod.isActive = false;

        assertEquals("camera mod is already shaking, elapsed: " + mod.elapsed + ", duration: " + mod.duration, false, mod.isShaking());

        //start shaking camera
        mod.shake(5, 100);

        //update again
        mod.onUpdate(GameTime.getInstance(), params, new ModificationFinishedListener() {
            @Override
            public <T extends CameraModification> void onModificationFinished(T mod, Class<T> cls) {

            }
        });

        //start permanent shake
        mod.startPermantentShake(4);

        for (int i = 0; i < 100; i++) {
            //update again
            mod.onUpdate(GameTime.getInstance(), params, new ModificationFinishedListener() {
                @Override
                public <T extends CameraModification> void onModificationFinished(T mod, Class<T> cls) {

                }
            });
        }

        assertEquals(true, mod.isShaking());

        mod.dispose();
    }

}
