package com.jukusoft.mmo.updater;

import com.jukusoft.mmo.downloader.Downloader;
import com.jukusoft.mmo.utils.FileUtils;
import com.jukusoft.mmo.utils.HashUtils;
import com.jukusoft.mmo.utils.WebUtils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    //logger instance
    protected static final Logger LOGGER = Logger.getLogger("Updater");

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
        LOGGER.log(Level.INFO, "index directory (with file hashing): " + dir.getAbsolutePath() + " and save to '" + saveFile.getAbsolutePath() + "'.");

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

        LOGGER.log(Level.INFO, "indexing directory was successfully.");
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
            LOGGER.log(Level.WARNING, "Downgrade version from " + this.currentVersion.getBuildNumber() + " (\" + this.currentVersion.getFullVersion() + \") to build" + channel.getNewestBuildNumber());
        }

        listener.onProgress(false, 0.01f, "Find changes...");

        //get changed files
        List<String> changedFiles = this.getChangedFiles(channel);

        listener.onProgress(false, 0.05f, "download files...");

        //backup old files (which will be replaced)
        this.backupOldFiles(changedFiles, this.updaterDir + "backup");

        //download files
        this.downloadFiles(channel, changedFiles, listener);

        listener.onProgress(true, 1f, "Client was updated successfully");
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

                LOGGER.log(Level.INFO, "Updater has found new required file: {0}", file);

                continue;
            }

            String fileChecksum = localFiles.get(file);

            if (!checksum.equals(fileChecksum)) {
                //file was changed
                downloadFileList.add(file);

                LOGGER.log(Level.INFO, "Updater has found changed file: {0}", file);
            } else {
                LOGGER.log(Level.INFO, "Updater has found up to date file: {0}", file);
            }
        }

        return downloadFileList;
    }

    /**
    * copy files to updater/backup directory
    */
    protected void backupOldFiles (List<String> changedFiles, String backupDir) throws IOException {
        //check, that backup directory path ends with /
        if (!backupDir.endsWith(File.separator)) {
            backupDir += File.separator;
        }

        LOGGER.log(Level.INFO, "check backup directory: {0}", backupDir);

        //create updater directory, if not exists
        if (!new File(backupDir).exists()) {
            LOGGER.log(Level.INFO, "Create backup directory: {0}", new File(backupDir).getAbsolutePath());

            new File(backupDir).mkdirs();
        }

        //clear old backup files
        this.deleteBackupFiles(backupDir);

        //create backup directory
        this.createBackupDirectoryIfAbsent(backupDir);

        //copy files to backup directory
        for (String filePath : changedFiles) {
            //check, if file exists
            if (!new File(filePath).exists()) {
                //this file will be created, but doesnt exists yet
                continue;
            }

            String path = filePath.replace("\\", "/").replace("../", "/");

            Path sourcePath = new File(filePath).toPath();
            Path targetPath = new File(backupDir + path).toPath();

            File dir = new File(FileUtils.getDirectoryOfFilePath(backupDir + path));

            //create parent directory of file, if neccessary
            if (!dir.exists()) {
                LOGGER.log(Level.INFO, "create backup directory: {0}", dir.getAbsolutePath());
                dir.mkdirs();
            } else {
                LOGGER.log(Level.INFO, "backup directory already exists: {0}", dir.getAbsolutePath());
            }

            LOGGER.log(Level.INFO, "Copy file {0} to backup directory: {1}", new String[]{sourcePath.toString(), targetPath.toString()});

            //copy file
            Files.copy(sourcePath, targetPath);
        }
    }

    /**
    * delete all backup files including backup directory
     *
     * @param backupDir path to backup directory
    */
    protected void deleteBackupFiles (String backupDir) throws IOException {
        FileUtils.recursiveDeleteDirectory(new File(backupDir));
    }

    /**
    * create backup directory, if not exists
     *
     * @param backupDir path to backup directory
    */
    protected void createBackupDirectoryIfAbsent (String backupDir) {
        //create directory, if not exists
        if (!new File(backupDir).exists()) {
            new File(backupDir).mkdirs();
        }
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

    protected void downloadFile (String file, String baseURL) throws IOException {
        //remove ../ in file path
        String fileURL = file.replace("\\", "/").replace("../", "");

        String downloadURL = baseURL + fileURL;

        LOGGER.log(Level.INFO, "Download file {0} --> {1}", new String[]{downloadURL, file});

        File targetFile = new File(file);

        //delete file, if exists
        if (targetFile.exists()) {
            Files.delete(targetFile.toPath());
        }

        Downloader downloader = new Downloader();
        downloader.startDownload(downloadURL, targetFile);

        //wait while other thread is downloading
        while (downloader.isDownloading()) {
            Thread.yield();
        }
    }

    protected void downloadFiles (Channel channel, List<String> changedFiles, UpdateListener listener) throws IOException {
        //get file list of new update
        String content = WebUtils.readContentFromWebsite(channel.getUpdateURL());
        JsonObject json = new JsonObject(content);

        //get downlaod base url
        String baseURL = json.getString("base_download_url");

        //0.05 is already used by changed files detection, see startUpdate()
        float percentagePerFile = 0.95f / changedFiles.size();
        float percentage = 0.05f;

        //download all files
        for (String file : changedFiles) {
            //call listener
            listener.onProgress(false, percentage, "Download: " + file);

            //download and replace file
            this.downloadFile(file, baseURL);

            percentage += percentagePerFile;
        }

        //all files are downloaded
    }

}
