package com.jukusoft.mmo.network.traffic;

import com.jukusoft.mmo.network.NetworkManager;
import com.jukusoft.mmo.network.Protocol;

public class TrafficCounter {

    //udp traffic
    protected volatile long inboundUDPBytes = 0;
    protected volatile long outboundUDPBytes = 0;

    //tcp traffic
    protected volatile long inboundTCPBytes = 0;
    protected volatile long outboundTCPBytes = 0;

    /**
    * default constructor
    */
    public TrafficCounter () {
        //
    }

    public void addSendBytes (int bytes, Protocol protocol) {
        if (protocol == Protocol.TCP) {
            this.outboundTCPBytes += bytes;
        } else {
            this.outboundUDPBytes += bytes;
        }
    }

    public void addReceiveBytes (int bytes, Protocol protocol) {
        if (protocol == Protocol.TCP) {
            this.inboundTCPBytes += bytes;
        } else {
            this.inboundUDPBytes += bytes;
        }
    }

    public long getInBoundBytes () {
        return this.inboundTCPBytes + this.inboundUDPBytes;
    }

    public long getOutBoundBytes () {
        return this.outboundTCPBytes + this.outboundUDPBytes;
    }

}
