package com.investigate.newsupper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class UIEditText extends EditText {

    public UIEditText(Context context) {
        super(context);
    }

    public UIEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
