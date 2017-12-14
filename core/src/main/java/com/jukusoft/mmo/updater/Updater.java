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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Updater {

    //some string constants
    protected static final String FILE_STR = "file";
    protected static final String FILES_STR = "files";
    protected static final String CHECKSUM_STR = "checksum";

    protected String updateChannelsURL = "";

    //current version
    protected Version currentVersion = null;

    protected String updaterDir = "";
    protected String baseDir = "";

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
    public void load (String updaterDir, String baseDir) throws IOException {
        if (!updaterDir.endsWith("/")) {
            updaterDir = updaterDir + "/";
        }

        this.baseDir = baseDir;

        this.updaterDir = updaterDir;

        //load own version and get update url
        this.loadOwnVersion(updaterDir);

        //load update channels from server
        this.loadUpdateChannelsFromServer();

        //prepare file hashes, if neccessary this.prepareFileHashes(baseDir);
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

    public void prepareFileHashes (String baseDir) throws Exception {
        File f = new File(this.updaterDir + FILES_JSON_FILE);

        if (!f.exists()) {
            //generate file hashes
            generateFileHashes(f, new File(baseDir));
        }
    }

    protected void generateFileHashes (File saveFile, File dir) throws Exception {
        Logger.getAnonymousLogger().log(Level.INFO, "index directory (with file hashing): " + dir.getAbsolutePath() + " and save to '" + saveFile.getAbsolutePath() + "'.");

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
            json1.put(FILE_STR, relFilePath);
            json1.put(CHECKSUM_STR, fileChecksum);

            //add object to array
            array.add(json1);
        }

        //put array to json object
        json.put(FILES_STR, array);

        //save json object
        FileUtils.writeFile(saveFile.getAbsolutePath(), json.encodePrettily(), StandardCharsets.UTF_8);

        Logger.getAnonymousLogger().log(Level.INFO, "indexing directory was successfully.");
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

    public void invalideFileHashes () throws IOException {
        if (new File(this.updaterDir + FILES_JSON_FILE).exists()) {
            Files.delete(new File(this.updaterDir + FILES_JSON_FILE).toPath());
        }
    }

    public Version getCurrentVersion() {
        return this.currentVersion;
    }

    public List<Channel> listChannels () {
        return this.channelList;
    }

    /**
    * start update
    */
    public void startUpdate (Channel channel, UpdateListener listener) throws IOException {
        if (channel == null) {
            throw new NullPointerException("channel cannot be null.");
        }

        if (listener == null) {
            throw new NullPointerException("listener cannot be null.");
        }

        //check, if current version is equals to new version
        if (channel.getNewestBuildNumber() == this.currentVersion.getBuildNumber()) {
            //we dont need to update anything

            listener.onFinish(this.currentVersion.getFullVersion());
            return;
        }

        if (channel.getNewestBuildNumber() < this.currentVersion.getBuildNumber()) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Downgrade version from " + this.currentVersion.getBuildNumber() + " (\" + this.currentVersion.getFullVersion() + \") to build" + channel.getNewestBuildNumber());
        }

        listener.onProgress(false, 0.01f, "Find changes...");

        //get changed files
        List<String> changedFiles = this.getChangedFiles(channel);

        listener.onProgress(false, 0.05f, "download files...");

        //TODO: backup old files (which will be replaced)

        //TODO: download files

        listener.onProgress(true, 1f, "Client was updated successfully.");
        listener.onFinish(channel.getNewestFullVersion());

        //TODO: write newest version
    }

    /**
    * get a list with all files, which was added or changed in new version
     *
     * @param channel update channel
     *
     * @return list with all files, which was changed in new version
    */
    public List<String> getChangedFiles (Channel channel) throws IOException {
        if (channel == null) {
            throw new NullPointerException("channel cannot be null.");
        }

        //get file list of new update
        String content = WebUtils.readContentFromWebsite(channel.getUpdateURL());
        JsonObject json = new JsonObject(content);
        JsonArray fileArray = json.getJsonArray(FILES_STR);

        //get file list of current version
        String content1 = FileUtils.readFile(updaterDir + FILES_JSON_FILE, StandardCharsets.UTF_8);
        JsonObject json1 = new JsonObject(content1);
        JsonArray localFilesArray = json1.getJsonArray(FILES_STR);

        //convert json array to map with files & checksum
        Map<String,String> localFiles = this.convertJsonArrayToMap(localFilesArray);

        //create new list with files to download
        List<String> downloadFileList = new ArrayList<>();

        //compare files
        for (int i = 0; i < fileArray.size(); i++) {
            JsonObject fileJson = fileArray.getJsonObject(i);

            String file = fileJson.getString(FILE_STR);
            String checksum = fileJson.getString(CHECKSUM_STR);

            if (!localFiles.containsKey(file)) {
                //file not found --> download file
                downloadFileList.add(file);

                Logger.getAnonymousLogger().log(Level.INFO, "Updater has found new required file: " + file);

                continue;
            }

            String fileChecksum = localFiles.get(file);

            if (!checksum.equals(fileChecksum)) {
                //file was changed
                downloadFileList.add(file);
            }
        }

        return downloadFileList;
    }

    protected Map<String,String> convertJsonArrayToMap (JsonArray array) {
        if (array == null) {
            throw new NullPointerException("array cannot be null.");
        }

        //create new map
        Map<String,String> map = new HashMap<>();

        for (int i = 0; i < array.size(); i++) {
            //get json object
            JsonObject json = array.getJsonObject(i);

            String file = json.getString(FILE_STR);
            String checksum = json.getString(CHECKSUM_STR);

            map.put(file, checksum);
        }

        return map;
    }

}
