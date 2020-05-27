package com.investigate.newsupper.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.adapter.FullbAdatter;
import com.investigate.newsupper.adapter.SubscibeAdapter;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.Task;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.TaskType;
import com.investigate.newsupper.main.MainService;
import com.investigate.newsupper.util.ListViewUtil;
import com.investigate.newsupper.util.MD5;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.view.MyListView;
import com.investigate.newsupper.view.Toasts;
/**
 * 项目更新下载页面
 * @author Administrator
 *
 */
public class SubscibeActivity extends BaseActivity implements OnClickListener {
	//大哥   用本类的名称来命名tag成么   这页出错了我去设置界面去找能找到么？？？？？
	private static final String TAG = "SubscibeActivity";
	LinearLayout subscibeIv;
	SubscibeAdapter subscibeAdapter;
	FullbAdatter fullbAdatter;
	ScrollView mRootScrollView;
	private EditText etSurvey;
	private MyListView lvAuthor;
	private ListView listView;
	private TextView tvNoAuthorList;
	private boolean isRefreshing = false;
	private boolean isFirst = true;
	private MyApp ma;
	private ImageView iv_search;// 搜索
	private ArrayList<Survey> ss;
	private ArrayList<String> list = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.subscibe_activity);
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		subscibeIv = (LinearLayout) findViewById(R.id.subscibe_left_iv);
		mRootScrollView = (ScrollView) findViewById(R.id.scrollview);
		subscibeIv.setOnClickListener(this);
		lvAuthor = (MyListView) findViewById(R.id.listView1);
		listView = (ListView) findViewById(R.id.listview);
		iv_search = (ImageView) findViewById(R.id.iv_search);
		iv_search.setOnClickListener(this);
		tvNoAuthorList = (TextView) findViewById(R.id.no_author_list_tv);
		if (NetUtil.checkNet(this)) {
			isFirst = true;
			show();
			refreshAuthorList();
		}
		etSurvey = (EditText) findViewById(R.id.etSurvey);
		etSurvey.setInputType(InputType.TYPE_NULL);
		etSurvey.setOnTouchListener(new FocusListener());
		Intent intent = getIntent();
		setResult(11, intent);
		
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
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
			if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			Log.i(TAG, "普通项目状态："+subscibeAdapter.isDownloading);
//			Log.i(TAG, "随访项目状态："+fullbAdatter.isDownloading);
			if (null != subscibeAdapter && subscibeAdapter.isDownloading ) {
				Toasts.makeText(SubscibeActivity.this, "正在更新问卷请勿退出!", Toast.LENGTH_SHORT).show();
				return true;
			}
			if (fullbAdatter != null) {
				if (  fullbAdatter.isDownloading) {
					Toasts.makeText(SubscibeActivity.this, "正在更新问卷请勿退出!", Toast.LENGTH_SHORT).show();
					return true;
				}
			}
			SubscibeActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.subscibe_left_iv:
			if (null != subscibeAdapter && subscibeAdapter.isDownloading ) {
				Toasts.makeText(SubscibeActivity.this, "正在更新问卷请勿退出!", Toast.LENGTH_SHORT).show();
				break;
			}
			if (fullbAdatter != null) {
				if (  fullbAdatter.isDownloading) {
					Toasts.makeText(SubscibeActivity.this, "正在更新问卷请勿退出!", Toast.LENGTH_SHORT).show();
					break;
				}
			}
			SubscibeActivity.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.iv_search://搜索按钮
			
			mRootScrollView.fullScroll(ScrollView.FOCUS_UP);
			
			// 假如问卷为空
			String words = etSurvey.getText().toString();
			if (Util.isEmpty(words)) {
				Util.viewShake(this, etSurvey);
				Toasts.makeText(SubscibeActivity.this, R.string.input_name, Toast.LENGTH_LONG).show();
			}
			ss = ma.dbService.searchSurveyByWord(ma.userId, words);
			//判断搜索结果是否为空
//			if (!Util.isEmpty(ss)) {
//				tvNoAuthorList.setVisibility(View.GONE);
//				lvAuthor.setVisibility(View.VISIBLE);
//				if (null == subscibeAdapter) {
//					subscibeAdapter = new SubscibeAdapter(SubscibeActivity.this, ss, ma, TAG);
//					lvAuthor.setAdapter(subscibeAdapter);
//				} else {
//					subscibeAdapter.refresh(ss);
//				}
//			} else {
//				lvAuthor.setVisibility(View.GONE);
//				listView.setVisibility(View.GONE);
//				tvNoAuthorList.setVisibility(View.VISIBLE);
//			}
			
			
			
			
			if (!Util.isEmpty(ss)) {
				tvNoAuthorList.setVisibility(View.GONE);
				lvAuthor.setVisibility(View.VISIBLE);
				listView.setVisibility(View.VISIBLE);
				if (null == subscibeAdapter) {
					ArrayList<ArrayList<Survey>> slist = setdata(ss);
					subscibeAdapter = new SubscibeAdapter(SubscibeActivity.this, slist.get(0), ma, TAG);
					lvAuthor.setAdapter(subscibeAdapter);
					ListViewUtil.setListViewHeight(lvAuthor);
					slist.remove(0);
					fullbAdatter = new FullbAdatter(SubscibeActivity.this, slist,ma);
					listView.setAdapter(fullbAdatter);
					ListViewUtil.setListViewHeight(listView);
				} else {
					ArrayList<ArrayList<Survey>> slist = setdata(ss);
					subscibeAdapter.refresh(slist.get(0));
					lvAuthor.setAdapter(subscibeAdapter);
					
					slist.remove(0);
					fullbAdatter.updateListView(slist);
					ListViewUtil.setListViewHeight(listView);
				}
			} else {
				lvAuthor.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				tvNoAuthorList.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
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
	protected void onResume() {
		super.onResume();
	}

	private void refreshAuthorList() {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		// 大树测试
		// hm.put(Cnt.USER_ID, "dap1");
		// hm.put(Cnt.USER_PWD, MD5.Md5Pwd("dap1"));
		hm.put(Cnt.USER_ID, ma.userId);
		hm.put(Cnt.USER_PWD, MD5.Md5Pwd(ma.userPwd));
		MainService.newTask(new Task(TaskType.TS_AUTHOR, hm));
		isRefreshing = true;
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... param) {
		switch ((Integer) param[0]) {
		case TaskType.TS_AUTHOR:
			ss = (ArrayList<Survey>) param[1];

			if (!Util.isEmpty(ss)) {
				tvNoAuthorList.setVisibility(View.GONE);
				lvAuthor.setVisibility(View.VISIBLE);
				listView.setVisibility(View.VISIBLE);
				if (null == subscibeAdapter) {
					ArrayList<ArrayList<Survey>> slist = setdata(ss);
					subscibeAdapter = new SubscibeAdapter(SubscibeActivity.this, slist.get(0), ma, TAG);
					lvAuthor.setAdapter(subscibeAdapter);
					ListViewUtil.setListViewHeight(lvAuthor);
					slist.remove(0);
					fullbAdatter = new FullbAdatter(SubscibeActivity.this, slist,ma);
					listView.setAdapter(fullbAdatter);
					ListViewUtil.setListViewHeight(listView);
				} else {
					ArrayList<ArrayList<Survey>> slist = setdata(ss);
					subscibeAdapter.refresh(slist.get(0));
					lvAuthor.setAdapter(subscibeAdapter);
					
					slist.remove(0);
					fullbAdatter.updateListView(slist);
					ListViewUtil.setListViewHeight(listView);
				}
			} else {
				lvAuthor.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				tvNoAuthorList.setVisibility(View.VISIBLE);
			}
			if (isFirst) {
				dismiss();
				isFirst = false;
			} else {
				lvAuthor.onRefreshComplete();
			}
			isRefreshing = false;
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		ma.remove(this);
		super.onDestroy();
	}
	
	public ArrayList<ArrayList<Survey>> setdata(ArrayList<Survey> ss){
		//存scid的list
		ArrayList<String> strlist = new ArrayList<String>();
		//新的数组
		ArrayList<Survey> surveylist = new ArrayList<Survey>();
		for (int i = 0; i < ss.size(); i++) {
			//如果没有scid
			if (Util.isEmpty(ss.get(i).getSCID())) {
					surveylist.add(ss.get(i));
			}else{
				//如果有scid
				if (!strlist.contains(ss.get(i).getSCID())) {
					strlist.add(ss.get(i).getSCID());
				}
			}
		}
		for (int i = 0; i < strlist.size(); i++) {
			System.out.println("strlist-------------"+strlist.get(i));
			
		}
		//获取scid-------end---------------------------------

		//相同scid的数组放到这里
		ArrayList<ArrayList<Survey>> sclist = new ArrayList<ArrayList<Survey>>();
		//没有scid 的放到这里
		ArrayList<Survey> UnScidsurveys = new ArrayList<Survey>();
		for (int i = 0; i < strlist.size(); i++) {
			//相同scid的放到一个数组里
			ArrayList<Survey> surveys = new ArrayList<Survey>();
			
				for (int j = 0; j < ss.size(); j++) {
					System.out.println("scid 是------"+ss.get(j).getSCID());
					if (!Util.isEmpty(ss.get(j).getSCID())) {
						System.out.println("循环scid的数量======="+strlist.size());
							if (strlist.get(i).equals(ss.get(j).getSCID())) {
								surveys.add(ss.get(j));
							}
					}
				}
			
			sclist.add(surveys);
		}
		for (int i = 0; i < ss.size(); i++) {
			if (Util.isEmpty(ss.get(i).getSCID())) {
				UnScidsurveys.add(ss.get(i));
			}
		}
		for (int i = 0; i < sclist.size(); i++) {
			System.out.println("第"+i+"次循环外层");
			for (int j = 0; j < sclist.get(i).size(); j++) {
				
				System.out.println("第"+j+"次循环内层");
				System.out.println("scid 是"+sclist.get(i).get(j).getSCID()+"surveyID 是"+sclist.get(i).get(j).getSurveyId());
			}
			
			
		}
		sclist.add(0,UnScidsurveys);
		return sclist;
		
		
	}
}
