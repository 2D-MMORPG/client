package com.jukusoft.mmo.updater;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class UpdaterTest {

    @BeforeClass
    public static void beforeClass () {
        //
    }

    @AfterClass
    public static void afterClass () throws IOException {
        Files.delete(new File("files.json").toPath());
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
        updater.prepareFileHashes("../junit-tests/updater/");
    }

    @Test
    public void testLoad () throws Exception {
        Updater updater = new Updater();
        updater.load("../updater/", "../");
    }

}
