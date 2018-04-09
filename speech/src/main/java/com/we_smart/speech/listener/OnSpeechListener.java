package com.we_smart.speech.listener;

/**
 * Created by zhaol on 2018/4/9.
 */

public interface OnSpeechListener {
    //开始说话
    void onBeginSpeech();

    //没有说话
    void onNotSpeech();

    //发生错误
    void onError(String errorDesc, int errorCode);

    //监听的结果
    void onResult(String result);

    //音量变化
    void onVolumeChange(int volume);

    //结束说话
    void onEndSpeech();

    //识别完成
    void onFinish();

    //语音唤起成功
    void onWakeup();
}
