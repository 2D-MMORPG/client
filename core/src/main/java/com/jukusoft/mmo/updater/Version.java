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

    public void save (File file) throws IOException {
        //delete file, if exiss
        if (file.exists()) {
            Files.delete(file.toPath());
        }

        //create new file
        Files.createFile(file.toPath());

        //create json object
        JsonObject json = new JsonObject();
        json.put("version", this.version);
        json.put("full_version", this.fullVersion);
        json.put("build_number", this.buildNumber);
        json.put("build_date", this.buildDate);
        json.put("build_time", this.buildTime);
        json.put("update_channel_url", this.updateURL);

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
