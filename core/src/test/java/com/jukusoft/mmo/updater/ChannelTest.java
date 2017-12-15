package com.jukusoft.mmo.updater;

import io.vertx.core.json.JsonObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChannelTest {

    @Test
    public void testConstructor () {
        //
    }

    @Test (expected = NullPointerException.class)
    public void testLoadNull () {
        Channel channel = new Channel();
        channel.load(null);
    }

    @Test
    public void testLoad () {
        //create new instance of channel
        Channel channel = new Channel();

        //create example json object
        JsonObject json = new JsonObject();
        json.put("name", "test-name");
        json.put("title", "test-title");
        json.put("activated", true);
        json.put("public", true);
        json.put("update_url", "http://example.tld");
        json.put("newest_build", 1);
        json.put("newest_build_date", "15.12.2017");
        json.put("newest_build_time", "14.32");
        json.put("newest_version", "1.0.0-alpha");
        json.put("newest_full_version", "1.0.0 Pre-Alpha");

        //load channel from json object
        channel.load(json);

        //check object values
        assertEquals("test-name", channel.name);
        assertEquals("test-title", channel.title);
        assertEquals(true, channel.activated);
        assertEquals(true, channel.publicChannel);
        assertEquals("http://example.tld", channel.updateURL);
        assertEquals(1, channel.newestBuildNumber);
        assertEquals("15.12.2017", channel.buildDate);
        assertEquals("14.32", channel.buildTime);
        assertEquals("1.0.0-alpha", channel.newestVersion);
        assertEquals("1.0.0 Pre-Alpha", channel.newestFullVersion);

        //test getter methods
        assertEquals("test-name", channel.getName());
        assertEquals("test-title", channel.getTitle());
        assertEquals(true, channel.isActivated());
        assertEquals(true, channel.isPublic());
        assertEquals("http://example.tld", channel.getUpdateURL());
        assertEquals(1, channel.getNewestBuildNumber());
        assertEquals("15.12.2017", channel.getBuildDate());
        assertEquals("14.32", channel.getBuildTime());
        assertEquals("1.0.0-alpha", channel.getNewestVersion());
        assertEquals("1.0.0 Pre-Alpha", channel.getNewestFullVersion());
    }

}
