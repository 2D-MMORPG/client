package com.jukusoft.mmo.updater;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UpdaterTest {

    @BeforeClass
    public static void beforeClass () throws IOException {
        if (new File("files.json").exists()) {
            Files.delete(new File("files.json").toPath());
        }

        if (new File("../updater/files.json").exists()) {
            Files.delete(new File("../updater/files.json").toPath());
        }

        if (new File("../junit-tests/files.json").exists()) {
            Files.delete(new File("../junit-tests/files.json").toPath());
        }
    }

    @AfterClass
    public static void afterClass () throws IOException {
        if (new File("files.json").exists()) {
            Files.delete(new File("files.json").toPath());
        }

        if (new File("../updater/files.json").exists()) {
            Files.delete(new File("../updater/files.json").toPath());
        }

        if (new File("../junit-tests/files.json").exists()) {
            Files.delete(new File("../junit-tests/files.json").toPath());
        }
    }

    @Test
    public void testConstructor () {
        new Updater();
    }

    @Test
    public void testLoadOwnVersion () throws IOException {
        Updater updater = new Updater();
        updater.loadOwnVersion("../updater/");
    }

    @Test
    public void testPrepareFileHashesNotExistentDir () throws Exception {
        Updater updater = new Updater();
        updater.updaterDir = "../junit-tests/updater/";
        updater.prepareFileHashes("../junit-tests/");
    }

    @Test
    public void testLoad () throws Exception {
        Updater updater = new Updater();
        updater.load("../updater/", "../");
    }

    @Test
    public void testLoad1 () throws Exception {
        Updater updater = new Updater();
        updater.load("../updater", "../");
    }

    @Test
    public void testInvalideFileHashes () throws Exception {
        Updater updater = new Updater();
        updater.updaterDir = "../junit-tests/updater/";

        //create files.json file
        updater.prepareFileHashes("../junit-tests/");

        updater.invalideFileHashes();

        //invalidate again
        updater.invalideFileHashes();
    }

    @Test
    public void testGetCurrentVersion () throws Exception {
        Updater updater = new Updater();
        updater.load("../junit-tests/updater", "../");

        assertNotNull(updater.getCurrentVersion());
        assertEquals("0.0.1-dev-preview", updater.getCurrentVersion().getVersion());
    }

    @Test
    public void testListChannels () throws Exception {
        Updater updater = new Updater();
        updater.load("../junit-tests/updater", "../");

        assertNotNull(updater.listChannels());
        assertEquals(true, updater.listChannels().size() > 0);
    }

    @Test (expected = NullPointerException.class)
    public void testStartUpdateNull () throws IOException {
        Updater updater = new Updater();
        updater.load("../junit-tests/updater", "../");

        updater.startUpdate(null, null);
    }

    @Test (expected = NullPointerException.class)
    public void testStartUpdateNull1 () throws IOException {
        Updater updater = new Updater();
        updater.load("../junit-tests/updater", "../");

        updater.startUpdate(updater.listChannels().get(0), null);
    }

    @Test
    public void testStartUpdate () throws IOException {
        Updater updater = new Updater();
        updater.load("../junit-tests/updater", "../");

        updater.startUpdate(updater.listChannels().get(0), new UpdateListener() {
            @Override
            public void onProgress(boolean finished, float progress) {

            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onFinish(String newVersion) {

            }
        });
    }

}
