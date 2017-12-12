package com.jukusoft.mmo.updater;

import io.vertx.core.json.JsonObject;

/**
* data model for updater/version.json file
*/
public class Version {

    protected String version = "";
    protected String fullVersion = "";
    protected int buildNumber = -1;

    //build date & time
    protected String buildDate = "";
    protected String buildTime = "";

    //url to update channels on server
    protected String updateURL = "";

    /**
    * default constructor
    */
    public Version () {
        //
    }

    /**
    * load from json object
     *
     * @param json json object
    */
    public void load (JsonObject json) {
        if (!json.containsKey("version") || !json.containsKey("full_version") || !json.containsKey("build_number") || !json.containsKey("build_date") || !json.containsKey("build_time") || !json.containsKey("update_channel_url")) {
            throw new IllegalArgumentException("json object doesnt contains all required keys.");
        }

        //get values
        this.version = json.getString("version");
        this.fullVersion = json.getString("full_version");
        this.buildNumber = json.getInteger("build_number");
        this.buildDate = json.getString("build_date");
        this.buildTime = json.getString("build_time");
        this.updateURL = json.getString("update_channel_url");
    }

    public String getVersion() {
        return version;
    }

    public String getFullVersion() {
        return fullVersion;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public String getUpdateURL() {
        return updateURL;
    }
}
