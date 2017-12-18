package com.jukusoft.mmo.updater;

import io.vertx.core.json.JsonObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class VersionTest {

    @Test
    public void testConstructor () {
        new Version();
    }

    @Test (expected = NullPointerException.class)
    public void testLoadNull () {
        Version version = new Version();
        version.load(null);
    }

    @Test
    public void testLoad () {
        //create new example json object
        JsonObject json = new JsonObject();
        json.put("version", "1.0.0-alpha");
        json.put("full_version", "1.0.0 Pre-Alpha");
        json.put("build_number", 1);
        json.put("build_date", "12.12.2017");
        json.put("build_time", "11.28");
        json.put("update_channel_url", "http://example.tld");

        //create new data model
        Version version = new Version();

        //load data model from json object
        version.load(json);

        assertEquals("1.0.0-alpha", version.version);
        assertEquals("1.0.0 Pre-Alpha", version.fullVersion);
        assertEquals(1, version.buildNumber);
        assertEquals("12.12.2017", version.buildDate);
        assertEquals("11.28", version.buildTime);
        assertEquals("http://example.tld", version.updateURL);

        //test public getter methods
        assertEquals("1.0.0-alpha", version.getVersion());
        assertEquals("1.0.0 Pre-Alpha", version.getFullVersion());
        assertEquals(1, version.getBuildNumber());
        assertEquals("12.12.2017", version.getBuildDate());
        assertEquals("11.28", version.getBuildTime());
        assertEquals("http://example.tld", version.getUpdateURL());

        version.setUpdateURL("http://example.tld2");
        assertEquals("http://example.tld2", version.getUpdateURL());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLoadInvalideKeys () {
        //create new example json object
        JsonObject json = new JsonObject();
        json.put("full_version", "1.0.0 Pre-Alpha");
        json.put("build_number", 1);
        json.put("build_date", "12.12.2017");
        json.put("build_time", "11.28");
        json.put("update_channel_url", "http://example.tld");

        //create new data model
        Version version = new Version();

        //load data model from json object
        version.load(json);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLoadInvalideKeys1 () {
        //create new example json object
        JsonObject json = new JsonObject();
        json.put("version", "1.0.0-alpha");
        json.put("build_number", 1);
        json.put("build_date", "12.12.2017");
        json.put("build_time", "11.28");
        json.put("update_channel_url", "http://example.tld");

        //create new data model
        Version version = new Version();

        //load data model from json object
        version.load(json);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLoadInvalideKeys2 () {
        //create new example json object
        JsonObject json = new JsonObject();
        json.put("version", "1.0.0-alpha");
        json.put("full_version", "1.0.0 Pre-Alpha");
        json.put("build_date", "12.12.2017");
        json.put("build_time", "11.28");
        json.put("update_channel_url", "http://example.tld");

        //create new data model
        Version version = new Version();

        //load data model from json object
        version.load(json);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLoadInvalideKeys3 () {
        //create new example json object
        JsonObject json = new JsonObject();
        json.put("version", "1.0.0-alpha");
        json.put("full_version", "1.0.0 Pre-Alpha");
        json.put("build_number", 1);
        json.put("build_time", "11.28");
        json.put("update_channel_url", "http://example.tld");

        //create new data model
        Version version = new Version();

        //load data model from json object
        version.load(json);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLoadInvalideKeys4 () {
        //create new example json object
        JsonObject json = new JsonObject();
        json.put("version", "1.0.0-alpha");
        json.put("full_version", "1.0.0 Pre-Alpha");
        json.put("build_number", 1);
        json.put("build_date", "12.12.2017");
        json.put("update_channel_url", "http://example.tld");

        //create new data model
        Version version = new Version();

        //load data model from json object
        version.load(json);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLoadInvalideKeys5 () {
        //create new example json object
        JsonObject json = new JsonObject();
        json.put("version", "1.0.0-alpha");
        json.put("full_version", "1.0.0 Pre-Alpha");
        json.put("build_number", 1);
        json.put("build_date", "12.12.2017");
        json.put("build_time", "11.28");

        //create new data model
        Version version = new Version();

        //load data model from json object
        version.load(json);
    }

    @Test
    public void testSave () throws IOException {
        //create new data model
        Version version = new Version();

        version.save(new File("../junit-tests/version-test.json"));
    }

}
