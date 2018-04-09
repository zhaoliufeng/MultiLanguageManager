package com.we_smart.lansharelib.bean;

import java.net.InetAddress;

/**
 * 从机 向发送方上报数据的接收端实体
 */

public class Client {
    /**
     * 接收端机型名称
     */
    public String mClientName;

    /**
     * 接收端IP地址
     */
    public InetAddress mIpAddress;
}
