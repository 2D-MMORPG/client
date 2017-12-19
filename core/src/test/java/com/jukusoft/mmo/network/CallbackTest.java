package com.jukusoft.mmo.network;

import org.junit.Test;

public class CallbackTest {

    @Test
    public void testInterface () {
        Callback<String> cb = new Callback<String>() {
            @Override
            public void handle(String param) {
                //
            }
        };

        cb.handle("test");
    }

}
