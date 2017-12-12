package com.jukusoft.mmo.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HashUtilsTest {

    @Test
    public void testConstructor () {
        new HashUtils();
    }

    @Test
    public void testHashSHA256 () {
        String password = "hello";
        String salt = "";

        //expected hash (generated with PHP SHA 256)
        String expectedHash = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"; //"2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e730";

        assertEquals(expectedHash.length(), HashUtils.computeSHA256Hash(password, salt).length());
        assertEquals(expectedHash, HashUtils.computeSHA256Hash(password, salt));
    }

    @Test
    public void testToHex () {
        assertEquals("00", HashUtils.toHex(new byte[] {0x00}));
        assertEquals("01020304", HashUtils.toHex(new byte[] {0x01, 0x02, 0x03, 0x04}));
    }

    @Test
    public void testComputePasswordSHAHash () {
        assertEquals("7iaw3Ur350mqGo7jwQrpkj9hiYB3Lkc/iBml1JQODbJ6wYX4oOHV+E+IvIh/1nsUNzLDBMxfqa2Ob1f1ACio/w==", HashUtils.computePasswordSHAHash("test"));
    }

    @Test
    public void testComputeSHA256Hash () {
        assertEquals("4edf07edc95b2fdcbcaf2378fd12d8ac212c2aa6e326c59c3e629be3039d6432", HashUtils.computeSHA256Hash("test", "salt"));
    }

    @Test
    public void testComputeMD5Hash () {
        assertEquals("098f6bcd4621d373cade4e832627b4f6", HashUtils.computeMD5Hash("test"));
    }

    @Test (expected = NullPointerException.class)
    public void testComputeNullFileHash () throws Exception {
        HashUtils.computeMD5FileHash(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testComputeNullFileHashOfDirectory () throws Exception {
        HashUtils.computeMD5FileHash(new File("../junit-tests"));
    }

    @Test (expected = IOException.class)
    public void testComputeNullFileHashOfNotExistentDirectory () throws Exception {
        HashUtils.computeMD5FileHash(new File("../junit-tests2"));
    }

    @Test
    public void testComputeMD5FileHash () throws Exception {
        //old value: cf4e96739d454bc2b9e4f2a6ffecb13d
        assertEquals("ca09c4fbc809b36b6075da4a129dec20", HashUtils.computeMD5FileHash(new File("../test-file.txt")));
    }

    @Test (expected = NullPointerException.class)
    public void testListFileHashesOfNullDirectory () throws Exception {
        HashUtils.listFileHashesOfDirectory(null, new File("."));
    }

    @Test (expected = NullPointerException.class)
    public void testListFileHashesOfNullDirectory1 () throws Exception {
        HashUtils.listFileHashesOfDirectory(new File("."), null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testListFileHashesOfNotExistentDirectory () throws Exception {
        HashUtils.listFileHashesOfDirectory(new File("not-existent-directory"), new File("."));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testListFileHashesOfNotExistentDirectory1 () throws Exception {
        HashUtils.listFileHashesOfDirectory(new File("."), new File("not-existent-directory"));
    }

    @Test
    public void testListFileHashesOfDirectory () throws Exception {
        Map<String,String> hashes = HashUtils.listFileHashesOfDirectory(new File(new File("../junit-tests/file-hashes").getAbsolutePath()), new File("."));

        assertEquals(8, hashes.size());

        //print hashes
        for (Map.Entry<String,String> entry : hashes.entrySet()) {
            System.out.println("[file=" + entry.getKey() + "] hash: " + entry.getValue());

            assertEquals(false, entry.getKey().startsWith(".\\"));
            assertEquals(false, entry.getKey().startsWith("./"));

            //check, that its not an directory
            assertEquals(false, entry.getKey().endsWith("/"));
        }
    }

    @Test
    public void testListFileHashesOfDirectoryWithUpperBaseDir () throws Exception {
        Map<String,String> hashes = HashUtils.listFileHashesOfDirectory(new File(new File("../junit-tests/file-hashes").getAbsolutePath()), new File("../"));

        assertEquals(8, hashes.size());

        //print hashes
        for (Map.Entry<String,String> entry : hashes.entrySet()) {
            System.out.println("[file=" + entry.getKey() + "] hash: " + entry.getValue());

            assertEquals(false, entry.getKey().startsWith(".\\"));
            assertEquals(false, entry.getKey().startsWith("./"));

            //check, that its not an directory
            assertEquals(false, entry.getKey().endsWith("/"));
        }
    }

}
