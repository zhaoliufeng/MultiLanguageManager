package com.we_smart.speech.speechs;

import android.content.Context;
import android.os.Bundle;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.sunflower.FlowerCollector;
import com.we_smart.speech.uitls.JsonParser;

/**
 * Created by zhaol on 2018/4/10.
 */

public class XunfeiSpeech extends Speech{

    private SpeechRecognizer mIat;

    /**
     * @param context 上下文对象
     * @param appId AppId
     */
    public XunfeiSpeech(Context context, String appId) {
        super(context, appId);
    }

    /**
     * 初始化科大讯飞语音
     */
    @Override
    protected void initSpeech() {
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID + "=" + appId);
        mIat = SpeechRecognizer.createRecognizer(mContext, null);
        mIat.setParameter(com.iflytek.cloud.SpeechConstant.ENGINE_TYPE, com.iflytek.cloud.SpeechConstant.TYPE_CLOUD);
        mIat.setParameter(com.iflytek.cloud.SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(com.iflytek.cloud.SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(com.iflytek.cloud.SpeechConstant.ACCENT, "mandarin ");
        mIat.setParameter(com.iflytek.cloud.SpeechConstant.VAD_EOS, "1000");
    }

    /**
     * 开始识别语音
     */
    @Override
    public void startSpeechRecog() {
        FlowerCollector.onEvent(mContext, "iat_recognize");
        mIat.startListening(mRecognizerListener);
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            if (mOnSpeechListener != null)
                mOnSpeechListener.onBeginSpeech();
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (mOnSpeechListener == null) return;
            if (error.getErrorCode() == 10118) {
                mOnSpeechListener.onNotSpeech();
            } else {
                mOnSpeechListener.onError(error.getPlainDescription(true), error.getErrorCode());
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            if (mOnSpeechListener == null) return;
            mOnSpeechListener.onEndSpeech();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            if (mOnSpeechListener == null) return;
            String text = JsonParser.parseIatResult(results.getResultString());
            // TODO 最后的结果
            if ("。".equals(text) || "？".equals(text) || "，".equals(text)) {
                mOnSpeechListener.onFinish();
                return;
            }
            mOnSpeechListener.onResult(text);
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            if (mOnSpeechListener == null) return;
            mOnSpeechListener.onVolumeChange(volume == 0 ? 10 : volume + 40);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };

    /**
     * 释放引擎
     */
    @Override
    public void release() {
        if (null != mIat) {
            mIat.cancel();
            mIat.destroy();
        }
    }
}
