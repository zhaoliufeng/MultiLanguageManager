package com.we_smart.lansharelib.tools;

import java.io.IOException;

/**
 * 网络集合
 */
public class NetUtils {

    /**
     * 是否可以ping通指定IP地址
     * @param ipAddress
     * @return
     */
    public static boolean pingIpAddress(String ipAddress) {
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 100 " + ipAddress);
            int status = process.waitFor();
            return status == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
