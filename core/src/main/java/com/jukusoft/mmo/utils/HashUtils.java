/*
* Coypright (c) 2015 Justin Kuenzel
* Apache 2.0 License
*
* This file doesnt belongs to the Pentaquin Project.
* This class is owned by Justin Kuenzel and licensed under the Apache 2.0 license.
* Many projects use this class.
*/

package com.jukusoft.mmo.utils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Justin on 26.01.2015.
 */
public class HashUtils {

    protected static final Logger LOGGER = Logger.getLogger("HashUtils");
    protected static final String LOG_MESSAGE = "an exception was thrown";

    /**
     * private constructor, so other classes cannot create an instance of HashUtils
     */
    protected HashUtils () {
        //
    }

    /**
    * convert byte data to hex
     *
     * @deprecated because it isnt used anymore
    */
    @Deprecated
    private static String convertToHex(byte[] data) {
        //create new instance of string buffer
        StringBuilder stringBuffer = new StringBuilder();
        String hex = "";

        //encode byte data with base64
        hex = Base64.getEncoder().encodeToString(data);
        stringBuffer.append(hex);

        //return string
        return stringBuffer.toString();
    }

    /**
    * converts an byte array to an hex string
     *
     * @param data byte array
     *
     * @return hex string
    */
    public static String toHex (byte[] data) {
        StringBuilder hash = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            String h = Integer.toHexString(0xFF & data[i]);
            while (h.length() < 2)
                h = "0" + h;
            hash.append(h);
        }

        return hash.toString();
    }

    /**
     * generates SHA-512 Hash for passwords
     *
     * @param password text
     *
     * @return hash
     */
    public static String computePasswordSHAHash(String password) {
        MessageDigest mdSha1 = null;
        String shaHash = "";

        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e1) {
            LOGGER.log(Level.SEVERE, LOG_MESSAGE, e1);
            throw new RuntimeException("NoSuchAlgorithmException: " + e1.getLocalizedMessage());
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, LOG_MESSAGE, e);

            throw new RuntimeException("UnsupportedEncodingException: " + e.getLocalizedMessage());
        }
        byte[] data = mdSha1.digest();
        shaHash = convertToHex(data);

        return shaHash;
    }

    /**
    * generate an SHA256 hash compatible to PHP 5
     *
     * @param password password
     * @param salt salt
     *
     * @return sha hash
    */
    public static String computeSHA256Hash (String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] data = digest.digest((password + salt).getBytes("UTF-8"));
            return toHex(data);
        } catch (NoSuchAlgorithmException | IOException e) {
            LOGGER.log(Level.SEVERE, LOG_MESSAGE, e);
            return null;
        }
    }

    /**
     * generates MD5 hash
     *
     * This method is compatible to PHP 5 and Java 8.
     *
     * @param password text
     * @return hash
    */
    public static String computeMD5Hash(String password) {
        StringBuilder md5Hash = new StringBuilder();

        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            for (int i = 0; i < messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                md5Hash.append(h);
            }

        }
        catch (NoSuchAlgorithmException e)
        {
            LOGGER.log(Level.SEVERE, LOG_MESSAGE, e);
        }

        return md5Hash.toString();
    }

    /**
    * generates an MD5 file hash, like an file checksum
     *
     * @param file file
     * @return hash
    */
    public static String computeMD5FileHash (File file) throws Exception {
        byte[] b = createFileChecksum(file);
        String result = "";

        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    private static byte[] createFileChecksum(File file) throws Exception {
        InputStream fis = null;

        try {
            fis =  new FileInputStream(file);

            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;

            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            fis.close();
            return complete.digest();
        } catch (Exception e) {
            if (fis != null) {
                fis.close();
            }

            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

}
