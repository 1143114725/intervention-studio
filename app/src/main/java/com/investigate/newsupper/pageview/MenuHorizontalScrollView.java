package com.investigate.newsupper.pageview;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class MenuHorizontalScrollView extends HorizontalScrollView {
	
	/*当前控件*/
	private MenuHorizontalScrollView me;
	
	/*菜单*/
	private LinearLayout menu;
	
	/*菜单状态*/
	public static boolean menuOut;
	
	/*扩展宽度*/
	private final int ENLARGE_WIDTH = 80;
	
	/*菜单的宽度*/
	private int menuWidth;
	
	/*手势动作最开始时的x坐标*/
	private float lastMotionX = -1;
	
	/*按钮*/
	private LinearLayout menuBtn;
		
	/*当前滑动的位置*/
	private int current;
	
	private int scrollToViewPos;
	
	public int isScorll=0;//是否滑动
	
	public int screenWidth;
	public int morePx;
	public MenuHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public MenuHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public MenuHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		this.setHorizontalFadingEdgeEnabled(false);
		this.setVerticalFadingEdgeEnabled(false);
		this.me = this;
		this.me.setVisibility(View.INVISIBLE);
		menuOut = false;
	}
	
	public void initViews(View[] children, SizeCallBack sizeCallBack, LinearLayout menu, int screenWidth, int morePx) {
		this.menu = menu;
		this.screenWidth = screenWidth;
		this.morePx = morePx;
		ViewGroup parent = (ViewGroup) getChildAt(0);

		for (int i = 0; i < children.length; i++) {
			children[i].setVisibility(View.INVISIBLE);
			parent.addView(children[i]);
		}
		this.menuWidth = screenWidth - morePx;
		OnGlobalLayoutListener onGlLayoutistener = new MenuOnGlobalLayoutListener(parent, children, sizeCallBack);
		getViewTreeObserver().addOnGlobalLayoutListener(onGlLayoutistener);

	}
	
	 @Override
	 public boolean onInterceptTouchEvent(MotionEvent ev){
	        return false;
	 }
	 
	 /**
	  * 设置按钮
	  * @param btn 
	  */
	 public void setMenuBtn(LinearLayout btn){
		 this.menuBtn = btn;
	 } 
	 
	 
	 public void clickMenuBtn(){
//		 System.out.println("menuOut:"+menuOut);
		 if(!menuOut){
			 this.menuWidth = 0;
		 }
		 else{
			 this.menuWidth = screenWidth - morePx;
		 }
		// System.out.println("menuWidth="+menuWidth+"morePx="+morePx+"screenWidth="+screenWidth);
		 menuSlide();
	 }
	 
	 /**
	  * 滑动出菜单
	  */
	 public void menuSlide(){
		 if(this.menuWidth == 0){
			 menuOut = true;
		 }
		 else{
			 menuOut = false;
		 }
		 me.smoothScrollTo(this.menuWidth, 0);
//		 if(menuOut == true)
//			 
//		//	 this.menuBtn.setBackgroundResource(R.drawable.icon_menu);
////			 this.menuBtn.setBackgroundResource(R.drawable.menu_fold);
//		 else
//		//	 this.menuBtn.setBackgroundResource(R.drawable.icon_menu);
////			 this.menuBtn.setBackgroundResource(R.drawable.menu_unfold);
	 }
	 
	 
//	@Override
//	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//		// TODO Auto-generated method stub
//		System.out.println("onScrollChanged()");
//		super.onScrollChanged(l, t, oldl, oldt);
//		if (l < (screenWidth - morePx) / 2) {
//			System.out.println("onScrollChanged()__yes"+screenWidth);
//			this.menuWidth = 0;
//			//menuSlide();
//		} else {
//			System.out.println("onScrollChanged()__no"+screenWidth);
//			this.menuWidth = screenWidth - morePx;
//		}
//		this.current = l;
//	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (1 == isScorll) {
			return true;
		}
		// TODO Auto-generated method stub
		int x = (int) ev.getRawX();
		System.out.println("x:" + x);
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			/* 手指按下时的x坐标 */
			this.lastMotionX = (int) ev.getRawX();
		}
		if ((this.current == 0 && x < this.scrollToViewPos)
				|| (this.current == this.scrollToViewPos * 2 && x > this.ENLARGE_WIDTH)) {
			return false;
		}
		if (menuOut == false && this.lastMotionX > 1000) {
			return true;
		} else {
			if (ev.getAction() == MotionEvent.ACTION_UP) {
				menuSlide();
				return false;
			}
		}
		return super.onTouchEvent(ev);
	}


	/****************************************************/
	/*-												   -*/
	/*-			Class 			Area				   -*/
	/*-												   -*/
	/****************************************************/		
	
	public class MenuOnGlobalLayoutListener implements OnGlobalLayoutListener {
		
		private ViewGroup parent;
		private View[] children;
		//private int scrollToViewIndex = 0;
		private SizeCallBack sizeCallBack;
		
		public MenuOnGlobalLayoutListener(ViewGroup parent, View[] children, SizeCallBack sizeCallBack) {
			
			this.parent = parent;
			this.children = children;
			this.sizeCallBack = sizeCallBack;
		}

		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			me.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	        this.sizeCallBack.onGlobalLayout();
	        this.parent.removeViewsInLayout(0, children.length);
	        int width = me.getMeasuredWidth();
	        int height = me.getMeasuredHeight();
	        
	        int[] dims = new int[2];
	        scrollToViewPos = 0;
	        
	        for(int i = 0; i < children.length; i++){
	        	this.sizeCallBack.getViewSize(i, width, height, dims);
	        	children[i].setVisibility(View.VISIBLE);
	            
	        	parent.addView(children[i], dims[0], dims[1]);
	            if(i == 0){
	                scrollToViewPos += dims[0];
	            }
	        }
	        //if(firstLoad){
	        	new Handler().post(new Runnable(){
		            @Override
		            public void run(){
		            	//-1000已用 
		            	me.scrollBy(scrollToViewPos, 0);
		            	/*视图不是中间视图*/
		            	me.setVisibility(View.VISIBLE);
		                menu.setVisibility(View.VISIBLE);
		            }
		        });
	        //}
	        
	    }
	}

}
