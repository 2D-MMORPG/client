package com.jukusoft.mmo.utils;

import com.teamunify.i18n.I;

import java.io.*;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
* Utility class to send exception reports to jukusoft.com server
 *
 * Copyright (c) 2017 jukusoft.com
 *
 * @author JuKu
*/
public class ReportUtils {

    public static String SERVER_URL = "http://mmo.jukusoft.com/api/send_exception.php";

    /**
    * send client exception to server, so developers can fix it
     *
     * @param cause catched exception
    */
    public static void sendExceptionToServer (Throwable cause) {
        try {
            URL url = new URL(SERVER_URL);

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("exception_class", cause.getClass().getName());
            params.put("exception_message", cause.getMessage());
            params.put("localized_message", cause.getLocalizedMessage());
            params.put("class_name", cause.getStackTrace()[0].getClassName());

            //convert stacktrace into string
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            cause.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string

            //add stacktrace to message
            params.put("stacktrace", sStackTrace);

            //add system information
            addSystemInformationToMap(params);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            //log sended information
            logSendedInformation(params);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String response = "";

            for (int c; (c = in.read()) >= 0;) {
                response += (char) c;
            }

            //log response
            System.out.println("\n\n========\nServer response\n========\n\n" + response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addSystemInformationToMap (Map<String,Object> params) {
        //add OS information
        params.put("os_name", System.getProperty("os.name"));
        params.put("os_arch", System.getProperty("os.arch"));
        params.put("os_version", System.getProperty("os.version"));

        //add CPU information
        int nOfCores = Runtime.getRuntime().availableProcessors();

        params.put("cpu_cores", nOfCores);
        params.put("cpu_identifier", System.getenv("PROCESSOR_IDENTIFIER"));
        params.put("cpu_architecture", System.getenv("PROCESSOR_ARCHITECTURE"));
        params.put("cpu_arch_w6432", System.getenv("PROCESSOR_ARCHITEW6432"));
        params.put("number_of_processors", System.getenv("NUMBER_OF_PROCESSORS"));

        //get RAM information (in bytes --> MB)
        float freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        float maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        float totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;

        params.put("free_memory", freeMemory + "MB");
        params.put("max_memory", maxMemory + "MB");
        params.put("total_memory", totalMemory + "MB");

        try {
            params.put("language", I.getLanguage());
        } catch (Exception e) {
            //it isnt set yet, so it isnt available yet
            params.put("language", "na");
        }
    }

    private static void logSendedInformation (Map<String,Object> params) {
        System.err.println("send anonymous exception report to server...\n\nData:\n");

        for (Map.Entry<String,Object> entry : params.entrySet()) {
            System.err.println("" + entry.getKey() + " = " + entry.getValue());
        }
    }

}
