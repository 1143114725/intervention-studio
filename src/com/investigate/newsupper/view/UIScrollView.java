package com.investigate.newsupper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.investigate.newsupper.R;
import com.investigate.newsupper.listener.OnActionListener;

public class UIScrollView extends ScrollView {

	public UIScrollView(Context context) {
		this(context, null, 0);
	}

	public UIScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public UIScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		SCROLL_SLOP = getResources().getDimensionPixelSize(R.dimen.dimen_20dp);
	}

	private OnActionListener mOnActionListener;

	public void setOnActionListener(OnActionListener listener) {
		mOnActionListener = listener;
	}
	
	private boolean mIntercept;
	public void setIntercept(boolean isIntercept) {
		mIntercept = isIntercept;
	}

	// 获取点击事件
	private int x0 = 0;
	private int y0 = 0;
	private final int SCROLL_SLOP;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//System.out.println("dispatchTouchEvent");
		final int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			x0 = (int) ev.getX();
			y0 = (int) ev.getY();
			mOnActionListener.onDown(ev);
		}
		if (!mIntercept) {
			if (mOnActionListener != null && action == MotionEvent.ACTION_UP) {
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				int ys = y0 > y ? (y0 - y) : (y - y0);
				int xs = x0 > x ? (x0 - x) : (x - x0);
				if (ys >= xs) {
					return super.dispatchTouchEvent(ev);
				} else if (x0 - x > SCROLL_SLOP) {
					System.out.println("onLeftScroll()2");
					mOnActionListener.onLeftScroll();
					//return true;
				} 
			}
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//System.out.println("onTouchEvent");
		final int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			x0 = (int) ev.getX();
			y0 = (int) ev.getY();
			mOnActionListener.onDown(ev);
		}
		if (mIntercept) {
			if (mOnActionListener != null && ev.getAction() == MotionEvent.ACTION_UP) {
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				int ys = y0 > y ? (y0 - y) : (y - y0);
				int xs = x0 > x ? (x0 - x) : (x - x0);
				if (ys >= xs) {
					return super.onTouchEvent(ev);
				} else if (x0 - x > SCROLL_SLOP) {
					System.out.println("onLeftScroll()1");
					mOnActionListener.onLeftScroll();
					// return true;
					// } else if (x - x0 > SCROLL_SLOP) {
					// mOnActionListener.onRightScroll();
					// return true;
				}
			}
		}
		// return true;
		return super.onTouchEvent(ev);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		//System.out.println("onInterceptTouchEvent");
		if (mIntercept) {
			return !(ev.getAction() == MotionEvent.ACTION_DOWN);
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}

}
