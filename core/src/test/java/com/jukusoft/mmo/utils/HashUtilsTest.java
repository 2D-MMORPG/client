package com.jukusoft.mmo.utils;

import org.junit.Test;

import java.io.File;

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

    @Test
    public void testComputeMD5FileHash () throws Exception {
        //assertEquals("cf4e96739d454bc2b9e4f2a6ffecb13d", HashUtils.computeMD5FileHash(new File("../test-file.txt")));
        assertEquals("6366346539363733396434353462633262396534663261366666656362313364", HashUtils.computeMD5FileHash(new File("../test-file.txt")));
    }

}
