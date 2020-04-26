package com.investigate.newsupper.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.investigate.newsupper.R;
import com.investigate.newsupper.bean.Parameter;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.service.recordserver.RecordUtils;
import com.investigate.newsupper.util.Util;
/**
 * 内部名单确认界面
 * @author EraJi
 *
 */
public class CheckAddrActivity extends Activity {
	/**
	 * 内部名单显示的4个属性
	 */
	private TextView tvTo1, tvTo2, tvTo3, tvTo4,phone,mobile;
	private TextView tvAddTo1, tvAddTo2, tvAddTo3, tvAddTo4;
	private TextView add_addr_now;
	private Button left_btn, right_btn;
	private UploadFeed feed;
	private Survey survey;
	private ImageView callphone,callmobile;
	private String phonenum = "",mobilenum = "";
	private LinearLayout ll_phone,ll_mobile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_addr);
		add_addr_now = (TextView) findViewById(R.id.add_addr_now);
		feed=(UploadFeed) getIntent().getExtras().get("feed");
		survey=feed.getSurvey();
		tvAddTo1 = (TextView) findViewById(R.id.addto1_tv);
		tvAddTo2 = (TextView) findViewById(R.id.addto2_tv);
		tvAddTo3 = (TextView) findViewById(R.id.addto3_tv);
		tvAddTo4 = (TextView) findViewById(R.id.addto4_tv);

		phone = (TextView) findViewById(R.id.tv_phone);
		mobile = (TextView) findViewById(R.id.tv_mobile);
		
		ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
		ll_mobile = (LinearLayout) findViewById(R.id.ll_mobile);
		
		callphone = (ImageView) findViewById(R.id.img_callphone);
		callmobile = (ImageView) findViewById(R.id.img_callmobile);
		
		tvTo1 = (TextView) findViewById(R.id.tvTo1);
		tvTo2 = (TextView) findViewById(R.id.tvTo2);
		tvTo3 = (TextView) findViewById(R.id.tvTo3);
		tvTo4 = (TextView) findViewById(R.id.tvTo4);
		left_btn = (Button) findViewById(R.id.left_btn);
		right_btn = (Button) findViewById(R.id.right_btn);
		if (null != feed) {
			if (1 == survey.openStatus) {
//				 
//				System.out.println("parameter1::"+survey.getParameter1());
//				System.out.println("parameter2::"+survey.getParameter2());
//				System.out.println("parameter3::"+survey.getParameter3());
//				System.out.println("parameter4::"+survey.getParameter4());
				HashMap<String, Parameter> hm = feed.getInnerPanel().getPsMap();
				tvAddTo1.setText(null != hm.get("Parameter1") ? hm.get("Parameter1").getContent() : "");
				tvAddTo2.setText(null != hm.get("Parameter2") ? hm.get("Parameter2").getContent() : "");
				tvAddTo3.setText(null != hm.get("Parameter3") ? hm.get("Parameter3").getContent() : "");
				tvAddTo4.setText(null != hm.get("Parameter4") ? hm.get("Parameter4").getContent() : "");
				tvTo1.setText(survey.getParameter1() + ":");
				tvTo2.setText(survey.getParameter2() + ":");
				tvTo3.setText(survey.getParameter3() + ":");
				tvTo4.setText(survey.getParameter4() + ":");
			}
		}
		String addr="";
		if(!Util.isEmpty(feed.getVisitAddress())){
			addr=feed.getVisitAddress();
		}else if(!Util.isEmpty(feed.getLat())&&!Util.isEmpty(feed.getLng())){
			addr=getResources().getString(R.string.get_addr_fail);
		}else{
			addr=getResources().getString(R.string.get_addr_null);
		}
		add_addr_now.setText(addr);
		if(Util.isEmpty(tvAddTo1.getText())){
			tvTo1.setVisibility(View.GONE);
		}
		if(Util.isEmpty(tvAddTo2.getText())){
			tvTo2.setVisibility(View.GONE);
		}
		if(Util.isEmpty(tvAddTo3.getText())){
			tvTo3.setVisibility(View.GONE);
		}
		if(Util.isEmpty(tvAddTo4.getText())){
			tvTo4.setVisibility(View.GONE);
		}
		
		
		String immer = feed.getParametersStr();
		ArrayList<Parameter> pip = (ArrayList<Parameter>) JSON.parseArray(immer, Parameter.class);
		if (!Util.isEmpty(pip)) {
			for (int i = 0; i < pip.size(); i++) {
				if (pip.get(i).getSid().equals("Phone")) {
					phonenum = pip.get(i).getContent();
					if (!Util.isEmpty(phonenum)) {
						phone.setText(phonenum);
					}else {
						ll_phone.setVisibility(View.GONE);
					}
				}else if (pip.get(i).getSid().equals("Mobile")) {
					mobilenum = pip.get(i).getContent();
					if (!Util.isEmpty(mobilenum)) {
						mobile.setText(mobilenum);
					}else {
						ll_mobile.setVisibility(View.GONE);
					}
				}
			}	
		}else {
			ll_phone.setVisibility(View.GONE);
			ll_mobile.setVisibility(View.GONE);
		}
		ma = (MyApp) getApplication();
		//拨打手机号
		callphone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("打电话给："+ phonenum);
//				phonenum = "10086";
//				sendNotification(CheckAddrActivity.this, "title", "content", 666666666);
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenum));
				String path;
				/**
				 * 录音地址变更 如果存在 SDCARD 并且 大小有100M 的空间
				 */
				// 存到本地 SDcard 中
				path = Util.getRecordPath(feed.getSurveyId());
				//新建个UploadFeed
				UploadFeed callrecord = new UploadFeed();
				
				callrecord.setUserId(feed.getUserId());
				callrecord.setFeedId(feed.getFeedId());
				callrecord.setSurveyId(feed.getSurveyId());
				callrecord.setUuid(feed.getUuid());
				callrecord.setIsUploaded(feed.getIsUploaded());
				callrecord.setIsCompleted(feed.getIsCompleted());
				callrecord.setVisitMode(feed.getVisitMode());
				
				callrecord.setPath(path);
				callrecord.setName(Util.getCallRecordName(
            		  feed.getUserId(), 
            		  feed.getSurveyId(),
            		  feed.getUuid(),
            		  phonenum));
				
				int inner = 0;
				String num = "";
				ma.dbService.addcallRecord(//
						feed.getUserId(), //
						feed.getSurveyId(), //
						feed.getUuid(), //
						path, //
						callrecord.getName(), //
						System.currentTimeMillis(), //
						Cnt.FILE_TYPE_MP3, //
						num,//
						inner,
					feed.getFeedId());
				RecordUtils.getInstance().startCallRecord(CheckAddrActivity.this, path, callrecord.getName(),ma);
				
				
				startActivity(intent);
			}
		});
		//拨打电话号
		callmobile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("打电话给："+ mobilenum);
//				shownotification();
//				mobilenum = "10086";
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobilenum));
				String path;
				/**
				 * 录音地址变更 如果存在 SDCARD 并且 大小有100M 的空间
				 */
				// 存到本地 SDcard 中
				path = Util.getRecordPath(feed.getSurveyId());
				//新建个UploadFeed
				UploadFeed callrecord = new UploadFeed();
				
				callrecord.setUserId(feed.getUserId());
				callrecord.setFeedId(feed.getFeedId());
				callrecord.setSurveyId(feed.getSurveyId());
				callrecord.setUuid(feed.getUuid());
				callrecord.setIsUploaded(feed.getIsUploaded());
				callrecord.setIsCompleted(feed.getIsCompleted());
				callrecord.setVisitMode(feed.getVisitMode());
				
				callrecord.setPath(path);
				callrecord.setName(Util.getCallRecordName(
            		  feed.getUserId(), 
            		  feed.getSurveyId(),
            		  feed.getUuid(),
            		  mobilenum));
				
				int inner = 0;
				String num = "";
				ma.dbService.addcallRecord(//
						feed.getUserId(), //
						feed.getSurveyId(), //
						feed.getUuid(), //
						path, //
						callrecord.getName(), //
						System.currentTimeMillis(), //
						Cnt.FILE_TYPE_MP3, //
						num,//
						inner,
						feed.getFeedId());
				RecordUtils.getInstance().startCallRecord(CheckAddrActivity.this, path, callrecord.getName(),ma);
				
				
				startActivity(intent);
				
			}
		});
		
	}     
	
	private MyApp ma;
	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.right_btn:
			if (true) {
				Intent it=null;
				if(0==survey.showpageStatus){
					it = new Intent(this, NativeModeNoPageActivity.class);
				}else {
					it = new Intent(this, NativeModeActivity.class);
				}
				Bundle bundle = new Bundle();
				bundle.putSerializable("feed", feed);
				it.putExtras(bundle);
				this.startActivity(it);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				finish();
			}
			break;
		}
	}

	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	}
	
	
	
	/**
     * 发送最简单的通知,该通知的ID = 999999
     */
    private void sendNotification(Context mActivity,String title,String content,int NotificationID){
//        Intent resultIntent = new Intent(this, NoViewAcitvity.class);
//        通知栏上面按钮的点击事件
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
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
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentText("");
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remoteviews);
        //设置标题文字
        remoteViews.setTextViewText(R.id.title, title);
        //设置内容文字
        remoteViews.setTextViewText(R.id.con, content);
        //设置按钮的点击事件
//        remoteViews.setOnClickPendingIntent(R.id.mybtn, resultPendingIntent);
        builder.setContent(remoteViews);

        NotificationManager notifyManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        builder.build().flags = Notification.FLAG_AUTO_CANCEL;
        notifyManager.notify(NotificationID, builder.build());
    }
    
    /**
     * 测试notification
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN) @SuppressLint("NewApi") private void shownotification() {

        /**
         * 发送最简单的通知,该通知的ID = 999999
         */
            Intent resultIntent = new Intent(this, NoViewAcitvity.class);
//        通知栏上面按钮的点击事件
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this)
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
            remoteViews.setTextViewText(R.id.title, "访问专家");
            //设置内容文字
            remoteViews.setTextViewText(R.id.con, "通话录音中。。。");
            //设置按钮的点击事件
            remoteViews.setOnClickPendingIntent(R.id.mybtn, resultPendingIntent);
            builder.setContent(remoteViews);

            NotificationManager notifyManager = (NotificationManager) CheckAddrActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
            builder.build().flags = Notification.FLAG_AUTO_CANCEL;
            notifyManager.notify(1, builder.build());

    }
    
    
}
