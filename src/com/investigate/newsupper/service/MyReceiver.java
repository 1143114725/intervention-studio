package com.investigate.newsupper.service;

import com.investigate.newsupper.util.ServiceUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 *  <!-- 闹钟广播 -->
 * @author EraJi
 *
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        // TODO Auto-generated method stub
        String uuid = intent.getStringExtra("msg");
        Log.d("MyReceiver", "uuid="+uuid);
        
//        boolean isservice = ServiceUtils.isServiceRunning(context, "com.investigate.newsupper.service.MyLocation");
//    	if (isservice) {
//			Log.i("MyReceiver", "定位服务正在运行中。。。。。。。");
//		}else {
			Log.i("MyReceiver", "定位服务已经被杀死，重新启动中！！！！");
	        Intent intents = new Intent(context, MyLocation.class);
			Bundle bundle = new Bundle();
			bundle.putString("uuid", uuid);
			intents.putExtras(bundle);// 定位间隔
			context.startService(intent);
//		}
        
        
        

    }

}
