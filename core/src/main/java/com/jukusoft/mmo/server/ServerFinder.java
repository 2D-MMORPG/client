package com.jukusoft.mmo.server;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServerFinder {

    protected static final String UTF8 = "UTF-8";

    //ini4j instance
    protected Ini ini = null;

    //server section in config file
    protected Profile.Section section = null;

    //api content
    protected String content = "";

    //list with all proxy servers
    protected List<Server> serverList = new ArrayList<>();

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

    public void load () throws IOException {
        //clear list
        this.serverList.clear();

        //read json file from website
        this.content = getContent(this.getServerListURL());

        //parse content
        JsonObject json = new JsonObject(content);

        JsonArray array = json.getJsonArray("server");

        for (int i = 0; i < array.size(); i++) {
            //get json object
            JsonObject serverJson = array.getJsonObject(i);

            //create new server object
            Server server = new Server();
            server.load(serverJson);

            //add server to list
            this.serverList.add(server);
        }
    }

    public String getServerListURL () {
        return this.section.get("serverlist");
    }

    protected String getContent (String serverURL) throws IOException {
        URL url = new URL(serverURL);

        Map<String,Object> params = new LinkedHashMap<>();

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), UTF8));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), UTF8));
        }

        byte[] postDataBytes = postData.toString().getBytes(UTF8);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF8));

        String response = "";

        for (int c; (c = in.read()) >= 0;) {
            response += (char) c;
        }

        while (!response.startsWith("{") && response.length() > 0) {
            response = response.substring(1, response.length());
        }

        return response;
    }

    public List<Server> listServer () {
        return this.serverList;
    }

}
