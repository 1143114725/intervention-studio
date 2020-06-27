package com.investigate.newsupper.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.investigate.newsupper.R;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.fragment.PersionFragment;
import com.investigate.newsupper.fragment.ProjectFragment;
import com.investigate.newsupper.fragment.TimeFragment;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.service.LocationService;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.BaseToast;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.UIUtils;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.locationutils.RxLocationTool;
import com.investigate.newsupper.view.CustomProgressDialog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 列表依赖的activity Created by EEH on 2018/3/28.
 */

public class FragmentMain extends BaseActivity implements View.OnClickListener {
	private static final String TAG = "FragmentMain";
	private TextView tv_persion, tv_time, tv_project;
	private TextView tv_persion_line, tv_time_line, tv_project_line;
	private ProjectFragment mProjectFragment;
	private PersionFragment mPersionFragment;
	private TimeFragment mTimeFragment;
	private Survey survey;
	private static MyApp ma;
	private static String scid, scname;
	// 标题栏
	private LinearLayout visit_left_iv, more_opt_ll;
	private UITextView tvSurveyTile;
	private TextView refresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		setListener();
		initData();
		dialogBtnSize = (int) (UIUtils
				.getDimenPixelSize(R.dimen.button_text_size) * TextSizeManager.getRealScale());
	}

	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainfragment_layout);
		tv_project = (TextView) findViewById(R.id.tv_title_project);
		tv_persion = (TextView) findViewById(R.id.tv_title_persion);
		tv_time = (TextView) findViewById(R.id.tv_title_time);

		tv_project_line = (TextView) findViewById(R.id.tv_title_project_line);
		tv_persion_line = (TextView) findViewById(R.id.tv_title_persion_line);
		tv_time_line = (TextView) findViewById(R.id.tv_title_time_line);

		refresh = (TextView) findViewById(R.id.refresh);

		// 返回按钮
		visit_left_iv = (LinearLayout) findViewById(R.id.visit_left_iv);
		// //标题tv
		tvSurveyTile = (UITextView) findViewById(R.id.visit_survey_name_tv);
		TextSizeManager.getInstance().addTextComponent(TAG, tvSurveyTile);

	}

	protected void setListener() {
		tv_persion.setOnClickListener(this);
		tv_project.setOnClickListener(this);
		tv_time.setOnClickListener(this);
		visit_left_iv.setOnClickListener(this);
		refresh.setOnClickListener(this);
	}

	private FragmentManager fragmentManager;

	protected void initData() {

		ma = (MyApp) this.getApplication();
		survey = (Survey) getIntent().getExtras().get("s");
		scid = survey.getSCID();
		scname = survey.getSCName();
		tvSurveyTile.setMaxLines(3);
		tvSurveyTile.setText(scname);

		fragmentManager = getSupportFragmentManager();

		mProjectFragment = ProjectFragment.newInstance();
		mPersionFragment = PersionFragment.newInstance();
		mTimeFragment = TimeFragment.newInstance();

		fragmentManager.beginTransaction()
				.add(R.id.ll_main_root, mProjectFragment)
				.add(R.id.ll_main_root, mPersionFragment)
				.add(R.id.ll_main_root, mTimeFragment).commit();

		changeFragment(1);
		changeline(1);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_project:
			Log.i(TAG, "onClick: project");
			changeFragment(1);
			changeline(1);
			break;
		case R.id.tv_title_persion:
			Log.i(TAG, "onClick: persion");

			if (NetUtil.checkNet(FragmentMain.this)) {
				PersionFragment.newInstance().refreshdata();
			} else {
				String text = "请检查网络状态！";
				DialogUtil.newdialog(FragmentMain.this, text);
			}

			changeFragment(2);
			changeline(2);
			break;
		case R.id.tv_title_time:
			if (NetUtil.checkNet(FragmentMain.this)) {
				TimeFragment.newInstance().refreshdata();
			} else {
				String text = "请检查网络状态！";
				DialogUtil.newdialog(FragmentMain.this, text);
			}

			changeFragment(3);
			changeline(3);
			Log.i(TAG, "onClick: time");
			break;
		case R.id.visit_left_iv:// 返回按钮
			FragmentMain.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
			break;
		case R.id.refresh:
			// if (NetUtil.checkNet(FragmentMain.this)) {
			// PersionFragment.newInstance().refreshdata();
			// TimeFragment.newInstance().refreshdata();
			// }else {
			// String text = "请检查网络状态！";
			// DialogUtil.newdialog(FragmentMain.this, text);
			// }

			// newplan();
			locationtime = 0;
			newbtnClickable(false);
			if (!RxLocationTool.isGpsEnabled(FragmentMain.this)) {

				// 如果需要定位跳转到打开gps界面
				if (survey.getForceGPS() != 0) {
					BaseToast.showLongToast(FragmentMain.this,
							"请将GPS模式设置为高精度模式！");
					RxLocationTool.openGpsSettings(FragmentMain.this);
					return;
				}
			}

			if (NetUtil.checkNet(mActivity)) {
				BaseLog.w("true");
				getlocation();
			}else{
				BaseLog.w("false");
				//提示  “抱歉,您的网络不佳!”
				BaseToast.showLongToast(mActivity, R.string.exp_net);
			}
			newbtnClickable(true);
//			return;
//			

			break;
		}
	}

	/**
	 * 获取定位点的流程
	 */
	private void getlocation() {
		// TODO Auto-generated method stub
		initlocation();
		if (2 != survey.forceGPS) {// 如果需要定位
			startProgressDialog();
		} else {
			newplan();
		}
	}
	/**
	 * 更换fragment 容器
	 * @param index
	 */
	public void changeFragment(int index) {

		FragmentTransaction transaction = fragmentManager.beginTransaction();

		hideFragment(transaction);
		switch (index) {
		case 1:
			if (null == mProjectFragment) {
				mProjectFragment = ProjectFragment.newInstance();
				transaction.add(R.id.ll_main_root, mProjectFragment);
			} else {
				transaction.show(mProjectFragment);
			}
			break;
		case 2:
			if (null == mPersionFragment) {
				mPersionFragment = PersionFragment.newInstance();
				transaction.add(R.id.ll_main_root, mPersionFragment);
			} else {
				transaction.show(mPersionFragment);
			}
			break;
		case 3:
			if (null == mTimeFragment) {
				mTimeFragment = TimeFragment.newInstance();
				transaction.add(R.id.ll_main_root, mTimeFragment);
			} else {
				transaction.show(mTimeFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}
/**
 * 更换fragment时更换按钮和提示的线
 * @param tab
 */
	private void changeline(int tab) {
		// TODO Auto-generated method stub
		// tv_persion_line,tv_time_line,tv_project_line
		switch (tab) {
		case 1:
			tv_project_line.setVisibility(View.VISIBLE);
			tv_persion_line.setVisibility(View.INVISIBLE);
			tv_time_line.setVisibility(View.INVISIBLE);
			break;
		case 2:
			tv_project_line.setVisibility(View.INVISIBLE);
			tv_persion_line.setVisibility(View.VISIBLE);
			tv_time_line.setVisibility(View.INVISIBLE);
			break;
		case 3:
			tv_project_line.setVisibility(View.INVISIBLE);
			tv_persion_line.setVisibility(View.INVISIBLE);
			tv_time_line.setVisibility(View.VISIBLE);
			break;
		}

	}
/**
 * 更换fragment辅助操作
 * @param transaction
 */
	private void hideFragment(FragmentTransaction transaction) {

		if (null != mTimeFragment) {
			transaction.hide(mTimeFragment);
		}
		if (null != mPersionFragment) {
			transaction.hide(mPersionFragment);
		}
		if (null != mProjectFragment) {
			transaction.hide(mProjectFragment);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FragmentMain.this.finish();
			overridePendingTransition(R.anim.right1, R.anim.left1);
		}
		return super.onKeyDown(keyCode, event);
	}

	public static String gatscid() {
		return scid;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}

	private void newplan() {
		if (!Util.isEmpty(survey.getSCID())) {
			// 是随访项目，进入新建名单界面
			newbtnClickable(false);
			Bundle bundle = new Bundle();
			String sid[] = survey.getSCNum().split(",");
			bundle.putString("SurveyId", sid[0]);
			bundle.putString("SC_ID", survey.getSCID());
			bundle.putString("Type", "1");
			goToActivity(FragmentMain.this, SetInnerActivity.class, bundle);
			return;
		}
	}
	/**
	 * 初始化定位
	 */
	private static LocationService locationServices;
	private MyFragmentLocationListener myListener = new MyFragmentLocationListener();

	private void initlocation() {

		locationServices = ((MyApp) getApplication()).locationService;
		// 获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		locationServices.registerListener(myListener);
		// 注册监听
		locationServices.setLocationOption(locationServices
				.getDefaultLocationClientOption());
		// 开始定位
		startloc();

	}

	/**
	 * 开始定位
	 */
	private void startloc() {
		Log.i(TAG, "startloc");
		locationServices.start();
	}

	/**
	 * 结束定位
	 */
	private void stoplocation() {
		Log.i(TAG, "stoplocation");
		locationServices.stop();
	}

	public double lat;
	public double lng;
	public String Addr;

	public class MyFragmentLocationListener extends BDAbstractLocationListener {

		@SuppressWarnings("unused")
		@Override
		public void onReceiveLocation(BDLocation location) {
			String coorType = location.getCoorType();
			// 获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
			int errorCode = location.getLocType();
			// 获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
			String latLongString;
			if (location != null) {
				lat = location.getLatitude();
				lng = location.getLongitude();
				Addr = location.getAddrStr();
				latLongString = "纬度:" + lat + "经度:" + lng + "coorType"
						+ coorType + "errorCode" + errorCode;
				// 存数据库
				Log.i(TAG, "lat:" + lat);
				Log.i(TAG, "lng:" + lng);
				Log.i(TAG, "Addr:" + Addr);
				Log.i(TAG, "isupdate:" + latLongString);
				// 关闭定位服务
				Log.i(TAG, "关闭定位服务");

			} else {
				latLongString = "无法获取地理信息" + errorCode;
			}

			Log.i(TAG, "updateWithNewLocation:您当前的位置是:" + latLongString);

		}
	}

	// 定位进度条
	private CustomProgressDialog progressDialog = null;

	// 启动定位等待动画
	public void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog
					.createDialog(FragmentMain.this);
			progressDialog.setMessage(FragmentMain.this.getResources()
					.getString(R.string.start_get_loc));
			progressDialog.setCancelable(false);
			progressDialog.findViewById(R.id.loadingImageView)
					.setOnLongClickListener(new DismissListener());
		}
		progressDialog.show();
		//
		handlerloc.post(startlocationrun);
	}

	// 定位dialog的长按事件
	TimerTask task;

	private final class DismissListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			if (null != task) {
				task.cancel();
				task = null;
			}
			stopProgressDialog();
			return true;
		}

	};

	// 关闭定位动画
	private void stopProgressDialog() {
		newbtnClickable(true);
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;

				// 停止定位
				stoplocation();
				if (lat != 0.0) {
					creatLocDialog(Addr, lat + "", lng + "");
					
				} else {
					// 按钮可操作
					creatLocDialog(Addr, "", "");
				}

			}
		}
	}

	Handler handlerloc = new Handler();

	int locationtime = 0;

	Runnable startlocationrun = new Runnable() {
		@Override
        public void run() {
			
       	 if (lat == 0.0) {
       		 stoplocation();
       		 startloc();
       		 handlerloc.postDelayed(startlocationrun, 1000);  
			}else if (lat != 0.0) {
				stopProgressDialog();
			}else {
				if (locationtime == 30) {
					stopProgressDialog();
				}
				locationtime++;
				handlerloc.postDelayed(startlocationrun, 1000);  
			}
        }
	};

	/**
	 * 检查定位点
	 */
	private boolean verifuLoc(String str) {
		if (Util.isEmpty(str)) {
			return false;
		} else if (-1 == str.indexOf("E")) {
			// 没有E
			return false;
		} else {
			// 有E
			return true;
		}
	}

	/**
	 * 显示定位信息的dialog
	 */
	private void creatLocDialog(String addrStr, String lat, String lng) {

		String latLongString = "纬度:" + lat + "经度:" + lng;
		 Log.i(TAG, "creatLocDialog:"+latLongString);
		if (!FragmentMain.this.isFinishing()) {


			AlertDialog.Builder locBuilder = new AlertDialog.Builder(
					FragmentMain.this, AlertDialog.THEME_HOLO_LIGHT);
			final AlertDialog create;
			
//			定位获取失败，请检查网络或到空旷处重试！
			if (lat.indexOf("E") == -1) {
				 Log.i(TAG, "creatLocDialog:isEmpty--"+latLongString);
				//判断是不是 4.9E-324
				if (verifuLoc(lat)) {
					Log.i(TAG, "creatLocDialog:verifuLoc"+latLongString);
					
					if (locationtime >= 30) {
						locBuilder.setMessage(Html.fromHtml("定位获取失败，请检查网络或到空旷处重试！", null, null));
						
						create = locBuilder.create();
						create.show();
						new Handler().postDelayed(new Runnable(){  
						    @Override
                            public void run() {
						    //execute the task  
							//关闭提示框
							create.dismiss();
							//按钮可操作
							newbtnClickable(true); 
						    }  
						}, 700);
						return;
					}else {
						handlerloc.postDelayed(startlocationrun, 1000); 
						return;
					}
					
					
					
				}else if (!Util.isEmpty(addrStr)) {
					locBuilder.setMessage(Html.fromHtml(addrStr + "<br><br>纬度:" + lat
							+ "<br><br>" + "经度:" + lng, null, null));
				}else{
					locBuilder.setMessage(Html.fromHtml("纬度:" + lat
							+ "<br><br>" + "经度:" + lng, null, null));
				}
				
				create = locBuilder.create();
				create.show();
				new Handler().postDelayed(new Runnable(){  
				    @Override
                    public void run() {
				    //execute the task  
					//关闭提示框
					create.dismiss();
					//按钮可操作
					newbtnClickable(true); 
					newplan();
				    }  
				}, 700);
			}else{
				if (2 != survey.forceGPS) {//如果需要定位
					
					if (0 == survey.forceGPS) {//不强制
						locBuilder.setMessage(Html.fromHtml("定位获取失败，请检查网络或到空旷处重试！", null, null));
						
						create = locBuilder.create();
						create.show();
						new Handler().postDelayed(new Runnable(){  
						    @Override
                            public void run() {
						    //execute the task  
							//关闭提示框
							create.dismiss();
							//按钮可操作
							newbtnClickable(true); 
						    }  
						}, 700);
						newplan();
						return;
					}else{
						System.out.println("forceGPS !=2");
//						if (locationtime >= 30) {
							locBuilder.setMessage(Html.fromHtml("定位获取失败，请检查网络或到空旷处重试！", null, null));
							
							create = locBuilder.create();
							create.show();
							new Handler().postDelayed(new Runnable(){  
							    @Override
                                public void run() {
							    //execute the task  
								//关闭提示框
								create.dismiss();
								//按钮可操作
								newbtnClickable(true); 
							    }  
							}, 700);
							return;
//						}else {
//							handlerloc.postDelayed(startlocationrun, 1000); 
//							return;
//						}
					}
					
					
				}else {
					System.out.println("forceGPS == 2");
					locBuilder.setMessage(Html.fromHtml("获取定位失败！", null, null));
					create = locBuilder.create();
					create.show();
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (null != create) {
								//关闭提示框
								create.dismiss();
								//按钮可操作
								newbtnClickable(true);
								newplan();
							}
						}
					}, 700);
				}
			}
			
			
			
			TextView msgTv=((TextView) create.findViewById(android.R.id.message));
			msgTv.setMinLines(2);
			msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
			msgTv.setGravity(Gravity.CENTER_VERTICAL);
			create.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(
					TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
			create.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(
					TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
			Button okBtn = create.getButton(DialogInterface.BUTTON_POSITIVE);
			okBtn.setBackgroundColor(0xFF6751B6);
			okBtn.setTextColor(Color.WHITE);
		}
	};

	private int dialogBtnSize;

	/**
	 * 设置新建按钮是否可用
	 * 
	 * @param isclickable
	 *            t: 可用 f:不可用
	 */
	private void newbtnClickable(boolean isclickable) {
		refresh.setClickable(isclickable);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		newbtnClickable(true); 
	}
}
