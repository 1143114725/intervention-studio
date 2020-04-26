package com.investigate.newsupper.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.investigate.newsupper.R;
import com.investigate.newsupper.adapter.VisitAdapter;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.Task;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.bean.UserPosition;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.TaskType;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UICheckBox;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.main.MainService;
import com.investigate.newsupper.service.LocationService;
import com.investigate.newsupper.service.MyLocation;
import com.investigate.newsupper.util.BaseToast;
import com.investigate.newsupper.util.CheckAudioPermission;
import com.investigate.newsupper.util.ComUtil;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.Publisher;
import com.investigate.newsupper.util.Publisher.Subscriber;
import com.investigate.newsupper.util.UIUtils;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.locationutils.RxLocationTool;
import com.investigate.newsupper.view.CustomProgressDialog;
import com.investigate.newsupper.view.HotalkMenuView;
import com.investigate.newsupper.view.Toasts;

/**
 * 项目列表
 * @author Administrator
 * survey.getForceGPS()
 * 2	不定位
 * 0	不强制
 * 1	强制定位
 *
 */
public class VisitActivity extends BaseActivity implements OnClickListener,
		Subscriber {

	private static final String TAG = VisitActivity.class.getSimpleName();
//	String feeduuid;
	 
	String uuid;
	String issustain = "false";
	private ListView lvVisit;
	private LinearLayout visit_left_iv, more_opt_ll;
	private ImageView more_opt;
	Button new_panel;
	LinearLayout gps_circle;
	ImageView gps_img;
	TextView gps_tv;
	private VisitAdapter visitAdapter;
	private LinearLayout globle_search;// 全局搜索条
	private View globle_search_line;
	private UITextView tv_spinner;// 下拉选项
	// 嵌入
	private Survey survey;
	private MyApp ma;
	private ArrayList<UploadFeed> fs;
	private ArrayList<UploadFeed> fsAll = new ArrayList<UploadFeed>();
	private ArrayList<UploadFeed> fsCompleted = new ArrayList<UploadFeed>();
	private UITextView tvSurveyTile, completed_num_count;
	private UICheckBox formal_mode_cb, test_mode_cb;
	/** , tvSurveyTile **/
	private UITextView tvNoList;
	private AutoCompleteTextView actvKeyWords;
	private int position;// 下拉位置

//	private BDLocation mBDLocation;

	private UITextView completed_num;

	private UITextView uploaded_num;

	private UITextView unUploaded_num;

	private Intent intent;

	private int dialogBtnSize;

	private boolean isLocation;
	private boolean isFirstLoc = true;
	private boolean showCompleted = true;


	String selectitem = "";
	String spinnertext = "";
	
	


	public int isTestMode = 0;

	private boolean isRemove;
	TimerTask task;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 去掉状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.visit_activity);
		Publisher.getInstance().addSubscriber(this);
		// 大树动画 弹出底部动画
		ma = (MyApp) getApplication();
		ma.addActivity(this);
		dialogBtnSize = (int) (UIUtils
				.getDimenPixelSize(R.dimen.button_text_size) * TextSizeManager
				.getInstance().getRealScale());
		boolean isClearMemory = ma.cfg.getBoolean("clearMemory");
		boolean isLoginMode = ma.cfg.getBoolean(Cnt.LOGIN_MODE);
		// 是否清理内存并且不是离线登录状态才 重新读取
		if (isClearMemory) {
			if (!isLoginMode) {
				ma.userId = ma.cfg.getString("userId",
						this.getString(R.string.user_name_test));
				ma.userPwd = ma.cfg.getString("memoryPP", null);
			} else {
				ma.userId = ma.cfg.getString("userId",
						this.getString(R.string.user_name_test));
			}
			ma.cfg.putBoolean("clearMemory", false);
		}
		formal_mode_cb = (UICheckBox) findViewById(R.id.formal_mode_cb);
		CbClicklistener cbClicklistener = new CbClicklistener();
		formal_mode_cb.setOnClickListener(cbClicklistener);
		formal_mode_cb.setButtonDrawable(R.drawable.small_radiobutton);
		test_mode_cb = (UICheckBox) findViewById(R.id.test_mode_cb);
		test_mode_cb.setButtonDrawable(R.drawable.small_radiobutton);
		test_mode_cb.setOnClickListener(cbClicklistener);
		if (test_mode_cb.isChecked()) {
			isTestMode = 1;
		} else {
			isTestMode = 0;
		}

		completed_num_count = (UITextView) findViewById(R.id.completed_num_count);
		completed_num = (UITextView) findViewById(R.id.completed_num);
		uploaded_num = (UITextView) findViewById(R.id.uploaded_num);
		unUploaded_num = (UITextView) findViewById(R.id.unUploaded_num);
		TextSizeManager.getInstance()
				.addTextComponent(TAG, completed_num_count)
				.addTextComponent(TAG, completed_num)
				.addTextComponent(TAG, uploaded_num)
				.addTextComponent(TAG, unUploaded_num)
				.addTextComponent(TAG, tv_spinner)
				.addTextComponent(TAG, tvNoList)
				.addTextComponent(TAG, test_mode_cb)
				.addTextComponent(TAG, formal_mode_cb);
		new_panel = (Button) findViewById(R.id.new_panel);
		
		gps_circle = (LinearLayout) findViewById(R.id.GPS_circle);
		gps_img = (ImageView) findViewById(R.id.GPS_img);
		gps_tv=(TextView) findViewById(R.id.GPS_tv);
		globle_search = (LinearLayout) findViewById(R.id.globle_search);
		globle_search_line = (View) findViewById(R.id.globle_search_line);
		more_opt = (ImageView) findViewById(R.id.more_opt);
		more_opt_ll = (LinearLayout) findViewById(R.id.more_opt_ll);
		more_opt_ll.setOnClickListener(this);
		visit_left_iv = (LinearLayout) findViewById(R.id.visit_left_iv);
		lvVisit = (ListView) findViewById(R.id.visit_list);


		visit_left_iv.setOnClickListener(this);
		actvKeyWords = (AutoCompleteTextView) findViewById(R.id.keyword_actv);
		actvKeyWords.setInputType(InputType.TYPE_NULL);
		actvKeyWords.setOnTouchListener(new FocusListener());
		tvSurveyTile = (UITextView) findViewById(R.id.visit_survey_name_tv);
		TextSizeManager.getInstance().addTextComponent(TAG, tvSurveyTile);
		int width = this.getWindowManager().getDefaultDisplay().getWidth();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				(int) (width / 17 * 9),
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		tvSurveyTile.setMaxLines(3);
		tvNoList = (UITextView) findViewById(R.id.no_visit_list_tv);
		tv_spinner = (UITextView) findViewById(R.id.spinner);
		//初始化定位
//		if (NetUtils.isConnected(VisitActivity.this)) {
			//有网
			
//		}else{
//			//没网，走gps定位
//		}
		
		
		
			
		survey = (Survey) getIntent().getExtras().get("s");
		
		/**
		 * 接收LocalActivity列表中传递过来的Survey
		 */
		if(survey.getForceGPS() != 0){
			if (!RxLocationTool.isGpsEnabled(VisitActivity.this)) {
				//如果需要定位跳转到打开gps界面`
				if (survey.getForceGPS() !=2) {
					BaseToast.showLongToast(VisitActivity.this, "请将GPS模式设置为高精度模式！");
					RxLocationTool.openGpsSettings(VisitActivity.this);
					VisitActivity.this.finish();
				}
			}
		}
			
			
		String type = getIntent().getStringExtra("type");
		//设置title		
		if (!Util.isEmpty(survey.surveyTitle)) {
			tvSurveyTile.setText(survey.surveyTitle);
		}
		// 新建是否是内部受邀
		if (survey.openStatus == 1) {
			isRemove = true;
			new_panel.setVisibility(View.GONE);
		} else {
			globle_search.setVisibility(View.GONE);
			globle_search_line.setVisibility(View.GONE);
		}
		if (1 == survey.openGPS && 0 < survey.timeInterval) {
			gps_circle.setVisibility(View.VISIBLE);
		} else {
			gps_circle.setVisibility(View.GONE);
		}
	 	gps_circle.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				gps_img.setImageResource(R.drawable.gps_close);
				gps_tv.setText(R.string.gps_line_close);
				isOpenGps = false;
				MyLocation.stoplocation();
				hideNotification();
				return true;
			}
		});
		//是连续性访问
				if (type.equals("1")) {
					selectitem = getIntent().getStringExtra("selectitem");
					spinnertext = getIntent().getStringExtra("spinnertext");
					position = getIntent().getIntExtra("selectnum",0);
					
					if (position > 0) {
						//设置搜索条件
						tv_spinner.setText(selectitem);
						actvKeyWords.setText(spinnertext);
						ImageView search_btn = (ImageView) findViewById(R.id.search_btn);
						if (survey.openStatus == 1) {
							//点击搜索按钮
							btnClick(search_btn);
						}
					}else {
						tv_spinner.setText("类别");
						actvKeyWords.setText(spinnertext);
					}
				}
				initlocation();
				
	}

	/**
	 * 测试模式选择监听器
	 * 
	 * @author Administrator
	 * 
	 */
	class CbClicklistener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.formal_mode_cb: {
				formal_mode_cb.setChecked(true);
				test_mode_cb.setChecked(false);
				isTestMode = 0;
			}
				break;

			case R.id.test_mode_cb: {
				test_mode_cb.setChecked(true);
				formal_mode_cb.setChecked(false);
				isTestMode = 1;
			}
				break;
			}

		}

	}

	@Override
	protected void onResume() {
		// 新建限制
		if (1 == survey.oneVisit) {
			boolean isNewPanel = ma.dbService.IsExistSurvey(survey.surveyId);
			if (isNewPanel) {
				isRemove = true;    
				new_panel.setVisibility(View.GONE);
			}
		}
		if (Util.isEmpty(ma.userId)) {
			ma.userId = ((null == ma.cfg) ? (ma.cfg = new Config(this))
					: (ma.cfg)).getString("UserId", "");
		}
		fs = ma.dbService.getAllXmlUploadFeedList(survey.surveyId, ma.userId);
		fsAll.clear();
		fsAll.addAll(fs);
		fsCompleted.clear();
		for (UploadFeed f : fsAll) {
			if (9 == f.getIsUploaded()) {
				fsCompleted.add(f);
			}
		}
		if (!showCompleted) {
			fs.removeAll(fsCompleted);
		}
		if (Util.isEmpty(fsAll)) {
			uploaded_num.setText(getResources().getString(
					R.string.uploaded_num, 0 + "", 0 + ""));
			unUploaded_num.setText(getResources().getString(
					R.string.unUploaded_num, 0 + "", 0 + ""));
		} else {
			// lvVisit.setOnItemClickListener(this);
			long un = ma.dbService.recordUnUploadCounts(survey.surveyId,
					ma.userId);
			long up = ma.dbService.recordUploadCounts(survey.surveyId,
					ma.userId);
			completed_num_count.setText(getCompletedCount() + "");
			uploaded_num.setText(getResources().getString(
					R.string.uploaded_num, getUploadedCount() + "", up + ""));
			unUploaded_num.setText(getResources()
					.getString(R.string.unUploaded_num,
							getUnUploadedCount() + "", un + ""));
		}
		if (Util.isEmpty(fs)) {
			lvVisit.setVisibility(View.GONE);
			tvNoList.setVisibility(View.VISIBLE);

		} else {
			tvNoList.setVisibility(View.GONE);
			lvVisit.setVisibility(View.VISIBLE);
			// 地图位置传入更改
			if (null == visitAdapter) {
//				if (mBDLocation != null) {
//					visitAdapter = new VisitAdapter(VisitActivity.this, fs,
//							mBDLocation.getLatitude(),
//							mBDLocation.getLongitude(),
//							mBDLocation.getAddrStr(), survey, ma);
//				} else {
					visitAdapter = new VisitAdapter(VisitActivity.this, fs,
							null, null, null, survey, ma);
//				}

				lvVisit.setAdapter(visitAdapter);
			} else {
//				if (mBDLocation != null) {
//					visitAdapter.refresh(fs, mBDLocation.getLatitude(),
//							mBDLocation.getLongitude(),
//							mBDLocation.getAddrStr());
//				} else {
					visitAdapter.refresh(fs, null, null, null);
//				}
			}
		}
		newbtnClickable(true);
		super.onResume();
	}

	public int getCompletedCount() {
		int counts = 0;
		for (UploadFeed uf : fsAll) {
			if (1 == uf.getIsCompleted()) {
				counts++;
			}
		}
		return counts;
	}

	public int getUploadedCount() {
		int counts = 0;
		for (UploadFeed uf : fsAll) {
			if (9 == uf.getIsUploaded()) {
				counts++;
			}
		}
		return counts;
	}

	/**
	 * 未上传的数据
	 * 
	 * @return
	 */
	public int getUnUploadedCount() {
		return getCompletedCount() - getUploadedCount();
	}

	public void reSetCount() {
		long un = ma.dbService.recordUnUploadCounts(survey.surveyId, ma.userId);
		long up = ma.dbService.recordUploadCounts(survey.surveyId, ma.userId);
		completed_num_count.setText(getCompletedCount() + "");
		uploaded_num.setText(getResources().getString(R.string.uploaded_num,
				getUploadedCount() + "", up + ""));
		unUploaded_num.setText(getResources().getString(
				R.string.unUploaded_num, getUnUploadedCount() + "", un + ""));
	}

	private String spinnerTv;

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.ll_spinner:
			AlertDialog.Builder builder = new AlertDialog.Builder(
					VisitActivity.this, AlertDialog.THEME_HOLO_LIGHT);
			builder.setIcon(R.drawable.ic_menu_archive);
			builder.setTitle(R.string.input_category);
			spinnerTv = getString(R.string.more_thing);
			// 指定下拉列表的显示数据
			final String[] cities = { getString(R.string.input_category),
					survey.getParameter1(), survey.getParameter2(),
					survey.getParameter3(), survey.getParameter4() };
			// 设置一个下拉的列表选择项
			builder.setItems(cities, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toasts.makeText(
							VisitActivity.this,
							VisitActivity.this.getString(R.string.choice_mode)
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
			if (0 == position) {
				Util.viewShake(VisitActivity.this, globle_search);
				Toasts.makeText(VisitActivity.this,
						R.string.input_category_please, Toast.LENGTH_LONG)
						.show();
				return;
			}
			String words = actvKeyWords.getText().toString().trim();
			if (Util.isEmpty(words)) {
				// Util.viewShake(VisitActivity.this, actvKeyWords);
				// Toasts.makeText(VisitActivity.this, R.string.input_keyword,
				// Toast.LENGTH_LONG).show();
				// return;
				fs = ma.dbService.getAllXmlUploadFeedList(survey.surveyId,
						ma.userId);
			} else {
				fs = ma.dbService.searchXmlUploadFeedList(survey.surveyId,
						ma.userId, position, words);
				if (Util.isEmpty(fs)) {
					Toasts.makeText(VisitActivity.this,
							VisitActivity.this.getString(R.string.no_find),
							Toast.LENGTH_SHORT).show();
				}
			}
			// 地图位置传入更改
			// BDLocation _loc = null;
			if (null == visitAdapter) {
//				if (mBDLocation != null) {
//					visitAdapter = new VisitAdapter(VisitActivity.this, fs,
//							mBDLocation.getLatitude(),
//							mBDLocation.getLongitude(),
//							mBDLocation.getAddrStr(), survey, ma);
//				} else {
					visitAdapter = new VisitAdapter(VisitActivity.this, fs,
							null, null, null, survey, ma);
//				}
				lvVisit.setAdapter(visitAdapter);
			} else {
//				if (mBDLocation != null) {
//					visitAdapter.refresh(fs, mBDLocation.getLatitude(),
//							mBDLocation.getLongitude(),
//							mBDLocation.getAddrStr());
//				} else {
					visitAdapter.refresh(fs, null, null, null);
//				}
			}
			break;
			//新建按钮
		case R.id.new_panel:
			locationtime =0;
			newbtnClickable(false);
			if (!RxLocationTool.isGpsEnabled(VisitActivity.this)) {
				
				//如果需要定位跳转到打开gps界面
				if (survey.getForceGPS() != 2) {
					BaseToast.showLongToast(VisitActivity.this, "请将GPS模式设置为高精度模式！");
					RxLocationTool.openGpsSettings(VisitActivity.this);
					return;
				}
			}
			
			getlocation();
			
			
			break;
		case R.id.GPS_circle:
			//先判断是否开启了GPS 没开启Gps让他去开启GPS   
			Log.i("gps", "oncleck:gps_circle isOpenGps = "+isOpenGps);
			if (!isOpenGps) {
				//开启gps轨迹定位
				if (2 != survey.forceGPS && !Cnt.LOC_SERVICE_START) {
					System.out.println("定位服务开启");
//					showNotification();
					newUUIDInstance();
					issustain = "true";
					intent = new Intent(ma, MyLocation.class);
					Bundle bundle = new Bundle();
					bundle.putString("uuid", uuid);
					bundle.putString("issustain", issustain);
					intent.putExtras(bundle);// 定位间隔
					ma.startService(intent);
					Cnt.LOC_SERVICE_START = true;
					startalarm();
				}
				gps_img.setImageResource(R.drawable.gps_open);
				gps_tv.setText(R.string.gps_line_open);
				isOpenGps = true;
			} 
			else {
				//关闭gps轨迹定位
//				ma.stopService(intent);
//				gps_img.setImageResource(R.drawable.gps_close);
//				gps_tv.setText(R.string.gps_line_close);
//				isOpenGps = false;
				String text = "长按图标关闭定位！";
				Toast.makeText(VisitActivity.this, text, Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	//是否开启了gps轨迹定位
	public boolean isOpenGps;
	/**
	 * 新建访问
	 */
	private void newVisit() {
		if (1 == survey.openGPS && 0 < survey.timeInterval && !isOpenGps) {//检查是否开启定位轨迹
			Toasts.makeText(VisitActivity.this, R.string.no_gps_line,
					Toast.LENGTH_SHORT).show();

			return;
		}
		
		if (survey.globalRecord == 1) {//是否有全局录音
			if (!CheckAudioPermission.isHasPermission(ma)) {//启用录音失败
				AlertDialog.Builder Recorddialog = new AlertDialog.Builder(
						VisitActivity.this, AlertDialog.THEME_HOLO_LIGHT);
				Recorddialog
						.setMessage("录音启用失败，请检查录音权限！")
						.setPositiveButton(
								VisitActivity.this.getResources().getString(
										R.string.ok),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();

										return;
									}

								});

				AlertDialog Recordcreate = Recorddialog.create();
				Recordcreate.show();
				TextView msgTv=((TextView) Recordcreate.findViewById(android.R.id.message));
				msgTv.setMinLines(2);
				msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				msgTv.setGravity(Gravity.CENTER_VERTICAL);
				Recordcreate.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(
						TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				Button okBtn = Recordcreate.getButton(AlertDialog.BUTTON_POSITIVE);
				okBtn.setBackgroundColor(0xFF6751B6);
				okBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
				okBtn.setTextColor(Color.WHITE);

			} else {
				// 继续判断是否有定位-->改成直接过了
				implement();
			}

		} else {
			 //继续判断是否有定位-->改成直接过了
			implement();
		}

	}

	/**
	 * 定位的判断逻辑
	 */
//	private void isforceGPS() {
//
//		// 判断定位是否强制
//		if (!isLocation && 2 != survey.forceGPS) {
//			if (1 == survey.forceGPS) {
//				//强制定位  没有定位信息 直接跳出
//				AlertDialog.Builder locBuilder = new AlertDialog.Builder(
//						VisitActivity.this, AlertDialog.THEME_HOLO_LIGHT);
//				locBuilder
////						.setTitle("提示")
////						.setIcon(android.R.drawable.ic_dialog_info)
//						.setMessage("尚未获取到定位信息，请稍后重试")
//						.setPositiveButton(
//								VisitActivity.this.getResources().getString(
//										R.string.ok),
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										dialog.dismiss();
//										return;
//									}
//
//								});
//				AlertDialog create = locBuilder.create();
//				create.show();
//				TextView msgTv=((TextView) create.findViewById(android.R.id.message));
//				msgTv.setMinLines(2);
//				msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
//				msgTv.setGravity(Gravity.CENTER_VERTICAL);
//				create.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(
//						TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
//				Button okBtn = create.getButton(AlertDialog.BUTTON_POSITIVE);
//				okBtn.setBackgroundColor(0xFF6751B6);
//				okBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
//				okBtn.setTextColor(Color.WHITE);
//
//			} else {
//				//非强制定位   没有定位信息也可以继续新建问卷
//				AlertDialog.Builder locBuilder = new AlertDialog.Builder(
//						VisitActivity.this, AlertDialog.THEME_HOLO_LIGHT);
//				locBuilder
////						.setTitle("提示")
////						.setIcon(android.R.drawable.ic_dialog_info)
//						.setMessage("尚未获取到定位信息，是否继续？")
//						.setPositiveButton(
//								VisitActivity.this.getResources().getString(
//										R.string.ok),
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//
//										implement();
//
//										dialog.dismiss();
//										return;
//									}
//
//								})
//						.setNegativeButton(
//								VisitActivity.this.getResources().getString(
//										R.string.cancel),
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										dialog.dismiss();
//									}
//								});
//				AlertDialog create = locBuilder.create();
//				create.show();
//				TextView msgTv=((TextView) create.findViewById(android.R.id.message));
//				msgTv.setMinLines(2);
//				msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
//				msgTv.setGravity(Gravity.CENTER_VERTICAL);
//				create.getButton(create.BUTTON_NEGATIVE).setTextSize(
//						TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
//				create.getButton(create.BUTTON_POSITIVE).setTextSize(
//						TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
//				Button okBtn = create.getButton(create.BUTTON_POSITIVE);
//				okBtn.setBackgroundColor(0xFF6751B6);
//				okBtn.setTextColor(Color.WHITE);
//
//			}
//
//		} else {
//			implement();
//		}
//	}

	/**
	 * 定位和录音判断完了继续执行的代码
	 */
	private void implement() {
		//是否有上一页密码
		if (!"".equals(survey.getPassword())) {
			final EditText et = new EditText(VisitActivity.this);
			new AlertDialog.Builder(VisitActivity.this,
					AlertDialog.THEME_HOLO_LIGHT)
					.setTitle(VisitActivity.this.getString(R.string.input_pwd))
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(et)
					.setPositiveButton(
							VisitActivity.this.getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String pwd = et.getText().toString();
									if ("".equals(pwd.trim())) {
										Toasts.makeText(
												VisitActivity.this,
												VisitActivity.this
														.getString(R.string.null_reverse_input),
												Toast.LENGTH_SHORT).show();
										return;
									}
									if (pwd.equals(survey.getPassword())) {
										// 跳转
										UploadFeed feed = new UploadFeed();
										
										feed.setSurvey(survey);
										feed.setVisitMode(survey.visitMode);
										feed.setSurveyId(survey.surveyId);
										feed.setSurveyTitle(survey.surveyTitle);
										feed.setUserId(ma.userId);
										feed.setFeedId("");
										feed.setShowpageStatus(survey.showpageStatus);
										feed.setTestMode(isTestMode);
										//如果有定位点就存经纬度、没有定位点就存空
										
//										String text = "定位点信息是-lat:"+ mBDLocation.getLatitude()+"lon:"+mBDLocation.getLongitude();
										String text = "定位点信息是-lat:"+ lat+"lon:"+lng;
										Log.e("showlocation", text);
										
										
										if (!verifuLoc(lat+"")) {
											feed.setLat(lat+"");
											feed.setLng(lng+"");
											feed.setVisitAddress(Addr);
										}else {
											feed.setLat("");
											feed.setLng("");	
										}
										
										Intent it = null;
										if (0 == survey.showpageStatus) {
											it = new Intent(
													VisitActivity.this,
													NativeModeNoPageActivity.class);
										} else {
											it = new Intent(VisitActivity.this,
													NativeModeActivity.class);
										}
										
										
										Bundle bundle = new Bundle();
										bundle.putSerializable("feed", feed);
										it.putExtras(bundle);
										startActivity(it);
										overridePendingTransition(
												R.anim.zzright, R.anim.zzleft);
										// 跳转结束
										dialog.dismiss();
									} else {
										Toasts.makeText(
												VisitActivity.this,
												VisitActivity.this
														.getString(R.string.pwd_no),
												Toast.LENGTH_SHORT).show();
										dialog.dismiss();
									}
								}
							})
					.setNegativeButton(
							VisitActivity.this.getString(R.string.cancle), null)
					.show();

		} else {
			UploadFeed feed = new UploadFeed();
			feed.setSurvey(survey);
			feed.setVisitMode(survey.visitMode);
			feed.setSurveyId(survey.surveyId);
			feed.setSurveyTitle(survey.surveyTitle);
			feed.setUserId(ma.userId);
			feed.setFeedId("");
			feed.setShowpageStatus(survey.showpageStatus);
			feed.setTestMode(isTestMode);
			
//			String text = "定位点信息是-lat:"+ mBDLocation.getLatitude()+"lon:"+mBDLocation.getLongitude();
			String text = "定位点信息是-lat:"+ lat+"lon:"+lng;
			Log.e("showlocation", text);
			
			
			if (!verifuLoc(lat+"")) {
				feed.setLat(lat+"");
				feed.setLng(lng+"");
				feed.setVisitAddress(Addr);
			}else {
				feed.setLat("");
				feed.setLng("");	
			}
			
			Intent it = null;
			if (0 == survey.showpageStatus) {
				it = new Intent(VisitActivity.this,
						NativeModeNoPageActivity.class);
			} else {
				it = new Intent(VisitActivity.this, NativeModeActivity.class);
			}
			//不等于2 的时候才是开启gps定位了
			if (survey.getForceGPS() !=2) {
//				CheckGps.getInstance().startlocation(feeduuid,ma);
			}
			Bundle bundle = new Bundle();
			bundle.putSerializable("feed", feed);
			it.putExtras(bundle);
			startActivity(it);
			overridePendingTransition(R.anim.zzright, R.anim.zzleft);
		}
	}
	//Autocompletetextview监听
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
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("isOpenGps", "isOpenGps="+isOpenGps);
			if (isOpenGps) {
				String text = "长按图标关闭定位！";
				Toast.makeText(VisitActivity.this, text, Toast.LENGTH_SHORT).show();
				return true;
			}else {
				VisitActivity.this.finish();
				overridePendingTransition(R.anim.right1, R.anim.left1);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.visit_left_iv://返回按钮
			Log.i("isOpenGps", "isOpenGps="+isOpenGps);
			if (isOpenGps) {
				String text = "长按图标关闭定位！";
				Toast.makeText(VisitActivity.this, text, Toast.LENGTH_SHORT).show();
			}else {
				VisitActivity.this.finish();
				overridePendingTransition(R.anim.right1, R.anim.left1);
			}
			break;
		case R.id.more_opt_ll://菜单
			super.openOptionsMenu();
			break;
		default:
			break;
		}
	}

	// 菜单
	public HotalkMenuView menuListView = null;

	/**
	 * 系统菜单初始化 void 大树动画 2 查看地图 访前说明 重置
	 */
	private void initSysMenu() {
		if (menuListView == null) {
			menuListView = new HotalkMenuView(this);
		}
		menuListView.listview.setOnItemClickListener(listClickListener);
		menuListView.clear();
		// 添加按位置添加 ic_menu_mapmode_2 ic_menu_paste_holo_light_2
		// ic_menu_refresh_2 ic_menu_archive
		menuListView.add(HotalkMenuView.MENU_SEND_MSG_FORMULAR,
				VisitActivity.this.getString(R.string.read_quota),
				R.drawable.test_zsj33);
		menuListView.add(HotalkMenuView.MENU_VIEW_CONTACT,
				VisitActivity.this.getString(R.string.read_map),
				R.drawable.test_zsj22);
		menuListView.add(HotalkMenuView.MENU_ADD_CONTACT,
				VisitActivity.this.getString(R.string.read_ins),
				R.drawable.test_zsj33);
		// 版本兼容 只有IPSOS才能添加
		if (2 == Cnt.appVersion) {
			menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES,
					VisitActivity.this.getString(R.string.reset),
					R.drawable.test_zsj44);
		}
		// 删除附件
		menuListView.add(HotalkMenuView.MENU_DELETE_MULTI_MSG,
				VisitActivity.this.getString(R.string.read_attach),
				R.drawable.test_zsj11);
	}

	// 大树动画
	protected void switchSysMenuShow() {
		// 初始化系统菜单
		initSysMenu();
		if (!menuListView.getIsShow()) {
			menuListView.show();
		} else {
			menuListView.close();
		}
	}

	/**
	 * 创建菜单,只在创建时调用一次. 大树动画
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		// 注意返回值.
		return super.onCreateOptionsMenu(menu);
	}

	// 大树动画
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		switchSysMenuShow();
		return false;// 返回为true 则显示系统menu
	}

	/**
	 * 菜单点击事件处理 大树动画 1 跳转在这里
	 */
	OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			// 获取key唯一标识
			int key = Integer.parseInt(view.getTag().toString());
			// 跳转
			switch (key) {
			case HotalkMenuView.MENU_SEND_MSG_FORMULAR:
				view.setBackgroundColor(Color.YELLOW);
				Intent itquota = new Intent(VisitActivity.this,
						MyQuotaActivity.class);
				itquota.putExtra("s", survey);
				startActivity(itquota);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				break;
			case HotalkMenuView.MENU_VIEW_CONTACT:
				// System.out.println("查看地图");
				view.setBackgroundColor(Color.YELLOW);
				Intent it = new Intent(VisitActivity.this, MapActivity.class);
				startActivity(it);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				break;
			case HotalkMenuView.MENU_ADD_CONTACT:
				// System.out.println("访前说明");
				view.setBackgroundColor(Color.YELLOW);
				Intent itRed = new Intent(VisitActivity.this,
						MyWordActivity.class);
				itRed.putExtra("survey", survey);
				startActivity(itRed);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				break;
			case HotalkMenuView.MENU_ADD_TO_FAVORITES:
				// System.out.println("重置");
				view.setBackgroundColor(Color.YELLOW);
				if (NetUtil.checkNet(VisitActivity.this)) {
					show();
					Log.i("zrl1", "重置走这里");
					HashMap<String, Object> hm = new HashMap<String, Object>();
					hm.put("surveyID", survey.surveyId);
					MainService.newTask(new Task(TaskType.TS_RESET, hm));
				} else {
					Toasts.makeText(VisitActivity.this, R.string.net_exception,
							Toast.LENGTH_LONG).show();
				}
				break;
			case HotalkMenuView.MENU_DELETE_MULTI_MSG:
				// 删除附件
				// System.out.println("查看删除附件");
				view.setBackgroundColor(Color.YELLOW);
				Intent intent = new Intent(VisitActivity.this,
						AttachActivity.class);
				intent.putExtra("sid", survey.surveyId);
				startActivity(intent);
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				break;
			default:
				break;
			}
			// 关闭
			menuListView.close();
		}

	};


	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		ma.remove(this);
		Publisher.getInstance().removeSubscriber(this);
		System.out.println("访问列表销毁");
		// 退出项目保存数据
		if (2 != survey.forceGPS && Cnt.LOC_SERVICE_START) {
			ma.stopService(intent);
			Cnt.LOC_SERVICE_START = false;
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	// 大树 重置 功能 2
	ArrayList<UploadFeed> feeds = new ArrayList<UploadFeed>();
	private String arr = "";

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		switch ((Integer) param[0]) {
		case TaskType.TS_REDEAL:
			dismiss();
			if (null != param[1]) {
				int state = (Integer) param[1];
				if (state == 100) {
					Toasts.makeText(this, R.string.reset_success,
							Toast.LENGTH_LONG).show();
				} else {
					Toasts.makeText(this, R.string.reset_fail,
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		case TaskType.TS_RESET:
			dismiss();
			if (null != param[1]) {
				feeds = (ArrayList<UploadFeed>) param[1];
				if (feeds.size() > 0) {
					setPromot(VisitActivity.this
							.getString(R.string.less_compare) + feeds.size());
					UploadFeed resetFeed = (UploadFeed) feeds.get(0);
					new RecoverTask(resetFeed, 1).execute();
					return;
				} else {
					Toasts.makeText(this, R.string.no_need_reset,
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toasts.makeText(this, R.string.reset_fail, Toast.LENGTH_LONG)
						.show();
			}
			break;
		}

	}

	// 大树 重置 3 重置 任务 的实现
	// 重置功能线程
	class RecoverTask extends AsyncTask<Void, Integer, Boolean> {
		private UploadFeed u;
		private int isAll;// 0单个, 1所有

		public RecoverTask(UploadFeed upload, int _isAll) {
			this.u = upload;
			this.isAll = _isAll;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean isSuceess = Boolean.FALSE;
			if (null != u) {
				try {
					// 通过feedId比对工作
					String feedId = u.getFeedId();
					UploadFeed resetFeed = ma.dbService.getResetFeed(
							survey.surveyId, feedId);
					// 存在改状态
					if (null != resetFeed && !Util.isEmpty(resetFeed.getUuid())) {
						// 1.清空答案表
						int updateResetFeed = ma.dbService
								.updateResetFeed(resetFeed.getUuid());
						// 2.清空upload文件位置 数据库状态。
						int deleteResetFeedAttach = ma.dbService
								.deleteResetFeedAttach(resetFeed.getUuid());
						// 3.删除upload相关文件的数据库
						int deleteResetAnswer = ma.dbService.deleteResetAnswer(
								survey.surveyId, resetFeed.getUuid());
						// 证明成功
						if (updateResetFeed > 0 && deleteResetAnswer > 0) {
							arr += u.getFeedId() + ",";
						}
					}
					isSuceess = Boolean.TRUE;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return isSuceess;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				// 成功
				sendSyncMessage(1006, isAll, u);
			} else {
				// 失败
				sendSyncMessage(1006, isAll, u);
			}
			super.onPostExecute(result);
		}

		private void sendSyncMessage(int what, int arg2, Object u) {
			Message msg = Message.obtain();
			msg.what = what;
			msg.arg2 = arg2;
			msg.obj = u;
			handler.sendMessage(msg);
		}
	}

	// 大树 重置 开发 4

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1212:
//				stopProgressDialog();
				break;
			case 1006:
				feeds.remove(0);
				if (feeds.size() > 0) {
					UploadFeed uploadFile = feeds.get(0);
					setPromot(VisitActivity.this
							.getString(R.string.less_compare) + feeds.size());
					new RecoverTask(uploadFile, 1).execute();
				} else {
					// 传串
					HashMap<String, Object> hm = new HashMap<String, Object>();
					hm.put("surveyID", survey.surveyId);
					hm.put("feedList", arr);
					MainService.newTask(new Task(TaskType.TS_REDEAL, hm));
				}
				break;

			default:
				break;
			}
		}

	};
/**
 * 显示定位信息的dialog
 */
	private void creatLocDialog(String addrStr,String lat,String lng) {

		String latLongString = "纬度:" + lat + "经度:" + lng;
		 Log.i(TAG, "creatLocDialog:"+latLongString);
		if (!VisitActivity.this.isFinishing()) {


			AlertDialog.Builder locBuilder = new AlertDialog.Builder(
					VisitActivity.this, AlertDialog.THEME_HOLO_LIGHT);
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

	// 大树 重置 功能 5 滚动条的生成
	public volatile ProgressDialog mPd;

	public void show() {
		if (null == mPd) {
			mPd = new ProgressDialog(VisitActivity.this);
			/** 生成一个进度条 **/
			mPd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mPd.setMessage(VisitActivity.this.getResources().getString(
					R.string.dialog_content));
			mPd.setOnKeyListener(new MyOnKeyListener());
			mPd.setCanceledOnTouchOutside(false);
		}
		mPd.show();
	}

	public void dismiss() {
		if (null != mPd) {
			mPd.dismiss();
		}
	}

	public void setPromot(String msg) {
		if (null != mPd) {
			mPd.setMessage(getResources().getString(R.string.wait_pro, msg));
		}
	}

	// 滚动条以上 部分 adapter里调用
//	public BDLocation getmBDLocation() {
//		return mBDLocation;
//	}
	// 获取
//	public void setmBDLocation() {
//		this.mBDLocation= LocationUtil.getLocation(); 
//		isLoc();
//	}
	@Override
	public void onPublish(int key, Object... data) {
//		if (key == SubscriberKey.KEY_LOCATION_UPDATE) {
//			if (data == null || data.length < 1) {
//				return;
//			}
//			Object obj = data[0];
//			if (obj instanceof BDLocation) {
//				mBDLocation = (BDLocation) obj;
//			}
//		}
//		isLoc();
	}

//	private void isLoc() {
//		if (null != mBDLocation) {
//			Double lat = mBDLocation.getLatitude();
//			if (lat == 4.9E-324) {
//				more_opt.setImageDrawable(getResources().getDrawable(
//				R.drawable.icon_more));
//		isLocation = false;
//			}
//					if (null != task) {
//						task.cancel();
//						task = null;
//					}
//			
//			more_opt.setImageDrawable(getResources().getDrawable(
//			R.drawable.icon_more_addr));
//			isLocation = true;
//		} else {
//			if (isLocation) {
//				more_opt.setImageDrawable(getResources().getDrawable(
//						R.drawable.icon_more));
//				isLocation = false;
//			}
//		}
//	}

	public void removeFeed(UploadFeed feed) {
		if (fsAll.contains(feed)) {
			fsAll.remove(feed);
		}
		if (fsCompleted.contains(feed)) {
			fsCompleted.remove(feed);
		}
	}
	
	//新加一个通知栏
    private static final int NO_1 = 0x1;
    public void showNotification(){

        Bitmap abcd =  BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon( R.drawable.icon);
        builder.setContentTitle("访问专家");
        builder.setContentText("访问专家工作进行中......");
        builder.setLargeIcon(abcd);
        Notification notification = builder.build();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(this, WelcomeActivity.class));//用ComponentName得到class对象
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式，两种情况

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);//将经过设置了的Intent绑定给PendingIntent
        notification.contentIntent = contentIntent;// 通知绑定 PendingIntent
        notification.flags= Notification.FLAG_INSISTENT;//设置不被取消
        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NO_1, notification);
    }
    //隐藏一个通知栏
    public void hideNotification(){
    	NotificationManager manger = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manger.cancel(NO_1);
    }

	
	/**
	 * 唯一行
	 * @return
	 */
	public void newUUIDInstance() {
        if (uuid != null) {
        } else {
        	uuid = UUID.randomUUID().toString();
        	UserPosition up = new UserPosition();
    		up.setUserId(ma.userId);
    		up.setSurveyId(survey.surveyId);
    		up.setTimes(ComUtil.getTime(System.currentTimeMillis(), 0));
    		up.setUuid(uuid);
    		ma.dbService.addPosition(up);
        }
    }
	
	
	
	 /**
     * 结束闹钟
     */
    private void endalarm() {
        Log.i(TAG, "endalarm: ");
        am.cancel(pi);
    }
    PendingIntent pi;
    AlarmManager am;
    /**
     * 初始化闹钟
     */
    private void startalarm() {
        Log.i(TAG, "startalarm: ");
        Intent intent = new Intent("ELITOR_CLOCK");
        intent.putExtra("msg",uuid);
        pi = PendingIntent.getBroadcast(this,0,intent,0);
        am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60*1000,pi);
    }
/**
 * 跳转到下一个activity带参数
 * @param context
 * @param cls
 * @param bundle
 */
    public static void goToActivity(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    /**
     * 设置新建按钮是否可用
     * @param isclickable t: 可用  f:不可用
     */
    private void newbtnClickable(boolean isclickable){
    	new_panel.setClickable(isclickable); 
    }
    
    /**
     * 获取定位点的流程
     */
    private void getlocation() {
		// TODO Auto-generated method stub
//    	LocationUtil.CreateLocation(VisitActivity.this);
    	initlocation();
    	
    	
    	if (2 != survey.forceGPS) {//如果需要定位
    		startProgressDialog();
//			task = new TimerTask() {// 任务
//				public void run() {
////					没有定位在走3s
////					handler.sendEmptyMessage(1212);
//					if (progressDialog != null) {
//						stopProgressDialog();
//						creatLocDialog(null,null,null);
//					}
//				}
//			};
//			Timer timer = new Timer(true);
//			timer.schedule(task, 10000);
		}else{
			newplan();
		}
    	
    	
	}
    /**
     * 新建问卷跳转界面
     */
    private void newplan(){
    	if (!Util.isEmpty(survey.getSCID())) {
			//是随访项目，进入新建名单界面
			Bundle bundle = new Bundle();
			bundle.putString("SurveyId", survey.getSurveyId());
			bundle.putString("SC_ID", survey.getSCID());
//			bundle.putString("panelID", "panelID");
//			bundle.putString("FeedID", "FeedID");
			bundle.putString("Type", "1");
			goToActivity(VisitActivity.this, SetInnerActivity.class,bundle);
			return;
		}
		// 证明有这个密码,弹出窗口
		if (1 == isTestMode) {
			AlertDialog.Builder locBuilder = new AlertDialog.Builder(
					VisitActivity.this, AlertDialog.THEME_HOLO_LIGHT);
			locBuilder
					//.setTitle("提示")
					//.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("当前状态为测试模式，确认继续？")
					.setPositiveButton(
							VisitActivity.this.getResources().getString(
									R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									newVisit();
									dialog.dismiss();
									return;
								}

							})
					.setNegativeButton(
							VisitActivity.this.getResources().getString(
									R.string.cancel),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
			AlertDialog create = locBuilder.create();
			create.show();
			TextView msgTv=((TextView) create.findViewById(android.R.id.message));
			msgTv.setMinLines(2);
			msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
			msgTv.setGravity(Gravity.CENTER_VERTICAL);
			create.getButton(create.BUTTON_NEGATIVE).setTextSize(
					TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
			create.getButton(create.BUTTON_POSITIVE).setTextSize(
					TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
			Button okBtn = create.getButton(create.BUTTON_POSITIVE);
			okBtn.setBackgroundColor(0xFF6751B6);
			okBtn.setTextColor(Color.WHITE);
		} else {
			newVisit();
		}
    }
    
	// 定位进度条
	private CustomProgressDialog progressDialog = null;
	//启动定位等待动画
	public void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog
					.createDialog(VisitActivity.this);
			progressDialog.setMessage(VisitActivity.this.getResources()
					.getString(R.string.start_get_loc));
			progressDialog.setCancelable(false);
			progressDialog.findViewById(R.id.loadingImageView)
					.setOnLongClickListener(new DismissListener());
		}
		progressDialog.show();
		//
		handlerloc.post(startlocationrun); 
	}
	//定位dialog的长按事件
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
	//关闭定位动画
	private void stopProgressDialog() {
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
				

			    //停止定位
					stoplocation();
				if (lat != 0.0) {
					creatLocDialog(Addr, lat+"", lng+"");
					newbtnClickable(true); 
				}else {
					//按钮可操作
					creatLocDialog(Addr, "", "");
					newbtnClickable(true);
				}
				
			}
		}
		newbtnClickable(true);
	}
	
	
	 private static LocationService locationServices;
	    private MyLocationListener myListener = new MyLocationListener();
	 /**
     * 初始化定位
     */
	 public void initlocation() {
    	
        locationServices = ((MyApp)getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationServices.registerListener(myListener);
        //注册监听
        locationServices.setLocationOption(locationServices.getDefaultLocationClientOption());
        //开始定位
        startloc();

    }
    
	 /**
     * 开始定位
     */
    public void startloc() {
    	 Log.i(TAG, "startloc");
        locationServices.start();
    }
    /**
     * 结束定位
     */
    public void stoplocation(){
		 Log.i(TAG, "stoplocation");
		 locationServices.stop();
	}
    public double lat;
    public double lng;
    public String Addr;
	 public class MyLocationListener extends BDAbstractLocationListener {

			@SuppressWarnings("unused")
			@Override
	        public void onReceiveLocation(BDLocation location) {
	            String coorType = location.getCoorType();
	            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
	            int errorCode = location.getLocType();
	            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
	            String latLongString;
	            if ( location != null ) {
	                 lat = location.getLatitude();
	                 lng = location.getLongitude();
	                 Addr = location.getAddrStr();
	                latLongString = "纬度:" + lat + "经度:" + lng+"coorType"+coorType+"errorCode"+errorCode;
	                //存数据库
	                Log.i(TAG, "filename:"+uuid);
	                Log.i(TAG, "lat:"+lat);
	                Log.i(TAG, "lng:"+lng);
	                Log.i(TAG, "Addr:"+Addr);
	                Log.i(TAG, "isupdate:"+latLongString);
	                //关闭定位服务
	                stoplocation();
	                Log.i(TAG, "关闭定位服务");
	            } else {
	                latLongString = "无法获取地理信息"+errorCode;
	            }

	            Log.i(TAG, "updateWithNewLocation:您当前的位置是:" +
	                    latLongString);
	            

	        }
	    }

	 /**
	  * 检查定位点
	  */
	 private boolean verifuLoc(String str){
		 if (Util.isEmpty(str)) {
			 return false;
		}else  if (-1 == str.indexOf("E")) {
			//没有E
			 return false;
		}else{
			//有E
			return true;
		}
	 }
	 
	

	Handler handlerloc = new Handler();  
     
     int locationtime = 0;
     /**
      * 循环定位
      */
     Runnable startlocationrun = new Runnable() {  
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
 
    
}
