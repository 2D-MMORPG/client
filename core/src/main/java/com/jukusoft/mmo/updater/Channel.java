package com.jukusoft.mmo.updater;

import io.vertx.core.json.JsonObject;

/**
* Data model for update channel
*/
public class Channel {

    //data values
    protected String name = "";
    protected String title = "";

    //flag, if channel is activated and can be used
    protected boolean activated = false;

    //flag, if channel is public, so every user can login to server with normal credentials
    protected boolean publicChannel = false;

    //url to file hashes
    protected String updateURL = "";

    //newest build number
    protected int newestBuildNumber = -1;

    /**
    * default constructor
    */
    public Channel () {
        //
    }

    /**
    * load channel from json object
    */
    public void load (JsonObject json) {
        if (json == null) {
            throw new NullPointerException("json cannot be null.");
        }

        //get values
        this.name = json.getString("name");
        this.title = json.getString("title");
        this.activated = json.getBoolean("activated");
        this.publicChannel = json.getBoolean("public");
        this.updateURL = json.getString("update_url");
        this.newestBuildNumber = json.getInteger("newest_build");
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public boolean isActivated() {
        return activated;
    }

    public boolean isPublic() {
        return publicChannel;
    }

    public String getUpdateURL() {
        return updateURL;
    }

    public int getNewestBuildNumber () {
        return this.newestBuildNumber;
    }

}
