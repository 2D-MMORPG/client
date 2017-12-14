package com.jukusoft.mmo.updater;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        if (new File("../junit-tests/updater/files.json").exists()) {
            Files.delete(new File("../junit-tests/updater/files.json").toPath());
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

        if (new File("../junit-tests/updater/files.json").exists()) {
            Files.delete(new File("../junit-tests/updater/files.json").toPath());
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

        //create new channel
        Channel channel = new Channel();
        channel.name = "test-channel";
        channel.title = "Test Channel";
        channel.newestBuildNumber = 1;
        channel.updateURL = "http://mmo.jukusoft.com/update/junit-test/files.json";

        updater.startUpdate(channel, new UpdateListener() {
            @Override
            public void onProgress(boolean finished, float progress, String message) {
                Logger.getAnonymousLogger().log(Level.INFO, "progress: " + progress + ", message: " + message);
            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onFinish(String newVersion) {

            }
        });
    }

    @Test (expected = NullPointerException.class)
    public void testConvertJsonArrayToMapNull () {
        Updater updater = new Updater();
        Map<String,String> map = updater.convertJsonArrayToMap(null);
    }

    @Test
    public void testConvertJsonArrayToMap () {
        JsonArray array = new JsonArray();

        JsonObject json = new JsonObject();
        json.put("file", "test-file.txt");
        json.put("checksum", "test");
        array.add(json);

        Updater updater = new Updater();
        Map<String,String> map = updater.convertJsonArrayToMap(array);

        assertEquals(1, map.size());
    }

    @Test (expected = NullPointerException.class)
    public void testGetChangedFilesNull () throws IOException {
        Updater updater = new Updater();
        updater.getChangedFiles(null);
    }

    @Test
    public void testGetChangedFiles () throws Exception {
        //create new channel
        Channel channel = new Channel();
        channel.name = "test-channel";
        channel.title = "Test Channel";
        channel.newestBuildNumber = 1;
        channel.updateURL = "http://mmo.jukusoft.com/update/junit-test/files.json";

        Updater updater = new Updater();
        updater.load("../junit-tests/updater", "../");
        updater.invalideFileHashes();
        updater.prepareFileHashes("../");
        List<String> list = updater.getChangedFiles(channel);

        assertEquals(2, list.size());
        assertEquals(true, list.contains("../my-test-file"));
        assertEquals(false, list.contains("../junit-tests/my-file1.txt"));
        assertEquals(true, list.contains("../junit-tests/my-file2.txt"));
    }

}
