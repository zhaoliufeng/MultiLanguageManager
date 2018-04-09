package com.we_smart.lansharelib;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.we_smart.lansharelib.bean.ReceiveSocket;
import com.we_smart.lansharelib.conts.OrderConsts;
import com.we_smart.lansharelib.listener.OnReceiverDataListener;
import com.we_smart.lansharelib.tools.NetUtils;
import com.we_smart.lansharelib.tools.PoolTools;
import com.we_smart.lansharelib.tools.SystemUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static com.we_smart.lansharelib.conts.Port.UDP_SEND_PORT;

/**
 * 局域网分享接收数据类
 *
 * @author zhaoliufeng 2018/1/15
 */

public class LanShareReceiver {

    /**
     * 广播UDPSocket
     */
    private DatagramSocket mDatagramSocket;

    /**
     * 发送端的Ip地址
     */
    private InetAddress mSendIpAddress;

    /**
     * 数据接收监听
     */
    private OnReceiverDataListener mOnReceiverDataListener;

    /**
     * 接收区域大小
     * 默认最大值
     */
    private int mRecvBufferSize = 1024 * 64;

    /**
     * 接收区最大为 1024 * 64
     */
    private final int MAX_SIZE = 1024 * 64;

    /**
     * 从发送端收到了信息
     */
    private final int RECEIVE_DATA_FROM_SENDER = 0x00;

    public LanShareReceiver() throws SocketException {
        this.mDatagramSocket = new ReceiveSocket();
        clientOnline();
    }

    public LanShareReceiver(int receiveBufferSize) throws SocketException {
        this.mDatagramSocket = new ReceiveSocket();
        //接收区 最大为 1024 * 64 UDP包的上限大小
        if (receiveBufferSize < MAX_SIZE)
            this.mRecvBufferSize = receiveBufferSize;
        else
            this.mRecvBufferSize = MAX_SIZE;
        clientOnline();
    }

    /**
     * 发送设备离线广播
     * 通知接收端离线
     */
    public void clientOffline() {
        PoolTools.MAIN_EXECUTOR.execute(offlineMsgTask);
    }

    /**
     * 发送设备上线广播
     * 通知接收端上线
     */
    private void clientOnline() {
        PoolTools.MAIN_EXECUTOR.execute(onlineMsgTask);
        //上线后直接开启数据监听
        PoolTools.MAIN_EXECUTOR.execute(receiveTask);
    }

    /**
     * 设置数据监听
     */
    public void setOnDataReceiveListener(OnReceiverDataListener onDataReceiveListener) {
        mOnReceiverDataListener = onDataReceiveListener;
    }

    /**
     * 发送在线广播
     */
    private Runnable onlineMsgTask = new Runnable() {
        @Override
        public void run() {
            try {
                //每隔1s广播一次数据 被周围的发送者发现
                Log.i(OrderConsts.LAN_SHARE, "start send online packet");
                while (true) {
                    Thread.sleep(1000);
                    String msgOnline = OrderConsts.MSG_CLIENT_ONLINE + OrderConsts.SPLIT
                            + SystemUtil.getDeviceBrand() + " " + SystemUtil.getSystemModel();
                    final InetAddress address = InetAddress.getByName("255.255.255.255");
                    //通知发送端已经上线
                    DatagramPacket datagramPacket = new DatagramPacket(msgOnline.getBytes(), msgOnline.getBytes().length,
                            address, UDP_SEND_PORT);
                    mDatagramSocket.send(datagramPacket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 广播离线
     */
    private Runnable offlineMsgTask = new Runnable() {
        @Override
        public void run() {
            try {
                final InetAddress address = InetAddress.getByName("255.255.255.255");
                DatagramPacket datagramPacket = new DatagramPacket(OrderConsts.MSG_CLIENT_OFFLINE.getBytes(), OrderConsts.MSG_CLIENT_OFFLINE.getBytes().length,
                        address, UDP_SEND_PORT);
                mDatagramSocket.send(datagramPacket);
                mDatagramSocket.close();
                mDatagramSocket = null;
                Log.i(OrderConsts.LAN_SHARE, "send offline packet");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 监听接收数据
     */
    private Runnable receiveTask = new Runnable() {
        @Override
        public void run() {
            try {
                //监听端口数据
                while (true) {
                    byte[] receiveData = new byte[mRecvBufferSize];
                    final DatagramPacket datagramPacket = new DatagramPacket(receiveData, receiveData.length);
                    mDatagramSocket.receive(datagramPacket);
                    String response = new String(datagramPacket.getData()).trim();
                    Log.i(OrderConsts.LAN_SHARE, "receive data from:" + datagramPacket.getAddress().getHostAddress());
                    if (response.length() > 0) {
                        if (response.split(OrderConsts.SPLIT)[0].equals(OrderConsts.MSG_SEND_DATA)) {
                            Message msg = new Message();
                            msg.what = RECEIVE_DATA_FROM_SENDER;
                            msg.obj = response.split(OrderConsts.SPLIT)[1];
                            handler.sendMessage(msg);
                            mSendIpAddress = datagramPacket.getAddress();
                            PoolTools.MAIN_EXECUTOR.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //是否可以ping通指定IP地址
                                        int tryCount = 0;
                                        while (!NetUtils.pingIpAddress(mSendIpAddress.getHostAddress()) && tryCount < OrderConsts.DEFAULT_TRY_COUNT) {
                                            Thread.sleep(500);
                                            Log.i(OrderConsts.LAN_SHARE, "Try to ping ------" + mSendIpAddress.getHostAddress() + " - " + tryCount);
                                            tryCount++;
                                        }

                                        //返回发送端 ok
                                        DatagramPacket datagramPacket = new DatagramPacket(OrderConsts.MSG_CONFIRM.getBytes(), OrderConsts.MSG_CONFIRM.getBytes().length,
                                                mSendIpAddress, UDP_SEND_PORT);
                                        mDatagramSocket.send(datagramPacket);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RECEIVE_DATA_FROM_SENDER:
                    Log.i(OrderConsts.LAN_SHARE, "receive data");
                    if (mOnReceiverDataListener != null) {
                        mOnReceiverDataListener.receiveData(String.valueOf(msg.obj), mSendIpAddress);
                    }
                    break;
                default:
            }
        }
    };
}
