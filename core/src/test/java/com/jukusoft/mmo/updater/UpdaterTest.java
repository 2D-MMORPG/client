package com.jukusoft.mmo.updater;

import com.jukusoft.mmo.utils.FileUtils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
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

        if (new File("../my-test-file").exists()) {
            new File("../my-test-file").delete();
        }

        //override files
        FileUtils.writeFile("../junit-tests/my-file1.txt", "file1", StandardCharsets.UTF_8);
        FileUtils.writeFile("../junit-tests/my-file2.txt", "file2", StandardCharsets.UTF_8);
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

        if (new File("../my-test-file").exists()) {
            new File("../my-test-file").delete();
        }

        if (new File("my-test-file").exists()) {
            new File("my-test-file").delete();
        }

        //override files
        FileUtils.writeFile("../junit-tests/my-file1.txt", "file1", StandardCharsets.UTF_8);
        FileUtils.writeFile("../junit-tests/my-file2.txt", "file2", StandardCharsets.UTF_8);

        //reset version
        Version version = new Version();
        String content = FileUtils.readFile("../junit-tests/updater/version.json", StandardCharsets.UTF_8);
        version.load(new JsonObject(content));
        version.setBuildNumber(1);
        version.setBuildDate("12.12.2017");
        version.setBuildTime("09:38");
        version.setVersion("0.0.1-dev-preview");
        version.setFullVersion("0.0.1 Developer Preview Build 1");
        version.save(new File("../junit-tests/updater/version.json"));
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

        //prepare again
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
                Logger.getAnonymousLogger().log(Level.SEVERE, "Error: {0}", errorMessage);
            }

            @Override
            public void onFinish(String newVersion) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Finished! New version: {0}", newVersion);
            }
        });
    }

    @Test
    public void testStartUpdate1 () throws Exception {
        Updater updater = new Updater();
        updater.load("../junit-tests/updater", "../");
        updater.prepareFileHashes("../");

        //create new channel
        Channel channel = new Channel();
        channel.name = "test-channel";
        channel.title = "Test Channel";
        channel.newestBuildNumber = 0;
        channel.updateURL = "http://mmo.jukusoft.com/update/junit-test/files.json";

        updater.startUpdate(channel, new UpdateListener() {
            @Override
            public void onProgress(boolean finished, float progress, String message) {
                Logger.getAnonymousLogger().log(Level.INFO, "progress: " + progress + ", message: " + message);
            }

            @Override
            public void onError(String errorMessage) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Error: {0}", errorMessage);
            }

            @Override
            public void onFinish(String newVersion) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Finished! New version: {0}", newVersion);
            }
        });
    }

    @Test
    public void testStartUpdate2 () throws Exception {
        Updater updater = new Updater();
        updater.load("../junit-tests/updater", "../");
        updater.prepareFileHashes("../");

        //create new channel
        Channel channel = new Channel();
        channel.name = "test-channel";
        channel.title = "Test Channel";
        channel.newestBuildNumber = 2;
        channel.updateURL = "http://mmo.jukusoft.com/update/junit-test/files.json";

        updater.startUpdate(channel, new UpdateListener() {
            @Override
            public void onProgress(boolean finished, float progress, String message) {
                Logger.getAnonymousLogger().log(Level.INFO, "progress: " + progress + ", message: " + message);
            }

            @Override
            public void onError(String errorMessage) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Error: {0}", errorMessage);
            }

            @Override
            public void onFinish(String newVersion) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Finished! New version: {0}", newVersion);
            }
        });
    }

    @Test
    public void testStartUpdate3 () throws IOException {
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
                Logger.getAnonymousLogger().log(Level.SEVERE, "Error: {0}", errorMessage);
            }

            @Override
            public void onFinish(String newVersion) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Finished! New version: {0}", newVersion);
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

    @Test
    public void testDeleteBackupFiles () throws IOException {
        //create directory, if not exists
        if (!new File("../junit-tests/updater/backup/").exists()) {
            new File("../junit-tests/updater/backup/").mkdirs();
        }

        Updater updater = new Updater();
        updater.deleteBackupFiles("../junit-tests/updater/backup/");

        assertEquals(false, new File("../junit-tests/updater/backup/").exists());
        assertEquals(true, new File("../junit-tests/updater/").exists());
    }

    @Test
    public void testCreateBackupDirectoryIfAbsent () throws IOException {
        Updater updater = new Updater();
        updater.deleteBackupFiles("../junit-tests/updater/backup/");

        assertEquals(false, new File("../junit-tests/updater/backup/").exists());
        assertEquals(true, new File("../junit-tests/updater/").exists());

        updater.createBackupDirectoryIfAbsent("../junit-tests/updater/backup/");
        assertEquals(true, new File("../junit-tests/updater/backup/").exists());

        //create directory again
        updater.createBackupDirectoryIfAbsent("../junit-tests/updater/backup/");
        assertEquals(true, new File("../junit-tests/updater/backup/").exists());
    }

    @Test
    public void testBackupOldFiles () throws IOException {
        Updater updater = new Updater();

        List<String> changedList = new ArrayList<>();
        changedList.add("../junit-tests/my-backup-file.txt");
        changedList.add("../junit-tests/dir1/my-backup-file2.txt");

        updater.backupOldFiles(changedList, "../junit-tests/updater/backup/");

        //backup again (test override and path without slash)
        updater.backupOldFiles(changedList, "../junit-tests/updater/backup");

        //backup again (test override and path without slash)
        updater.backupOldFiles(changedList, "../junit-tests/updater/backup" + File.separator);

        assertEquals(true, new File("../junit-tests/updater/backup/junit-tests/my-backup-file.txt").exists());
        assertEquals(true, new File("../junit-tests/updater/backup/junit-tests/dir1/my-backup-file2.txt").exists());
    }

}
