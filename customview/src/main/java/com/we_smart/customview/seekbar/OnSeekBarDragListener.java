package com.we_smart.customview.seekbar;

/**
 * Created by zhaol on 2018/4/17.
 */

public interface OnSeekBarDragListener {
    //开始拖动
    void startDrag();
    //拖动中
    void dragging(int process);
    //停止拖动
    void stopDragging(int endProcess);
}
