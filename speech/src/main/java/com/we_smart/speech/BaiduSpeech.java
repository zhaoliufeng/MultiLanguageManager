package com.we_smart.speech;

import android.content.Context;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.we_smart.speech.listener.OnSpeechListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhaol on 2018/4/9.
 */

public class BaiduSpeech implements EventListener {
    //语音识别
    private EventManager asr;
    //语音唤醒
    private EventManager wakeup;
    //上下文件对象
    private Context mContext;
    //语音回调
    private OnSpeechListener mOnSpeechListener;

    private String appId;
    private String appKey;
    private String secretKey;

    public BaiduSpeech(Context context){
        this.mContext = context;
        initSpeech();
    }

    public BaiduSpeech(Context context,String appId, String appKey, String secretKey){
        this.mContext = context;
        this.appId = appId;
        this.appKey = appKey;
        this.secretKey = secretKey;
        initSpeech();
    }

    /**
     * 次构造器 默认打开语音唤醒
     */
    public BaiduSpeech(Context context,String appId, String appKey, String secretKey,Boolean openWakeUp) {
        this(context, appId, appKey, secretKey);
        if (openWakeUp) {
            openWakeup();
        }
    }

    /**
     * 初始化百度语音
     */
    private void initSpeech() {
        asr = EventManagerFactory.create(mContext, "asr");
        asr.registerListener(this); //  EventListener 中 onEvent方法

        wakeup = EventManagerFactory.create(mContext, "wp");
        wakeup.registerListener(this); //  EventListener 中 onEvent方法

    }

    /**
     * 开始识别语音
     */
    public void startSpeechRecog() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = SpeechConstant.ASR_START;

        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);

        if (appId != null){
            params.put(SpeechConstant.APP_ID, appId);
            params.put(SpeechConstant.APP_KEY, appKey);
            params.put(SpeechConstant.SECRET, secretKey);
        }
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, true);
        //  params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 800);
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        //  params.put(SpeechConstant.PROP ,20000);
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        String json = null; //可以替换成自己的json
        json = new JSONObject(params).toString();
        asr.send(event, json, null, 0, 0);
        Log.i("DataPrint", "输入参数：" + json);
        mOnSpeechListener.onBeginSpeech();
    }

    /**
     * 打开唤醒功能
     */
    public void openWakeup() {
        Map<String, Object> params = new TreeMap<>();

        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下

        String json = new JSONObject(params).toString();
        wakeup.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
    }

    public void setOnSpeechListener(OnSpeechListener onSpeechListener) {
        this.mOnSpeechListener = onSpeechListener;
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        switch (name){
            //引擎准备就绪，可以开始说话
            case SpeechConstant.CALLBACK_EVENT_ASR_READY:

                break;
            //检测到说话开始
            case SpeechConstant.CALLBACK_EVENT_ASR_BEGIN:
                mOnSpeechListener.onBeginSpeech();
                break;
            //检测到说话结束
            case SpeechConstant.CALLBACK_EVENT_ASR_END:
                mOnSpeechListener.onEndSpeech();
                break;
            //识别结果
            case SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL:
                Log.i("DataPrint", params);
                break;
            //识别结束（可能含有错误信息）
            case SpeechConstant.CALLBACK_EVENT_ASR_FINISH:
                Log.i("DataPrint", params);
                break;
            //音量回调
            case SpeechConstant.CALLBACK_EVENT_ASR_VOLUME:

                break;
            //识别结束 释放资源
            case SpeechConstant.CALLBACK_EVENT_ASR_EXIT:

                break;
        }
        String logTxt = "name: " + name;

        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
        }
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            if (params.contains("\"nlu_result\"")) {
                if (length > 0 && data.length > 0) {
                    logTxt += ", 语义解析结果：" + new String(data, offset, length);
                }
            }
        } else if (data != null) {
            logTxt += " ;data length=" + data.length;
        }
        if (params != null){
            try {
                JSONObject jsonObject = new JSONObject(params);
                //判断是不是唤起指令
                try {
                    String desc = (String) jsonObject.get("errorDesc");
                    if (desc.equals("wakeup success")){
                        mOnSpeechListener.onWakeup();
                        startSpeechRecog();
                    }
                }catch (JSONException e){
                    try {
                        int noMatch = (int) jsonObject.get("sub_error");
                        if (noMatch == 7001 || noMatch == 3101){
                            mOnSpeechListener.onNotSpeech();
                        }
                    }catch (JSONException je){
                        String result = (String) jsonObject.get("result_type");
                        if (result != null){
                            //检测到说话 并显示最佳的识别语句
                            if (result.equals("final_result")){
                                mOnSpeechListener.onResult((String) jsonObject.get("best_result"));
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i("DataPrint", logTxt);
    }
}
