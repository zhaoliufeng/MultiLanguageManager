package com.we_smart.lansharelib.listener;

import java.net.InetAddress;

/**
 * 接收数据方数据指令监听
 * @author zhaoliufeng 2018/1/15
 */

public interface OnReceiverDataListener {
    /**
     * 从发送端收到了消息
     *
     * @param jsonString 接受到的json数据字符串
     * @param senderIp 发送端的地址信息
     */
    void receiveData(String jsonString, InetAddress senderIp);
}
