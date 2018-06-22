package com.we_smart.customview;

/**
 * Created by zhaol on 2018/5/9.
 */

public enum Theme {
    defaultTheme(""),
    redTheme("_r"),
    greenTheme("_g");

    String mExtendStr;

    Theme(String extendStr) {
        mExtendStr = extendStr;
    }
}
