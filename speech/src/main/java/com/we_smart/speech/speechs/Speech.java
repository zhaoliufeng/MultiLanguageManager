package com.we_smart.speech.speechs;

import android.content.Context;

import com.we_smart.speech.listener.OnSpeechListener;

/**
 * Created by zhaol on 2018/4/10.
 */

public abstract class Speech {
    String appId;
    Context mContext;
    OnSpeechListener mOnSpeechListener;

    Speech(Context context) {
        this.mContext = context;
        initSpeech();
    }

    Speech(Context context, String appId) {
        this.mContext = context;
        this.appId = appId;
        initSpeech();
    }

    //初始化引擎
    protected abstract void initSpeech();

    //开始识别语音
    public abstract void startSpeechRecog();

    //释放引擎
    public abstract void release();

    //添加监听
    public void setOnSpeechListener(OnSpeechListener onSpeechListener){
        mOnSpeechListener = onSpeechListener;
    }
}
