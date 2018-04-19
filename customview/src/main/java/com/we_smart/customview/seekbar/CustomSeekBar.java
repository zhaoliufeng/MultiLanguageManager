package com.we_smart.customview.seekbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.we_smart.customview.R;

/**
 *  Created by zhaol on 2018/4/16.
 */

public class CustomSeekBar extends View implements View.OnTouchListener {

    public static final String TAG = "CustomSeekBar";
    private final int DEFAULT_WIDTH;
    private final int DEFAULT_HEIGHT;
    //view的宽高
    private int mWidth;
    private int mHeight;
    //当前进度 0 - 100;
    private int mCurrProcess;
    private OnSeekBarDragListener mSeekBarDragListener = new OnSeekBarDragListener() {
        @Override
        public void startDrag() {

        }

        @Override
        public void dragging(int process) {

        }

        @Override
        public void stopDragging(int endProcess) {

        }
    };     //拖动事件监听

    /************   滑块  ***********/
    private Paint mThumbPaint;  //滑块画笔
    private int mThumbColor = Color.GRAY;   //滑块颜色
    private int mThumbWidth;   //滑块高度
    private float mThumbX;    //滑块的X位置
    private float mProcessStartX; //进度条开始位置
    private float mProcessEndX;     //进度条结束位置

    /************   进度条背景  ***********/
    private Paint mProcessBgPaint;     //进度背景画笔
    private int mProcessBgColor = Color.WHITE;
    private int mProcessBgHeight;   //进度条背景高度

    /************   进度条  ***********/
    private Paint mProcessPaint;
    private int mProcessColor = Color.BLACK;
    private int mProcessHeight;   //进度条高度

    private Context mContext;

    public CustomSeekBar(Context context) {
        super(context);
        this.mContext = context;

        DEFAULT_WIDTH = dp2px(350);
        DEFAULT_HEIGHT = dp2px(20);
        initData();
        initView();
    }

    public CustomSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        DEFAULT_WIDTH = dp2px(350);
        DEFAULT_HEIGHT = dp2px(20);
        initAttrsSet(attrs);
        initView();
    }

    private void initAttrsSet(AttributeSet attrs) {
        mWidth = DEFAULT_WIDTH;
        mHeight = DEFAULT_HEIGHT;
        mCurrProcess = 0;

        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomSeekBar, 0, 0);
        mThumbColor = a.getColor(R.styleable.CustomSeekBar_thumb_color, Color.GRAY);
        mProcessColor = a.getColor(R.styleable.CustomSeekBar_process_color, Color.BLACK);
        mProcessBgColor = a.getColor(R.styleable.CustomSeekBar_process_bg_color, Color.WHITE);
        mProcessBgHeight = (int) a.getDimension(R.styleable.CustomSeekBar_process_bg_height, DEFAULT_HEIGHT/2);
        mProcessHeight = (int) a.getDimension(R.styleable.CustomSeekBar_process_height, mProcessBgHeight);
        mThumbWidth = (int) a.getDimension(R.styleable.CustomSeekBar_thumb_radius, DEFAULT_HEIGHT/2) * 2;

        mThumbX = mThumbWidth / 2;
        mProcessStartX = mThumbWidth / 2;
        mProcessEndX = mWidth - mThumbWidth / 2;
    }

    private void initData() {
        mWidth = DEFAULT_WIDTH;
        mHeight = DEFAULT_HEIGHT;
        mCurrProcess = 0;

        mThumbWidth = DEFAULT_HEIGHT;
        mProcessBgHeight = mThumbWidth / 2;
        mProcessHeight = mProcessBgHeight;

        mThumbX = mThumbWidth / 2;

        mProcessStartX = mThumbWidth / 2;
        mProcessEndX = mWidth - mThumbWidth / 2;
    }

    private void initView() {
        mThumbPaint = new Paint();
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setColor(mThumbColor);

        mProcessBgPaint = new Paint();
        mProcessBgPaint.setAntiAlias(true);
        mProcessBgPaint.setColor(mProcessBgColor);

        mProcessPaint = new Paint();
        mProcessPaint.setAntiAlias(true);
        mProcessPaint.setColor(mProcessColor);

        this.setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST &&
                heightMode == MeasureSpec.AT_MOST) {
                mHeight = mThumbWidth;
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }

        mProcessEndX = mWidth - mThumbWidth / 2;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制进度条背景
        drawProcessBg(canvas);
        //绘制进度条
        drawProcessForeground(canvas);
        //绘制滑块
        drawThumb(canvas);
    }

    /**
     * 绘制当前seekbar进度条背景
     */
    private void drawProcessBg(Canvas canvas) {
        RectF rectF = new RectF();
        //进度条起点是滑块宽的一半
        rectF.left = mProcessStartX;
        rectF.top = mHeight / 2 - mProcessBgHeight / 2;
        rectF.right = mWidth - mThumbWidth / 2;
        rectF.bottom = mHeight / 2 + mProcessBgHeight / 2;
        int radius = mProcessBgHeight / 2;

        canvas.drawRoundRect(rectF, radius, radius, mProcessBgPaint);
    }

    /**
     * 绘制当前seekbar进度条
     */
    private void drawProcessForeground(Canvas canvas) {
        RectF rectF = new RectF();
        //进度条起点是滑块宽的一半
        rectF.left = mProcessStartX;
        rectF.top = mHeight / 2 - mProcessHeight / 2;
        rectF.right = mThumbX;
        rectF.bottom = mHeight / 2 + mProcessHeight / 2;
        int radius = mProcessHeight / 2;
        canvas.drawRoundRect(rectF, radius, radius, mProcessPaint);
    }

    /**
     * 绘制seekbar滑块
     */
    private void drawThumb(Canvas canvas) {
        //滑块一半的高度 作为画圆形滑块时的半径
        int thumbHalfHeight = mThumbWidth / 2;

        canvas.drawCircle(mThumbX, mHeight / 2, thumbHalfHeight, mThumbPaint);
    }

    private int dp2px(int dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int px2dp(int px) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSeekBarDragListener.startDrag();
                break;
            case MotionEvent.ACTION_MOVE:
                //滑块坐标上限为 X = thumb Radius 下限为 X = mWidth - thumb Radius
                if (x < mProcessStartX) {
                    mThumbX = mProcessStartX;
                } else if (x > mProcessEndX) {
                    mThumbX = mProcessEndX;
                } else {
                    mThumbX = x;
                }
                mCurrProcess = (int) ((mThumbX - mProcessStartX)/(mProcessEndX - mProcessStartX) * 100);
                invalidate();
                mSeekBarDragListener.dragging(mCurrProcess);
                break;
            case MotionEvent.ACTION_UP:
                mSeekBarDragListener.stopDragging(mCurrProcess);
                break;
        }
        return true;
    }

    /**
     * 获取进度
     * @return 进度
     */
    public int getProcess() {
        return mCurrProcess;
    }

    /**
     * 设置进度
     * @param process 当前进度
     */
    public void setProcess(int process){
        synchronized (this){
            this.mCurrProcess = process;
            this.mThumbX = (mCurrProcess / 100.0f) * (mProcessEndX - mProcessStartX) + mProcessStartX;
            invalidate();
            mSeekBarDragListener.stopDragging(mCurrProcess);
        }
    }

    /**
     * 带动画设置当前进度
     * @param process 当前进度
     */
    public void setProcessWithAnimation(int process){
        synchronized (this){
            this.mCurrProcess = process;
            ValueAnimator animator = ValueAnimator.ofFloat(this.mThumbX,
                    (mCurrProcess / 100.0f) * (mProcessEndX - mProcessStartX) + mProcessStartX);
            animator.setDuration(300);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mThumbX = (float) animation.getAnimatedValue();
                    postInvalidate();
                    mSeekBarDragListener.dragging((int) ((mThumbX - mProcessStartX)/(mProcessEndX - mProcessStartX) * 100));
                }
            });
            mSeekBarDragListener.stopDragging(this.mCurrProcess);
        }
    }

    /**
     * 设置滑块颜色
     */
    public void setThumbColor(int color){
        this.mThumbColor = color;
        this.mThumbPaint.setColor(color);
        invalidate();
    }

    /**
     * 设置进度条颜色
     */
    public void setProcessColor(int color){
        this.mProcessColor = color;
        this.mProcessPaint.setColor(color);
        invalidate();
    }

    public void setProcessLinearBgShader(int[] doughnutColors){
        this.mProcessBgPaint.setShader(new LinearGradient(
                mProcessStartX,
                mHeight / 2 - mProcessHeight / 2,
                mProcessEndX,
                mHeight / 2 + mProcessBgHeight / 2,
                doughnutColors, null, Shader.TileMode.CLAMP));
        invalidate();
    }

    /**
     * 设置背景进度条颜色
     */
    public void setProcessBgColor(int color){
        this.mProcessBgColor = color;
        this.mProcessBgPaint.setColor(color);
        invalidate();
    }

    public void setOnSeekBarDragListener(OnSeekBarDragListener onSeekBarDragListener) {
        this.mSeekBarDragListener = onSeekBarDragListener;
    }
}
