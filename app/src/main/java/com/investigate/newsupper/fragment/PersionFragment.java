package com.investigate.newsupper.fragment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xutils.http.RequestParams;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.FragmentMain;
import com.investigate.newsupper.activity.PersionItemActivity;
import com.investigate.newsupper.adapter.PersionAdapter;
import com.investigate.newsupper.base.BaseFragment;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.bean.UserBean;
import com.investigate.newsupper.bean.UserList;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.MD5;
import com.investigate.newsupper.util.NetService;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;
import com.investigate.newsupper.view.Toasts;
import com.investigate.newsupper.xhttp.HttpCallBack;
import com.investigate.newsupper.xhttp.SendHttp;

/**
 * 人员列表 Created by EEH on 2018/3/20.
 */

public class PersionFragment extends BaseFragment implements
		OnItemClickListener, OnClickListener {
	private static final String TAG = "PersionFragment";
	private ListView mlv_persion;
	private PersionAdapter mAdapter;
	private MyApp ma;
	private String scid;
	private static PersionFragment mine;
	Config cfg;
	ArrayList<UserList> list = new ArrayList<UserList>();
	// 搜索相关
	AutoCompleteTextView actvKeyWords;
	private String spinnerTv;
	private UITextView tv_spinner;// 下拉选项
	int position;
	private ArrayList<UploadFeed> fs;
	ArrayList<UserList> searchlist = new ArrayList<UserList>();
	LinearLayout ll_spinner, globle_search;
	ImageView search_btn;
	
	
	private TextView nodata;

	public static PersionFragment newInstance() {
		if (mine != null) {
			return mine;
		} else {
			mine = new PersionFragment();
		}
		return mine;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_persion, container,
				false);
		mlv_persion = (ListView) view.findViewById(R.id.lv_persion);
		nodata = (TextView) view.findViewById(R.id.nodata);
		tv_spinner = (UITextView) view.findViewById(R.id.spinner);
		globle_search = (LinearLayout) view.findViewById(R.id.globle_search);
		ll_spinner = (LinearLayout) view.findViewById(R.id.ll_spinner);
		ll_spinner.setOnClickListener(this);
		search_btn = (ImageView) view.findViewById(R.id.search_btn);
		search_btn.setOnClickListener(this);
		actvKeyWords = (AutoCompleteTextView) view
				.findViewById(R.id.keyword_actv);

		TextSizeManager.getInstance().addTextComponent(TAG, tv_spinner);

		
		scid = FragmentMain.gatscid();
		cfg = new Config(mActivity);
		ma = (MyApp) mActivity.getApplication();
		
		
		if (NetUtil.checkNet(mActivity)) {
			initData();
		}else {
			String text = "请检查网络状态！";
			DialogUtil.newdialog(mActivity, text);
			mlv_persion.setVisibility(View.GONE);
			nodata.setVisibility(View.VISIBLE);
		}
		
		
		mlv_persion.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}

	/**
	 * 获取数据
	 */
	@SuppressWarnings("unchecked")
	private void initData() {
		// TODO Auto-generated method stub
		
		
			show();
			
			// 大树 共用变量 对象
			actvKeyWords.setText("");
			
			System.out.println("scid:" + scid);
			if (list != null) {
				list.clear();
			}
//			new ContinueUserTask(ma.userId, cfg.getString(Cnt.USER_PWD, ""), scid)
//			.execute();
			RequestParams params = new RequestParams(Cnt.ContinueUser);
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("userId", ma.userId);
			hm.put("userPsd",  MD5.Md5Pwd(cfg.getString(Cnt.USER_PWD, "")));
			hm.put("SC_ID", scid);
			hm.put("getFlag", "1");
			System.out.println("xutils+md5+pass::"+cfg.getString(Cnt.USER_PWD, ""));
			SendHttp.gethttp(Cnt.ContinueUser, hm, new HttpCallBack() {
				@Override
				public void onError() {
					// TODO Auto-generated method stub
					Toast.makeText(mActivity, "数据获取失败请检查网络！", Toast.LENGTH_SHORT).show();
					dismiss();
				}
				@Override
				public void onNext(Object result) {
					// TODO Auto-generated method stub
					System.out.println("httpOk+result"+result.toString());
					InputStream is = new ByteArrayInputStream(((String) result).getBytes());
					UserBean ublist = XmlUtil.PersionList(is);
					if (ublist != null) {
						if (ublist.getUserlist() != null && ublist.getUserlist().size() !=0) {
							list.clear();
							list.addAll(ublist.getUserlist());
							System.out.println("httpOk");
						}
					}
					if (mAdapter == null) {
						mAdapter = new PersionAdapter(mActivity, list, ma);
						mlv_persion.setAdapter(mAdapter);
						System.out.println("httpOk::setadapter");
					}else {
						mAdapter.updateListView(list);
						System.out.println("httpOk::list.size"+list.size());
						System.out.println("httpOk::updateListView");
					}
					//没数据的时候显示“暂无数据view”
					if (Util.isEmpty(list)) {
						mlv_persion.setVisibility(View.GONE);
						nodata.setVisibility(View.VISIBLE);
					}else {
						mlv_persion.setVisibility(View.VISIBLE);
						nodata.setVisibility(View.GONE);
					}
					
					
					
					dismiss();
				}
			});
			
		
	}

	// 人员列表
	private final class ContinueUserTask extends
			AsyncTask<Void, Integer, Boolean> {
		private String userId;
		private String userPsd;
		private String SC_ID;

		public ContinueUserTask(String userId, String userPsd, String SC_ID) {
			this.SC_ID = SC_ID;
			this.userId = userId;
			this.userPsd = userPsd;

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("userId", userId);
				hm.put("userPsd", MD5.Md5Pwd(userPsd));
				hm.put("SC_ID", SC_ID);
				hm.put("getFlag", "1");
				InputStream inStream = NetService.openUrl(Cnt.ContinueUser, hm,
						"GET");
				UserBean ublist = XmlUtil.PersionList(inStream);
				if (ublist != null) {
					if (ublist.getUserlist() != null && ublist.getUserlist().size() !=0) {
						list.addAll(ublist.getUserlist());
						System.out.println("httpOk");
					}
				}
				
				
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

			
			if (Util.isEmpty(list)) {
				mlv_persion.setVisibility(View.GONE);
				nodata.setVisibility(View.VISIBLE);
			}else {
				mlv_persion.setVisibility(View.VISIBLE);
				nodata.setVisibility(View.GONE);
//				System.out.println("size11111+"+list.size());
				for (int i = 0; i < list.size(); i++) {
					System.out.println("lsit+"+i+"条"+list.get(i).toString());
				}
				mAdapter.updateListView(list);
			}
			
			dismiss();

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			System.out.println("http onProgressUpdate");
		}
	}

	/**
	 * 列表点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putString("type", "2");
		bundle.putString("pid", list.get(position).getPanelID());
		bundle.putString("name", list.get(position).getUserName());
		bundle.putString("tel", list.get(position).getPhone());
		bundle.putString("scid", FragmentMain.gatscid());

		goToActivity(mActivity, PersionItemActivity.class, bundle);

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.ll_spinner:
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity,
					AlertDialog.THEME_HOLO_LIGHT);
			builder.setIcon(R.drawable.ic_menu_archive);
			builder.setTitle(R.string.input_category);
			spinnerTv = getString(R.string.more_thing);
			// 指定下拉列表的显示数据
			final String[] cities = { getString(R.string.input_category), "PID","姓名",
					"电话" };
			// 设置一个下拉的列表选择项
			builder.setItems(cities, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toasts.makeText(
							mActivity,
							mActivity.getString(R.string.choice_mode)
									+ cities[which], Toast.LENGTH_SHORT).show();
					position = which;
					if (which != 0) {
						spinnerTv = cities[which];
						tv_spinner.setText(spinnerTv);
					} else {
						tv_spinner.setText(spinnerTv);
					}
				}
			});
			builder.show();
			break;
		case R.id.search_btn:
			String words = actvKeyWords.getText().toString().trim();
			if (Util.isEmpty(words)) {
				initData();
				return;
			}
			searchlist.clear();
			mAdapter.updateListView(searchlist);
			
			if (0 == position) {
				Util.viewShake(mActivity, globle_search);
				Toasts.makeText(mActivity, R.string.input_category_please,
						Toast.LENGTH_LONG).show();
				return;
			}
			if (Util.isEmpty(words)) {
				mAdapter.updateListView(list);
			} else {
				for (int i = 0; i < list.size(); i++) {
					if (position == 1) {
						if (list.get(i).getPanelID().equals(words)) {
							searchlist.add(list.get(i));
						}

					} else if (position == 2) {
						if (list.get(i).getUserName().equals(words)) {
							searchlist.add(list.get(i));
						}

					} else if (position == 3) {
						if (list.get(i).getPhone().equals(words)) {
							searchlist.add(list.get(i));
						}
					}
				}
				if (searchlist.size() == 0 || searchlist == null) {
					Toasts.makeText(mActivity,
							mActivity.getString(R.string.no_find),
							Toast.LENGTH_SHORT).show();
				}
				list.clear();
				list.addAll(searchlist);

				mAdapter.updateListView(list);

			}
			break;

		}

	}
	public void refreshdata(){
		initData();
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		mAdapter = null;
	}

}
