package com.we_smart.lansharelib;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.we_smart.lansharelib.bean.Client;
import com.we_smart.lansharelib.bean.SenderSocket;
import com.we_smart.lansharelib.conts.OrderConsts;
import com.we_smart.lansharelib.conts.Port;
import com.we_smart.lansharelib.listener.OnSenderDataListener;
import com.we_smart.lansharelib.tools.NetUtils;
import com.we_smart.lansharelib.tools.PoolTools;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static com.we_smart.lansharelib.conts.OrderConsts.MSG_CLIENT_OFFLINE;
import static com.we_smart.lansharelib.conts.OrderConsts.MSG_CLIENT_ONLINE;
import static com.we_smart.lansharelib.conts.OrderConsts.MSG_CONFIRM;

/**
 * 局域网分享发送数据类
 * 需要的权限
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 *
 * @author zhaoliufeng 2018/1/15
 */

public class LanShareSender {

    private DatagramSocket mDatagramSocket;

    private OnSenderDataListener mOnSenderDataListener;
    /**
     * 设备上线
     */
    private final int MSG_RECEIVE_CLIENT_ONLINE = 0x1000;
    /**
     * 设备接收确认
     */
    private final int MSG_RECEIVE_CLIENT_CONFIRM = 0x1001;
    /**
     * 设备离线
     */
    private final int MSG_RECEIVE_CLIENT_OFFLINE = 0x1002;

    public LanShareSender() throws SocketException {
        mDatagramSocket = new SenderSocket();
        setClientStateMonitor();
    }

    /**
     * 设置接收端数据的状态监听线程
     */
    private void setClientStateMonitor() {
        //打开端口 监听设备上线广播
        PoolTools.DefRandTaskPool().PushTask(receiveTaskRunnable());
    }

    /**
     * 向接收端发送数据单播
     */
    public void sendDataToReceive(final InetAddress inetAddress, final String sendData) {
        PoolTools.DefRandTaskPool().PushTask(new Runnable() {
            @Override
            public void run() {
                try {
                    //选择目标设备 向目标设备单播数据
                    String data = OrderConsts.MSG_SEND_DATA + OrderConsts.SPLIT + sendData;

                    //是否可以ping通指定IP地址
                    int tryCount = 0;
                    while ( !NetUtils.pingIpAddress(inetAddress.getHostAddress()) && tryCount < OrderConsts.DEFAULT_TRY_COUNT ) {
                        Thread.sleep(500);
                        Log.i("Data", "Try to ping ------" + inetAddress.getHostAddress() + " - " + tryCount);
                        tryCount++;
                    }

                    DatagramPacket datagramPacket = new DatagramPacket(data.getBytes(), data.getBytes().length, inetAddress, Port.UDP_RECEIVE_PORT);
                    mDatagramSocket.send(datagramPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置数据监听
     */
    public void setOnSenderDataListener(OnSenderDataListener onSenderDataListener) {
        mOnSenderDataListener = onSenderDataListener;
    }

    public void overListener() {
        mOnSenderDataListener = null;
        mDatagramSocket.close();
        mDatagramSocket = null;
    }

    private Runnable receiveTaskRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    //开始接收接收端发来的指令
                    listenToClientMsg();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void listenToClientMsg() {
        try {
            //监听端口数据
            while ( true ) {
                byte[] receiveData = new byte[ 1024 ];
                DatagramPacket datagramPacket = new DatagramPacket(receiveData, receiveData.length);
                mDatagramSocket.receive(datagramPacket);
                String response = new String(datagramPacket.getData()).trim();
                if (response.length() > 0) {
                    Message msg = new Message();
                    //监听接收端上线 online
                    if (response.split(OrderConsts.SPLIT)[ 0 ].equals(MSG_CLIENT_ONLINE)) {
                        msg.what = MSG_RECEIVE_CLIENT_ONLINE;
                        Client device = new Client();
                        device.mIpAddress = datagramPacket.getAddress();
                        device.mClientName = response.split(OrderConsts.SPLIT)[ 1 ];
                        msg.obj = device;
                        handler.sendMessage(msg);
                    }
                    //监听confirm
                    if (response.split(OrderConsts.SPLIT)[ 0 ].equals(MSG_CONFIRM)) {
                        msg.what = MSG_RECEIVE_CLIENT_CONFIRM;
                        msg.obj = datagramPacket.getAddress();
                        handler.sendMessage(msg);
                    }

                    //监听offline
                    if (response.split(OrderConsts.SPLIT)[ 0 ].equals(MSG_CLIENT_OFFLINE)) {
                        msg.what = MSG_RECEIVE_CLIENT_OFFLINE;
                        msg.obj = datagramPacket.getAddress();
                        handler.sendMessage(msg);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mOnSenderDataListener == null) {
                return;
            }
            switch (msg.what) {
                case MSG_RECEIVE_CLIENT_ONLINE:
                    mOnSenderDataListener.clientOnline((Client) msg.obj);
                    break;
                case MSG_RECEIVE_CLIENT_CONFIRM:
                    mOnSenderDataListener.clientReceiveConfirm((InetAddress) msg.obj);
                    break;
                case MSG_RECEIVE_CLIENT_OFFLINE:
                    mOnSenderDataListener.clientOffline((InetAddress) msg.obj);
                    break;
                default:
            }
        }
    };
}
