package com.jukusoft.mmo.utils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

    @BeforeClass
    public static void beforeClass () {
        //
    }
    @AfterClass
    public static void afterClass () {
        //delete file, if file exists
        if (new File("./test2.txt").exists()) {
            //delete temporary file
            new File("./test2.txt").delete();
        }

        //delete test file, if exists
        if (new File("junit-test-file.txt").exists()) {
            new File("junit-test-file.txt").delete();
        }

        //delete test file, if exists
        if (new File("null").exists()) {
            new File("null").delete();
        }
    }

    @Test
    public void testConstructor () {
        new FileUtils();
    }

    @Test(expected = NullPointerException.class)
    public void testWrite () throws IOException {
        FileUtils.writeFile(null, "", StandardCharsets.UTF_8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrite1 () throws IOException {
        FileUtils.writeFile("", "", StandardCharsets.UTF_8);
    }

    @Test(expected = NullPointerException.class)
    public void testWrite2 () throws IOException {
        FileUtils.writeFile("./test.txt", null, StandardCharsets.UTF_8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrite3 () throws IOException {
        FileUtils.writeFile("./test.txt", "", StandardCharsets.UTF_8);
    }

    @Test(expected = NullPointerException.class)
    public void testWrite4 () throws IOException {
        FileUtils.writeFile("./test.txt", "test", null);
    }

    @Test
    public void testWrite5 () throws IOException {
        //write temporary file
        FileUtils.writeFile("./test.txt", "test", StandardCharsets.UTF_8);

        //check, if file exists
        assertEquals(true, new File("./test.txt").exists());

        //delete temporary file
        new File("./test.txt").delete();
    }

    @Test
    public void testWriteAndRead () throws IOException {
        //write temporary file
        FileUtils.writeFile("./test2.txt", "test2", StandardCharsets.UTF_8);

        //read temporary file
        String content = FileUtils.readFile("./test2.txt", StandardCharsets.UTF_8);

        //delete temporary file
        new File("./test2.txt").delete();

        //compare content
        assertEquals("test2", content);
    }

    @Test
    public void testOverride () throws IOException {
        //write temporary file
        FileUtils.writeFile("./test.txt", "test", StandardCharsets.UTF_8);

        //write same temporary file again
        FileUtils.writeFile("./test.txt", "test2", StandardCharsets.UTF_8);

        //read temporary file
        String content = FileUtils.readFile("./test.txt", StandardCharsets.UTF_8);

        //delete temporary file
        new File("./test.txt").delete();

        //compare content
        assertEquals("test2", content);
    }

    @Test(expected = NullPointerException.class)
    public void testReadFileNullPath () throws IOException {
        FileUtils.readFile(null, StandardCharsets.UTF_8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadFileEmptyPath () throws IOException {
        FileUtils.readFile("", StandardCharsets.UTF_8);
    }

    @Test(expected = IOException.class)
    public void testReadFileFileNotExists () throws IOException {
        FileUtils.readFile("./not-existent-file.txt", StandardCharsets.UTF_8);
    }

    /*@Test (expected = IOException.class)
    public void testReadFileCannotRead () throws Exception {
        //delete test file, if exists
        if (new File("junit-test-file.txt").exists()) {
            new File("junit-test-file.txt").delete();
        }

        //create new file and make it not readable
        File file = new File("junit-test-file.txt");
        file.createNewFile();
        file.setReadable(false);

        //read file
        FileUtils.readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);
    }*/

    @Test(expected = NullPointerException.class)
    public void testReadLinesNullPath () throws IOException {
        FileUtils.readLines(null, StandardCharsets.UTF_8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadLinesEmptyPath () throws IOException {
        FileUtils.readLines("", StandardCharsets.UTF_8);
    }

    @Test(expected = IOException.class)
    public void testReadLinesFileNotExists () throws IOException {
        FileUtils.readLines("./not-existent-file.txt", StandardCharsets.UTF_8);
    }

    @Test
    public void testReadLines () throws IOException {
        //write temporary file
        FileUtils.writeFile("./test2.txt", "test2", StandardCharsets.UTF_8);

        //read lines as list
        List<String> lines = FileUtils.readLines("./test2.txt", StandardCharsets.UTF_8);

        //delete temporary file
        new File("./test2.txt").delete();

        //there should be only one line in file
        assertEquals(1, lines.size());
        assertEquals("test2", lines.get(0));
    }

    @Test
    public void testReadLines2 () throws IOException {
        //write temporary file
        FileUtils.writeFile("./test2.txt", "test2\nline2", StandardCharsets.UTF_8);

        //read lines as list
        List<String> lines = FileUtils.readLines("./test2.txt", StandardCharsets.UTF_8);

        //delete temporary file
        new File("./test2.txt").delete();

        //there should be only one line in file
        assertEquals(2, lines.size());
        assertEquals("test2", lines.get(0));
        assertEquals("line2", lines.get(1));
    }

    @Test (expected = NullPointerException.class)
    public void testRecursiveDeleteNullDirectory () throws IOException {
        FileUtils.recursiveDeleteDirectory(null);
    }

    @Test
    public void testRecursiveDeleteNotExistentDirectory () throws IOException {
        FileUtils.recursiveDeleteDirectory(new File("not-existent-directory"));
    }

    @Test
    public void testRecursiveDeleteDirectory () throws IOException {
        AppUtils.setAppName("junit-test");

        //create cache directory
        CacheUtils.createCacheDirIfAbsent();
        CacheUtils.createCacheDirIfAbsent("my-cache2");

        //write temporary file
        FileUtils.writeFile(CacheUtils.getCacheDir("my-cache2") + "my-test-file.txt", "test2\nline2", StandardCharsets.UTF_8);

        File f = new File(CacheUtils.getCachePath());

        FileUtils.recursiveDeleteDirectory(f);

        //check, if file exists
        assertEquals(false, f.exists());
    }

    @Test (expected = NullPointerException.class)
    public void testRemoveDoubleDotInNullDir () {
        FileUtils.removeDoubleDotInDir(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRemoveDoubleDotInEmptyDir () {
        FileUtils.removeDoubleDotInDir("");
    }

    @Test
    public void testRemoveDoubleDotInDir () {
        assertEquals("C:/Users/my-user1/test/", FileUtils.removeDoubleDotInDir("C:/Users/my-users/../my-user1/test"));
        assertEquals("test/test-dir2/", FileUtils.removeDoubleDotInDir("test/test-dir1/../test-dir2"));
    }

    @Test (expected = NullPointerException.class)
    public void testGetRelativeFileNullPath () throws IOException {
        FileUtils.getRelativeFile(null, new File("."));
    }

    @Test (expected = NullPointerException.class)
    public void testGetRelativeFileNullPath1 () throws IOException {
        FileUtils.getRelativeFile(new File("."), null);
    }

    @Test
    public void testGetRelativeFile () throws IOException {
        assertEquals("c/d", FileUtils.getRelativeFile(new File("/a/b/c/d/"), new File("/a/b/")).getPath().replace("\\", "/"));
        assertEquals("../c/d", FileUtils.getRelativeFile(new File("/a/b/../c/d/"), new File("/a/b/")).getPath().replace("\\", "/"));

        assertEquals("c/d", FileUtils.getRelativeFile(new File("/a/b/c/d/"), new File("/a/../a/b")).getPath().replace("\\", "/"));
    }

}
