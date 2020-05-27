package com.investigate.newsupper.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.UUID;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.InputType;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.bean.Application;
import com.investigate.newsupper.bean.HttpBean;
import com.investigate.newsupper.bean.Task;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.TaskType;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UIEditText;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.main.MainService;
import com.investigate.newsupper.util.MD5;
import com.investigate.newsupper.util.NetService;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.UIUtils;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.view.Toasts;
import com.tencent.bugly.crashreport.CrashReport;

public class LoginActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {
	private static final String TAG = LoginActivity.class.getSimpleName();
	LinearLayout leftIv;
	private UIEditText etUser, etPass;
	UITextView registTv; // 大树 记得修改 XML 注册
	private Button payLogin, freeLogin; // 大树 付费登陆 免费 登陆
	private MyApp ma;

	private LinearLayout userNameLL, userPassLL;
	private InputMethodManager inm;
	private CheckBox checkBox;
	private String isMemory;
	private int hintTextSize;
	private UITextView loginTv;
	private int protocolType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login_activity);
		hintTextSize = (int) (UIUtils.getDimenPixelSize(R.dimen.sry_text_small) * TextSizeManager
				.getInstance().getRealScale());
		leftIv = (LinearLayout) findViewById(R.id.login_left_iv);
		leftIv.setOnClickListener(this);
		loginTv = (UITextView) findViewById(R.id.textView1);
		registTv = (UITextView) findViewById(R.id.regist_tv);
		registTv.setOnClickListener(this);
		etPass = (UIEditText) findViewById(R.id.password_et);
		etUser = (UIEditText) findViewById(R.id.username_et);
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		ma = (MyApp) getApplication();
		if (!Util.isEmpty(ma.cfg.getString(Cnt.USER_ID, ""))) {
			etUser.setText(ma.cfg.getString(Cnt.USER_ID, ""));
		}
		//判断数据库存的是哪种网络协议
		protocolType = ma.cfg.getInt("protocolType", 0);
		isMemory = ma.cfg.getString("isMemory", "no");
		// 进入界面时，这个if用来判断SharedPreferences里面name和password有没有数据，有的话则直接打在EditText上面
		if (isMemory.equals("yes")) {
			String password = ma.cfg.getString(Cnt.USER_PWD, "");
			if (!Util.isEmpty(password)
					&& !Util.isEmpty(ma.cfg.getString(Cnt.USER_ID, ""))) {
				etPass.setText(password);
			}
			checkBox.setChecked(true);
		}
		checkBox.setOnCheckedChangeListener(this);
		checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSize);
		userNameLL = (LinearLayout) findViewById(R.id.userNameLL);
		userPassLL = (LinearLayout) findViewById(R.id.userPassLL);
		userNameLL.setOnClickListener(this);
		userPassLL.setOnClickListener(this);
		inm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mVersion = getVersion();
		// etUser.setInputType(InputType.TYPE_NULL);
		// etUser.setOnTouchListener(new FocusListener());
		TextSizeManager.getInstance().addTextComponent(TAG, loginTv)
				.addTextComponent(TAG, registTv).addTextComponent(TAG, etUser)
				.addTextComponent(TAG, etPass);
	}

	// -------------------------------------隐藏输入法-----------------------------------------------------
	// 获取点击事件
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View view = getCurrentFocus();
			if (isHideInput(view, ev)) {
				HideSoftInput(view.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	// 判定是否需要隐藏
	private boolean isHideInput(View v, MotionEvent ev) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (ev.getX() > left && ev.getX() < right && ev.getY() > top
					&& ev.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	// 隐藏软键盘
	private void HideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private final class FocusListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				((EditText) v).setInputType(InputType.TYPE_CLASS_TEXT);
			}
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LoginActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}

	int userCount = 0;
	int passCount = 0;
	private int buttonSize;

	@Override
	protected void onResume() {
		etUser.requestFocus();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.userNameLL:
			passCount = 0;
			etUser.requestFocus();
			if (0 == userCount) {
				// 显示键盘
				inm.showSoftInput(etUser, 0);
				userCount = 1;
			} else {
				// 隐藏键盘
				inm.hideSoftInputFromWindow(etUser.getWindowToken(), 0);
				userCount = 0;
			}
			break;
		case R.id.userPassLL:
			userCount = 0;
			etPass.requestFocus();
			if (0 == passCount) {
				// 显示键盘
				inm.showSoftInput(etPass, 0);
				passCount = 1;
			} else {
				// 隐藏键盘
				inm.hideSoftInputFromWindow(etPass.getWindowToken(), 0);
				passCount = 0;
			}

			break;
		case R.id.login_left_iv:
			LoginActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.regist_tv:
			Uri uri = Uri.parse(Cnt.FREE_REGIST_URL);//网页注册
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
//			Intent intent = new Intent(LoginActivity.this, RegistActivity.class);//原生注册
//			startActivity(intent);
			overridePendingTransition(R.anim.zzright, R.anim.zzleft);
			break;
		// 大树 付费登陆
		case R.id.pay_login_btn:
			if (!NetUtil.checkNet(LoginActivity.this)) {
				Toasts.makeText(LoginActivity.this, R.string.exp_net,
						Toast.LENGTH_SHORT).show();
				return;
			}
			String userId = etUser.getText().toString().trim();
			String pwd = etPass.getText().toString().trim();
			if (Util.isEmpty(userId)) {
				Util.viewShake(this, etUser);
				etUser.setError(getResources().getString(R.string.null_userid));
				return;
			}
			if (Util.isEmpty(pwd)) {
				Util.viewShake(this, etPass);
				etPass.setError(getResources().getString(R.string.null_pwd));
				return;
			}
			Cnt.changeNewURL(false, payIp, freeIp, payIp, protocolType);
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put(Cnt.USER_ID, userId);
			hm.put(Cnt.USER_PWD, MD5.Md5Pwd(pwd));
			String[] versions = mVersion.split(".");
			if (versions.length > 1) {
				mVersion = versions[0] + "u002E" + versions[1];
			}
			hm.put("version", mVersion);// app版本号
			hm.put("xmlName", Cnt.CONFIG_XML_NAME);// xml文件名称
			hm.put(Cnt.USER_MAC, Util.getLocalMacAddress(LoginActivity.this));
			hm.put("type", 0);
			show();
			MainService.newTask(new Task(TaskType.TS_ONLINE_LOGIN, hm));
			break;
		// 大树 免费 登陆
		case R.id.free_login_btn:
			if (!NetUtil.checkNet(LoginActivity.this)) {
				Toasts.makeText(LoginActivity.this, R.string.exp_net,
						Toast.LENGTH_SHORT).show();
				return;
			}
			String userIdFree = etUser.getText().toString().trim()
					.toLowerCase();
			String pwdFree = etPass.getText().toString().trim();
			if (Util.isEmpty(userIdFree)) {
				Util.viewShake(this, etUser);
				etUser.setError(getResources().getString(R.string.null_userid));
				return;
			}
			if (Util.isEmpty(pwdFree)) {
				Util.viewShake(this, etPass);
				etPass.setError(getResources().getString(R.string.null_pwd));
				return;
			}
			Cnt.changeNewURL(true, freeIp, freeIp, payIp, protocolType);
			HashMap<String, Object> hmFree = new HashMap<String, Object>();
			hmFree.put(Cnt.USER_ID, userIdFree);
			hmFree.put(Cnt.USER_PWD, MD5.Md5Pwd(pwdFree));
			hmFree.put(Cnt.USER_MAC,
					Util.getLocalMacAddress(LoginActivity.this));
			show();
			MainService.newTask(new Task(TaskType.TS_FREE_LOGIN, hmFree));
			break;
		default:
			break;
		}
	}

	/**
	 * 获得版本号 app上线更新
	 */
	private String getVersion() {
		String mVersion;// 版本号
		try {
			PackageManager pm = getPackageManager();
			PackageInfo pi = pm.getPackageInfo(getPackageName(),
					PackageManager.GET_ACTIVITIES);
			mVersion = pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			mVersion = "";
		}
		return mVersion;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		// 大树 登陆 免费 付费 地址
		buttonSize = (int) (UIUtils.getDimenPixelSize(R.dimen.button_text_size) * TextSizeManager
				.getInstance().getRealScale());
		payLogin = (Button) findViewById(R.id.pay_login_btn);
		freeLogin = (Button) findViewById(R.id.free_login_btn);
		// 版本兼容 非访问专家版本隐藏免费登录
		if (1 != Cnt.appVersion && 4 != Cnt.appVersion) {
			freeLogin.setVisibility(View.GONE);
		}
		payLogin.setOnClickListener(this);
		freeLogin.setOnClickListener(this);
		payLogin.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonSize);
		freeLogin.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonSize);
	}

	// app上线更新
	private Application app = null;
	private String mVersion;// 版本号

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		switch ((Integer) param[0]) {
		// 大树 免费 付费 登陆
		case TaskType.TS_ONLINE_LOGIN:
			refreshMethod(param);
			// 如果是 付费的那么 isFree==false 大树免费付费 1
			cfg.putBoolean("isFree", false);
			break;
		case TaskType.TS_FREE_LOGIN:
			refreshMethod(param);
			// 如果是 免费的 那么 isFree==true 大树免费付费 2
			cfg.putBoolean("isFree", true);
			break;
		case TaskType.TS_GET_LOGIN_APP:
			dismiss();
			app = (Application) param[1];
			if (!Util.isEmpty(mVersion)) {
				if (null != app) {
					/**
					 * 获取版本号名称
					 */
					double version = Double.parseDouble(mVersion);
					System.out.println("本地版本号:" + version + ",服务器版本号" + app.getVersion());
					dismiss();
					if (version < app.getVersion()) {
						/**
						 * 服务器的版本比本地版本大, 则更新.
						 */
						if (Util.isEmpty(app.getPath())) {
							System.out.println("没有更新");
						} else {
							System.out.println("更新");
							// 更新
							showNewDialog(app.getPath(),app.getContent(),app.getNotice(),app.getVersion()+"");
						}
					}
				} else {
					dismiss();
				}
			} else {
				
			}
			dismiss();
			break;
		default:
			break;
		}
		
	}
	/**
	 * 更新dialog按钮 app上线更新
	 */
	private void showReinstallDialog(final String path,String appContent,String appNotice,String appVersion) {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		
		mDialog = new Dialog(this, R.style.question_ds);
		mDialog.setContentView(R.layout.update_dialog);
		LinearLayout progressView = (LinearLayout) mDialog.findViewById(R.id.progress_view);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (width / 1.3),
				FrameLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		progressView.setLayoutParams(params);
		ProgressBar progressBar = (ProgressBar) mDialog.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		TextView progressInfo = (TextView) mDialog.findViewById(R.id.progressInfo);
		TextView progressDesc = (TextView) mDialog.findViewById(R.id.progressDesc);
		Button btnUpdate = (Button) mDialog.findViewById(R.id.update_ok_btn);
		TextPaint paint = btnUpdate.getPaint();
		paint.setFakeBoldText(true); 
		btnUpdate.setText("复制链接");
		btnUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		        // 将文本内容放到系统剪贴板里。
		        cm.setText(path);
		        Toast.makeText(LoginActivity.this, "已成功复制到剪切板。", Toast.LENGTH_LONG).show();
		       
			}
		});
		//btnUpdate.setOnClickListener(new CstOnClickListener(path));
		TextView content = (TextView) mDialog.findViewById(R.id.content);
		String strContent = appContent;
		if (!Util.isEmpty(strContent)) {
			String replace = strContent.replace("\\n", "\n");
			content.setText(replace);
			content.setVisibility(View.VISIBLE);
		}
		TextView notice = (TextView) mDialog.findViewById(R.id.notice);
		String strNotice = appNotice;
		if (!Util.isEmpty(strNotice)) {
			String replace = strNotice.replace("\\n", "\n");
			notice.setText(replace);
			notice.setVisibility(View.VISIBLE);
		}
		Button btnCancel = (Button) mDialog.findViewById(R.id.update_cancel_btn);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
				mDialog.cancel();
			}
		});
		//mDialog.setCancelable(false);
		mDialog.show();
		if(!Util.isEmpty(appVersion)){
			progressDesc.setText(getResources().getString(R.string.version_info, appVersion));
		}else{
			progressDesc.setVisibility(View.GONE);
		}
		progressInfo.setText("");
	}
	public Dialog mDialog;
	private ProgressBar progressBar = null;
	private TextView progressInfo = null;
	private TextView progressDesc = null;
	private Button btnUpdate;
	private File apkFile = null;
	UpdateTask updateTask;
	/**
	 * 更新dialog按钮 app上线更新
	 */
	private void showNewDialog(String path,String appContent,String appNotice,String appVersion) {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		mDialog = new Dialog(this, R.style.question_ds);
		mDialog.setContentView(R.layout.update_dialog);
		LinearLayout progressView = (LinearLayout) mDialog.findViewById(R.id.progress_view);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (width / 1.3),
				FrameLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		progressView.setLayoutParams(params);
		progressBar = (ProgressBar) mDialog.findViewById(R.id.progressBar);
		progressInfo = (TextView) mDialog.findViewById(R.id.progressInfo);
		progressDesc = (TextView) mDialog.findViewById(R.id.progressDesc);
		btnUpdate = (Button) mDialog.findViewById(R.id.update_ok_btn);
		btnUpdate.setOnClickListener(new CstOnClickListener(path));
		TextView content = (TextView) mDialog.findViewById(R.id.content);
		String strContent = appContent;
		if (!Util.isEmpty(strContent)) {
			String replace = strContent.replace("\\n", "\n");
			content.setText(replace);
			content.setVisibility(View.VISIBLE);
		}
		TextView notice = (TextView) mDialog.findViewById(R.id.notice);
		String strNotice = appNotice;
		if (!Util.isEmpty(strNotice)) {
			String replace = strNotice.replace("\\n", "\n");
			notice.setText(replace);
			notice.setVisibility(View.VISIBLE);
		}
		Button btnCancel = (Button) mDialog.findViewById(R.id.update_cancel_btn);
		btnCancel.setOnClickListener(new CstOnClickListener(path));
		mDialog.setCancelable(false);
		mDialog.show();
		progressBar.setProgress(0);
		if(!Util.isEmpty(appVersion)){
			progressDesc.setText(getResources().getString(R.string.version_info, appVersion));
		}else{
			progressDesc.setVisibility(View.GONE);
		}
		progressInfo.setText("0/0");
	}
	

	private final class CstOnClickListener implements OnClickListener {
		String path;

		public CstOnClickListener(String path) {
			this.path = path;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.update_ok_btn:
				if (null == updateTask) {
					updateTask = new UpdateTask(path);
				}
				if (!AsyncTask.Status.RUNNING.equals(updateTask.getStatus())) {
					btnUpdate.setClickable(false);
					updateTask.execute();
				}
				break;

			case R.id.update_cancel_btn:
				if (null == updateTask) {
					/**
					 * 不是在下载状态
					 */
					mDialog.dismiss();
				} else {
					if (null != updateTask) {
						/**
						 * 正在下载的状态
						 */
						// 正在下载的
						updateTask.cancel(true);
						updateTask = null;
					}
					if (null != apkFile && apkFile.exists()) {
						apkFile.delete();
					}
					mDialog.dismiss();
				}
				break;
			}
		}

	}
	private final class UpdateTask extends AsyncTask<Void, Integer, Boolean> {
		private String path;

		public UpdateTask(String path) {
			this.path = path;
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				HttpBean hb = NetService.obtainHttpBean(path + "?" + UUID.randomUUID().toString(), null,
						"GET");
				if (200 == hb.code) {
					// substring(path.lastIndexOf("/") + 1);
					apkFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
							path.substring(path.lastIndexOf("/") + 1));
					if (apkFile.exists()) {
						apkFile.delete();
					}
					if (!apkFile.getParentFile().exists()) {
						apkFile.getParentFile().mkdirs();
					}
					FileOutputStream fos = new FileOutputStream(apkFile);
					byte[] buffer = new byte[1024];
					int len = 0;
					int size = 0;
					while ((len = hb.inStream.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
						size += len;
						publishProgress(size, hb.contentLength);
					}
					fos.flush();
					fos.close();
					hb.inStream.close();
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			mDialog.dismiss();
			if (result) {
				Application.openFile(LoginActivity.this, apkFile);
				if (null != updateTask) {
					/**
					 * 正在下载的状态
					 */
					// 正在下载的
					updateTask.cancel(true);
					updateTask = null;
				}
			} else {

			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (isCancelled()) {
				return;
			}
			progressInfo.setText(values[0] + "/" + values[1]);
			if (0 < values[1]) {
				progressBar.setProgress((int) (values[0] * 100) / values[1]);
			}
		}
	}

	private void refreshMethod(Object... param) {
		dismiss();
		if (null != param[1]) {
			HashMap<String, String> hm = (HashMap<String, String>) param[1];
			if (!Util.isEmpty(hm)) {
				// System.out.println(hm);
//				authorID=12385
				String authorID = hm.get("authorID");
				String name = hm.get("name");
				String state = hm.get("state");
				String apkUrl = hm.get("apkUrl");
				
				
							
				
				
				if (!Util.isEmpty(state)) {
					int s = Integer.parseInt(state);
					switch (s) {
					case Cnt.STATE_SUCCESS:
						if(!Util.isEmpty(name)){
							Toasts.makeText(LoginActivity.this,name,Toast.LENGTH_LONG).show();
						}else{
							//Toasts.makeText(LoginActivity.this,getString(R.string.sucess_login),Toast.LENGTH_LONG).show();
							Toasts.makeText(LoginActivity.this,getString(R.string.unknown_error),Toast.LENGTH_LONG).show();
						}
						
						if(!Util.isEmpty(hm.get("AccessKeyID"))){
							//key 存在
							 ma.AK = hm.get("AccessKeyID");
							 ma.SK = hm.get("SecretAccessKey");
							 ma.Endpoint = hm.get("EndPoint");
							 ma.Bucket_Name = hm.get("BucketName");
							 
							 
							 cfg.putString(Cnt.AK, ma.AK);
							 cfg.putString(Cnt.SK, ma.SK);
							 cfg.putString(Cnt.Endpoint, ma.Endpoint);
							 cfg.putString(Cnt.Bucket_Name, ma.Bucket_Name);
						}else{
							 cfg.putString(Cnt.AK, "");
							 cfg.putString(Cnt.SK, "");
							 cfg.putString(Cnt.Endpoint,"");
							 cfg.putString(Cnt.Bucket_Name, "");
						}
						
						cfg.putBoolean(Cnt.LOGIN_MODE, false);
						ma.userId = etUser.getText().toString().trim()
								.toLowerCase();
						ma.userPwd = etPass.getText().toString().trim();
						ma.authorId = authorID;
						
						cfg.putString(Cnt.AUTHORID, ma.authorId);
						cfg.putString(Cnt.USER_ID, ma.userId);
						cfg.putString(Cnt.USER_PWD, ma.userPwd);// userPsd
						CrashReport.putUserData(getApplicationContext(),
								"AUTHORID", cfg.getString(Cnt.AUTHORID, "AUTHORID"));
						CrashReport.putUserData(getApplicationContext(),
								"UserId", cfg.getString(Cnt.USER_ID, "UserId"));
						CrashReport.putUserData(getApplicationContext(),
								"UserPwd",
								cfg.getString(Cnt.USER_PWD, "UserPwd"));
						cfg.putString("memoryPP", ma.userPwd);
						String authorId = hm.get("authorID");
						if (Util.isEmpty(authorId)) {
							Toasts.makeText(LoginActivity.this,
									getString(R.string.login_error, s),
									Toast.LENGTH_LONG).show();
							return;
						}
						cfg.putString("authorId", authorId);
						// 大树设置第一次访问标志
						cfg.putBoolean("isFirst", true);
						LoginActivity.this.finish();
						overridePendingTransition(R.anim.right1, R.anim.left1);
						break;
					case 98:
						if(!Util.isEmpty(name)){
							Toasts.makeText(LoginActivity.this,name,Toast.LENGTH_LONG).show();
						}else{
//							Toasts.makeText(LoginActivity.this,getString(R.string.account_frozen),Toast.LENGTH_LONG).show();
							Toasts.makeText(LoginActivity.this,getString(R.string.unknown_error),Toast.LENGTH_LONG).show();
						}
						break;

					case 97:
						if(!Util.isEmpty(name)){
							Toasts.makeText(LoginActivity.this,name,Toast.LENGTH_LONG).show();
						}else{
//							Toasts.makeText(LoginActivity.this,getString(R.string.error_permission),Toast.LENGTH_LONG).show();
							Toasts.makeText(LoginActivity.this,getString(R.string.unknown_error),Toast.LENGTH_LONG).show();
						}break;

					case 99:
						if(!Util.isEmpty(name)){
							Toasts.makeText(LoginActivity.this,name,Toast.LENGTH_LONG).show();
						}else{
//							Toasts.makeText(LoginActivity.this,getString(R.string.user_error),Toast.LENGTH_LONG).show();
							Toasts.makeText(LoginActivity.this,getString(R.string.unknown_error),Toast.LENGTH_LONG).show();
						}break;

					case 90:
						if(!Util.isEmpty(name)){
							Toasts.makeText(LoginActivity.this,name,Toast.LENGTH_LONG).show();
						}else{
//							Toasts.makeText(LoginActivity.this,getString(R.string.xml_error),Toast.LENGTH_LONG).show();
							Toasts.makeText(LoginActivity.this,getString(R.string.unknown_error),Toast.LENGTH_LONG).show();
						}
						// 检查更新
						/**
						 * 检查升级
						 */
						show();
						MainService.newTask(new Task(TaskType.TS_GET_LOGIN_APP,null));
						break;

					case 91:
						if(!Util.isEmpty(name)){
							Toasts.makeText(LoginActivity.this,name,Toast.LENGTH_LONG).show();
						}else{
							//Toasts.makeText(LoginActivity.this,getString(R.string.version_null_error),Toast.LENGTH_LONG).show();
							Toasts.makeText(LoginActivity.this,getString(R.string.unknown_error),Toast.LENGTH_LONG).show();
						}

						break;

					case 92:
						if(!Util.isEmpty(name)){
							Toasts.makeText(LoginActivity.this,name,Toast.LENGTH_LONG).show();
						}else{
							//Toasts.makeText(LoginActivity.this,getString(R.string.version_under_error),Toast.LENGTH_LONG).show();
							Toasts.makeText(LoginActivity.this,getString(R.string.unknown_error),Toast.LENGTH_LONG).show();
						}
						// 检查更新
						/**
						 * 检查升级
						 */
						show();
						MainService.newTask(new Task(TaskType.TS_GET_LOGIN_APP,null));
						break;

					case 101:
						if (!Util.isEmpty(apkUrl)) {
							showNewDialog(apkUrl, name, "", "");
						}
						if (!Util.isEmpty(name)) {
							Toasts.makeText(LoginActivity.this,getString(R.string.version_not_matched),Toast.LENGTH_LONG).show();
						}
//						else {
//							Toasts.makeText(LoginActivity.this,getString(R.string.version_update_error),Toast.LENGTH_LONG).show();
//							Toasts.makeText(LoginActivity.this,getString(R.string.unknown_error),Toast.LENGTH_LONG).show();
//						}

						break;
					case 102:
						if (!Util.isEmpty(apkUrl)) {
							showReinstallDialog(apkUrl, name, "", "");
						}
//						if(!Util.isEmpty(name)){
//							Toasts.makeText(LoginActivity.this,name,Toast.LENGTH_LONG).show();
//						}else{
////							Toasts.makeText(LoginActivity.this,getString(R.string.version_reset_error),Toast.LENGTH_LONG).show();
//							Toasts.makeText(LoginActivity.this,getString(R.string.unknown_error),Toast.LENGTH_LONG).show();
//						}

						break;
					default:
						if(!Util.isEmpty(name)){
							Toasts.makeText(LoginActivity.this,name,Toast.LENGTH_LONG).show();
						}else{
//							Toasts.makeText(LoginActivity.this,getString(R.string.login_error, s),Toast.LENGTH_LONG).show();
							Toasts.makeText(LoginActivity.this,getString(R.string.unknown_error),Toast.LENGTH_LONG).show();
						}
						break;
					}
				} else {
					Toasts.makeText(this, R.string.fail_login,Toast.LENGTH_LONG).show();
				}
			} else {
				Toasts.makeText(this, R.string.fail_login, Toast.LENGTH_LONG).show();
			}
		} else {
			Toasts.makeText(this, R.string.fail_login, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			ma.cfg.putString("isMemory", "yes");
		} else {
			ma.cfg.putString("isMemory", "no");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		TextSizeManager.getInstance().removeTextComponent(TAG);
	}
}
