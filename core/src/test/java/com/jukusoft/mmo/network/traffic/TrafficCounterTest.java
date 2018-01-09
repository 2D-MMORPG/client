package com.jukusoft.mmo.network.traffic;

import com.jukusoft.mmo.network.NetworkManager;
import com.jukusoft.mmo.network.Protocol;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrafficCounterTest {

    @Test
    public void testConstructor () {
        new TrafficCounter();
    }

    @Test
    public void testAddSendBytes () {
        TrafficCounter counter = new TrafficCounter();

        assertEquals(0, counter.inboundTCPBytes);
        assertEquals(0, counter.outboundTCPBytes);
        assertEquals(0, counter.inboundUDPBytes);
        assertEquals(0, counter.outboundUDPBytes);

        //add traffic
        counter.addSendBytes(20, Protocol.TCP);

        assertEquals(0, counter.inboundTCPBytes);
        assertEquals(20, counter.outboundTCPBytes);
        assertEquals(0, counter.inboundUDPBytes);
        assertEquals(0, counter.outboundUDPBytes);

        //add traffic
        counter.addSendBytes(50, Protocol.UDP);

        assertEquals(0, counter.inboundTCPBytes);
        assertEquals(20, counter.outboundTCPBytes);
        assertEquals(0, counter.inboundUDPBytes);
        assertEquals(50, counter.outboundUDPBytes);

        //add traffic
        counter.addSendBytes(80, Protocol.TCP);

        assertEquals(0, counter.inboundTCPBytes);
        assertEquals(100, counter.outboundTCPBytes);
        assertEquals(0, counter.inboundUDPBytes);
        assertEquals(50, counter.outboundUDPBytes);

        assertEquals(150, counter.getOutBoundBytes());
    }

    @Test
    public void testAddReceiveBytes () {
        TrafficCounter counter = new TrafficCounter();

        assertEquals(0, counter.inboundTCPBytes);
        assertEquals(0, counter.outboundTCPBytes);
        assertEquals(0, counter.inboundUDPBytes);
        assertEquals(0, counter.outboundUDPBytes);

        //add traffic
        counter.addReceiveBytes(20, Protocol.TCP);

        assertEquals(20, counter.inboundTCPBytes);
        assertEquals(0, counter.outboundTCPBytes);
        assertEquals(0, counter.inboundUDPBytes);
        assertEquals(0, counter.outboundUDPBytes);

        //add traffic
        counter.addReceiveBytes(50, Protocol.UDP);

        assertEquals(20, counter.inboundTCPBytes);
        assertEquals(0, counter.outboundTCPBytes);
        assertEquals(50, counter.inboundUDPBytes);
        assertEquals(0, counter.outboundUDPBytes);

        //add traffic
        counter.addReceiveBytes(80, Protocol.TCP);

        assertEquals(100, counter.inboundTCPBytes);
        assertEquals(0, counter.outboundTCPBytes);
        assertEquals(50, counter.inboundUDPBytes);
        assertEquals(0, counter.outboundUDPBytes);

        assertEquals(150, counter.getInBoundBytes());
    }

}
