package com.investigate.newsupper.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.ChoiceModeActivity;
import com.investigate.newsupper.activity.VisitActivity;
import com.investigate.newsupper.bean.Answer;
import com.investigate.newsupper.bean.Parameter;
import com.investigate.newsupper.bean.ReturnType;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.AcquireLocation;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.BaseToast;
import com.investigate.newsupper.util.DateUtil;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.UIUtils;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.locationutils.RxLocationTool;
import com.investigate.newsupper.view.Toasts;

public class VisitAdapter extends BaseAdapter {
	private static final String TAG = "VisitAdapter";
	private VisitActivity mContext;
	private ArrayList<UploadFeed> fs;
	private LayoutInflater inflater;

	private Double lat, lng;
	private MyApp ma;
	private String addr;

	private Survey survey;
	private int dialogBtnSize;

	@Override
	public int getCount() {
		return fs.size();
	}

	public VisitAdapter(VisitActivity _c, ArrayList<UploadFeed> feeds,
			Double lat, Double lng, String address, Survey _survey, MyApp ma) {
		dialogBtnSize = (int) (UIUtils
				.getDimenPixelSize(R.dimen.button_text_size) * TextSizeManager
				.getInstance().getRealScale());
		this.mContext = _c;
		this.fs = feeds;
		// this.lat = lat;
		// this.lng = lng;
		// this.addr = address;
		this.survey = _survey;
		this.ma = ma;
		inflater = (LayoutInflater) _c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getItem(int position) {
		return fs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		// if (null == convertView) {
		convertView = inflater.inflate(R.layout.visit_item, null);
		vh = new ViewHolder();

		vh.innerLL = (LinearLayout) convertView.findViewById(R.id.innerLL);
		vh.llTo1 = (LinearLayout) convertView.findViewById(R.id.llTo1);
		vh.llTo2 = (LinearLayout) convertView.findViewById(R.id.llTo2);
		vh.llTo3 = (LinearLayout) convertView.findViewById(R.id.llTo3);
		vh.llTo4 = (LinearLayout) convertView.findViewById(R.id.llTo4);
		vh.visitLL = (LinearLayout) convertView.findViewById(R.id.visitLL);// 数据背景
		vh.tvLocalId = (UITextView) convertView.findViewById(R.id.local_id);
		vh.tvEndTime = (UITextView) convertView.findViewById(R.id.end_time_tv);
		vh.feedId = (UITextView) convertView.findViewById(R.id.feedid_tv);
		vh.tvState = (UITextView) convertView.findViewById(R.id.visit_state_tv);
		/**
		 * 内部名单start
		 */
		vh.tvAddTo1 = (UITextView) convertView.findViewById(R.id.addto1_tv);
		vh.tvAddTo2 = (UITextView) convertView.findViewById(R.id.addto2_tv);
		vh.tvAddTo3 = (UITextView) convertView.findViewById(R.id.addto3_tv);
		vh.tvAddTo4 = (UITextView) convertView.findViewById(R.id.addto4_tv);

		vh.tvTo1 = (UITextView) convertView.findViewById(R.id.tvTo1);
		vh.tvTo2 = (UITextView) convertView.findViewById(R.id.tvTo2);
		vh.tvTo3 = (UITextView) convertView.findViewById(R.id.tvTo3);
		vh.tvTo4 = (UITextView) convertView.findViewById(R.id.tvTo4);

		vh.visit_ll = (LinearLayout) convertView.findViewById(R.id.visit_ll);

		convertView.setTag(vh);

		UploadFeed feed = fs.get(position);
		if (Util.isEmpty(feed.getFeedId())) {
			vh.feedId.setVisibility(View.INVISIBLE);
		} else {
			vh.feedId.setVisibility(View.VISIBLE);

			if (!Util.isEmpty(survey.getSCID())) {
				vh.feedId.setText("PID:" + feed.getPid());
			} else {
				vh.feedId.setText("卷号:" + feed.getFeedId());
			}
		}
		boolean isgone1 = true, isgone2 = true, isgone3 = true, isgone4 = true;
		if (null != feed) {
			HashMap<String, Parameter> hm = feed.getInnerPanel().getPsMap();
			vh.tvAddTo1.setText(null != hm.get("Parameter1") ? hm.get(
					"Parameter1").getContent() : "");
			vh.tvAddTo2.setText(null != hm.get("Parameter2") ? hm.get(
					"Parameter2").getContent() : "");
			vh.tvAddTo3.setText(null != hm.get("Parameter3") ? hm.get(
					"Parameter3").getContent() : "");
			vh.tvAddTo4.setText(null != hm.get("Parameter4") ? hm.get(
					"Parameter4").getContent() : "");
			vh.tvTo1.setText(survey.getParameter1() + ":");
			vh.tvTo2.setText(survey.getParameter2() + ":");
			vh.tvTo3.setText(survey.getParameter3() + ":");
			vh.tvTo4.setText(survey.getParameter4() + ":");

			vh.llTo1.setVisibility(View.VISIBLE);
			vh.llTo2.setVisibility(View.VISIBLE);
			vh.llTo3.setVisibility(View.VISIBLE);
			vh.llTo4.setVisibility(View.VISIBLE);
			if (Util.isEmpty(vh.tvAddTo1.getText())) {
				vh.llTo1.setVisibility(View.GONE);
				isgone1 = false;
			}
			if (Util.isEmpty(vh.tvAddTo2.getText())) {
				vh.llTo2.setVisibility(View.GONE);
				isgone2 = false;
			}
			if (Util.isEmpty(vh.tvAddTo3.getText())) {
				vh.llTo3.setVisibility(View.GONE);
				isgone3 = false;
			}
			if (Util.isEmpty(vh.tvAddTo4.getText())) {
				vh.llTo4.setVisibility(View.GONE);
				isgone4 = false;
			}
			// 删除匿名数据
			vh.visit_ll.setOnLongClickListener(new CstOnLongClick(feed));
			vh.visit_ll.setOnClickListener(new OnCstClickListener(feed) {

			});
			if (1 == feed.isTestMode()) {
				vh.tvLocalId.setTextColor(Color.parseColor("#FF6347"));
			} else {
				vh.tvLocalId.setTextColor(Color.parseColor("#babde1"));
			}
			vh.tvLocalId.setText(String.valueOf(feed.getId()));
			if (-1 == survey.showQindex) {
				vh.tvEndTime.setText(0 < feed.getRegTime() ? Util.getTime(
						feed.getRegTime(), 7) : "");
			} else {
				String tvEndTime = "";
				Answer textAnswer = ma.dbService.getAnswer(feed.getUuid(),
						survey.showQindex + "");
				if (null != textAnswer && null != textAnswer.getAnswerMapArr()
						&& 0 < textAnswer.getAnswerMapArr().size()) {
					tvEndTime = textAnswer.getAnswerMapArr().get(0)
							.getAnswerText();
				}
				vh.tvEndTime.setText(tvEndTime);
			}
			if (feed.getIsUploaded() == 9) {
				vh.visitLL
						.setBackgroundResource(R.drawable.visit_complete_background);
			} else {
				vh.visitLL.setBackgroundResource(R.drawable.visit_background);
			}
			// 大树拒访 如果返回码为0 正常 否则 拒访或则其他状态
			if (feed.getReturnTypeId() == 0 || feed.getReturnTypeId() == -2) {
				switch (feed.getIsCompleted()) {
				case Cnt.VISIT_STATE_NOACCESS:
					vh.tvState.setText(mContext.getResources().getString(
							R.string.visit_state_noaccess));
					break;

				case Cnt.VISIT_STATE_INTERRUPT:
					vh.tvState.setText(mContext.getResources().getString(
							R.string.visit_state_interrupt));
					break;

				case Cnt.VISIT_STATE_COMPLETED:
					// vh.tvState.setText("已完成");
					vh.tvState.setText(mContext.getResources().getString(
							R.string.completed));

					if (!Util.isEmpty(feed.getReturnType())) {
						if (feed.getReturnType().equals("22")) {
							vh.tvState.setText(mContext.getResources()
									.getString(R.string.visit_state_mismatch));
							vh.tvState.setTextColor(mContext.getResources()
									.getColor(R.color.red));
						}
					}

					if (!Util.isEmpty(feed.getReturnType())) {
						if (feed.getReturnType().equals("21")) {
							vh.tvState.setText(mContext.getResources()
									.getString(R.string.visit_state_quota));
							vh.tvState.setTextColor(mContext.getResources()
									.getColor(R.color.red));
						}
					}

					break;

				}
			} else {
				ArrayList<ReturnType> rlist = survey.getRlist();
				String returnState = getReturnState(feed.getReturnTypeId(),
						rlist);
				vh.tvState.setText(mContext.getResources().getString(
						R.string.no_fang, returnState));
			}
			// 大树拒访

		}
		// 每个条目下面的显示,有数据就显示 没数据就隐藏了
		if (isgone1 || isgone2 || isgone3 || isgone4) {

		} else {
			// 隐藏下面空白
			vh.innerLL.setVisibility(View.GONE);
		}
		return convertView;
	}

	class OnCstClickListener implements OnClickListener {
		private UploadFeed feed;

		public OnCstClickListener() {

		}

		public OnCstClickListener(UploadFeed feed) {
			this.feed = feed;
		}

		@Override
		public void onClick(View v) {

			if (1 == survey.openGPS && 0 < survey.timeInterval
					&& !mContext.isOpenGps) {
				Toasts.makeText(mContext, R.string.no_gps_line,
						Toast.LENGTH_SHORT).show();
				return;
			}
			boolean isfinish = true;
			// 完成
			if (feed.getIsCompleted() == 1) {
				isfinish = false;
			}
			// 中断
			if (feed.getIsCompleted() == 0) {
				isfinish = true;
			}
			// 未访问
			if (feed.getIsCompleted() == -1) {
				isfinish = true;
			}
			// 条件不符
			if (!Util.isEmpty(feed.getReturnType())) {
				if (feed.getReturnType().equals("22")) {
					isfinish = false;
				}
			}

			if (isfinish) {
				Log.i("isscid", "isscid=" + isscid(survey, feed));
				int isscid = isscid(survey, feed);
				// 0 没有间隔时间
				// 1 没有建个单位
				// 99 可以进入
				// 2项目已结束
				// 3 项目未开启
				if (isscid == 99) {

				} else if (isscid == 0) {
				} else if (isscid == 1) {
				} else if (isscid == 2) {
					String text = "已过期!";
					DialogUtil.newdialog(mContext, text);
					return;
				} else if (isscid == 3) {
					String tagdata = DateUtil.CalculateTime(tagtime + "", ""
							+ Long.parseLong(DateUtil.dateToStamp()));
					String text = "距离开启时间还有" + tagdata + "!";
					DialogUtil.newdialog(mContext, text);
					return;
				}
			}
			// String type = "yyyy-MM-dd HH:mm:ss";
			//
			// long reg = 0l;
			// 查询上次的提交时间
			// String scnum[] = survey.getSCNum().split(",");
			// for (int i = 1; i < scnum.length; i++) {
			// if (survey.getSurveyId().equals(scnum[i])) {
			// String backsid = scnum[i-1];
			// UploadFeed upfeed =
			// ma.dbService.getAllXmlUploadFeed(survey.getSurveyId(), ma.userId,
			// feed.getPid());
			// reg = upfeed.getRegTime();
			// }
			// }
			// survey.getSurveyId(), "dapyyb", feed.getPid()

			// String Reg = DateUtil.getStrTime(reg+"",type);
			// String text = "这个问卷的提交时间"+Reg+"!"+"\n"+"pid:"+feed.getPid();
			// DialogUtil.newdialog(mContext, text);

			addr = mContext.Addr;
			lat = mContext.lat;
			lng = mContext.lng;

			BaseLog.w("点击item：addr=" + addr + "lat" + lat + "lng" + lng);

			if (survey.getForceGPS() == 0) {
				// 不强制
				if (lat == 0.0) {
					BaseToast.showLongToast(mContext, "地点获取失败！");
					AcquireLocation.getInstance().initlocation(
							ma.locationService, mContext, myListener);
				} else {
					if (addr == null) {
						BaseToast
								.showLongToast(mContext, "已定位，获取地址信息失败，请检查网络！");
					}
				}
			} else if (survey.getForceGPS() == 1) {
				AcquireLocation.getInstance().initlocation(ma.locationService,
						mContext, myListener);

				// 强制
				if (lat == 0.0) {
					BaseToast.showLongToast(mContext, "地点获取失败！");
					return;
				} else {
					if (addr == null) {
						BaseToast
								.showLongToast(mContext, "已定位，获取地址信息失败，请检查网络！");
					}
				}
			} else if (survey.getForceGPS() == 2) {
				// 不定位
			}

			if (1 == mContext.isTestMode) {
				AlertDialog.Builder locBuilder = new AlertDialog.Builder(
						mContext, AlertDialog.THEME_HOLO_LIGHT);
				locBuilder
						// .setTitle("提示").setIcon(android.R.drawable.ic_dialog_info)
						.setMessage("当前状态为测试模式，确认继续？")
						.setPositiveButton(
								mContext.getResources().getString(R.string.ok),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// BDLocation mLocation = mContext
										// .getmBDLocation();
										// if (null != mLocation) {
										// lat = mLocation.getLatitude();
										// · lng = mLocation.getLongitude();
										// addr = mLocation.getAddrStr();
										// }

										if (!Util.isEmpty(feed.getLat())) {

										}
										if (mContext.lat != 0.0) {
											lat = mContext.lat;
											lng = mContext.lng;
											addr = mContext.Addr;
										} else if (!Util.isEmpty(feed.getLat())
												&& !feed.getLat().equals("0.0")) {
											lat = Double.parseDouble(feed
													.getLat());
											lng = Double.parseDouble(feed
													.getLng());
											addr = feed.getVisitAddress();
										} else {
											lat = 0.0;
											lng = 0.0;
											addr = "";
										}

										Log.i("定位：", "lat::" + lat + "lng::"
												+ lng + "addr::" + addr);

										// lat =
										// Double.parseDouble(feed.getLat());
										// lng =
										// Double.parseDouble(feed.getLng());
										// addr =feed.getVisitAddress();
										//

										if (2 != survey.forceGPS) {
											// 强制定位

										}

										Log.i("定位：", "lat::" + lat + "lng::"
												+ lng + "addr::" + addr);

										Intent it = new Intent(mContext,
												ChoiceModeActivity.class);
										feed.setTestMode(mContext.isTestMode);
										it.putExtra("uf", feed);
										it.putExtra("survey", survey);
										it.putExtra("lat", lat);
										it.putExtra("lng", lng);
										it.putExtra("addr", addr);
										mContext.startActivity(it);
										dialog.dismiss();
										return;
									}

								})
						.setNegativeButton(
								mContext.getResources().getString(
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
				TextView msgTv = ((TextView) create
						.findViewById(android.R.id.message));
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

				if (!Util.isEmpty(feed.getLat())) {

				}
				if (mContext.lat != 0.0) {
					lat = mContext.lat;
					lng = mContext.lng;
					addr = mContext.Addr;
				} else if (!Util.isEmpty(feed.getLat())
						&& !feed.getLat().equals("0.0")) {
					lat = Double.parseDouble(feed.getLat());
					lng = Double.parseDouble(feed.getLng());
					addr = feed.getVisitAddress();
				} else {
					lat = 0.0;
					lng = 0.0;
					addr = "";
				}

				Log.i("定位：", "lat::" + lat + "lng::" + lng + "addr::" + addr);
				Intent it = new Intent(mContext, ChoiceModeActivity.class);
				feed.setTestMode(mContext.isTestMode);
				it.putExtra("uf", feed);
				it.putExtra("survey", survey);
				it.putExtra("lat", lat);
				it.putExtra("lng", lng);
				it.putExtra("addr", addr);
				mContext.startActivity(it);
			}

		}
	}

	// 删除匿名数据
	class CstOnLongClick implements OnLongClickListener {
		private UploadFeed feed;

		public CstOnLongClick() {

		}

		public CstOnLongClick(UploadFeed feed) {
			this.feed = feed;
		}

		@Override
		public boolean onLongClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
					AlertDialog.THEME_HOLO_LIGHT);
			builder.setMessage(mContext.getString(R.string.delete)
					+ feed.getId() + mContext.getString(R.string.notes));
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// deletehttp(dialog,feed);
							puthdb(feed, dialog);
						}
					});

			builder.setNegativeButton(R.string.cancle,
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			AlertDialog create = builder.create();
			create.setCancelable(false);
			create.show();
			return false;
		}

	}

	// 访问状态
	private String getReturnState(int returnTypeId, ArrayList<ReturnType> rlist) {
		String returnState = mContext.getString(R.string.no_state);
		for (int i = 0; i < rlist.size(); i++) {
			ReturnType returnType = rlist.get(i);
			if (returnTypeId == returnType.getReturnId()) {
				returnState = returnType.getReturnName();
				break;
			}
		}
		return returnState;
	}

	public void refresh(ArrayList<UploadFeed> feeds, Double lat, Double lng,
			String address) {
		if (null != lat && null != lng) {
			this.lat = lat;
			this.lng = lng;
		}
		if (!Util.isEmpty(address)) {
			this.addr = address;
		}
		if (!Util.isEmpty(feeds)) {
			if (!Util.isEmpty(fs)) {
				fs.clear();
				fs.addAll(feeds);
			}
		}
		notifyDataSetChanged();
	}

	static class ViewHolder {
		/**
		 * 删除匿名数据
		 */
		private LinearLayout visit_ll;
		private LinearLayout visitLL;

		/**
		 * 项目Id
		 */
		private UITextView tvLocalId;
		/**
		 * 问卷的结束时间
		 */
		private UITextView tvEndTime;

		/**
		 * 已完成
		 */
		private UITextView tvState, feedId;

		private LinearLayout innerLL, llTo1, llTo2, llTo3, llTo4;// 内部名单布局

		/**
		 * 内部名单显示的4个属性
		 */
		private UITextView tvTo1, tvTo2, tvTo3, tvTo4;
		private UITextView tvAddTo1, tvAddTo2, tvAddTo3, tvAddTo4;

	}

	long tagtime;

	public int isscid(Survey s, UploadFeed feed) {
		int isreturn = 0;
		// 0 没有间隔时间
		// 1 没有建个单位
		// 99 可以进入
		// 2项目已结束
		// 3 项目未开启

		// 现在时间戳
		long nowtimes = Long.parseLong(DateUtil.dateToStamp());

		Long IntervalTimes = 0l;
		// 浮动时间不为空
		Long FloatingTimes = 0l;
		// 上个问卷的提交时间
		Long RegTime = 0l;

		// 有scid
		if (!Util.isEmpty(s.getSCID())) {
			// 查询上次的提交时间
			String scnum[] = s.getSCNum().split(",");
			for (int i = 1; i < scnum.length; i++) {
				if (s.getSurveyId().equals(scnum[i])) {
					String backsid = scnum[i - 1];
					UploadFeed upfeed = ma.dbService.getAllXmlUploadFeed(
							backsid, ma.userId, feed.getPid());
					RegTime = upfeed.getRegTime();
				}
			}

			Log.i("isscid", "RegTime=" + RegTime);
			Log.i("isscid", "nowtimes=" + nowtimes);

			// 有间隔时间
			if (!Util.isEmpty(s.getIntervalTime())) {
				// 有单位
				if (!Util.isEmpty(s.getIntervalTimeUnit())) {
					// 间隔时间戳
					IntervalTimes = DateUtil.DataChangeToTime(
							s.getIntervalTime(), s.getIntervalTimeUnit());
					// 有浮动时间
					if (!Util.isEmpty(s.getFloatingTime())) {
						// 有浮动时间单位
						if (!Util.isEmpty(s.getFloatingTimeUnit())) {

							Log.i("isscid",
									"有浮动时间-s.getFloatingTime()="
											+ s.getFloatingTime());
							Log.i("isscid", "有浮动时间-s.getFloatingTimeUnit()="
									+ s.getFloatingTimeUnit());

							FloatingTimes = DateUtil.DataChangeToTime(
									s.getFloatingTime(),
									s.getFloatingTimeUnit());
							Log.i("isscid", "有浮动时间-RegTime=" + RegTime);
							Log.i("isscid", "有浮动时间-IntervalTimes="
									+ IntervalTimes);
							Log.i("isscid", "有浮动时间-FloatingTimes="
									+ FloatingTimes);

							long starttime = RegTime + IntervalTimes
									- FloatingTimes;
							long endtime = RegTime + IntervalTimes
									+ FloatingTimes;
							Log.i("isscid", "有浮动时间-starttime=" + starttime
									+ "endtime=" + endtime);
							// 现在大于等于 开始 可以进如
							if (nowtimes >= starttime) {
								// 结束时间减去现在时间 大于0 项目已结束
								if (nowtimes < endtime) {

									return 99;
								} else {
									// 项目已结束
									return 2;
								}
							} else {
								tagtime = starttime;
								// 项目未开始不能进
								return 3;
							}
						}
					} else {// 86400000l
							// 间隔时间戳
						IntervalTimes = DateUtil.DataChangeToTime(
								s.getIntervalTime(), s.getIntervalTimeUnit());
						Log.i("isscid",
								"没有浮动时间-IntervalTimeUnit="
										+ s.getIntervalTimeUnit());
						Log.i("isscid",
								"没有浮动时间-getIntervalTime=" + s.getIntervalTime());
						Log.i("isscid", "没有浮动时间-IntervalTimes=" + IntervalTimes);
						long starttime = RegTime + IntervalTimes;
						long endtime = RegTime + IntervalTimes + 86400000l;
						Log.i("isscid", "没有浮动时间-starttime=" + starttime
								+ "endtime=" + endtime);
						// 现在大于等于 开始 可以进如
						if (nowtimes >= starttime) {
							// 结束时间减去现在时间 大于0 项目已结束
							if (nowtimes < endtime) {

								return 99;
							} else {
								// 项目已结束
								return 2;
							}
						} else {
							tagtime = starttime;
							// 项目未开始不能进
							return 3;
						}

					}

				}
			} else {
				return 1;
			}

		} else {
			return 0;
		}
		return isreturn;

	}

	// /**
	// * 删除数据
	// * @param username
	// * @param pass
	// * @param surveyid
	// * @param feedid
	// */
	// private void deletehttp(final DialogInterface dialog,final UploadFeed
	// feed) {
	// HashMap<String,String> paramsMap = new HashMap<String, String>();
	// paramsMap.put("username",ma.userId);
	// paramsMap.put("passworld", ma.cfg.getString(Cnt.USER_PWD, ""));
	// paramsMap.put("surveyid",feed.getSurveyId());
	// paramsMap.put("feedid",feed.getFeedId()+"");
	// Log.i(TAG, "username:"+ma.userId);
	// Log.i(TAG, "pass:"+ma.cfg.getString(Cnt.USER_PWD, ""));
	// Log.i(TAG, "SurveyId:"+feed.getSurveyId());
	// Log.i(TAG, "feedid:"+feed.getSurveyId()+"");
	// Xutils.getInstance().get(Cnt.DeletePanel, paramsMap, new
	// Xutils.XCallBack() {
	// @Override
	// public void onResponse(String result) {
	// Log.d(TAG, "onResponse: result-" + result);
	// DeletePanelBean deletePanelBean =
	// GsonUtil.GsonToBean(result,DeletePanelBean.class);
	// BaseToast.showLongToast(mContext, deletePanelBean.getMsg());
	// if (deletePanelBean.getState().equals("100")) {
	// puthdb(feed,dialog);
	// }else {
	// BaseToast.showLongToast(mContext, "删除失败");
	// dialog.dismiss();
	// }
	// }
	// });
	// }

	private void puthdb(UploadFeed feed, final DialogInterface dialog) {

		MyApp ma = (MyApp) mContext.getApplication();
		// 获取该uuid的所有数据
		ArrayList<UploadFeed> recordList = ma.dbService.getRecordList(
				feed.getUuid(), feed.getSurveyId());
		ArrayList<Long> idList = new ArrayList<Long>();
		long id = feed.getId();
		idList.add(id);
		if (!Util.isEmpty(recordList)) {
			for (UploadFeed up : recordList) {
				idList.add(up.getId());
			}
		}
		if (0 < feed.getIsUploaded()) {
			// 假如上传就是假删除
			ma.dbService.deleteFakeUploadFeed(idList);
		} else {
			// 假如没有上传就是真删除
			ma.dbService.deleteFakeUploadFeed(idList);
			for (UploadFeed recodeFeed : recordList) {
				String path = recodeFeed.getPath();
				String name = recodeFeed.getName();
				File file = new File(path, name);
				file.delete();
			}
		}
		VisitAdapter.this.fs.remove(feed);
		mContext.removeFeed(feed);
		mContext.reSetCount();
		notifyDataSetChanged();
		dialog.dismiss();

	}

	Handler handlerloc = new Handler();
	int getloctime = 0;
	Runnable getloc = new Runnable() {

		public void run() {

			lat = AcquireLocation.getInstance().lat;
			addr = AcquireLocation.getInstance().Addr;
			lng = AcquireLocation.getInstance().lng;

			addr = RxLocationTool.getStreet(mContext, lat, lng);

			BaseLog.w("vis", "run--" + lat);
			getloctime++;
			if (lat == 0.0) {

				handlerloc.postDelayed(getloc, 1000);
			} else if (lat != 0.0) {
				AcquireLocation.getInstance().stoplocation();
			} else {
				if (getloctime == 30) {
					// stopProgressDialog();
					AcquireLocation.getInstance().startProgressDialog();
				}

				handlerloc.postDelayed(getloc, 1000);
			}
		}
	};
	private MyLocationListener myListener = new MyLocationListener();

	public class MyLocationListener extends BDAbstractLocationListener {

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
				addr = location.getAddrStr();
				latLongString = "纬度:" + lat + "经度:" + lng + "coorType"
						+ coorType + "errorCode" + errorCode;
				// 存数据库
				Log.i(TAG, "lat:" + lat);
				Log.i(TAG, "lng:" + lng);
				Log.i(TAG, "Addr:" + addr);
				Log.i(TAG, "isupdate:" + latLongString);
				// 关闭定位服务
				AcquireLocation.getInstance().stoplocation();
				Log.i(TAG, "关闭定位服务");
			} else {
				latLongString = "无法获取地理信息" + errorCode;
				BaseToast.showLongToast(mContext, latLongString);
			}

			Log.i(TAG, "updateWithNewLocation:您当前的位置是:" + latLongString);

		}
	}

}
