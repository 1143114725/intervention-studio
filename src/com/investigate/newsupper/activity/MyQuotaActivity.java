package com.investigate.newsupper.activity;


import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.adapter.QuotaAdapter;
import com.investigate.newsupper.bean.Quota;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.Task;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.TaskType;
import com.investigate.newsupper.main.MainService;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.view.Toasts;

//配额
public class MyQuotaActivity extends BaseActivity implements OnClickListener{
	private Survey survey;

	private MyApp ma;

	private ListView lvVisit;
	private Button quotabtn;
	
	private TextView tvNoList;
	private ArrayList<Quota> qlist;

	private QuotaAdapter quotaAdapter;
	
	private ImageView quotaLeftIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, //
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.quota_activity);
		overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		ma = (MyApp) getApplication();
		lvVisit=(ListView) findViewById(R.id.quotalistview);
		quotabtn=(Button) findViewById(R.id.quota_update);
		quotabtn.setOnClickListener(this);
		tvNoList = (TextView) findViewById(R.id.no_quota_list_tv);
		quotaLeftIv=(ImageView) findViewById(R.id.quota_left_iv);
		quotaLeftIv.setOnClickListener(this);
		ma.addActivity(MyQuotaActivity.this);
		/**
		 * 接收LocalActivity列表中传递过来的Survey
		 */
		survey = (Survey) getIntent().getExtras().get("s");
		if (NetUtil.checkNet(this)) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("userId", ma.userId);
			hm.put("userPsd", ma.cfg.getString(Cnt.USER_PWD, ""));
			hm.put("surveyId", survey.surveyId);
			show();
			MainService.newTask(new Task(TaskType.TS_GET_QUOTA, hm));
		} else {
			qlist = ma.dbService.getSurveyQuotaList(survey.surveyId,ma.userId);
			if (Util.isEmpty(qlist)) {
				lvVisit.setVisibility(View.GONE);
				tvNoList.setVisibility(View.VISIBLE);
			} else {
				tvNoList.setVisibility(View.GONE);
				lvVisit.setVisibility(View.VISIBLE);
				if (null == quotaAdapter) {
					quotaAdapter = new QuotaAdapter(MyQuotaActivity.this, qlist, survey);
					lvVisit.setAdapter(quotaAdapter);
				} else {
					quotaAdapter.refresh(qlist);
				}
			}
		}
	}
	@Override
	public void init() {
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	

	@Override
	public void refresh(Object... param) {
		switch ((Integer) param[0]) {
		case TaskType.TS_GET_QUOTA:
			ArrayList<Quota> myQList = (ArrayList<Quota>) param[1];
			if (!Util.isEmpty(myQList)) {
				tvNoList.setVisibility(View.GONE);
				lvVisit.setVisibility(View.VISIBLE);
				if(null == quotaAdapter){
					quotaAdapter = new QuotaAdapter(MyQuotaActivity.this, myQList,survey);
					for (int i = 0; i < myQList.size(); i++) {
						int sie = myQList.get(i).getOptionlist().size();
						Log.i("QuotaAdapter--", "size===="+sie);
					}
					lvVisit.setAdapter(quotaAdapter);
				}else{
					quotaAdapter.refresh(myQList);
				}
			}else{
				Toasts.makeText(this, R.string.no_quota, Toast.LENGTH_LONG).show();
				lvVisit.setVisibility(View.GONE);
				tvNoList.setVisibility(View.VISIBLE);
			} 
			dismiss();
			break;
		}
		dismiss();
	}
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 更新配额
		case R.id.quota_update:
			if (NetUtil.checkNet(this)) {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("surveyId", survey.surveyId);
				hm.put("userPsd", ma.cfg.getString(Cnt.USER_PWD, ""));
				hm.put("userId", ma.userId);
				MainService.newTask(new Task(TaskType.TS_GET_QUOTA, hm));
				show();
			} else {
				Toasts.makeText(this, R.string.exp_net, Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.quota_left_iv:
			MyQuotaActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		default:
			break;
		}
	}

	

}
