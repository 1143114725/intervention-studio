package com.investigate.newsupper.service.recordserver;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.NoViewAcitvity;

/**
 * 通话时刷新通知栏的服务
 * Created by EEH on 2018/7/19.
 */
@SuppressLint("NewApi") public class CallListenerService extends Service {
    private static final String TAG = "CallListenerService";
    Handler handler = new Handler();
    Context mService = CallListenerService.this;
    private int recLen = 0;


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen++;
            Log.i(TAG, "run: " + getStringTime(recLen));
            String title = "访问专家通话中。。。";
            String content = ""+getStringTime(recLen);
            sendNotification(mService,title,content,RecordUtils.NotificationID);
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//    	String type = intent.getStringExtra("type");
//    	if (type.equals("start")) {
    		handler.postDelayed(runnable, 1000);
//		}else if (type.equals("stop")) {
//			CallListenerService.this.onDestroy();
//		}
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        handler.removeCallbacks(runnable);
//        clearNotification(mService,RecordUtils.NotificationID);
        
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return null;
    }

    /**
     * 正即使，传入秒数返回一个显示的时长{格式自己定义可以}
     * @param cnt   传入的秒数{90s}
     * @return      返回当前秒数所代表的时长 {00：01：30}
     */
    private String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }


    /**
     * 发送最简单的通知,该通知的ID = 999999
     */
private void sendNotification(Context mActivity,String title,String content,int NotificationID){
        Intent resultIntent = new Intent(this, NoViewAcitvity.class);
//        通知栏上面按钮的点击事件
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("")
                // Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
                // Notification.DEFAULT_SOUND：系统默认铃声。
                // Notification.DEFAULT_VIBRATE：系统默认震动。
                // Notification.DEFAULT_LIGHTS：系统默认闪光
                .setDefaults(Notification.FLAG_ONGOING_EVENT)
                // 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失
                .setAutoCancel(false)
                // 将Ongoing设为true 那么notification将不能滑动删除
                .setOngoing(true)
                // 从Android4.1开始，可以通过以下方法，设置notification的优先级，优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
                .setPriority(Notification.PRIORITY_MAX)
                .setContentText("");
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remoteviews);
        //设置标题文字
        remoteViews.setTextViewText(R.id.title, title);
        //设置内容文字
        remoteViews.setTextViewText(R.id.con, content);
        //设置按钮的点击事件
        remoteViews.setOnClickPendingIntent(R.id.mybtn, resultPendingIntent);
        builder.setContent(remoteViews);

        NotificationManager notifyManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        builder.build().flags = Notification.FLAG_AUTO_CANCEL;
        notifyManager.notify(NotificationID, builder.build());
    }

    /**
     * 取消指定Id的Notification
     * @param mActivity
     * @param NotificationID
     */
    private void clearNotification(Context mActivity,int NotificationID){
        NotificationManager manager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NotificationID);
        
        
    }
}
