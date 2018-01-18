package com.jukusoft.mmo.network.config;

import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
* class to parse network configuration file
*/
public class NetConfig {

    //ini4j instance
    protected Ini ini = null;

    //network section in config file
    protected Profile.Section section = null;

    //delay times in milliseconds
    protected int receiveDelay = 0;
    protected int sendDelay = 0;

    /**
     * default constructor
     *
     * @param filePath path to config file
     *
     * @return network configuration
     */
    public NetConfig (String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("filePath cannot be null.");
        }

        if (filePath.isEmpty()) {
            throw new IllegalArgumentException("filePath is empty.");
        }

        //check, if file exists
        if (!new File(filePath).exists()) {
            throw new FileNotFoundException("network config file doesnt exists: " + new File(filePath).getAbsolutePath());
        }

        //create ini instance
        this.ini = new Ini(new File(filePath));

        //get section
        this.section = this.ini.get("Network");

        this.receiveDelay = getInt("receiveDelay", 0);
        this.sendDelay = getInt("sendDelay", 0);
    }

    protected int getInt (String key) {
        return Integer.parseInt(this.section.get(key));
    }

    protected int getInt (String key, int defaultValue) {
        return Integer.parseInt(this.section.getOrDefault(key, "" + defaultValue));
    }

    public int getReceiveDelay () {
        return this.receiveDelay;
    }

    public int getSendDelay () {
        return this.sendDelay;
    }

}
