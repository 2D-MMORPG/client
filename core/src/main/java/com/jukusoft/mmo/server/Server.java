package com.jukusoft.mmo.server;

import io.vertx.core.json.JsonObject;

public class Server {

    //meta data of server
    protected String name = "";
    protected String description = "";
    protected String channel = "";
    protected String imageURL = "";

    //server data
    protected String proxyIP = "";
    protected int proxyPort = 2600;

    public Server () {
        //
    }

    public void load (JsonObject json) {
        if (json == null) {
            throw new NullPointerException("json cannot be null.");
        }

        //check keys
        if (!json.containsKey("name") || !json.containsKey("description") || !json.containsKey("channel") || !json.containsKey("proxy_ip") || !json.containsKey("proxy_port") || !json.containsKey("image_url")) {
            throw new IllegalArgumentException("json doesnt contains all neccessary keys.");
        }

        this.name = json.getString("name");
        this.description = json.getString("description");
        this.channel = json.getString("channel");
        this.proxyIP = json.getString("proxy_ip");
        this.proxyPort = json.getInteger("proxy_port");
        this.imageURL = json.getString("image_url");
    }

    public String getName () {
        return this.name;
    }

    public String getDescription () {
        return this.description;
    }

    public String getChannel () {
        return this.channel;
    }

    public String getImageURL () {
        return this.imageURL;
    }

    public String getProxyIP () {
        return this.proxyIP;
    }

    public int getProxyPort () {
        return this.proxyPort;
    }

}
