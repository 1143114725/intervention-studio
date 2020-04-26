package com.investigate.newsupper.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.investigate.newsupper.R;
import com.investigate.newsupper.adapter.PersionItemAdapter;
import com.investigate.newsupper.bean.PersionItemBean;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.MD5;
import com.investigate.newsupper.util.NetService;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.XmlUtil;

/**
 * 人员列表详情页面 Created by EEH on 2018/4/3.
 */

public class PersionItemActivity extends BaseActivity {

	private static final String TAG = "FragmentMain";
	private TextView tv_PID, tv_name, tv_tel;
	private ListView lv_perdion_item;
	private PersionItemAdapter mPersionItemAdapter;
	private MyApp ma;
	Config cfg;
	Activity mActivity;
	private ArrayList<UploadFeed> fs;
	// 标题栏
	private LinearLayout visit_left_iv;
	private UITextView tvSurveyTile;
	private TextView refresh;
	PersionItemBean pib = new PersionItemBean();
	String pid, name, tel, scid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mActivity = this;
		setContentView(R.layout.layout_persion_item);
		tv_PID = (TextView) findViewById(R.id.tv_item_persions_PID);
		tv_name = (TextView) findViewById(R.id.tv_item_persions_name);
		tv_tel = (TextView) findViewById(R.id.tv_item_persions_tel);
		lv_perdion_item = (ListView) findViewById(R.id.lv_perdion_item);
		

		pid = getIntent().getExtras().getString("pid");
		name = getIntent().getExtras().getString("name");
		tel = getIntent().getExtras().getString("tel");
		scid = getIntent().getExtras().getString("scid");

		
		refresh = (TextView)findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initdata();
			}
		});
		visit_left_iv = (LinearLayout) findViewById(R.id.visit_left_iv);
		//标题tv
		tvSurveyTile = (UITextView) findViewById(R.id.visit_survey_name_tv);
		visit_left_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PersionItemActivity.this.finish();
				overridePendingTransition(R.anim.right1, R.anim.left1);
			}
		});
		
		TextSizeManager.getInstance().addTextComponent(TAG, tvSurveyTile);
		
		String type = getIntent().getExtras().getString("type");
		if (type.equals("1")) {
			//访问记录
			tvSurveyTile.setText("访问记录");
		}else if (type.equals("2")) {
			//人员详情
			tvSurveyTile.setText("人员详情");
		}

		
		cfg = new Config(mActivity);
		ma = (MyApp) mActivity.getApplication();
		mPersionItemAdapter = new PersionItemAdapter(mActivity,
				pib.getmPersionItemListBean(), ma, pid);
		lv_perdion_item.setAdapter(mPersionItemAdapter);
		initdata();
	}
	
	public void initdata(){
		show();
		if (NetUtil.checkNet(mActivity)) {
			new UseritemTask(ma.userId, cfg.getString(Cnt.USER_PWD, ""), scid, pid)
			.execute();
		}else {
			String text = "请检查网络状态！";
			DialogUtil.newdialog(mActivity, text);
			dismiss();
		}
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			PersionItemActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		LocationUtil.stoplocation();
	}

	// 人员详情
	private final class UseritemTask extends AsyncTask<Void, Integer, Boolean> {
		private String userId;
		private String userPsd;
		private String SC_ID;
		private String PID;

		public UseritemTask(String userId, String userPsd, String SC_ID,
				String PID) {
			this.SC_ID = SC_ID;
			this.userId = userId;
			this.userPsd = userPsd;
			this.PID = PID;

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("userId", userId);
				hm.put("userPsd", MD5.Md5Pwd(userPsd));
				hm.put("SC_ID", SC_ID);
				hm.put("PID", PID);
				InputStream inStream = NetService.openUrl(Cnt.UserItem, hm,
						"GET");
				pib = XmlUtil.PeraionItemListxml(inStream);

				System.out.println("httpOk");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("http  catch");
			}
			return false;
		}

		@Override
		protected void onPreExecute() {
			System.out.println("http onPreExecute");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			System.out.println("http onPostExecute");

			tv_PID.setText(pib.getPanelID());
			tv_name.setText(pib.getUserName());
			tv_tel.setText(pib.getPhone());

			if (pib != null) {
				mPersionItemAdapter.updateListView(pib
						.getmPersionItemListBean());

			} else {
				System.out.println("list is null!!!!");
			}
			dismiss();

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			System.out.println("http onProgressUpdate");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initdata();
	}
}
