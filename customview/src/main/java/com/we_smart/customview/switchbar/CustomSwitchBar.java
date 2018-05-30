package com.we_smart.customview.switchbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.we_smart.customview.R;

/**
 *  Created by zhaol on 2018/4/19.
 */

public class CustomSwitchBar extends View implements View.OnTouchListener {

    private int DEFAULT_WIDTH;
    private int DEFAULT_HEIGHT;
    //view的宽高
    private int mWidth;
    private int mHeight;
    private Context mContext;
    /**************    背景   ***************/
    private Paint mBgPaint;
    private int mBgHeight;
    private int mBgColor;   //背景颜色

    private Paint mBgStrokePaint;
    private int mBgStrokeColor;  //背景线框颜色
    private int mBgStrokeWidth;  //背景线框粗细

    /**************    滑块   ***************/
    private Paint mThumbPaint;
    private int mThumbColor;    //滑块颜色
    private int mThumbMargin;   //滑块与边框的margin距离 默认紧贴边框
    private float mThumbX;    //滑块当前中心点位置
    private int mThumbHeight;   //滑块高度
    private int mThumbWidth;    //滑块宽度
    private float mThumbStartX;   //滑块开始X坐标
    private float mThumbEndX;     //滑块停止X坐标

    /**************    文字   ***************/
    private Paint mLeftTextPaint;
    private Paint mRightTextPaint;
    private int mTextColor;
    private int mTextSelectColor;
    private float mTextSize;
    private String mLeftText;
    private String mRightText;

    private OnSwitchBarValueChangeListener mValueChangeListener = new OnSwitchBarValueChangeListener() {
        @Override
        public void onSwitch(boolean isLeft) {

        }
    };

    public CustomSwitchBar(Context context) {
        super(context);
        this.mContext = context;

        initData();
        initView();
    }

    public CustomSwitchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        DEFAULT_WIDTH = dp2px(200);
        DEFAULT_HEIGHT = dp2px(50);

        initAttrsSet(attrs);
        initView();
    }

    private void initData() {
        DEFAULT_WIDTH = dp2px(200);
        DEFAULT_HEIGHT = dp2px(50);

        mBgStrokeColor = Color.WHITE;
        mBgStrokeWidth = dp2px(1);
        mBgHeight = DEFAULT_HEIGHT;
        mThumbMargin = 0;
        mThumbColor = Color.WHITE;
        mTextColor = Color.BLACK;
        mTextSize = sp2px(15);
    }

    private void initView() {
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(mBgColor);

        mBgStrokePaint = new Paint();
        mBgStrokePaint.setAntiAlias(true);
        mBgStrokePaint.setColor(mBgStrokeColor);
        mBgStrokePaint.setStrokeWidth(mBgStrokeWidth);
        mBgStrokePaint.setStyle(Paint.Style.STROKE);

        mThumbPaint = new Paint();
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setColor(mThumbColor);

        mLeftTextPaint = new Paint();
        mLeftTextPaint.setAntiAlias(true);
        mLeftTextPaint.setColor(mTextSelectColor);
        mLeftTextPaint.setTextSize(mTextSize);

        mRightTextPaint = new Paint();
        mRightTextPaint.setAntiAlias(true);
        mRightTextPaint.setColor(mTextColor);
        mRightTextPaint.setTextSize(mTextSize);

        this.setOnTouchListener(this);
    }

    private void initAttrsSet(AttributeSet attrs) {
        mWidth = DEFAULT_WIDTH;
        mHeight = DEFAULT_HEIGHT;

        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomSwitchBar, 0, 0);
        mBgStrokeColor = a.getColor(R.styleable.CustomSwitchBar_stroke_color, Color.WHITE);
        mBgStrokeWidth = (int) a.getDimension(R.styleable.CustomSwitchBar_stroke_width, dp2px(1));
        mBgHeight = (int) a.getDimension(R.styleable.CustomSwitchBar_bg_height, mHeight);
        mThumbMargin = (int) a.getDimension(R.styleable.CustomSwitchBar_thumb_margin, 0);
        mThumbColor = a.getColor(R.styleable.CustomSwitchBar_switch_thumb_color, Color.WHITE);
        mTextColor = a.getColor(R.styleable.CustomSwitchBar_text_color, Color.WHITE);
        mTextSelectColor = a.getColor(R.styleable.CustomSwitchBar_text_select_color, Color.BLACK);
        mTextSize = a.getDimension(R.styleable.CustomSwitchBar_text_size, sp2px(15));
        mLeftText = a.getString(R.styleable.CustomSwitchBar_text_left);
        mRightText = a.getString(R.styleable.CustomSwitchBar_text_right);

        mLeftText = mLeftText == null ? "NULL" : mLeftText;
        mRightText = mRightText == null ? "NULL" : mRightText;

        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) background;
            mBgColor = colorDrawable.getColor();
        }
        setBackground(null);
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
            mHeight = DEFAULT_HEIGHT + mBgStrokeWidth;
            mHeight = DEFAULT_HEIGHT + mBgStrokeWidth;
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize + mBgStrokeWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize + mBgStrokeWidth;
            mBgHeight = mHeight;
        }

        mThumbHeight = mHeight - mThumbMargin * 2;
        mThumbWidth = mWidth / 2 - mThumbMargin * 2;

        mThumbStartX = mThumbMargin + mThumbWidth / 2;
        mThumbEndX = mWidth - mThumbStartX;
        mThumbX = mThumbStartX;

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        drawBg(canvas);
        //绘制背景线框
        drawBgStroke(canvas);
        //绘制滑块
        drawThumb(canvas);

        //绘制左边文字
        drawLeftText(canvas);
        //绘制右边文字
        drawRightText(canvas);
        //话滑块中心点
        drawCenterPoint(canvas);
    }

    private void drawRightText(Canvas canvas) {
        mRightTextPaint.setColor(mThumbX > mWidth / 2
                ? mTextSelectColor : mTextColor);

        float textWidth = mRightTextPaint.measureText(mRightText);
        float x = mThumbEndX - textWidth / 2;

        Paint.FontMetrics metrics = mRightTextPaint.getFontMetrics();
        //metrics.ascent为负数
        float dy = -(metrics.descent + metrics.ascent) / 2;
        float y = mHeight / 2 + dy;
        canvas.drawText(mRightText, x, y, mRightTextPaint);
    }

    private void drawLeftText(Canvas canvas) {
        mLeftTextPaint.setColor(mThumbX < mWidth / 2
                ? mTextSelectColor : mTextColor);

        float textWidth = mLeftTextPaint.measureText(mLeftText);
        float x = mThumbStartX - textWidth / 2;

        Paint.FontMetrics metrics = mLeftTextPaint.getFontMetrics();
        //metrics.ascent为负数
        float dy = -(metrics.descent + metrics.ascent) / 2;
        float y = mHeight / 2 + dy;
        canvas.drawText(mLeftText, x, y, mLeftTextPaint);
    }

    private void drawCenterPoint(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(mThumbX, mHeight / 2, 2, paint);
    }

    private void drawThumb(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = mThumbX - mThumbWidth / 2;
        rectF.top = mHeight / 2 - mThumbHeight / 2;
        rectF.right = mThumbX + mThumbWidth / 2;
        rectF.bottom = mHeight / 2 + mThumbHeight / 2;
        canvas.drawRoundRect(rectF, mThumbHeight / 2, mThumbHeight / 2, mThumbPaint);
    }

    private void drawBg(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = mBgStrokeWidth;
        rectF.top = mHeight / 2 - mBgHeight / 2 + mBgStrokeWidth;
        rectF.right = mWidth - mBgStrokeWidth;
        rectF.bottom = mHeight / 2 + mBgHeight / 2 - mBgStrokeWidth;
        canvas.drawRoundRect(rectF, mBgHeight / 2, mBgHeight / 2, mBgPaint);
    }

    private void drawBgStroke(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = mBgStrokeWidth / 2;
        rectF.top = mHeight / 2 - mBgHeight / 2 + mBgStrokeWidth / 2;
        rectF.right = mWidth - mBgStrokeWidth / 2;
        rectF.bottom = mHeight / 2 + mBgHeight / 2 - mBgStrokeWidth / 2;
        canvas.drawRoundRect(rectF, mBgHeight / 2, mBgHeight / 2, mBgStrokePaint);
    }

    private int dp2px(int dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    boolean isInThumb = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //判断是否点击在滑块中
                isInThumb = x > mThumbX - mThumbWidth / 2 && x < mThumbX + mThumbWidth / 2;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInThumb) {
                    if (x < mThumbStartX) {
                        mThumbX = mThumbStartX;
                    } else if (x > mThumbEndX) {
                        mThumbX = mThumbEndX;
                    } else {
                        mThumbX = x;
                    }

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                //判断点击左右
                if (x < mWidth / 2) {
                    setThumbWithAnimation(false);
                } else {
                    setThumbWithAnimation(true);
                }
                break;
        }
        return true;
    }

    private void setThumbWithAnimation(boolean isLeft) {
        ValueAnimator animator = ValueAnimator.ofFloat(this.mThumbX, isLeft ? mThumbEndX : mThumbStartX);
        animator.setDuration(300);
        animator.setInterpolator(new OvershootInterpolator(0.6f));
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mThumbX = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getResult(mThumbX);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void getResult(float x) {
        if (x > mWidth / 2) {
            mValueChangeListener.onSwitch(false);
        } else {
            mValueChangeListener.onSwitch(true);
        }
    }

    public interface OnSwitchBarValueChangeListener {
        void onSwitch(boolean isLeft);
    }
}
