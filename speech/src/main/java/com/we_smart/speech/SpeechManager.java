package com.we_smart.speech;

import android.content.Context;

import com.we_smart.speech.listener.OnSpeechListener;
import com.we_smart.speech.speechs.BaiduSpeech;
import com.we_smart.speech.speechs.Speech;
import com.we_smart.speech.speechs.XunfeiSpeech;

/**
 * Created by zhaol on 2018/4/9.
 */

public class SpeechManager {

    private OnSpeechListener mOnSpeechListener;

    private Speech speech;

    private static SpeechManager mSpeechManager;

    public static SpeechManager getInstance() {
        if (mSpeechManager == null) {
            synchronized (SpeechManager.class) {
                if (mSpeechManager == null) {
                    mSpeechManager = new SpeechManager();
                }
            }
        }
        return mSpeechManager;
    }

    public SpeechManager initWithBaidu(Context context) {
        speech = new BaiduSpeech(context);
        setSpeechListener();
        return mSpeechManager;
    }

    public SpeechManager initWithBaidu(Context context, String appId, String appKey, String secretKey) {
        speech = new BaiduSpeech(context, appId, appKey, secretKey);
        setSpeechListener();
        return mSpeechManager;
    }

    public SpeechManager initWithXunfei(Context context) {
        speech = new XunfeiSpeech(context, "591bbd25");
        setSpeechListener();
        return mSpeechManager;
    }

    public SpeechManager initWithXunfei(Context context, String appId) {
        speech = new XunfeiSpeech(context, appId);
        setSpeechListener();
        return mSpeechManager;
    }

    private void setSpeechListener() {
        speech.setOnSpeechListener(new OnSpeechListener() {
            @Override
            public void onBeginSpeech() {
                mOnSpeechListener.onBeginSpeech();
            }

            @Override
            public void onNotSpeech() {
                mOnSpeechListener.onNotSpeech();
            }

            @Override
            public void onError(String errorDesc, int errorCode) {
                mOnSpeechListener.onError(errorDesc, errorCode);
            }

            @Override
            public void onResult(String result) {
                mOnSpeechListener.onResult(result);
            }

            @Override
            public void onVolumeChange(int volume) {
                mOnSpeechListener.onVolumeChange(volume);
            }

            @Override
            public void onEndSpeech() {
                mOnSpeechListener.onEndSpeech();
            }

            @Override
            public void onFinish() {
                mOnSpeechListener.onFinish();
            }

            @Override
            public void onWakeup() {
                mOnSpeechListener.onWakeup();
            }
        });
    }

    public void startSpeechRecog() {
        speech.startSpeechRecog();
    }

    public void setOnSpeechListener(OnSpeechListener onSpeechListener) {
        this.mOnSpeechListener = onSpeechListener;
    }

    public void destroy(){
        speech.release();
    }
}
