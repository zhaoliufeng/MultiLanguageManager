package com.we_smart.multilanguage;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * 多语言管理器
 */

public class MultiLanguageManager {

    private MultiLanguageSpUtils mSpUtils;
    private Context mContext;

    public MultiLanguageManager(Context context){
        if (mSpUtils == null){
            mSpUtils = new MultiLanguageSpUtils(context);
        }
        mContext = context;
    }

    public void init(){
        //设置当前语言 没有选择则为系统默认
        changeCurrentLocale(mContext.getResources(), new Locale(mSpUtils.getLanguage(), mSpUtils.getCountry()));
    }

    //获取当前语言环境
    public Locale getCurrentLocale(){
        return  new Locale(mSpUtils.getLanguage(), mSpUtils.getCountry());
    }

    /**
     * 修改当前系统语言
     * @param resources 系统资源
     * @param locale 修改语言
     */
    public void changeCurrentLocale(Resources resources, Locale locale){
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();

        configuration.locale = locale;
        resources.updateConfiguration(configuration, dm);

        mSpUtils.saveLanguageType(locale.toString());
    }

}
