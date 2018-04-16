package com.we_smart.customview;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhaol on 2018/4/16.
 */

public class CustomLayout extends ViewGroup{
    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //摆放子View
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int curHeight = 0;
        for (int i = 0; i < childCount; i++){
            View childView = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();

            int height = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            int width = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;

            childView.layout(l + params.leftMargin, curHeight + params.topMargin, l + width, curHeight + height);
            curHeight += height;
        }
    }

    //测量子view高度 让viewgroup高度跟随变化
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() > 0){
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);

            if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(getChildrenMaxWidth(), getChildrenTotalHeight());
            }else if (widthMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(getChildrenMaxWidth(), heightSize);
            }else if (heightMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(widthSize, getChildrenTotalHeight());
            }
        }
    }

    private int getChildrenTotalHeight(){
        int height = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++){
            View childView = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            height += childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
        }
        return height;
    }

    private int getChildrenMaxWidth(){
        int maxWidth = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++){
            View childView = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;

            if (childWidth > maxWidth){
                maxWidth = childWidth;
            }
        }
        return maxWidth;
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }
}
