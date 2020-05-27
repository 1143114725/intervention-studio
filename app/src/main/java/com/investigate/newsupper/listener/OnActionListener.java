package com.investigate.newsupper.listener;

import android.view.MotionEvent;

public interface OnActionListener {
    void onDown(MotionEvent ev);

    void onLeftScroll();

    void onRightScroll();
}
