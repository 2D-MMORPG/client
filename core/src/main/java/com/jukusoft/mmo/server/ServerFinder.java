package com.jukusoft.mmo.server;

import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ServerFinder {

    //ini4j instance
    protected Ini ini = null;

    //server section in config file
    protected Profile.Section section = null;

    /**
    * default constructor
     *
     * @param filePath path to configuration file
    */
    public ServerFinder (String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("filePath cannot be null.");
        }

        if (filePath.isEmpty()) {
            throw new IllegalArgumentException("filePath is empty.");
        }

        //check, if file exists
        if (!new File(filePath).exists()) {
            throw new FileNotFoundException("config file doesnt exists: " + new File(filePath).getAbsolutePath());
        }

        //create ini instance
        this.ini = new Ini(new File(filePath));

        //get section
        this.section = this.ini.get("Server");
    }

    public String getServerListURL () {
        return this.section.get("serverlist");
    }

}
