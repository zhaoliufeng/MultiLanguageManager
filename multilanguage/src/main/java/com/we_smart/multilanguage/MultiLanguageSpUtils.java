package com.we_smart.multilanguage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

/**
 * 多语言缓存
 */

public class MultiLanguageSpUtils {

    private static SharedPreferences mSharePreferences;

    //sp默认key
    private static final String SP_KEY = "sp_key";
    //默认语言key
    private static final String LANGUAGE_DEFAULT_KEY = "default_key";
    //默认语言key
    private static final String LANGUAGE_KEY = "language_key";
    //默认语言
    private static Locale mLocaleDefault = Locale.getDefault();

    MultiLanguageSpUtils(Context context){
        init(context);
    }

    //初始化
    private void init(Context context) {
        mSharePreferences = context.getSharedPreferences
                (SP_KEY, Context.MODE_PRIVATE);
    }

    /**
     * 获取当前语言环境
     * @return 当前保存的语言环境
     */
    private String getLanguageType(){
        return getString(LANGUAGE_KEY, mLocaleDefault.toString());
    }

    //获取国家
    String getCountry(){
        if (getLanguageType().length() > 2){
            return getLanguageType().substring(3, 5);
        }
        return "";
    }

    //获取语言
    String getLanguage(){
        return getLanguageType().substring(0, 2);
    }

    /**
     * 保存当前语言环境
     * @param type 需要保存的语言类型
     */
    void saveLanguageType(String type){
       saveString(LANGUAGE_KEY, type);
    }

    /**
     * 设置默认语言
     * @param type 需要保存的默认语言
     */
    public void setDefaultLanguageType(String type){
        saveString(LANGUAGE_DEFAULT_KEY, type);
    }

    private void saveString(String key, String value){
        SharedPreferences.Editor editor = mSharePreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getString(String key, String defaultVal){
        return mSharePreferences.getString(key, defaultVal);
    }
}
