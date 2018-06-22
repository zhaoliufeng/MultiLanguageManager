package com.we_smart.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by zhaol on 2018/5/9.
 */

public class ThemeImageView extends android.support.v7.widget.AppCompatImageView {
    private String mResName;
    private Context mContext;
    private Theme mCurrTheme;

    public ThemeImageView(Context context) {
        super(context);
    }

    public ThemeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ThemeImageView, 0, 0);
        mResName = a.getString(R.styleable.ThemeImageView_res_name);
    }

    public void changeTheme(Theme theme) {
        mCurrTheme = theme;
        String imgName = mResName + theme.mExtendStr;
        int iconId = getResources().getIdentifier(imgName, "drawable", mContext.getPackageName());
        setImageResource(iconId);
    }

    public Theme getTheme(){
        return mCurrTheme;
    }
}
