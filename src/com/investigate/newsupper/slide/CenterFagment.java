package com.investigate.newsupper.slide;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.HomeActivity;
import com.investigate.newsupper.activity.LoginActivity;
import com.investigate.newsupper.activity.NoticeActivity;
import com.investigate.newsupper.activity.SubscibeActivity;
import com.investigate.newsupper.adapter.HomeAdapter;
import com.investigate.newsupper.bean.DapException;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.service.AlarmReceiver;
import com.investigate.newsupper.util.ComUtil;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.view.Toasts;

public class CenterFagment extends Fragment implements OnClickListener {

	private static final String TAG = CenterFagment.class.getSimpleName();
	
	LinearLayout leftIv, subscibeIv;
	GridView gridView;
	HomeAdapter adapter;
	private int width; // 屏幕 宽度
	private int height; // 屏幕 高度
	private MyApp ma;
	private ArrayList<Survey> ss;
	private LinearLayout tvNoLoaclList;
	private ImageView ivName;
	private int index = 0;// 问卷索引
	private UITextView mTitleView;

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 99:
				/** 更改发送邮箱为成功 */
				String fileName = (String) msg.obj;
				ma.dbService.updateTabToUnEnable(fileName);
				break;
			}
		}
	};

	public void onResume() {
		// 没登录直接显示
		initSurvey();
		super.onResume();
	};
	private Config cfg;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_activity, null);
		mTitleView = (UITextView) view.findViewById(R.id.textView1);
		TextSizeManager.getInstance().addTextComponent(TAG, mTitleView);
		leftIv = (LinearLayout) view.findViewById(R.id.directory);
		subscibeIv = (LinearLayout) view.findViewById(R.id.head_subscibe_iv);
		gridView = (GridView) view.findViewById(R.id.gridView1);
		tvNoLoaclList = (LinearLayout) view.findViewById(R.id.tvNoLoaclList);
		ivName = (ImageView) view.findViewById(R.id.ivName);
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取// 屏幕宽高
		width = dm.widthPixels;
		height = dm.heightPixels;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (width / 2.3), (int) (height / 4));
		ivName.setLayoutParams(params);
		ivName.setOnClickListener(this);
		ma = (MyApp) this.getActivity().getApplication();
		cfg = new Config(getActivity().getApplicationContext());
		
		// initSurvey();
		
		if(NetUtil.checkNet(this.getActivity())&&!Util.isEmpty(cfg.getString(Cnt.USER_ID, ""))){
			//提示页面
			Intent in=new Intent(CenterFagment.this.getActivity(),NoticeActivity.class);
			in.putExtra("notice", "1");//1代表不能操作
			startActivity(in);
		}else if(Util.isEmpty(cfg.getString(Cnt.USER_ID, ""))){
			Toast.makeText(getActivity(), R.string.no_login_no_compare, Toast.LENGTH_LONG).show();
		}else if(!NetUtil.checkNet(this.getActivity())){
			Toast.makeText(getActivity(), R.string.no_net_no_compare, Toast.LENGTH_LONG).show();
		}
		return view;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		TextSizeManager.getInstance().removeTextComponent(TAG);
	}

	// 一些初始化操作
	private void initSurvey() {
		// 自定义logo功能 请求下载
		//开关判断地图监控
		if (Util.isEmpty(ma.userId)) {
			ma.userId = ((null == ma.cfg) ? (ma.cfg = new Config(this.getActivity())) : (ma.cfg)).getString("userId", getResources().getString(R.string.user_name_test));
			if(Util.isEmpty(ma.userId)){
				ma.userId = getResources().getString(R.string.user_name_test);
			}
		}
		ss = ma.dbService.getAllDownloadedSurvey(ma.userId);
		
		if (Util.isEmpty(ma.userPwd)) {
			Survey survey = ma.dbService.getTextSurvey();// 获得试用问卷
			if (survey.surveyTitle == null) {

			} else {
				ss.add(survey);
			}
		}
		
		//存scid的list
		ArrayList<String> strlist = new ArrayList<String>();
		if (!Util.isEmpty(ss)) {
			//新的数组
			ArrayList<Survey> surveylist = new ArrayList<Survey>();
			for (int i = 0; i < ss.size(); i++) {
//				如果没有scid
				if (Util.isEmpty(ss.get(i).getSCID())) {
						surveylist.add(ss.get(i));
				}else{
//					如果有scid
					if (strlist.contains(ss.get(i).surveyId)) {
//						判断数组里有没有相同的scid
						if (strlist.size() == 0) {
							surveylist.add(ss.get(i));
							String[] scnum = ss.get(i).getSCNum().split(",");
							for (int j = 0; j < scnum.length; j++) {
								strlist.add(scnum[j]);
							}
						}
						
					}else {
						surveylist.add(ss.get(i));
						String[] scnum = ss.get(i).getSCNum().split(",");
						for (int j = 0; j < scnum.length; j++) {
							strlist.add(scnum[j]);
						}
					}
				}
			}
			for (int i = 0; i < strlist.size(); i++) {
				System.out.println("strlist-------------"+strlist.get(i));
				
			}
			
			
//			for (Survey s : ss) {
//				if (Util.isEmpty(s.getSCID())) {
//					surveylist.add(s);
//				} 
//				else {
//
//					if (strlist.size() == 0) {
//						surveylist.add(s);
//
//						String[] scnum = s.getSCNum().split(",");
//						for (int i = 0; i < scnum.length; i++) {
//							strlist.add(scnum[i]);
//						}
//
//					} else {
//						for (int i = 0; i < strlist.size(); i++) {
//							if (strlist.get(i).equals(s.surveyId)) {
//								System.out.println("1111111111111");
//							} else {
//								surveylist.add(s);
//								System.out.println("2222222222222");
//								String[] scnum = s.getSCNum().split(",");
//								for (int j = 0; j < scnum.length; j++) {
//									strlist.add(scnum[j]);
//								}
//							}
//						}
//					}
//				}
//			}
			
			
			/**
			 * test
			 */

//		ArrayList<String> strlist = new ArrayList<String>();
//		for (int i = 0; i < ss.size(); i++) {
//			System.out.println("ss suveryID"+ss.get(i).surveyId);
//		}
//		
//		if (!Util.isEmpty(ss)) {
//			//新的数组
//			ArrayList<Survey> surveylist = new ArrayList<Survey>();
//			
//			for (int i = 0; i < ss.size(); i++) {
//				if (Util.isEmpty(ss.get(i).getSCID())) {
//						surveylist.add(ss.get(i));
//				}else{
//					if (strlist.size() == 0) {
//						surveylist.add(ss.get(i));
//						
//						String[] scnum = ss.get(i).getSCNum().split(",");
//						for (int j = 0; j < scnum.length; j++) {
//							strlist.add(scnum[j]);
//						}
//					}else {
//						for (int j = 0; j < strlist.size(); j++) {
//							if (strlist.get(j).equals(ss.get(i).surveyId)) {
//								
//							}else {
//								surveylist.add(ss.get(i));
//								String[] scnum = ss.get(i).getSCNum().split(",");
//								for (int k = 0; k < scnum.length; k++) {
//									strlist.add(scnum[k]);
//								}
//							}
//						}
//					}
//				}
//			}
			
			
			
			for (int i = 0; i < surveylist.size(); i++) {
				System.out.println("listis suveryID"+surveylist.get(i).surveyId);
				
			}
			
			
			tvNoLoaclList.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
			adapter = new HomeAdapter(CenterFagment.this.getActivity(), surveylist,TAG);
			gridView.setAdapter(adapter);

		} else {
			gridView.setVisibility(View.GONE);
			tvNoLoaclList.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		leftIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((HomeActivity) getActivity()).showLeft();
			}
		});

		subscibeIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 流程
				if (!Util.isEmpty(ma.userPwd) && NetUtil.checkNet(ma)) {
					ma.cfg.putBoolean("isFirst", false); //  设置第一次访问 否  
					((HomeActivity) getActivity()).skipActivity(SubscibeActivity.class);
				} else if (!NetUtil.checkNet(ma)) {
					// 没网情况下
					Toasts.makeText(ma, R.string.exp_net, Toast.LENGTH_LONG).show();
				} else if (Util.isEmpty(ma.userPwd)) {
					// 用户名为空
					Toasts.makeText(ma, R.string.no_login, Toast.LENGTH_LONG).show();
					((HomeActivity) getActivity()).skipActivity(LoginActivity.class,30);
				}
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
	//开关判断地图监控
	public void sendAlarmBroadCase() {
		Intent intent = new Intent(this.getActivity(), AlarmReceiver.class);
		intent.setAction("arui.alarm.action");
		PendingIntent sender = PendingIntent.getBroadcast(this.getActivity(), 0,//
				intent, 0);
		long firstime = SystemClock.elapsedRealtime();
		AlarmManager am = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);
		// 10秒一个周期，不停的发送广播
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
		//
				1 * 60 * 1000, sender);
	}

	/**
	 * 得到异常发送邮件
	 */
	private void getExceptionToEmail() {
		// 获得所有异常
		ArrayList<DapException> exList = ma.dbService.getTab1ByEnable();
		// 假如有异常，发送，否则什么都不干。
		if (exList.size() > 0) {
			// 假如有网
			if (ComUtil.checkNet(this.getActivity())) {
				for (int i = 0; i < exList.size(); i++) {
					sendThreadException(exList.get(i));
				}
				Toasts.makeText(this.getActivity(), R.string.doing_send, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * sendThreadException 发送邮件
	 */
	private void sendThreadException(final DapException dapException) {
		new Thread() {
			public void run() {
				try {
					sendException("depaiceshi", "depaiceshi1", "bug@dapchina.cn", dapException);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 发送错误信息到邮箱
	 */
	private void sendException(String userName, String passWord, String email, DapException dapException) throws Exception {
		// System.out.println("邮件:" + email);
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.163.com");
		props.setProperty("mail.smtp.auth", "true");
		class MyAuthenticator extends Authenticator {

			private String name;
			private String pass;

			public MyAuthenticator(String username, String password) {
				this.name = username;
				this.pass = password;
			}

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(name, pass);// @sohu.com
			}

		}
		Session session = Session.getInstance(props, new MyAuthenticator(userName, passWord));
		// Session session = Session.getInstance(props, new Authenticator() {
		//
		// @Override
		// protected PasswordAuthentication getPasswordAuthentication() {
		// return new PasswordAuthentication("dapserver",
		// "dapchina_2016");//@sohu.com
		// }
		//
		// });
		MimeMessage message = new MimeMessage(session);
		String emailFrom = userName + "@163.com";
		message.setFrom(InternetAddress.parse(MimeUtility.encodeText("SIM SYSTEM") + "<" + emailFrom + ">")[0]);
		message.setRecipient(RecipientType.TO, new InternetAddress(email));
		message.setSentDate(new Date());
		message.setSubject(this.getString(R.string.error_message, dapException.getFileName()));
		// message.setContent("Dear Participant,<br><br>This is a reminder to take medication according to your dosing schedule and complete the appropriate diary pages.Thanks.<br><br>CIMS System<br>",
		// "text/html");

		// 邮件正文
		MimeBodyPart text = new MimeBodyPart();

		text.setContent(this.getString(R.string.get_message, dapException.getUserId(), dapException.getMacAddress(), dapException.getFilePath()), "text/html;charset=gbk");

		// 描述数据关系
		MimeMultipart multipart = new MimeMultipart();
		// 附件
		File log = new File(dapException.getFilePath(), dapException.getFileName());
		/** 开始加的附件 */
		MimeBodyPart mbpFile = new MimeBodyPart();
		// 得到数据源
		FileDataSource fds = new FileDataSource(log);
		// 得到附件本身并至入BodyPart
		mbpFile.setDataHandler(new DataHandler(fds));
		// 得到文件名同样至入BodyPart
		mbpFile.setFileName(fds.getName());
		/** 结束加的附件 */
		multipart.addBodyPart(mbpFile);
		multipart.addBodyPart(text);
		multipart.setSubType("related");

		message.setContent(multipart);

		message.saveChanges();
		Transport.send(message);
		session.getTransport().close();
		// DataHandler dh = new DataHandler("","");
		// 回收垃圾内存
		Message msg = handler.obtainMessage();
		msg.obj = dapException.getFileName();
		msg.what = 99;
		handler.sendMessage(msg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivName:
			// 流程
			if (!Util.isEmpty(ma.userPwd) && NetUtil.checkNet(ma)) {
				Intent intent = new Intent();
				ma.cfg.putBoolean("isFirst", false); //  设置第一次访问 否  
				intent.setClass(this.getActivity(), SubscibeActivity.class);
				startActivityForResult(intent, 10);
				this.getActivity().overridePendingTransition(R.anim.right, R.anim.left);
			} else if (!NetUtil.checkNet(ma)) {
				// 没网情况下
				Toasts.makeText(ma, R.string.exp_net, Toast.LENGTH_LONG).show();
			} else if (Util.isEmpty(ma.userPwd)) {
				// 用户名为空
				Toasts.makeText(ma, R.string.no_login, Toast.LENGTH_LONG).show();
				((HomeActivity) getActivity()).skipActivity(LoginActivity.class,30);
			}
			break;
		default:
			break;
		}
	}

}
