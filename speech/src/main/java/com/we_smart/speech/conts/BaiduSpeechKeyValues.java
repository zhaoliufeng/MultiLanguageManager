package com.we_smart.speech.conts;

/**
 * Created by Zhao Liufeng on 2018/4/9.
 */

public class BaiduSpeechKeyValues {

    //result_type : final_result 代表最终解析结果
    public static final String VALUE_FINAL_RESULT = "final_result";
    //识别最佳结果
    public static final String VALUE_BEST_RESULT = "best_result";
    //没有说话错误码 3101
    public static final int VALUE_NO_SPEECH_CODE = 3101;
    //没有网络错误码 2100
    public static final int VALUE_NO_NETWORK_CODE = 2100;

    //音量百分比 0 - 100
    public static final String KEY_VOLUME_PERCENT = "volume-percent";
    //当前音量值 float类型
    public static final String KEY_VOLUME = "volume";
    //识别结果类型 pair/final
    public static final String KEY_TYPE_RESULT = "result_type";
    //错误码
    public static final String KEY_ERROR_CODE = "sub_error";
    //错误描述
    public static final String KEY_ERROR_DESC = "desc";
}
