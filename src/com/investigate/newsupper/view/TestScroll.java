package com.investigate.newsupper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
/**
 * zz解决滑动条与HorizontalScrollView不兼容的问题
 * @author Administrator
 *
 */
public class TestScroll extends HorizontalScrollView {
	private GestureDetector mGestureDetector;
	private static double SCROLL_ANGLE = 90;

	public TestScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new YScrollDetecotr());
		setFadingEdgeLength(0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetecotr extends SimpleOnGestureListener implements OnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// if(distanceY != 0 && distanceX != 0){
			//
			// }
			// if(Math.abs(distanceY) >= Math.abs(distanceX)){
			// System.out.println("distanceX = " + distanceX + " , distanceY = "
			// + distanceY);
			// return true;
			// }
			// return false;

			double angle = Math.atan2(Math.abs(distanceY), Math.abs(distanceX));
			// System.out.println("angle-->" + (180 * angle) / Math.PI);
			if ((180 * angle) / Math.PI < 180) {
				return false;
			}

			return false;
		}
	}

}
