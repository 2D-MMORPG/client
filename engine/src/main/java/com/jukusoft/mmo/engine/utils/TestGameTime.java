package com.jukusoft.mmo.engine.utils;

/**
* game time for junit tests
*/
public class TestGameTime extends GameTime {

    public TestGameTime () {
        super();
    }

    public void setDelta (float delta) {
        this.delta = delta;
    }

}
