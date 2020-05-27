package com.investigate.newsupper.view.adapterlinearlayout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;

import com.investigate.newsupper.R;
import com.investigate.newsupper.view.adapterlinearlayout.Pools.SimplePool;

public class AdapterLinearLayout extends LinearLayout {
    
    /**
     * 每一个Item点击事件的监听器
     */
    public interface OnItemClickListener {
        /**
         * 当item点击时调用
         * 
         * @param adapterView AdapterLinearLayout的实例
         * @param view 点击的View
         * @param position 点击的position
         */
        /*public*/ void onItemClick(AdapterLinearLayout adapterView, View view,
                int position);
    }

    /**
     * 布局参数
     */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        /**
         * 构造方法
         * 
         * @param width 宽
         * @param height 高
         */
        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    /**
     * Represents an invalid position. All valid positions are in the range 0 to
     * 1 less than the number of items in the current adapter.
     */
    public static final int INVALID_POSITION = -1;

    /**
     * Item之间的间隔
     */
    private int mSpace = 0;
    
    /**
     * 分隔线大小
     */
    private int mDividerSize = 0;
    
    /**
     * 选中的索引
     */
    private int mMontionIndex = INVALID_POSITION;
    
    /**
     * 按下的范围
     */
    private int mTouchSlop = 0;
    
    /**
     * 按下的Y的坐标
     */
    private int mMotionDownY = 0;
    
    /**
     * Up的Y的坐标
     */
    private int mMotionUpY = 0;
    
    /**
     * 选中的索引
     */
    private int mSelectedPosition = INVALID_POSITION;
    
    /**
     * 检查点击的标志
     */
    private boolean mIsPendingCheckTap = false;
    
    /**
     * 临时的Rect对象
     */
    private final Rect mTempRect = new Rect();
    
    /**
     * 临时的bound
     */
    private final Rect mBound = new Rect();
    
    /**
     * 分隔线的Drawable
     */
    private Drawable mDivider = null;
    
    /**
     * 按下的item效果
     */
    private Drawable mSelector = null;
    
    /**
     * 设置的Adapter
     */
    private Adapter mAdapter = null;
    
    /**
     * 点击的Runnable
     */
    private CheckForTap mPendingCheckForTap = null;
    
    /**
     * Touch up的runnable
     */
    private Runnable mPendingCheckForUp = null;
    
    /**
     * 数据变化的观察者
     */
    private ItemDataSetObserver mObserver = new ItemDataSetObserver();
    
    /**
     * View的回收池
     */
    private SimplePool<View> mViewRecycle = new SimplePool<View>(100);    //SUPPRESS CHECKSTYLE
    
    /**
     * 手势监听
     */
    private GestureDetector mGestureDetector = null;
    
    /**
     * Item的点击事件
     */
    private OnItemClickListener mItemClickListener = null;
    
    /**
     * 显示最后的分隔线
     */
    private boolean mShowLastDivider = false;
    
    /**
     * 构造方法
     * 
     * @param context context
     */
    public AdapterLinearLayout(Context context) {
        super(context);
        
        init(context, null);
    }
    
    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     */
    public AdapterLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init(context, attrs);
    }
    
    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AdapterLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        init(context, attrs);
    }
    
    /**
     * 初始化
     * 
     * @param context context
     */
    private void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);
        mGestureDetector = new GestureDetector(getContext(), mGestureListener);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AdapterLinearLayout, 0, 0);
            final Drawable d = a.getDrawable(R.styleable.AdapterLinearLayout_divider);
            if (d != null) {
                setDividerDrawable(d);
            }
            final int h = a.getDimensionPixelSize(R.styleable.AdapterLinearLayout_dividerHeight, 0);
            if (h > 0) {
                setDividerSize(h);
            }
            showLastDivider(a.getBoolean(R.styleable.AdapterLinearLayout_showLastDivider, false));
            a.recycle();
        }
        /*if (mDividerSize == 0) {
            float density = context.getResources().getDisplayMetrics().density;
            mDividerSize = (int) (1 * density);
        }*/
        mSpace = mDividerSize;
    }
    
    @Override
    protected void dispatchDraw(Canvas canvas) {
        // Draw the divider.
        if (LinearLayout.HORIZONTAL == getOrientation()) {
            drawHorizontalDividers(canvas);
        } else {
            drawVerticalDividers(canvas);
        }
        
        // Draw the selector.
        drawSelector(canvas);
        
        super.dispatchDraw(canvas);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_MOVE) {
            onMove(5);  // SUPPRESS CHECKSTYLE
        }
        
        return super.dispatchTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            // Helper method for lifted finger
            onUp(event);
        } else if (action == MotionEvent.ACTION_CANCEL) {
            onCancel(event);
        }
        
        // Give everything to the gesture detector
        boolean retValue = mGestureDetector.onTouchEvent(event);

        //super.onTouchEvent(event);
        return retValue;
    }
    
    /**
     * 设置Adapter
     * 
     * @param adapter Adapter对象
     */
    public void setAdapter(Adapter adapter) {
        if (null != mAdapter) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        
        mAdapter = adapter;
        
        if (null != mAdapter) {
            mAdapter.registerDataSetObserver(mObserver);
        }
        
        layoutChildren();
    }
    
    /**
     * 得到设置的Adapter对象
     *
     * @return Adapter对象
     */
    public Adapter getAdapter() {
        return mAdapter;
    }
    
    /**
     * 设置事件监听器
     * 
     * @param listener listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
    
    /**
     * 设置Selector
     * 
     * @param selector selector
     */
    public void setSelector(Drawable selector) {
        mSelector = selector;
        invalidate();
    }
    
    /**
     * 得到当前的Selector
     * 
     * @return Selector
     */
    public Drawable getSelector() {
        return mSelector;
    }
    
    /** 
     * 这个API是在3.0以后才有，所以不需要添加@Override标注
     * 
     * Set a drawable to be used as a divider between items.
     *
     * @param divider Drawable that will divide each item.
     * @see #setShowDividers(int)
     *
     * @attr ref android.R.styleable#LinearLayout_divider
     */
    //@Override
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setDividerDrawable(Drawable divider) {
        mDivider = divider;
        
        if (null != mDivider && mDivider instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) mDivider);
            if (LinearLayout.HORIZONTAL == getOrientation()) {
                setDividerSize(bitmapDrawable.getIntrinsicWidth());
            } else {
                setDividerSize(bitmapDrawable.getIntrinsicHeight());
            }
        }
        
        invalidate();
    }
    
    /**
     * 设置分隔线的大小
     * 
     * @param size 分隔线大小
     */
    public void setDividerSize(int size) {
        mDividerSize = size;
        
        if (mSpace != size) {
            mSpace = size;
            layoutChildren();
        }
        
        invalidate();
    }
    
    /**
     * 显示末尾的分隔线
     * 
     * @param showLastDivider showLastDivider
     */
    public void showLastDivider(boolean showLastDivider) {
        mShowLastDivider = showLastDivider;
    }
    
    /**
     * 选中某一个子项
     * 
     * @param position 选中项的索引
     */
    public void selectChild(int position) {
        mSelectedPosition = position;
        
        int count = getChildCount();
        for (int index = 0; index < count; ++index) {
            View child = getChildAt(index);
            if (null != child) {
                child.setSelected(index == position);
            }
        }
    }
    
    /**
     * 得到当前选中项的索引
     * 
     * @return 选中项的索引
     */
    public int getSelectedPosition() {
        return mSelectedPosition;
    }
    
    /**
     * 设置子项之间的间隔
     * 
     * @param space 间隔
     */
    public void setSpace(int space) {
        if (mSpace != space) {
            mSpace = space;
            layoutChildren();
        }
    }
    
    /**
     * 得到间隔
     * 
     * @return 间隔
     */
    public int getSpace() {
        return mSpace;
    }
    
    /**
     * 处理Up事件
     * 
     * @param e MotionEvent对象
     */
    protected void onUp(MotionEvent e) {
        mMotionUpY = (int) e.getY();
        int delta = mMotionUpY - mMotionDownY;
        if (Math.abs(delta) > mTouchSlop) {
            onMove(delta);
        }
        
        if (null == mPendingCheckForUp) {
            mPendingCheckForUp = new Runnable() {
                @Override
                public void run() {
                    reset();
                    positionSelector(INVALID_POSITION);
                    setPressed(false);
                    invalidate();
                }
            };
        }
        
        int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View child = getChildAt(i);
            if (null != child) {
                child.setPressed(false);
            }
        }
        
        if (mIsPendingCheckTap) {
            postDelayed(mPendingCheckForUp, ViewConfiguration.getTapTimeout());
        } else {
            mPendingCheckForUp.run();
        }
        
        mIsPendingCheckTap = false;
    }
    
    /**
     * 处理Cancel事件
     * 
     * @param e MotionEvent对象
     */
    protected void onCancel(MotionEvent e) {
        onUp(e);
    }
    
    /**
     * 处理Move事件
     * 
     * @param delta 偏移量
     */
    protected void onMove(int delta) {
        if (Math.abs(delta) > mTouchSlop) {
            removeCallbacks(mPendingCheckForTap);
            reset();
        }
    }
    
    /**
     * 处理Down事件
     * 
     * @param e MotionEvent对象
     * @return 是否处理
     */
    protected boolean onDown(MotionEvent e) {
        mMotionDownY = (int) e.getY();
        final int x = (int) e.getX();
        final int y = (int) e.getY();
        final int position = pointToPosition(x, y);
        if (position >= 0 && position < mAdapter.getCount()) {
            final View child = getChildAt(position);
            if (null != child) {
                if (null == mPendingCheckForTap) {
                    mPendingCheckForTap = new CheckForTap() {
                        @Override
                        public void run() {
                            setPressed(true);
                            positionSelector(getPosition());
                            invalidate();
                            mIsPendingCheckTap = false;
                        }
                    };
                }
                
                child.setPressed(true);
                mPendingCheckForTap.setPostion(position);
                postDelayed(mPendingCheckForTap, ViewConfiguration.getTapTimeout());
                mIsPendingCheckTap = true;
            }
        }
        
        return true;
    }
    
    /**
     * 单击时被调用
     * 
     * @param e MotionEvent对象
     * @return 是否处理
     */
    protected boolean onSingleTapUp(MotionEvent e) {
        final int x = (int) e.getX();
        final int y = (int) e.getY();
        int position = pointToPosition(x, y);
        if (position >= 0 && position < mAdapter.getCount()) {
            performItemClick(getChildAt(position), position);
        }
        
        //onUp(e);
        
        return true;
    }
    
    /**
     * 根据位置得到索引
     * 
     * @param x 坐标x
     * @param y 坐标y
     * @return 指定位置处的项的索引，找不到时返回-1
     */
    public int pointToPosition(int x, int y) {
        final Rect frame = mBound;
        final int count = this.getChildCount();
        for (int index = count - 1; index >= 0; --index) {
            final View child = getChildAt(index);
            if (child.getVisibility() == View.VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return index;
                }
            }
        }
        
        return -1;
    }
    
    @Override
    protected void dispatchSetPressed(boolean pressed) {
        // Do nothing, in super method, it will call #setPressed() method 
        // of every child.
    }
    
    /**
     * 设置绘制selector的索引
     * 
     * @param motionPosition 索引
     */
    protected void positionSelector(int motionPosition) {
        mMontionIndex = motionPosition;
    }
    
    /**
     * 重置状态
     */
    private void reset() {
        mIsPendingCheckTap = false;
        positionSelector(INVALID_POSITION);
    }

    /**
     * 指定索引处的项点击事件
     * 
     * @param position 索引
     */
    protected void onItemClick(int position) {
        
    }
    
    /**
     * 执行项的点击事件
     * 
     * @param v View对象
     * @param position 索引
     */
    private void performItemClick(final View v, final int position) {
        Runnable mTouchModeReset = new Runnable() {
            @Override
            public void run() {
                onItemClick(position);
                playSoundEffect(SoundEffectConstants.CLICK);
                
                if (null != mItemClickListener) {
                    mItemClickListener.onItemClick(AdapterLinearLayout.this,
                            v, position);
                }
            }
        };
        
        postDelayed(mTouchModeReset,
                ViewConfiguration.getPressedStateDuration());
    }

    /**
     * 布局Children
     */
    protected void layoutChildren() {
        if (null == mAdapter) {
            removeAllViews();
            return;
        }

        // First recycle children.
        recycleChildren();

        int count = mAdapter.getCount();
        int space = mSpace;

        for (int position = 0; position < count; ++position) {
            View convertView = mViewRecycle.acquire();
            View view = mAdapter.getView(position, convertView, this);
            if (null == view) {
                throw new NullPointerException("The view can not be null.");
            }
            
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (null == params) {
                params = getChildLayoutParameter();
            } else if (!(params instanceof LinearLayout.LayoutParams)) {
                throw new IllegalArgumentException(
                        "The type of layout parameter must be LinearLayout.LayoutParams");
            }
            
            if (!mShowLastDivider) {
                if (position == count - 1) {
                    space = 0;
                }
            }

            // Apply the space to the right margin.
            if (LinearLayout.HORIZONTAL == getOrientation()) {
                ((LinearLayout.LayoutParams) params).rightMargin  = space;
            } else {
                ((LinearLayout.LayoutParams) params).bottomMargin = space;
            }
            
            view.setSelected(mSelectedPosition == position);
            addView(view, params);
        }
        
        // Clear the recycled views.
        mViewRecycle.clear();
    }
    
    /**
     * 得到默认的布局参数
     * 
     * @return 布局参数
     */
    private ViewGroup.LayoutParams getChildLayoutParameter() {
        ViewGroup.LayoutParams params = null;
        
        if (LinearLayout.HORIZONTAL == getOrientation()) {
            params = new AdapterLinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            params = new AdapterLinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        
        return params;
    }
    
    /**
     * 绘制selector
     * 
     * @param canvas 画布
     */
    private void drawSelector(Canvas canvas) {
        if (null == mSelector) {
            return;
        }
        
        final int position = mMontionIndex;
        final View child = getChildAt(position);
        if (null != child) {
            final Rect bounds = mTempRect;
            bounds.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
            mSelector.setBounds(bounds);
            mSelector.draw(canvas); 
        }
    }
    
    /**
     * 绘制水平的分隔线
     * 
     * @param canvas 画布
     */
    private void drawHorizontalDividers(Canvas canvas) {
        if (mDividerSize <= 0) {
            return;
        }
        int count = mShowLastDivider ? getChildCount() : (getChildCount() - 1);
        if (null != mDivider && count > 0) {
            int width = mDividerSize;
            int left = 0;
            int offset = (mSpace - width) / 2;
            View child = null;
            Rect bounds = mTempRect;
            bounds.top = getPaddingTop();
            bounds.bottom = bounds.top + getHeight() - getPaddingBottom();
            
            for (int i = 0; i < count; ++i) {
                child = getChildAt(i);
                left = child.getRight() + offset;
                bounds.left  = left;
                bounds.right = left + width;
                drawDivider(canvas, bounds);
            }
        }
    }
    
    /**
     * 绘制垂直分隔线
     * 
     * @param canvas 画布
     */
    private void drawVerticalDividers(Canvas canvas) {
        if (mDividerSize <= 0) {
            return;
        }
        int count = mShowLastDivider ? getChildCount() : (getChildCount() - 1);
        if (null != mDivider && count > 0) {
            int height = mDividerSize;
            int top = 0;
            int offset = (mSpace - height) / 2;
            View child = null;
            Rect bounds = mTempRect;
            bounds.left = getPaddingLeft();
            bounds.right = bounds.left + getWidth() - getPaddingRight();
            
            for (int i = 0; i < count; ++i) {
                child = getChildAt(i);
                top = child.getBottom() + offset;
                bounds.top  = top;
                bounds.bottom = top + height;
                drawDivider(canvas, bounds);
            }
        }
    }
    
    /**
     * 绘制分配线
     * 
     * @param canvas 画布
     * @param bounds 区域大小
     */
    private void drawDivider(Canvas canvas, Rect bounds) {
        // FIX bug, 对于2.x的系统，如果divider是一个ColorDrawable的话，那么它绘制是在整个Canvas上面绘制出color
        // 确保绘制的元素不超出bound区域
        final Drawable divider = mDivider;
        if (null != divider) {
            canvas.save();
            canvas.clipRect(bounds);
            divider.setBounds(bounds);
            divider.draw(canvas);
            canvas.restore();
        }
    }
    
    /**
     * 回收children
     */
    private void recycleChildren() {
        
        mViewRecycle.clear();
        
        int count = getChildCount();
        for (int index = 0; index < count; ++index) {
            View child = getChildAt(index);
            mViewRecycle.release(child);
        }
        
        removeAllViews();
    }
    
    /**
     * 单击的Runnable
     */
    abstract class CheckForTap implements Runnable {
        /**
         * 索引
         */
        private int mPosition = -1;
        
        /**
         * 设置索引
         * 
         * @param position 索引
         */
        public void setPostion(int position) {
            mPosition = position;
        }
        
        /**
         * 得到索引
         * 
         * @return 索引
         */
        public int getPosition() {
            return mPosition;
        }
    }
    
    /**
     * 手势监听器
     */
    private SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return AdapterLinearLayout.this.onDown(e);
        }
        
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return AdapterLinearLayout.this.onSingleTapUp(e);
        }
    };
    
    /**
     * 数据变化的观察者
     */
    private class ItemDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            layoutChildren();
        }
    };
}
