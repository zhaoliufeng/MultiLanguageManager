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
    //错误域
    public static final String KEY_ERROR = "error";
    //错误描述
    public static final String KEY_ERROR_DESC = "desc";

    /*************  错误域   *****************/
    public static final int VALUE_SUCCESS = 0;

    //网络超时 出现原因可能为网络已经连接但质量比较差，建议检测网络状态
    public static final int VALUE_ERROR_NET_TIMEOUT = 1;

    //网络连接失败 出现原因可能是网络权限被禁用，或网络确实未连接，需要开启网络或检测无法联网的原因
    public static final int VALUE_ERROR_CONNECT_FAIL = 2;

    //音频错误 出现原因可能为：未声明录音权限，或 被安全软件限制，或 录音设备被占用，需要开发者检测权限声明。
    public static final int VALUE_ERROR_AUDIO = 3;

    //协议错误 出现原因可能是appid和appkey的鉴权失败
    public static final int VALUEERROR_PROXY_ERR = 4;

    //超时 未开启长语音时，当输入语音超过60s时，会报此错误
    public static final int VALUE_ERROR_TIMEOUT = 6;

    //没有识别结果
    public static final int VALUE_ERROR_NO_RESULT = 7;

    //缺少权限
    public static final int VALUE_ERROR_LACK_PERMISSION = 9;

    //其他错误
    public static final int VALUE_ERROR_OTHER = 10;
}
