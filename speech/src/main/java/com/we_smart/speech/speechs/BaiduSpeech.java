package com.we_smart.speech.speechs;

import android.content.Context;
import android.text.TextUtils;
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

import static com.we_smart.speech.conts.BaiduSpeechKeyValues.*;

/**
 * Created by zhaol on 2018/4/9.
 */

public class BaiduSpeech extends Speech implements EventListener {
    //语音识别
    private EventManager asr;
    //语音唤醒
    private EventManager wakeup;

    private String appKey;
    private String secretKey;


    /**
     * @param context 上下文
     */
    public BaiduSpeech(Context context) {
        super(context);
    }

    /**
     * 动态设置语音识别appid
     *
     * @param context   上下文
     * @param appId     语音识别AppID
     * @param appKey    AppKey
     * @param secretKey SecretKey
     */
    public BaiduSpeech(Context context, String appId, String appKey, String secretKey) {
        super(context, appId);
        this.appKey = appKey;
        this.secretKey = secretKey;
    }

    /**
     * 初始化百度语音
     */
    @Override
    protected void initSpeech() {
        asr = EventManagerFactory.create(mContext, "asr");
        asr.registerListener(this);

        wakeup = EventManagerFactory.create(mContext, "wp");
        wakeup.registerListener(this);
    }

    /**
     * 开始识别语音
     */
    @Override
    public void startSpeechRecog() {
        Map<String, Object> params = new LinkedHashMap<>();

        if (appId != null) {
            params.put(SpeechConstant.APP_ID, appId);
            params.put(SpeechConstant.APP_KEY, appKey);
            params.put(SpeechConstant.SECRET, secretKey);
        }
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, true);
        String json = new JSONObject(params).toString();
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
        Log.i("DataPrint", "输入参数：" + json);
    }

    /**
     * 打开唤醒功能
     */
    private void openWakeup() {
        Map<String, Object> params = new TreeMap<>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");

        String json = new JSONObject(params).toString();
        wakeup.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
        Log.i("DataPrint", "输入参数：" + json);
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        if (TextUtils.isEmpty(params))
            return;
        try {
            JSONObject jsonObject = new JSONObject(params);
            switch (name) {
                //引擎准备就绪，可以开始说话
                case SpeechConstant.CALLBACK_EVENT_ASR_READY:
                    mOnSpeechListener.onBeginSpeech();
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
                    //判断是否是最终结果
                    if (VALUE_FINAL_RESULT.equals(jsonObject.getString(KEY_TYPE_RESULT))) {
                        //获取best result
                        mOnSpeechListener.onResult(jsonObject.getString(VALUE_BEST_RESULT));
                        Log.i("DataPrint_Res", params);
                    }
                    Log.i("DataPrint", params);
                    break;
                //识别结束（可能含有错误信息）
                case SpeechConstant.CALLBACK_EVENT_ASR_FINISH:
                    //根据错误码判断有没有说话
                    switch (jsonObject.getInt(KEY_ERROR)) {
                        case VALUE_SUCCESS:
                            break;
                        case VALUE_ERROR_AUDIO:
                        case VALUE_ERROR_NO_RESULT:
                            mOnSpeechListener.onNotSpeech();
                            Log.i("DataPrint", "没有说话");
                            break;
                       default:
                            mOnSpeechListener.onError(jsonObject.getString(KEY_ERROR_DESC), jsonObject.getInt(KEY_ERROR_CODE));
                            break;
                    }
                    mOnSpeechListener.onFinish();
                    Log.i("DataPrint", params);
                    break;
                //音量回调
                case SpeechConstant.CALLBACK_EVENT_ASR_VOLUME:
                    mOnSpeechListener.onVolumeChange(jsonObject.getInt(KEY_VOLUME_PERCENT));
                    Log.i("DataPrint_Vol", String.valueOf(jsonObject.getInt(KEY_VOLUME)));
                    break;
                //识别结束 释放资源
                case SpeechConstant.CALLBACK_EVENT_ASR_EXIT:

                    break;
                case SpeechConstant.CALLBACK_EVENT_WAKEUP_READY:
                    Log.i("WakeUp", "唤醒准备完毕 " + params);
                    break;
                case SpeechConstant.CALLBACK_EVENT_WAKEUP_STARTED:
                    Log.i("WakeUp", "唤醒开启 " + params);
                    break;
                case SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS:
                    Log.i("WakeUp", "唤醒成功 " + params);
                    break;
                case SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR:
                    Log.i("WakeUp", "唤醒失败 " + params);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放引擎
     */
    @Override
    public void release() {
        asr.unregisterListener(this);
        wakeup.unregisterListener(this);
    }
}
