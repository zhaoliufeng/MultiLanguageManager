package com.we_smart.lansharelib.listener;

import com.we_smart.lansharelib.bean.Client;

import java.net.InetAddress;

/**
 * 发送数据方数据指令监听
 * @author zhaoliufeng 2018/1/15
 */

public interface OnSenderDataListener {

    /**
     * 新设备上线
     *
     * @param client 上线的客户端信息
     */
    void clientOnline(Client client);

    /**
     * 设备离线
     *
     * @param clientIp 离线设备的ip
     */
    void clientOffline(InetAddress clientIp);

    /**
     * 设备接收确认
     *
     * @param clientIp 确认接收到数据设备的ip
     */
    void clientReceiveConfirm(InetAddress clientIp);

}
