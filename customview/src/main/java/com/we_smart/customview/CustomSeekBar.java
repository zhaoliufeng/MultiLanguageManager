package com.we_smart.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by zhaol on 2018/4/16.
 */

public class CustomSeekBar extends View {

    private final int DEFAULT_WIDTH = 300;
    private final int DEFAULT_HEIGHT = 10;
    private Context mContext;

    public CustomSeekBar(Context context) {
        super(context);
        initView(context);
    }

    public CustomSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST &&
                heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(dp2px(DEFAULT_WIDTH), dp2px(DEFAULT_HEIGHT));
        }
    }

    /**
     * @return 控件测量后的宽度
     */
    private int getViewWidth(int widthMeasureSpec){

        return 0;
    }

    private int dp2px(int dp){
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int px2dp(int px){
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (px /scale + 0.5f);
    }
}
