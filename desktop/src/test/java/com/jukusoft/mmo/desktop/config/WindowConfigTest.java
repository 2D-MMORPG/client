package com.jukusoft.mmo.desktop.config;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.junit.Test;

import java.io.IOException;

public class WindowConfigTest {

    @Test
    public void testConstructor () throws IOException {
        new WindowConfig("../data/config/junit-window.cfg");
    }

    @Test (expected = NullPointerException.class)
    public void testNullConstructor () throws IOException {
        new WindowConfig(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testEmptyConstructor () throws IOException {
        new WindowConfig("");
    }

    @Test (expected = IOException.class)
    public void testNotExistentFileConstructor () throws IOException {
        new WindowConfig("not-existent-file.cfg");
    }

    @Test
    public void testFillConfig () throws IOException {
        WindowConfig windowConfig = new WindowConfig("../data/config/junit-window.cfg");

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        windowConfig.fillConfig(config);
    }

}
