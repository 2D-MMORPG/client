package com.jukusoft.mmo.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtilsTest {

    @Test
    public void testConstructor () {
        new FileUtils();
    }

    @Test(expected = NullPointerException.class)
    public void testWriteFileConstructor () throws IOException {
        FileUtils.writeFile(null, "", StandardCharsets.UTF_8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteFileConstructor1 () throws IOException {
        FileUtils.writeFile("", "", StandardCharsets.UTF_8);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteFileConstructor2 () throws IOException {
        FileUtils.writeFile("./test.txt", null, StandardCharsets.UTF_8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteFileConstructor3 () throws IOException {
        FileUtils.writeFile("./test.txt", "", StandardCharsets.UTF_8);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteFileConstructor4 () throws IOException {
        FileUtils.writeFile("./test.txt", "test", null);
    }

}
