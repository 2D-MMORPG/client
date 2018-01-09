package com.jukusoft.mmo.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MessageIDGeneratorTest {

    @Test
    public void testConstructor () {
        new MessageIDGenerator();
    }

    @Test
    public void testGenerateID () {
        //reset lastID first
        MessageIDGenerator.lastID.set(0);

        assertEquals(0, MessageIDGenerator.countGeneratedIDs());

        //generate ID
        assertEquals(1, MessageIDGenerator.generateID());

        //check count
        assertEquals(1, MessageIDGenerator.countGeneratedIDs());

        List<Integer> idList = new ArrayList<>();

        //generate 10 new unique IDs and check, that the IDs are unique
        for (int i = 0; i < 10; i++) {
            int id = MessageIDGenerator.generateID();

            //check, that id is really unique and doesnt exists before
            assertEquals(false, idList.contains(id));

            //add id to list
            idList.add(id);
        }

        assertEquals(11, MessageIDGenerator.countGeneratedIDs());
    }

    @Test (expected = IllegalStateException.class)
    public void testGenerateIDMaxValue () {
        MessageIDGenerator.lastID.set(Integer.MAX_VALUE - 1);

        MessageIDGenerator.generateID();
    }

}
