package com.jukusoft.mmo.network.traffic;

import com.jukusoft.mmo.network.NetworkManager;

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

    public void addSendBytes (int bytes, NetworkManager.PROTOCOL protocol) {
        if (protocol == NetworkManager.PROTOCOL.TCP) {
            this.outboundTCPBytes += bytes;
        } else {
            this.outboundUDPBytes += bytes;
        }
    }

    public void addReceiveBytes (int bytes, NetworkManager.PROTOCOL protocol) {
        if (protocol == NetworkManager.PROTOCOL.TCP) {
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
