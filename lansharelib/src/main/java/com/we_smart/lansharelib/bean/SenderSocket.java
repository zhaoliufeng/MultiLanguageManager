package com.we_smart.lansharelib.bean;

import com.we_smart.lansharelib.conts.Port;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 发送Socket
 */

public class SenderSocket extends DatagramSocket {
    public SenderSocket() throws SocketException {
        super(Port.UDP_SEND_PORT);
    }
}
