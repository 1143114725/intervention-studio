package com.investigate.newsupper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.investigate.newsupper.R;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.Config;

public class JumpListActivity extends BaseActivity implements OnClickListener {
	private MyApp ma;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			
			}
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.setting_activity);
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		if (null == ma.cfg) {
			ma.cfg = new Config(JumpListActivity.this);
		}
		initView();
		Intent intent = getIntent();
		setResult(11, intent);
	}

	private void initView() {}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			JumpListActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v) {}



	@Override
	public void init() {
		
	}

	@Override
	public void refresh(Object... param) {
		switch ((Integer) param[0]) {}
	}
}
