package com.investigate.newsupper.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {

	//MediaPlayer mp;
	//Uri uri;
	@Override
	public void onReceive(Context context, Intent intent) {
		if ("arui.alarm.action".equals(intent.getAction())) {
			//没有运行这个线程的话
			if (!DiaryService.isRun) {
				Intent i = new Intent();
				i.setClass(context, DiaryService.class);
				// 启动service
				// 多次调用startService并不会启动多个service 而是会多次调用onStart
				context.startService(i);
			}
		}
	}

}
