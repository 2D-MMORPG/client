package com.jukusoft.mmo.engine.network;

import com.badlogic.gdx.utils.Array;
import com.jukusoft.mmo.network.NetworkManager;
import com.jukusoft.mmo.network.Protocol;
import com.jukusoft.mmo.network.backend.UDPConnection;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    @Test
    public void testConstructor () {
        new Message();
    }

    @Test
    public void testReset () {
        Message msg = new Message();

        msg.priority = 10;

        msg.reset();

        assertEquals(0, msg.priority);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetPriority () {
        Message msg = new Message();
        msg.setPriority(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetPriority1 () {
        Message msg = new Message();
        msg.setPriority(0);
    }

    @Test
    public void testSetPriority2 () {
        Message msg = new Message();
        msg.priority = 10;
        assertEquals(10, msg.priority());

        msg.setPriority(1);
        assertEquals(1, msg.priority());

        msg.setPriority(20);
        assertEquals(20, msg.priority());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetPriorityMaxValue () {
        Message msg = new Message();
        msg.setPriority(22);
    }

    @Test
    public void testIncrementPriority () {
        Message msg = new Message();
        msg.setPriority(1);

        //simulate 10 frames / ticks
        for (int i = 0; i < 10; i++) {
            msg.incrementPriority();
        }

        assertEquals(11, msg.priority());
    }

    @Test
    public void testIncrementPriority1 () {
        Message msg = new Message();
        msg.setPriority(1);

        //simulate 10 frames / ticks
        for (int i = 0; i < 50; i++) {
            msg.incrementPriority();
        }

        //message priority doesnt have a limit, so it doesnt should thrown an exception
        assertEquals(51, msg.priority());
    }

    @Test
    public void testOrder () {
        //create4 4 messages
        Message msg1 = new Message();
        msg1.setPriority(1);

        Message msg2 = new Message();
        msg2.setPriority(1);

        Message msg3 = new Message();
        msg3.setPriority(3);

        Message msg4 = new Message();
        msg4.setPriority(2);

        //create list and add all messages to list
        List<Message> list = new ArrayList<>();
        list.add(msg1);
        list.add(msg2);
        list.add(msg3);
        list.add(msg4);

        //sort list
        Collections.sort(list);

        //check message indizes
        assertEquals(msg1, list.get(0));
        assertEquals(msg2, list.get(1));
        assertEquals(msg4, list.get(2));
        assertEquals(msg3, list.get(3));
    }

    @Test
    public void testOrderWithLibGDXArray () {
        //create4 4 messages
        Message msg1 = new Message();
        msg1.setPriority(1);

        Message msg2 = new Message();
        msg2.setPriority(1);

        Message msg3 = new Message();
        msg3.setPriority(3);

        Message msg4 = new Message();
        msg4.setPriority(2);

        //create list and add all messages to list
        Array<Message> list = new Array<>();
        list.add(msg1);
        list.add(msg2);
        list.add(msg3);
        list.add(msg4);

        //sort list
        list.sort();

        //check message indizes
        assertEquals(msg1, list.get(0));
        assertEquals(msg2, list.get(1));
        assertEquals(msg4, list.get(2));
        assertEquals(msg3, list.get(3));
    }

    @Test (expected = NullPointerException.class)
    public void testSetNullProtocol () {
        Message msg = new Message();
        msg.setProtocol(null);
    }

    @Test
    public void testSetProtocol () {
        Message msg = new Message();
        msg.setProtocol(Protocol.UDP);
        assertEquals(Protocol.UDP, msg.getProtocol());
        msg.setProtocol(Protocol.TCP);
    }

    @Test
    public void testGetterAndSetter () {
        Message msg = new Message();
        msg.setAckID(20);
        assertEquals(20, msg.getAckID());

        msg.version = 10;
        assertEquals(10, msg.getVersion());
    }

    @Test
    public void testEquals () {
        Message msg = new Message();
        assertEquals(false, msg.equals("new string"));
    }

    @Test
    public void testEquals1 () {
        Message msg = new Message();
        Message msg1 = new Message();
        assertEquals(false, msg.equals(msg1));
        assertEquals(false, msg1.equals(msg));
        assertEquals(true, msg.equals(msg));
        assertEquals(true, msg1.equals(msg1));
    }

}
