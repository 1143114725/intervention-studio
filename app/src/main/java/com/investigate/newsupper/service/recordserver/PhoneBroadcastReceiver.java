package com.investigate.newsupper.service.recordserver;

import com.investigate.newsupper.global.MyApp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 监听电话通话状态的广播
 */
public class PhoneBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneBroadcastReceiver";
    Context context;
    @Override
    public void onReceive(Context context, Intent intent){
        // TODO Auto-generated method stub
    	this.context = context;
        Log.i(TAG, "onReceive: 启动自定义BroadcastReceiver");
        System.out.println("action"+intent.getAction());
//        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            //如果是去电（拨出）
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "onReceive: 拨出"+phoneNumber);
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
//        }
    }
    PhoneStateListener listener=new PhoneStateListener(){

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
            //state 当前状态 incomingNumber,貌似没有去电的API
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(TAG, "onCallStateChanged: 挂断:来电号码"+incomingNumber);
                    RecordUtils.getInstance().stopRecord(context);
                    
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(TAG, "onCallStateChanged: 接听:来电号码"+incomingNumber);
                    System.out.println("接听:来电号码"+incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "onCallStateChanged: 响铃:来电号码"+incomingNumber);
                    //输出来电号码
                    break;
            }
        }
    };

}
