package com.we_smart.speech;

import android.content.Context;

import com.we_smart.speech.listener.OnSpeechListener;

/**
 * Created by zhaol on 2018/4/9.
 */

public class SpeechManager {

    private Context mContext;
    private OnSpeechListener mOnSpeechListener;
    private BaiduSpeech baiduSpeech;

    //初始化
    public SpeechManager(Context context) {
        mContext = context;
        baiduSpeech = new BaiduSpeech(context);
        baiduSpeech.setOnSpeechListener(new OnSpeechListener() {
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
            public void onFinish(String text) {
                mOnSpeechListener.onFinish(text);
            }

            @Override
            public void onWakeup() {
                mOnSpeechListener.onWakeup();
            }
        });
    }

    public void startSpeechRecog(){
        baiduSpeech.startSpeechRecog();
    }

    public void setOnSpeechListener(OnSpeechListener onSpeechListener) {
        this.mOnSpeechListener = onSpeechListener;
    }
}
