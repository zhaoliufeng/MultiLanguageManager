package com.we_smart.test;

import android.app.Application;

import com.we_smart.multilanguage.MultiLanguageManager;

/**
 * Created by zhaol on 2018/3/28.
 */

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new MultiLanguageManager(this).init();
    }
}
