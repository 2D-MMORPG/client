package com.jukusoft.mmo.server;

import io.vertx.core.json.JsonObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerTest {

    @Test
    public void testConstructor () {
        new Server();
    }

    @Test (expected = NullPointerException.class)
    public void testNullLoad () {
        Server server = new Server();

        server.load(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalideKeysLoad () {
        Server server = new Server();

        server.load(new JsonObject());
    }

    @Test
    public void testLoad () {
        Server server = new Server();

        //create new example json object
        JsonObject json = new JsonObject();
        json.put("name", "test-name");
        json.put("description", "test-description");
        json.put("channel", "stable");
        json.put("proxy_ip", "127.0.0.1");
        json.put("proxy_port", 2600);
        json.put("image_url", "none");

        server.load(json);

        //check values
        assertEquals("test-name", server.getName());
        assertEquals("test-description", server.getDescription());
        assertEquals("stable", server.getChannel());
        assertEquals("127.0.0.1", server.getProxyIP());
        assertEquals(2600, server.getProxyPort());
        assertEquals("none", server.getImageURL());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNotExistentKeyLoad1 () {
        Server server = new Server();

        //create new example json object
        JsonObject json = new JsonObject();
        json.put("description", "test-description");
        json.put("channel", "stable");
        json.put("proxy_ip", "127.0.0.1");
        json.put("proxy_port", 2600);
        json.put("image_url", "none");

        server.load(json);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNotExistentKeyLoad2 () {
        Server server = new Server();

        //create new example json object
        JsonObject json = new JsonObject();
        json.put("name", "test-name");
        json.put("channel", "stable");
        json.put("proxy_ip", "127.0.0.1");
        json.put("proxy_port", 2600);
        json.put("image_url", "none");

        server.load(json);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNotExistentKeyLoad3 () {
        Server server = new Server();

        //create new example json object
        JsonObject json = new JsonObject();
        json.put("name", "test-name");
        json.put("description", "test-description");
        json.put("proxy_ip", "127.0.0.1");
        json.put("proxy_port", 2600);
        json.put("image_url", "none");

        server.load(json);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNotExistentKeyLoad4 () {
        Server server = new Server();

        //create new example json object
        JsonObject json = new JsonObject();
        json.put("name", "test-name");
        json.put("description", "test-description");
        json.put("channel", "stable");
        json.put("proxy_port", 2600);
        json.put("image_url", "none");

        server.load(json);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNotExistentKeyLoad5 () {
        Server server = new Server();

        //create new example json object
        JsonObject json = new JsonObject();
        json.put("name", "test-name");
        json.put("description", "test-description");
        json.put("channel", "stable");
        json.put("proxy_ip", "127.0.0.1");
        json.put("image_url", "none");

        server.load(json);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNotExistentKeyLoad6 () {
        Server server = new Server();

        //create new example json object
        JsonObject json = new JsonObject();
        json.put("name", "test-name");
        json.put("description", "test-description");
        json.put("channel", "stable");
        json.put("proxy_ip", "127.0.0.1");
        json.put("proxy_port", 2600);

        server.load(json);
    }

}
