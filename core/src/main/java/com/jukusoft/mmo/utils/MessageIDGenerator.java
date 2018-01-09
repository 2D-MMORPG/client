package com.jukusoft.mmo.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
* Utility class to generate ordered, local-unique IDs
*/
public class MessageIDGenerator {

    //last message id
    protected static AtomicInteger lastID = new AtomicInteger(0);

    /**
    * private constructor
    */
    protected MessageIDGenerator () {
        //
    }

    /**
    * generate a new local unique ID
     *
     * @throws IllegalStateException if Integer.MAX_VALUE was reached
     *
     * @return unique id
    */
    public static int generateID () {
        //return i++
        int value = lastID.incrementAndGet();

        //check, if ID is in range
        if (value >= Integer.MAX_VALUE) {
            throw new IllegalStateException("max value of ID generation was reached! Please restart your client!");
        }

        return value;
    }

    /**
    * count generated IDs
     *
     * @return number of generated ids
    */
    public static int countGeneratedIDs () {
        return lastID.get();
    }

}
