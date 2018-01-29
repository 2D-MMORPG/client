package com.jukusoft.mmo.pool;

/**
* interface to use objects in a pool
*/
public interface Poolable {

    /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
    public void reset ();

}
