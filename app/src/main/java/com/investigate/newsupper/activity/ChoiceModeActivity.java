package com.investigate.newsupper.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.investigate.newsupper.R;
import com.investigate.newsupper.base.BaseInterface;
import com.investigate.newsupper.bean.FeedAnsBean;
import com.investigate.newsupper.bean.Parameter;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.SysFeedAnswer;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.BaseToast;
import com.investigate.newsupper.util.CheckAudioPermission;
import com.investigate.newsupper.util.GsonUtil;
import com.investigate.newsupper.util.MD5;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.UIUtils;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.view.CustomProgressDialog;
import com.investigate.newsupper.xhttp.Xutils;
import com.investigate.newsupper.xhttp.Xutils.XCallBack2;

/**
 * 访问预览界面
 * 
 * @author Administrator
 * 
 */
public class ChoiceModeActivity extends Activity implements OnClickListener {
	private LinearLayout choicell;
	private UploadFeed feed;
	private TextView tvfw, tvyl, fwjl;// 访问，预览
	private Survey survey;// 问卷
	private Double lat, lng;
	private String addr;
	String pid, name, tel, scid, isweb, feedid;
	
	private MyApp ma;
	private String userid;
	

	/**
	 * 存放下载下来得答案得map
	 */
	ArrayList<FeedAnsBean> feedansbean = new ArrayList<FeedAnsBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setFinishOnTouchOutside(true);
		
		setContentView(R.layout.choice_mode_activity);
//		 this.setFinishOnTouchOutside(false);
		choicell = (LinearLayout) findViewById(R.id.ll);
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				(int) (screenWidth / 2), (int) (screenHeigh / 4));
		choicell.setLayoutParams(params);

		feed = (UploadFeed) getIntent().getSerializableExtra("uf");
		survey = (Survey) getIntent().getSerializableExtra("survey");
		addr = getIntent().getStringExtra("addr");
		lat = (Double) getIntent().getSerializableExtra("lat");
		lng = (Double) getIntent().getSerializableExtra("lng");

		tvfw = (TextView) findViewById(R.id.tvfw);
		tvyl = (TextView) findViewById(R.id.tvyl);
		fwjl = (TextView) findViewById(R.id.fwjl);
		dialogBtnSize = (int) (UIUtils
				.getDimenPixelSize(R.dimen.button_text_size) * TextSizeManager
				.getInstance().getRealScale());
		tvfw.setOnClickListener(this);
		tvyl.setOnClickListener(this);
		fwjl.setOnClickListener(this);
		// 大树拒访 如果访问状态 0 表示正常 2代表拒访
		if (feed.getReturnTypeId() == 0 || feed.getReturnTypeId() == -2) {
			switch (feed.getIsCompleted()) {
			// 只是开始访问
			case Cnt.VISIT_STATE_NOACCESS:
				// 预览隐藏
				tvyl.setVisibility(View.GONE);
				break;
			// 中断断续
			case Cnt.VISIT_STATE_INTERRUPT:
				if (1 == survey.visitPreview) {
					tvyl.setVisibility(View.GONE);
					if (View.GONE == tvfw.getVisibility()) {
						finish();
					}
				} else {
					tvyl.setVisibility(View.VISIBLE);
				}
				break;
			// 完成
			case Cnt.VISIT_STATE_COMPLETED:
				// 已上传不能访问了
				if (Cnt.UPLOAD_STATE_UPLOADED <= feed.getIsUploaded()) {
					tvfw.setVisibility(View.GONE);
				} else if (1 == survey.appModify) {
					tvfw.setVisibility(View.GONE);
				}
				if (1 == survey.visitPreview) {
					tvyl.setVisibility(View.GONE);
					if (View.GONE == tvfw.getVisibility()) {
						finish();
					}
				} else {
					tvyl.setVisibility(View.VISIBLE);
				}
				break;
			}
			// 大树拒访 隐藏预览 上传显示
		} else {
			if (feed.getIsUploaded() < 1) {
				tvfw.setVisibility(View.VISIBLE);
				tvyl.setVisibility(View.GONE);
			} else {
				tvfw.setVisibility(View.GONE);
			}
			if (1 == survey.visitPreview) {
				tvyl.setVisibility(View.GONE);
				if (View.GONE == tvfw.getVisibility()) {
					finish();
				}
			} else {
				tvyl.setVisibility(View.VISIBLE);
			}
		}
		setFinishOnTouchOutside(true);

		String type = "";
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			type = bundle.getString("type");
			if (!Util.isEmpty(type)) {
				if (type.equals("1")) {
					pid = bundle.getString("pid");
					name = bundle.getString("name");
					tel = bundle.getString("tel");
					scid = bundle.getString("scid");

					fwjl.setVisibility(View.VISIBLE);
					params = new FrameLayout.LayoutParams(
							(int) (screenWidth / 2), (int) (screenHeigh / 3));
					choicell.setLayoutParams(params);
				} else {

					params = new FrameLayout.LayoutParams(
							(int) (screenWidth / 2), (int) (screenHeigh / 4));
					choicell.setLayoutParams(params);
				}
			}
		}

		if (getIntent().getStringExtra("isweb") == null) {
			BaseLog.w("isweb为空");
		} else {
			BaseLog.w("isweb为=" + getIntent().getStringExtra("isweb"));
			isweb = getIntent().getStringExtra("isweb");
			feedid = getIntent().getStringExtra("feedid");
			tvfw.setVisibility(View.GONE);
			fwjl.setVisibility(View.GONE);
			tvyl.setVisibility(View.VISIBLE);

		}

		ma = (MyApp) getApplication();
		userid = ma.cfg.getString(Cnt.USER_ID, "");
	}

	private int dialogBtnSize;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvfw:

			sendhttp(feed, new BaseInterface() {

				@Override
				public void onNext() {
					
					/*如果请求下来没有答案，或者没请求，跳过比对答案插入数据库得操作*/
					if (!Util.isEmpty(feedansbean)) {
						ArrayList<String> questionindexlist = ma.dbService.getFeedAnswer(userid, feed.getSurveyId(), feed.getUuid());
						for (int i = 0; i < questionindexlist.size(); i++) {
							BaseLog.w("数据库查询出有第"+questionindexlist.get(i)+"题得答案----");	
							for (int j = 0; j < feedansbean.size(); j++) {
								FeedAnsBean fab = feedansbean.get(j);
								if (!fab.getQuestionIndex().equals(questionindexlist.get(i))) {
									BaseLog.w("插入数据-QuestionIndex= "+fab.getQuestionIndex());	
									ma.dbService.addAnswer(fab);
								}
							}
						}
					}
					
					ChoiceModeActivity.this.dismiss();
					// TODO Auto-generated method stub
					if (survey.globalRecord == 1) {// 是否有全局录音
						if (!CheckAudioPermission
								.isHasPermission(getApplication())) {// 启用录音失败
							AlertDialog.Builder Recorddialog = new AlertDialog.Builder(
									ChoiceModeActivity.this,
									AlertDialog.THEME_HOLO_LIGHT);
							Recorddialog
									.setMessage("录音启用失败，请检查录音权限！")
									.setPositiveButton(
											ChoiceModeActivity.this
													.getResources().getString(
															R.string.ok),
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
													// 大树 关闭界面
													overridePendingTransition(
															R.anim.zzright,
															R.anim.zzleft);
													finish();
													return;
												}
											});

							AlertDialog Recordcreate = Recorddialog.create();
							Recordcreate.show();
							TextView msgTv = ((TextView) Recordcreate
									.findViewById(android.R.id.message));
							msgTv.setMinLines(2);
							msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									dialogBtnSize);
							msgTv.setGravity(Gravity.CENTER_VERTICAL);
							Recordcreate.getButton(AlertDialog.BUTTON_NEGATIVE)
									.setTextSize(TypedValue.COMPLEX_UNIT_PX,
											dialogBtnSize);
							Button okBtn = Recordcreate
									.getButton(AlertDialog.BUTTON_POSITIVE);
							okBtn.setBackgroundColor(0xFF6751B6);
							okBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									dialogBtnSize);
							okBtn.setTextColor(Color.WHITE);

						} else {
							// 继续判断是否有定位
							newVisite();
						}

					} else {
						// 继续判断是否有定位
						newVisite();
					}

				}

				@Override
				public void onError() {
					// TODO Auto-generated method stub
					ChoiceModeActivity.this.dismiss();
					BaseToast.showLongToast(ChoiceModeActivity.this, "答案同步失败！");
				}
			});

			break;
		case R.id.tvyl:
			feed.setSurvey(survey);
			if (Util.isEmpty(isweb)) {

				Intent it = new Intent(this, NativeReviewActivity.class);
				Bundle bundle = new Bundle();
				feed.setSurveyTitle(survey.surveyTitle);
				bundle.putSerializable("feed", feed);
				it.putExtras(bundle);
				this.startActivity(it);
				// 大树 关闭界面
				overridePendingTransition(R.anim.zzright, R.anim.zzleft);
				finish();
			} else {
				Bundle bundle = new Bundle();
				bundle.putString("fid", feedid);
				bundle.putString("sid", survey.getSurveyId());

				BaseLog.w("fid = " + feed.getFeedId());
				BaseLog.w("fid = " + survey.getSurveyId());

				Intent intent = new Intent();
				// intent.setClass(ChoiceModeActivity.this,
				// SurveyViewAnswerActivity.class);
				intent.setClass(ChoiceModeActivity.this,
						OnlineAnswerActivity.class);
				intent.putExtras(bundle);
				ChoiceModeActivity.this.startActivity(intent);
				finish();
			}
			break;
		case R.id.fwjl:
			// Toast.makeText(ChoiceModeActivity.this, "fwjl",0).show();

			Bundle bundle = new Bundle();
			bundle.putString("type", "1");
			bundle.putString("pid", pid);
			bundle.putString("name", name);
			bundle.putString("tel", tel);
			bundle.putString("scid", scid);

			Intent intent = new Intent();
			intent.setClass(ChoiceModeActivity.this, PersionItemActivity.class);
			intent.putExtras(bundle);
			ChoiceModeActivity.this.startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	private void newVisite() {
		feed.setSurvey(survey);
		// if (mBDLocation != null) {
		// lat = mBDLocation.getLatitude();
		// lng = mBDLocation.getLongitude();
		// addr = mBDLocation.getAddrStr();
		// } else {
		// lat = null;
		// lng = null;
		// addr = "";
		// }
		/**
		 * 假如经纬度中存在E或e 则将其置为空串
		 */
		if (null != lat) {
			String latStr = String.valueOf(lat);
			if (-1 < latStr.indexOf("E") || -1 < latStr.indexOf("e")) {
				feed.setLat("");
			} else {
				feed.setLat(latStr);
			}
		} else {
			feed.setLat("");
		}

		if (null != lng) {
			String lngStr = String.valueOf(lng);
			if (-1 < lngStr.indexOf("E") || -1 < lngStr.indexOf("e")) {
				feed.setLng("");
			} else {
				feed.setLng(lngStr);
			}
		} else {
			feed.setLng("");
		}

		// 命名规则开始
		String content = "";
		String parameterName = survey.getParameterName();
		ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
		String parametersStr = feed.getParametersStr();
		if (!Util.isEmpty(parametersStr)) {
			parameterList.clear();
			ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
					.parseArray(parametersStr, Parameter.class);
			if (!Util.isEmpty(tParameters)) {
				parameterList.addAll(tParameters);
			}
		}
		if (!Util.isEmpty(parameterName) && !Util.isEmpty(parameterList)) {
			for (Parameter parameter : parameterList) {
				if (parameter.getSid().equals(parameterName)) {
					content = parameter.getContent();
					// 是中文的就置为空
					if (Util.isContainChinese(content)) {
						content = "";
					}
					break;
				}
			}
		}
		// 命名规则结束

		if (true) {
			Intent it = null;
			if (1 == survey.openStatus) {
				it = new Intent(this, CheckAddrActivity.class);
			} else {
				if (0 == survey.showpageStatus) {
					it = new Intent(this, NativeModeNoPageActivity.class);
				} else {
					it = new Intent(this, NativeModeActivity.class);
				}
			}
			Bundle bundle = new Bundle();
			feed.setVisitAddress(Util.isEmpty(addr) ? "" : addr);
			feed.setSurveyTitle(survey.surveyTitle);
			// 命名规则设置参数
			feed.setParametersContent(content);
			bundle.putSerializable("feed", feed);
			it.putExtras(bundle);
			this.startActivity(it);
			overridePendingTransition(R.anim.zzright, R.anim.zzleft);
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 同步答案功能
	 */
	private void sendhttp(final UploadFeed feed, final BaseInterface linster) {
		// TODO Auto-generated method stub
//		Util.isEmpty(survey.getSCID())
		/*判断有没有网   没网直接next*/
		if (true) {
			/*判断是不是随访项目*/
			linster.onNext();
		} else {
			ChoiceModeActivity.this.show();
			if (!NetUtil.checkNet(ChoiceModeActivity.this)) {
				BaseLog.w("无网络连接！跳过同步答案！");
				linster.onNext();
			}
			
		
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("userId",userid);
			paramsMap.put("userPsd",MD5.Md5Pwd(ma.cfg.getString(Cnt.USER_PWD, "")));
			paramsMap.put("surveyId", feed.getSurveyId());
			paramsMap.put("feedId", feed.getFeedId());
			Xutils.getInstance().get2(Cnt.OfflineSysFeedAnswer, paramsMap, new XCallBack2() {

				@Override
				public void onResponse(String result) {
					// TODO Auto-generated method stub
					BaseLog.w("result = " + result);
					
					if (Util.isEmpty(result)) {
						
					}else{
						SysFeedAnswer bena = GsonUtil.GsonToBean(result,
								SysFeedAnswer.class);
						for (int i = 0, size = bena.getData().size(); i < size; i++) {
							SysFeedAnswer.DataBean dataBean = bena.getData().get(i);
							FeedAnsBean feedAnsBean = new FeedAnsBean();
							/* 设置questionid*/
							feedAnsBean.setQuestionIndex(dataBean
									.getQuestionindex());
							/* 把数据转成json格式 */
							String ansmap = GsonUtil.BeanToJson(dataBean
									.getAnswermap());
							/* 把转好的ansmap存到对象里 */
							feedAnsBean.setAnswerMap(ansmap);
							feedAnsBean.setSurveyId(feed.getSurveyId());
							feedAnsBean.setUserId(userid);
							feedAnsBean.setUuId(feed.getUuid());
							BaseLog.i("FeedAnsBean = " + feedAnsBean.toString());
							feedansbean.add(feedAnsBean);
						}
					}
					linster.onNext();
				}

				@Override
				public void onError() {
					// TODO Auto-generated method stub
					BaseToast.showLongToast(ChoiceModeActivity.this, "答案同步失败！");
					linster.onNext();
				}
			});
		}
	}

	public volatile CustomProgressDialog progressDialog;
	public void show() {
		if (null == progressDialog) {
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage(this.getResources().getString(R.string.please_wait));
			progressDialog.findViewById(R.id.loadingImageView).setOnLongClickListener(new DismissListener());
			progressDialog.setOnKeyListener(new MyOnKeyListener());
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	public void showWeb() {
		if (null == progressDialog) {
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage(this.getResources().getString(R.string.please_wait));
			progressDialog.setOnKeyListener(new MyOnKeyListener());
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	public void dismiss() {
		if (null != progressDialog) {
			try {
				progressDialog.dismiss();
				progressDialog = null;
			} catch (Exception e) {

			}
		}

	}
	
	private final class DismissListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			dismiss();
			return true;
		}

	};
	protected class MyOnKeyListener implements OnKeyListener {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (KeyEvent.KEYCODE_SEARCH == keyCode || KeyEvent.KEYCODE_BACK == keyCode) {
				return true;
			}
			return false;
		}

	}

	public MyOnKeyListener getMyOnKeyListener() {
		return new MyOnKeyListener();
	}

}