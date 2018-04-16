package com.we_smart.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 测试自定义控件
 * Created by zhaol on 2018/4/16.
 */

public class CustomView extends View {

    private final String TAG = "CustomView";

    private final int DEFAULT_SIZE = 100;

    private int mWidth = 0;
    private int mHeight = 0;

    private Context mContext;

    public CustomView(Context context) {
        super(context);
        initView(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        this.mContext = context;
        mWidth = dp2px(context, DEFAULT_SIZE);
        mHeight = dp2px(context, DEFAULT_SIZE);
    }

    //测量 当width < height 时 width = height
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        //如果width mode 和 height mode都是 warp-content/AT_MOST 则设置默认大小 100 * 100
        if (wMode == MeasureSpec.AT_MOST
                && hMode == MeasureSpec.AT_MOST){
            w = dp2px(mContext, DEFAULT_SIZE);
            h = dp2px(mContext, DEFAULT_SIZE);
        }else {
            switch (wMode) {
                case MeasureSpec.EXACTLY:
                    if (hMode == MeasureSpec.AT_MOST){
                        h = w;
                    }else {
                        w = w < h ? h : w;
                    }
                    break;
                case MeasureSpec.AT_MOST:
                    w = h;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    w = dp2px(mContext, DEFAULT_SIZE);
                    h = dp2px(mContext, DEFAULT_SIZE);
                    break;
            }
        }
        mWidth = w;
        mHeight = h;
        setMeasuredDimension(mWidth, mHeight);
    }

    private int px2dp(Context context, int px){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    private int dp2px(Context context, int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        Log.i(TAG, "scale " + scale);
        return (int) (dp * scale + 0.5f);
    }
}
