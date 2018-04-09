package com.we_smart.lansharelib.bean;

import com.we_smart.lansharelib.conts.Port;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 接收方socket
 */

public class ReceiveSocket extends DatagramSocket {
    public ReceiveSocket() throws SocketException {
        super(Port.UDP_RECEIVE_PORT);
    }
}
