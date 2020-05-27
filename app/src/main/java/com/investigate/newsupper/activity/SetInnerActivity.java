package com.investigate.newsupper.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.investigate.newsupper.R;
import com.investigate.newsupper.AsyncUtil.InnerTask;
import com.investigate.newsupper.bean.AccessPanelBean;
import com.investigate.newsupper.bean.GroupsBean;
import com.investigate.newsupper.bean.ReturnAnswer;
import com.investigate.newsupper.db.DBService;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.util.BaseToast;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.DensityUtils;
import com.investigate.newsupper.util.GsonUtil;
import com.investigate.newsupper.util.MD5;
import com.investigate.newsupper.util.ResolverXML;
import com.investigate.newsupper.util.UIUtils;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.xhttp.Xutils;

/**
 * 访问专家新界面，设置名单属性 Created by EEH on 2018/8/29.
 */
public class SetInnerActivity extends BaseActivity implements
		View.OnClickListener {

	private static final String TAG = "SetInnerActivity";
	int px;
	private LinearLayout llroot;
	private TextView tvSave, tvTitle;
	private ImageView backIcon;
	private ArrayList<AccessPanelBean> spdata = new ArrayList<AccessPanelBean>();
	private ArrayList<View> vs = new ArrayList<View>();
	private boolean isFirst;
	private Activity mActivity = SetInnerActivity.this;
	private AccessPanelBean bean;
	private volatile DBService dbService;
	int surveySize;
	ArrayList<GroupsBean> groupsBeans;

	// type 1 ：新建 0：修改
	String type, SurveyId, SC_ID, panelID, FeedID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		type = getIntent().getStringExtra("Type");
		SurveyId = getIntent().getStringExtra("SurveyId");
		SC_ID = getIntent().getStringExtra("SC_ID");
		// 如果是修改才会有这两个参数
		if (type.equals("0")) {
			panelID = getIntent().getStringExtra("panelID");
			FeedID = getIntent().getStringExtra("FeedID");
		}

		initData();
		initView();

	}

	private void initView() {

		setContentView(R.layout.setinner_layout);
		llroot = (LinearLayout) findViewById(R.id.ll_root_setinner);
		tvSave = (TextView) findViewById(R.id.setinner_tv_save);
		tvTitle = (TextView) findViewById(R.id.setinner_tv_title);
		tvTitle.setText(type);
		backIcon = (ImageView) findViewById(R.id.back_img_setinner);
		tvSave.setOnClickListener(this);
		backIcon.setOnClickListener(this);
		if (type.equals("1")) {
			tvTitle.setText("新建名单");
		} else {
			tvTitle.setText("修改名单");
		}

		llroot.addView(createview(mActivity, 1, bean));
		// 获取数据
		for (AccessPanelBean accessPanelBean : spdata) {
			if (accessPanelBean != null) {
				int type;
				if (Util.isEmpty(accessPanelBean.getPresetValue())) {
					type = 0;
				} else {
					type = 1;
				}
				llroot.addView(createview(mActivity, type, accessPanelBean));
			}
		}

		String jsonbean = GsonUtil.BeanToJson(spdata);
		Log.e(TAG, "initView: jsonbean-" + jsonbean);

	}

	/**
	 * 获取数据
	 */
	private void initData() {
		ma = (MyApp) getApplication();
		cfg = new Config(mActivity);
		surveySize = (int) (UIUtils.getDimenPixelSize(R.dimen.sry_text_small) * TextSizeManager
				.getRealScale());

		if (null == dbService) {
			dbService = new DBService(getApplicationContext());
		}

		ArrayList<String> str = dbService.getAccessPanelBean("-1");
		for (int i = 1, size = str.size(); i < size; i++) {
			System.out.println("str.get(i)::::::-" + str.get(i));
			AccessPanelBean bean = GsonUtil.GsonToBean(str.get(i),
					AccessPanelBean.class);
			// System.out.println("bean::::::-"+bean.toString());
			spdata.add(i - 1, bean);
		}
		groupsBeans = dbService.getGroups();
		Log.i(TAG, "groupsBeans" + groupsBeans.size());
		String presetValue = "";
		for (int i = 0, gs = groupsBeans.size(); i < gs; i++) {
			presetValue += "%%" + groupsBeans.get(i).getGroupName();
		}
		bean = new AccessPanelBean("UserGroupID", "", "", "选择公司组", "",
				presetValue);

	}

	/**
	 * @param context
	 * @param type
	 *            0:edittext 1:Spinner
	 * @return
	 */
	private LinearLayout createview(Context context, int type,
			AccessPanelBean accessPanelBean) {

		px = DensityUtils.dp2px(mActivity, 8);
		// 新建linearlayout
		LinearLayout linearLayout = new LinearLayout(mActivity);
		linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);
		linearLayout.setPadding(0, px, 0, px);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		params.setMargins(0, 0, 0, px / 8);
		linearLayout.setLayoutParams(params);

		// 新建textView
		// android:textColor="#808080"
		// android:textSize="@dimen/local_text_size"
		TextView textView = new TextView(mActivity);
		textView.setText(accessPanelBean.getShowtext());
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
		textView.setTextColor(Color.BLACK);

		LinearLayout.LayoutParams textviewparams = new LinearLayout.LayoutParams(
				0, ViewGroup.LayoutParams.WRAP_CONTENT);

		textviewparams.setMargins(2 * px, 2 * px, 2 * px, 2 * px);
		textviewparams.weight = 2;
		textView.setLayoutParams(textviewparams);

		linearLayout.addView(textView);

		if (type == 0) {// 新建edittext
			linearLayout.addView(createEditText(mActivity, accessPanelBean));
		} else {
			linearLayout.addView(createSpinner(mActivity, accessPanelBean));
		}
		return linearLayout;
	}

	/**
	 * 设置edittext
	 * 
	 * @param mActivity
	 * @param accessPanelBean
	 * @return
	 */
	private EditText createEditText(Context mActivity,
			AccessPanelBean accessPanelBean) {

		EditText editText = new EditText(mActivity);

		 editText.setText(accessPanelBean.getSubmit());
//		editText.setText("123");

		editText.setBackgroundResource(R.drawable.bg_et_setlinner);
		editText.setPadding(px, px, px, px);
		editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, surveySize);
		editText.setTextColor(Color.BLACK);
		LinearLayout.LayoutParams editTextparams = new LinearLayout.LayoutParams(
				0, ViewGroup.LayoutParams.WRAP_CONTENT);
		editTextparams.setMargins(0, 0, 2 * px, 0);
		editTextparams.weight = 8;
		editText.setLayoutParams(editTextparams);
		editText.setTag(accessPanelBean);
		editText.setSingleLine();
		if (true) {
			if (!Util.isEmpty(accessPanelBean.getAppointPerson())) {
				if (accessPanelBean.getAppointPerson().equals("1")) {
					// editText 不可以编辑
					editText.setFocusable(false);
					editText.setFocusableInTouchMode(false);

				}
			}
		}
		vs.add(editText);
		return editText;
	}

	/**
	 * 设置spinner
	 * 
	 * @param mActivity
	 * @param accessPanelBean
	 * @return
	 */
	private Spinner createSpinner(Context mActivity,
			AccessPanelBean accessPanelBean) {

		String valueArreay[] = accessPanelBean.getPresetValue().split("%%");
		ArrayList<String> strlist = new ArrayList<String>();
		for (String string : valueArreay) {
			strlist.add(string);
		}

		Spinner spinner = new Spinner(mActivity);
		spinner.setPadding(px, px, px, px);

		LinearLayout.LayoutParams spinnerparams = new LinearLayout.LayoutParams(
				0, ViewGroup.LayoutParams.WRAP_CONTENT);
		spinnerparams.setMargins(0, 0, 2 * px, 0);
		spinnerparams.weight = 8;
		spinner.setLayoutParams(spinnerparams);

		spinner.setAdapter(new ArrayAdapter<String>(mActivity,
				R.layout.item_setinner, strlist));
		// 预选中 有答案的也就是修改的时候用的
		if (!Util.isEmpty(accessPanelBean.getSubmit())) {
			int selectsubmit = Integer.parseInt(accessPanelBean.getSubmit());
			spinner.setSelection(selectsubmit);
		}

		spinner.setTag(accessPanelBean);

		if (true) {
			if (!Util.isEmpty(accessPanelBean.getAppointPerson())) {
				if (accessPanelBean.getAppointPerson().equals("1")) {
					// spinner 不可以编辑
					spinner.setEnabled(false);
					spinner.setSelection(0, true);

				}
			}
		}
		vs.add(spinner);
		return spinner;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.setinner_tv_save:
			if (savedata()) {
				sendhttp();
			}
			showdata();

			break;
		case R.id.back_img_setinner:
			mActivity.finish();
			break;
		}
	}

	/**
	 * 保存数据，并且返回能否上传
	 * 
	 * @return
	 */
	private boolean savedata() {
		boolean issubmit = true;
		for (int i = 0; i < vs.size(); i++) {
			if (vs.get(i) instanceof EditText) {
				EditText editText = (EditText) vs.get(i);
				AccessPanelBean bean = (AccessPanelBean) editText.getTag();
				if (bean != null) {
					if (editText.getText().toString().trim().length() == 0) {
						BaseToast.showShortToast(mActivity,
								"请填写" + bean.getShowtext() + "项！");
						issubmit = false;
					} else {
						// spdata.
						for (int j = 0; j < spdata.size(); j++) {
							AccessPanelBean AccessPanelbean = spdata.get(j);
							if (AccessPanelbean != null) {
								if (AccessPanelbean.getSid().equals(
										bean.getSid())) {
									spdata.get(j).setSubmit(
											editText.getText().toString()
													.trim());
								}
							}
						}
					}
				}
			}

			if (vs.get(i) instanceof Spinner) {
				Spinner spinner = (Spinner) vs.get(i);
				AccessPanelBean bean = (AccessPanelBean) spinner.getTag();
				if (bean != null) {
					int value = spinner.getSelectedItemPosition();
					if (value == 0) {
						BaseToast.showShortToast(mActivity,
								"请填写" + bean.getShowtext() + "项！");
						issubmit = false;
					} else {
						for (int j = 0; j < spdata.size(); j++) {
							AccessPanelBean AccessPanelbean = spdata.get(j);
							if (AccessPanelbean != null) {
								if (AccessPanelbean.getSid().equals(
										bean.getSid())) {
									spdata.get(j).setSubmit(value + "");
								}
							}
						}
					}
				}
			}
		}
		return issubmit;
	}

	private void showdata() {
		Log.i(TAG, "showdata:size= " + spdata.size());
		for (int i = 0; i < spdata.size(); i++) {
			if (spdata.get(i) != null) {
				Log.i(TAG, "showdata: " + spdata.get(i).getSid() + ":"
						+ spdata.get(i).getSubmit());
			}

		}
	}

	/**
	 * http://www.survey-expert.cn/test/alisoft/OfflinePanel.asp?
	 * userId=zhouhong2014& userPsd=zhouhong2014& surveyID=10260& SC_ID=4&
	 * panelID=224920& FeedID=108& UserGroupID=142
	 */

	private void sendhttp() {
		show();
//		String url = "http://www.survey-expert.cn/test/alisoft/OfflinePanel.asp";
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userId", ma.userId);
		paramsMap.put("userPsd", MD5.Md5Pwd(cfg.getString(Cnt.USER_PWD, "")));
		paramsMap.put("surveyID", SurveyId);
		paramsMap.put("SC_ID", SC_ID);
		
		Log.i(TAG, "sendhttp:userId"+ma.userId);
		Log.i(TAG, "sendhttp:userPsd"+MD5.Md5Pwd(cfg.getString(Cnt.USER_PWD, "")));
		Log.i(TAG, "sendhttp:surveyID"+SurveyId);
		Log.i(TAG, "sendhttp:SC_ID"+SC_ID);
		
		
		
		// 如果是修改才会有这两个参数
		if (type.equals("0")) {
			paramsMap.put("panelID", panelID);
			paramsMap.put("FeedID", FeedID);
		}

		for (int i = 0, size = spdata.size(); i < size; i++) {
			AccessPanelBean accessPanelBean = spdata.get(i);
			if (accessPanelBean != null) {
				if (accessPanelBean.getSid().equals("UserGroupID")) {
					// 选择的公司组
					int value = Integer.parseInt(bean.getSubmit());
					paramsMap.put("UserGroupID", groupsBeans.get(value)
							.getSid());
				} else {
					paramsMap.put(accessPanelBean.getSid(),
							accessPanelBean.getSubmit());
				}
			}

		}

		
		Xutils.getInstance().post(Cnt.OfflinePanel, paramsMap, new Xutils.XCallBack() {
			@Override
			public void onResponse(String result) {

				Log.d(TAG, "onResponse: result-" + result);
				success(result);
			}

		});
	}

	private MyApp ma;

	/**
	 * 请求成功的解析
	 * 
	 * @param result
	 */
	private void success(String result) {
		// TODO Auto-generated method stub
		ReturnAnswer returnAnswer = ResolverXML.getInstance()
				.SubmitPanelParseXml(result);
		if (returnAnswer.getState().equals("100")) {
			// 请求成功，存库，更新名单

			String authorId = ma.cfg.getString(Cnt.AUTHORID, "");
			new InnerTask(authorId, ma.userId,
					ma.dbService.getSurvey(returnAnswer.getSurveyID()),
					mActivity, ma).execute();

			ArrayList<String> str = createaccessoanejsonlist(spdata);
			dbService.addAccessPanelBean(returnAnswer.getFeedID(), str);

			BaseToast.showLongToast(mActivity, "名单添加完成");
			
			new Thread() {
				@Override
				public void run() {
					super.run();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}// 休眠3秒
					/**
					 * 要执行的操作
					 */
					
					dismiss();
					finish();
				}
			}.start();

		} else if(returnAnswer.getState().equals("99")){
			BaseToast.showLongToast(mActivity, "用户名密码错误！");
			dismiss();
			
		}else if(returnAnswer.getState().equals("97")){
			BaseToast.showLongToast(mActivity, "提交失败!错误代码97");
			dismiss();
			
		}else {
			BaseToast.showLongToast(mActivity, "提交失败！");
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

	/**
	 * 把名单信息转成json形式
	 * 
	 * @param bean
	 * @return
	 */
	public ArrayList<String> createaccessoanejsonlist(
			ArrayList<AccessPanelBean> bean) {
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < 45; i++) {
			list.add(i, "");
		}

		for (int i = 0, size = bean.size(); i < size; i++) {
			if (bean.get(i) != null) {

				String sid = bean.get(i).getSid();
				if (sid.equals("PanelPassword")) {
					list.add(0, GsonUtil.BeanToJson(bean.get(i)));
				} else if (sid.equals("Company")) {
					list.add(1, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("UserName")) {
					list.add(2, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("MailAddress")) {
					list.add(3, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("PanelCode")) {
					list.add(4, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Phone")) {
					list.add(5, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Phone2")) {
					list.add(6, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Mobile")) {
					list.add(7, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Mobile2")) {
					list.add(8, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Fax")) {
					list.add(9, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Fax2")) {
					list.add(10, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Sex")) {
					list.add(11, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Address")) {
					list.add(12, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("PostCode")) {
					list.add(13, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Married")) {
					list.add(14, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Birthday")) {
					list.add(15, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Age")) {
					list.add(16, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Payment")) {
					list.add(17, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Degree")) {
					list.add(18, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Industry")) {
					list.add(19, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Duty")) {
					list.add(20, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Idcard")) {
					list.add(21, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Region")) {
					list.add(22, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Extra2")) {
					list.add(23, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("Extra3")) {
					list.add(24, GsonUtil.BeanToJson(bean.get(i)));
				} else if (sid.equals("AddTo1")) {
					list.add(25, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("AddTo2")) {
					list.add(26, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("AddTo3")) {
					list.add(27, GsonUtil.BeanToJson(bean.get(i)));
				} else if (sid.equals("AddTo4")) {
					list.add(28, GsonUtil.BeanToJson(bean.get(i)));
				} else if (sid.equals("AddTo5")) {
					list.add(29, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("AddTo6")) {
					list.add(30, GsonUtil.BeanToJson(bean.get(i)));
				} else if (sid.equals("AddTo7")) {
					list.add(31, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("AddTo8")) {
					list.add(32, GsonUtil.BeanToJson(bean.get(i)));
				} else if (sid.equals("AddTo9")) {
					list.add(33, GsonUtil.BeanToJson(bean.get(i)));
				} else if (sid.equals("AddTo10")) {
					list.add(34, GsonUtil.BeanToJson(bean.get(i)));
				} else if (sid.equals("AddTo11")) {
					list.add(35, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("AddTo12")) {
					list.add(36, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("AddTo13")) {
					list.add(37, GsonUtil.BeanToJson(bean.get(i)));
				} else if (sid.equals("AddTo14")) {
					list.add(38, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("AddTo15")) {
					list.add(39, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("AddTo16")) {
					list.add(40, GsonUtil.BeanToJson(bean.get(i)));
				} else if (sid.equals("AddTo17")) {
					list.add(41, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("AddTo18")) {
					list.add(42, GsonUtil.BeanToJson(bean.get(i)));
				} else

				if (sid.equals("AddTo19")) {
					list.add(43, GsonUtil.BeanToJson(bean.get(i)));
				} else if (sid.equals("AddTo20")) {
					list.add(44, GsonUtil.BeanToJson(bean.get(i)));
				}

			}
		}

		return list;

	}
}
