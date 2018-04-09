package com.we_smart.lansharelib.conts;

/**
 * 指令常量
 */

public class OrderConsts {

    /**
     *  接受端设备上线指令
     */
    public final static String MSG_CLIENT_ONLINE = "msg_client_online";
    /**
     * 接受端设备下线指令
     */
    public final static String MSG_CLIENT_OFFLINE = "msg_client_offline";
    /**
     * 发送端发送信息指令
     */
    public final static String MSG_SEND_DATA = "msg_send_data";
    /**
     * 接收端确认接收到信息
     */
    public final static String MSG_CONFIRM = "msg_ok";

    /**
     * 信息分割符 分割指令与附带信息
     */
    public final static String SPLIT = "-0-";

    /**
     * 最大尝试次数
     */
    public static final int DEFAULT_TRY_COUNT = 10;

    /**
     * WiFi连接成功时未分配的默认IP地址
     */
    public static final String DEFAULT_UNKNOW_IP = "0.0.0.0";

    /**
     *  LOG TAG
     */
    public static final String LAN_SHARE = "lan_share";
}
