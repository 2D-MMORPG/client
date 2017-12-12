package com.jukusoft.mmo.updater;

import com.jukusoft.mmo.utils.FileUtils;
import com.jukusoft.mmo.utils.HashUtils;
import com.jukusoft.mmo.utils.WebUtils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Updater {

    protected String updateChannelsURL = "";

    //current version
    protected Version currentVersion = null;

    protected String updaterDir = "";

    //list with all update channels
    protected List<Channel> channelList = new ArrayList<>();

    protected static final String FILES_JSON_FILE = "files.json";

    /**
    * default constructor
    */
    public Updater () {
        //
    }

    /**
    * load updater
     *
     * @throws IOException if I/O error occurs
    */
    public void load (String updaterDir, String baseDir) throws Exception {
        if (!updaterDir.endsWith("/")) {
            updaterDir = updaterDir + "/";
        }

        this.updaterDir = updaterDir;

        //load own version and get update url
        this.loadOwnVersion(updaterDir);

        //load update channels from server
        this.loadUpdateChannelsFromServer();

        //prepare file hashes, if neccessary
        this.prepareFileHashes(baseDir);
    }

    /**
    * load own installed version
     *
     * @throws IOException if file couldnt be loaded
    */
    protected void loadOwnVersion (String updaterDir) throws IOException {
        //read file content
        String content = FileUtils.readFile(updaterDir + "version.json", StandardCharsets.UTF_8);

        //create json object from json string
        JsonObject json = new JsonObject(content);

        //create & load data model
        this.currentVersion = new Version();
        this.currentVersion.load(json);

        //get channel update url
        this.updateChannelsURL = this.currentVersion.getUpdateURL();
    }

    protected void prepareFileHashes (String baseDir) throws Exception {
        File f = new File(this.updaterDir + FILES_JSON_FILE);

        if (!f.exists()) {
            //generate file hashes
            generateFileHashes(f, new File(baseDir));
        }
    }

    protected void generateFileHashes (File saveFile, File dir) throws Exception {
        //generate file hashes of current directory
        Map<String,String> hashes = HashUtils.listFileHashesOfDirectory(dir, new File("."));

        //create new json object
        JsonObject json = new JsonObject();

        //create new json array
        JsonArray array = new JsonArray();

        //iterate through all file hashes
        for (Map.Entry<String,String> entry : hashes.entrySet()) {
            String relFilePath = entry.getKey();
            String fileChecksum = entry.getValue();

            //create new json object
            JsonObject json1 = new JsonObject();

            //put data
            json1.put("file", relFilePath);
            json1.put("checksum", fileChecksum);

            //add object to array
            array.add(json1);
        }

        //put array to json object
        json.put("files", array);

        //save json object
        FileUtils.writeFile(saveFile.getAbsolutePath(), json.encode(), StandardCharsets.UTF_8);
    }

    protected void loadUpdateChannelsFromServer () throws IOException {
        //read update channels file from server
        String content = WebUtils.readContentFromWebsite(updateChannelsURL);

        //create json object from json string
        JsonObject json = new JsonObject(content);

        //get array
        JsonArray channels = json.getJsonArray("channels");

        //iterate through channels
        for (int i = 0; i < channels.size(); i++) {
            //get json object of channel
            JsonObject channelJson = channels.getJsonObject(i);

            //create & load channel
            Channel channel = new Channel();
            channel.load(channelJson);

            //add channel to list
            this.channelList.add(channel);
        }
    }

    protected void invalideFileHashes () throws IOException {
        if (new File(this.updaterDir + FILES_JSON_FILE).exists()) {
            Files.delete(new File(this.updaterDir + FILES_JSON_FILE).toPath());
        }
    }

    public Version getCurrentVersion() {
        return this.currentVersion;
    }
}
