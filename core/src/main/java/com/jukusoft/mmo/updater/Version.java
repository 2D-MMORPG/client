package com.jukusoft.mmo.updater;

import com.jukusoft.mmo.utils.FileUtils;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
* data model for updater/version.json file
*/
public class Version {

    protected static final String VERSION_STR = "version";
    protected static final String FULL_VERSION_STR = "full_version";
    protected static final String BUILD_NUMBER_STR = "build_number";
    protected static final String BUILD_DATE_STR = "build_date";
    protected static final String BUILD_TIME_STR = "build_time";
    protected static final String UPDATE_URL_STR = "update_channel_url";

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
        if (!json.containsKey(VERSION_STR) || !json.containsKey(FULL_VERSION_STR) || !json.containsKey(BUILD_NUMBER_STR) || !json.containsKey(BUILD_DATE_STR) || !json.containsKey(BUILD_TIME_STR) || !json.containsKey(UPDATE_URL_STR)) {
            throw new IllegalArgumentException("json object doesnt contains all required keys.");
        }

        //get values
        this.version = json.getString(VERSION_STR);
        this.fullVersion = json.getString(FULL_VERSION_STR);
        this.buildNumber = json.getInteger(BUILD_NUMBER_STR);
        this.buildDate = json.getString(BUILD_DATE_STR);
        this.buildTime = json.getString(BUILD_TIME_STR);
        this.updateURL = json.getString(UPDATE_URL_STR);
    }

    public void save (File file) throws IOException {
        //delete file, if exiss
        if (file.exists()) {
            Files.delete(file.toPath());
        }

        //create new file
        Files.createFile(file.toPath());

        //create json object
        JsonObject json = new JsonObject();
        json.put(VERSION_STR, this.version);
        json.put(FULL_VERSION_STR, this.fullVersion);
        json.put(BUILD_NUMBER_STR, this.buildNumber);
        json.put(BUILD_DATE_STR, this.buildDate);
        json.put(BUILD_TIME_STR, this.buildTime);
        json.put(UPDATE_URL_STR, this.updateURL);

        //convert json object to string
        String jsonStr = json.encodePrettily();

        FileUtils.writeFile(file.getAbsolutePath(), jsonStr, StandardCharsets.UTF_8);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFullVersion() {
        return fullVersion;
    }

    public void setFullVersion(String fullVersion) {
        this.fullVersion = fullVersion;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getUpdateURL() {
        return updateURL;
    }

    public void setUpdateURL(String updateURL) {
        this.updateURL = updateURL;
    }

}
