package com.jukusoft.mmo.utils;

import com.teamunify.i18n.I;
import org.jutils.jhardware.HardwareInfo;
import org.jutils.jhardware.model.ProcessorInfo;

import java.io.*;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Utility class to send exception reports to jukusoft.com server
 *
 * Copyright (c) 2017 jukusoft.com
 *
 * @author JuKu
*/
public class ReportUtils {

    public static final String SERVER_URL = "http://mmo.jukusoft.com/api/send_exception.php";
    protected static final String UTF8 = "UTF-8";
    protected static final String LOG_MESSAGE = "an exception was thrown";

    //logger
    protected static final Logger LOGGER = Logger.getLogger("ReportUtils");

    /**
     * private constructor, so other classes cannot create an instance of ReportUtils
     */
    private ReportUtils () {
        //
    }

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
                postData.append(URLEncoder.encode(param.getKey(), UTF8));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), UTF8));
            }

            byte[] postDataBytes = postData.toString().getBytes(UTF8);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            //log sended information
            logSendedInformation(params);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF8));

            String response = "";

            for (int c; (c = in.read()) >= 0;) {
                response += (char) c;
            }

            //log response
            LOGGER.log(Level.WARNING, "\n\n================\nServer response\n================\n\n" + response);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, LOG_MESSAGE, e);
            System.exit(0);
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

        //get more detailed cpu information
        ProcessorInfo info = HardwareInfo.getProcessorInfo();
        params.put("cpu_cache_size", info.getCacheSize());
        params.put("cpu_family", info.getFamily());
        params.put("cpu_speed (MHZ)", info.getMhz());
        params.put("cpu_model", info.getModel());
        params.put("cpu_model_name", info.getModelName());

        //get RAM information (in bytes --> MB)
        float freeMemory = ((float) Runtime.getRuntime().freeMemory()) / 1024f / 1024f;
        float maxMemory = ((float) Runtime.getRuntime().maxMemory()) / 1024f / 1024f;
        float totalMemory = ((float) Runtime.getRuntime().totalMemory()) / 1024f / 1024f;

        params.put("free_memory", freeMemory + "MB");
        params.put("max_memory", maxMemory + "MB");
        params.put("total_memory", totalMemory + "MB");

        //add java version
        params.put("java_version", System.getProperty("java.version"));
        params.put("java_vendor", System.getProperty("java.vendor"));
        params.put("jvm_spec_version", System.getProperty("java.vm.specification.version"));
        params.put("jvm_version", System.getProperty("java.vm.version"));

        params.put("file_seperator", System.getProperty("file.separator"));
        params.put("path_seperator", System.getProperty("path.separator"));
        params.put("line_seperator", System.getProperty("line.separator"));

        try {
            params.put("language", I.getLanguage());
        } catch (Exception e) {
            //it isnt set yet, so it isnt available yet
            params.put("language", "na");
        }
    }

    private static void logSendedInformation (Map<String,Object> params) {
        LOGGER.log(Level.WARNING, "send anonymous exception report to server...\n\n================\nData\n================\n");

        for (Map.Entry<String,Object> entry : params.entrySet()) {
            LOGGER.log(Level.WARNING, "" + entry.getKey() + " = " + entry.getValue());
        }
    }

}
