package com.investigate.newsupper.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.ChoiceModeActivity;
import com.investigate.newsupper.activity.FragmentMain;
import com.investigate.newsupper.activity.SubscibeActivity;
import com.investigate.newsupper.adapter.MyBillPineListAdapter;
import com.investigate.newsupper.base.BaseFragment;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.TimeBean;
import com.investigate.newsupper.bean.TimeList;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.DateUtil;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.LocationUtil;
import com.investigate.newsupper.util.LocationUtil.ImPlement;
import com.investigate.newsupper.util.MD5;
import com.investigate.newsupper.util.NetService;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;
import com.investigate.newsupper.view.PinnedSectionListView;
import com.investigate.newsupper.view.Toasts;

/**
 * 时间列表 Created by EEH on 2018/3/28.
 */

public class TimeFragment extends BaseFragment implements OnClickListener,OnItemClickListener {
	private static final String TAG = "TimeFragment";
	private static TimeFragment mine;

	public static TimeFragment newInstance() {
		if (mine != null) {
			return mine;
		} else {
			mine = new TimeFragment();
		}
		return mine;
	}

	private PinnedSectionListView lv_pinn;
	private MyBillPineListAdapter myBillPineListAdapter;
	private ArrayList<TimeBean> list = new ArrayList<TimeBean>();
	private MyApp ma;
	private String scid;
	Config cfg;
	private ArrayList<UploadFeed> fs;
	// 搜索相关
	AutoCompleteTextView actvKeyWords;
	private String spinnerTv;
	private UITextView tv_spinner;// 下拉选项
	int position;
	LinearLayout ll_spinner, globle_search;
	ImageView search_btn;

	
	private TextView nodata;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_time, container, false);
		lv_pinn = (PinnedSectionListView) view.findViewById(R.id.lv_pinn);
		lv_pinn.setOnItemClickListener(this);

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
		if (NetUtil.checkNet(mActivity)) {
			initData();
		}else {
//			String text = "请检查网络状态！";
//			DialogUtil.newdialog(mActivity, text);
			lv_pinn.setVisibility(View.GONE);
			nodata.setVisibility(View.VISIBLE);
		}
		

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
	private void initData() {
		// TODO Auto-generated method stub
		scid = FragmentMain.gatscid();
		// 大树 共用变量 对象
		cfg = new Config(mActivity);
		ma = (MyApp) mActivity.getApplication();
		System.out.println("scid:" + scid);
		actvKeyWords.setText("");
		// list.clear();
		myBillPineListAdapter = new MyBillPineListAdapter(mActivity, list,ma,scid);
		lv_pinn.setAdapter(myBillPineListAdapter);
			show();
			// 大树 共用变量 对象
			actvKeyWords.setText("");
			show();
			LocationUtil.CreateLocation(mActivity);
			new ContinueUserTask(ma.userId, cfg.getString(Cnt.USER_PWD, ""), scid)
					.execute();
	}

	// 数据筛选
	private ArrayList<TimeBean> DataProcessing(
			ArrayList<TimeBean> timeBeanArrayList) {
		ArrayList<TimeBean> timeBean = new ArrayList<TimeBean>();
		// 现在的时间戳
		Long NowDate;
		if (Util.isEmpty(timeBeanArrayList)) {
			NowDate = Long.parseLong(DateUtil.dateToStamp());
		} else {
			if (!Util.isEmpty(timeBeanArrayList.get(0).getNowDate_ms())) {
				NowDate = Long.parseLong(timeBeanArrayList.get(0)
						.getNowDate_ms());
			} else {
				NowDate = 0l;
			}

		}
		// 标题栏的数量
		int titlssize = 7;
		for (int j = 0; j < titlssize; j++) {
			Long typetitle = NowDate + (j * 86400000);
			ArrayList<TimeBean> listbean = new ArrayList<TimeBean>();
			String titlestr = DateUtil.getStrTime(typetitle + "", "MM-dd");
			if (j == 0) {
				titlestr = titlestr + "(今天)";
			} else if (j == 1) {
				titlestr = titlestr + "(明天)";
			}
			timeBean.add(new TimeBean("", "", "", "", "", "", "", "", "", "",
					"", titlestr + ""));
			
			
			
			if (!Util.isEmpty(timeBeanArrayList)) {
				for (int i = 0; i < timeBeanArrayList.size(); i++) {
					//获取开始时间
					Long startdata;
					if (!Util.isEmpty(timeBeanArrayList.get(i).getNextBeginDate_ms())) {
						startdata = Long.parseLong(timeBeanArrayList.get(i)
								.getNextBeginDate_ms());
						Log.i("startdata", "有开始时间="+startdata);
					} else {
						
						startdata = 0l;
						Log.i("startdata", "没有有开始时间="+startdata);
//						break;
					}
					//结束时间戳
					Long enddata = 0l;
					if (!Util.isEmpty(timeBeanArrayList.get(i).getNextEndDate_ms())) {
						enddata = Long.parseLong(timeBeanArrayList.get(i).getNextEndDate_ms());
					}else{
						if (startdata ==0l) {
							enddata = 0l;
						}else{
							enddata = startdata+86400000l;
						}
					}
					
					
					String titledata = DateUtil.getStrTime(typetitle + "",
							"yyyy-MM-dd");
					//获取当前时间
					Long itemdata;
					if (!Util.isEmpty(timeBeanArrayList.get(i).getNowDate_ms())) {
						itemdata = Long.parseLong(timeBeanArrayList.get(i)
								.getNowDate_ms());
					} else {
						itemdata = Long.parseLong(DateUtil.dateToStamp());
						break;
					}
					if (j == 0) {
						if (startdata ==0l) {
							//没有开始时间 显示
							Log.i("startdata", "startdata="+startdata);
								listbean.add(timeBeanArrayList.get(i));
						}
					}
					if ((startdata > itemdata)) {
						//开始时间大于现在时间  项目未开始   不显示
						String starttime = DateUtil.getStrTime(startdata + "",
								"yyyy-MM-dd");
						//判断是不是今天的事今天的就加到数组里
						if (titledata.equals(starttime)) {
							listbean.add(timeBeanArrayList.get(i));
						}
					} else {
						String starttime = DateUtil.getStrTime(startdata + "",
								"yyyy-MM-dd");
						//判断是不是今天的事今天的就加到数组里
						if (titledata.equals(starttime)) {
							if(enddata < NowDate){
								//过期项目 不显示
							}else{
								listbean.add(timeBeanArrayList.get(i));
							}
						}
					}

					
				}
			}
			if (Util.isEmpty(listbean)) {
				listbean.add(new TimeBean("-1", "", "", "", "", "", "", "", "",
						"", "", ""));
			}
			timeBean.addAll(listbean);
		}
		return timeBean;
	}

	// 时间列表
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
				hm.put("getFlag", "2");
				InputStream inStream = NetService.openUrl(Cnt.ContinueUser, hm,
						"GET");
				// 解析xml数组赋值
				TimeList tblist = XmlUtil.TimeListxml(inStream);

				if (tblist.getState().equals("100")) {
					list = tblist.getTimeBean();
					System.out.println("httpOk");
				} else {
					String text = "请求失败，请检查网络后重试！";
					DialogUtil.newdialog(mActivity, text);
					return true;
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
			// 刷新数组
			if (!Util.isEmpty(list)) {
				Collections.sort(list);
				list = DataProcessing(list);
			}
			
			
			
			if (Util.isEmpty(list)) {
				lv_pinn.setVisibility(View.GONE);
				nodata.setVisibility(View.VISIBLE);
			}else {
				lv_pinn.setVisibility(View.VISIBLE);
				nodata.setVisibility(View.GONE);
				myBillPineListAdapter.updateListView(list);
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
	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {
		// TODO Auto-generated method stub
		// 时间标题的点击事件取消
		if (!list.get(position).getTypeTitle().equals("")) {
			return;
		}
		// 暂无数据的点击事件取消
		if (list.get(position).getPanelID().equals("-1")) {
			return;
		}
		
		String nextsid = list.get(position).getNextSurveyID();
		final Survey s = ma.dbService.getSurvey(nextsid);
		if (Util.isEmpty(s.surveyId)) {
			String text = "您还没有下载此项目，请前往下载！";
			
			DialogUtil.newdialog(mActivity, text);
			
			Intent intent = new Intent(mActivity, SubscibeActivity.class);
			startActivity(intent);
			return;
		}
		final UploadFeed feed = ma.dbService.getAllXmlUploadFeed(nextsid, ma.userId,
				list.get(position).getPanelID());
		if (Util.isEmpty(feed.getUuid())) {
			String text = "此问卷数据不在本设备上！";
			DialogUtil.newdialog(mActivity, text);
			return;
		}
		
		
		TextView tv = (TextView) view.findViewById(R.id.tv_item_residue);
        String residue = tv.getText().toString().trim();
		
		if (!Util.isEmpty(list.get(position).getNextBeginDate_ms())) {
			long surveytype = Long.parseLong(list.get(position).getNextBeginDate_ms())-Long.parseLong(list.get(position).getNowDate_ms());
			if (surveytype > 0) {
				//不能进入
				String text = "此问卷暂未开始！";
				DialogUtil.newdialog(mActivity, text);
				return;
			}
		
		}
		double d= 0;
//		startlocation(feed.getUuid());
		if (s.openGPS==0) {
			//可以进入
			Intent it = new Intent(mActivity, ChoiceModeActivity.class);
			
			feed.setTestMode(0);
			it.putExtra("uf", feed);
			it.putExtra("survey", s);
			it.putExtra("lat", d);
			it.putExtra("lng", d);
			it.putExtra("addr", "");

			Bundle b = new Bundle();
			b.putString("type", "1");
			b.putString("pid", list.get(position).getPanelID());
			b.putString("name", list.get(position).getUserName());
			b.putString("tel", list.get(position).getPhone());
			b.putString("scid", FragmentMain.gatscid());

			it.putExtras(b);
			mActivity.startActivity(it);
		}else {
			location = LocationUtil.getLocation();
			LocationUtil.isforceGPS(location,s, mActivity,new ImPlement() {
				
				@Override
				public void implement() {
					// TODO Auto-generated method stub
					
					//可以进入
					Intent it = new Intent(mActivity, ChoiceModeActivity.class);
					
					feed.setTestMode(0);
					it.putExtra("uf", feed);
					it.putExtra("survey", s);
					it.putExtra("lat", location.getLatitude());
					it.putExtra("lng", location.getLongitude());
					it.putExtra("addr", location.getAddrStr());

					Bundle b = new Bundle();
					b.putString("type", "1");
					b.putString("pid", list.get(position).getPanelID());
					b.putString("name", list.get(position).getUserName());
					b.putString("tel", list.get(position).getPhone());
					b.putString("scid", FragmentMain.gatscid());

					it.putExtras(b);
					mActivity.startActivity(it);
					
				}
			},ma);
		}
		
		
	}

	BDLocation location;

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	private ArrayList<TimeBean> searchlist = new ArrayList<TimeBean>();

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
			final String[] cities = { getString(R.string.input_category),
					"PID", "姓名", "电话" };
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
			myBillPineListAdapter.updateListView(searchlist);

			if (0 == position) {
				Util.viewShake(mActivity, globle_search);
				Toasts.makeText(mActivity, R.string.input_category_please,
						Toast.LENGTH_LONG).show();
				return;
			}

			if (Util.isEmpty(words)) {
				myBillPineListAdapter.updateListView(list);
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
				if (!Util.isEmpty(searchlist)) {
					list.addAll(DataProcessing(searchlist));
				}
				myBillPineListAdapter.updateListView(list);

			}
			break;

		}

	}
	
	public void refreshdata(){
		initData();
	}
	
	
//    /**
//     * 开启新定位的服务（仅有开启，关闭在服务里关闭）
//     */
//    private void startlocation(String feeduuid){
//    	System.out.println("开始定位 feeduuid"+feeduuid);
//    	
//    	Intent locationintent = new Intent(ma, NewLocationService.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("uuid", feeduuid);
//		locationintent.putExtras(bundle);
//		ma.startService(locationintent);
//		
//    }
}
