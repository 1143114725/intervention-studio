package com.investigate.newsupper.activity;

import com.investigate.newsupper.service.recordserver.CallListenerService;
import com.investigate.newsupper.service.recordserver.RecordUtils;

import android.content.Intent;
import android.util.Log;

/**
 * 没有界面的activity   用来取消通知栏，结束通话录音等操作
 * Created by EEH on 2018/7/17.
 */
public class NoViewAcitvity extends BaseActivity {
    private static final String TAG = "NoViewAcitvity";
	@Override
	public void init() {
		// TODO Auto-generated method stub
		Log.i(TAG, "initView: 取消通知栏");
//        NotificationManager manager = (NotificationManager) 
//        		NoViewAcitvity.this.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.cancel(RecordUtils.NotificationID);
//
//        
//        Log.i(TAG, "setListener: 关闭录音！");
//        MediaRecorderManager MRManager = MediaRecorderManager.getInstance();
//        MRManager.stopRecordAndFile();
		RecordUtils.getInstance().stopRecord(NoViewAcitvity.this);
        Intent sint = new Intent(this,CallListenerService.class);
        stopService(sint);
        
        finish();
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
}
