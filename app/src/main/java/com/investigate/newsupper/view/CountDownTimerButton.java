package  com.investigate.newsupper.view;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义 倒计时按钮 可设置倒计时时间  文字  以及倒计时过程中的文字
 * Created by EEH on 2018/1/22.
 */

public class CountDownTimerButton extends Button implements View.OnClickListener{

    private Context mContext;
    private OnClickListener mOnClickListener;
    private Timer mTimer;//调度器
    private TimerTask mTask;
    private long duration = 10000;//倒计时时长 设置默认10秒
    private long temp_duration;
    private String clickBeffor = "下一页";//点击前
    private String clickAfter = "秒后进入下一页";//点击后

    public CountDownTimerButton(Context context) {
        super(context);
        mContext = context;
        setOnClickListener(this);
    }

    public CountDownTimerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnClickListener(this);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String showtext = temp_duration/1000 + clickAfter;
            CountDownTimerButton.this.setText(showtext);
            temp_duration -= 1000;
            if (temp_duration < 0) {//倒计时结束
                CountDownTimerButton.this.setEnabled(true);
                CountDownTimerButton.this.setText(clickBeffor);
                stopTimer();
            }
        }
    };
    @Override
    public void setOnClickListener(OnClickListener onClickListener) {//提供外部访问方法
        if (onClickListener instanceof CountDownTimerButton) {
            super.setOnClickListener(onClickListener);
        }else{
            this.mOnClickListener = onClickListener;
        }
    }

    @Override
    public void onClick(View view) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(view);
        }
//        startTimer();
    }

    //计时开始
    public void startTimer(){
        temp_duration = duration;
        CountDownTimerButton.this.setEnabled(false);
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0x01);
            }
        };
        mTimer.schedule(mTask, 0, 1000);//调度分配，延迟0秒，时间间隔为1秒
    }

    //计时结束
    private void stopTimer(){
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 设置倒计时时间 单位/s
     * @param duration
     */
    public void setDuration(long duration) {
        this.duration = duration*1000;
    }


    /**
     * 倒计时前的文字
     * @param clickBeffor
     */
    public void setClickBeffor(String clickBeffor) {
        this.clickBeffor = clickBeffor;
    }

    /**
     * 倒计时进行的时候时间后的文字
     * @param clickAfter
     */
    public void setClickAfter(String clickAfter) {
        this.clickAfter = clickAfter;
    }
}