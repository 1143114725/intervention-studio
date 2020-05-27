package com.investigate.newsupper.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.ChoiceModeActivity;
import com.investigate.newsupper.activity.SubscibeActivity;
import com.investigate.newsupper.base.MyBaseAdapter;
import com.investigate.newsupper.bean.PersionItemListBean;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.DateUtil;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.LocationUtil;
import com.investigate.newsupper.util.LocationUtil.ImPlement;
import com.investigate.newsupper.util.Util;

/**
 * 人员详情adapter
 * 
 * @author EraJi
 * 
 */
public class PersionItemAdapter extends
		MyBaseAdapter<PersionItemListBean, ListView> {

	private MyApp ma;
	private BDLocation location;
	private String PID;

	String statetext = "";
	String enddatatext = "";
	String residuetext = "";

	String projiect, type = "", starttime = "", endtime = "", residuetime = "",
			residuetitle = "剩余时间：";
	String nowtime;

	public PersionItemAdapter(Context context, List<PersionItemListBean> list,
			MyApp ma, String PID) {
		super(context, list);
		// TODO Auto-generated constructor stub
		this.ma = ma;
		// this.location = location;
		this.PID = PID;
		// 初始化定位
		LocationUtil.CreateLocation(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_persion_item, null);
			viewHoder = new ViewHoder(convertView);
			convertView.setTag(viewHoder);
		} else {
			viewHoder = (ViewHoder) convertView.getTag();
		}
		final PersionItemListBean mPersionItemListBean = list.get(position);

		if (!Util.isEmpty(mPersionItemListBean.getNowDate_ms())) {
			nowtime = mPersionItemListBean.getNowDate_ms();
		} else {
			nowtime = "0";
		}

		projiect = mPersionItemListBean.getSurveyName();

		if (!Util.isEmpty(mPersionItemListBean.getReturnType())) {
			if (mPersionItemListBean.getReturnType().equals("2")) {
				// 已完成
				type = context.getResources()
						.getString(R.string.item_Completed);
				viewHoder.llresidue.setVisibility(View.GONE);
				viewHoder.llendtime.setVisibility(View.VISIBLE);
				viewHoder.llstarttime.setVisibility(View.GONE);
				if (!Util.isEmpty(mPersionItemListBean.getRegDate_ms())) {
					endtime = DateUtil.getStrTime(
							mPersionItemListBean.getRegDate_ms(),
							"yyyy-MM-dd HH:mm");
					Log.e("Unix时间戳", endtime);
					Log.e("Unix时间戳", mPersionItemListBean.getRegDate_ms());
				} else {
					endtime = "";
				}
				viewHoder.starttitle.setText("项目开启时间：");
				viewHoder.endtitle.setText("项目结束时间：");

			} else if (mPersionItemListBean.getReturnType().equals("1")) {
				// 条件不符
				type = context.getResources().getString(
						R.string.item_UnCondition);
				viewHoder.llresidue.setVisibility(View.GONE);
				viewHoder.llendtime.setVisibility(View.VISIBLE);
				viewHoder.llstarttime.setVisibility(View.GONE);
				if (!Util.isEmpty(mPersionItemListBean.getRegDate_ms())) {
					endtime = DateUtil.getStrTime(
							mPersionItemListBean.getRegDate_ms(),
							"yyyy-MM-dd HH:mm");
				} else {
					endtime = "未获取到结束时间！";
				}
				viewHoder.starttitle.setText("项目开启时间：");
				viewHoder.endtitle.setText("项目结束时间：");
			} else if (mPersionItemListBean.getReturnType().equals("0")) {
				// 未进行
				type = context.getResources()
						.getString(R.string.item_UnProceed);
				viewHoder.llresidue.setVisibility(View.VISIBLE);
				viewHoder.llendtime.setVisibility(View.VISIBLE);
				viewHoder.llstarttime.setVisibility(View.VISIBLE);

				// 有没有结束时间
				if (!Util.isEmpty(mPersionItemListBean.getEndDate_ms())) {
					endtime = DateUtil.getStrTime(
							mPersionItemListBean.getEndDate_ms(),
							"yyyy-MM-dd HH:mm");
				} else {
					endtime = "";
				}

				// 有没有开始时间
				if (!Util.isEmpty(mPersionItemListBean.getBeginDate_ms())) {
					starttime = DateUtil.getStrTime(
							mPersionItemListBean.getBeginDate_ms(),
							"yyyy-MM-dd HH:mm");

					long surveytype = Long.parseLong(mPersionItemListBean
							.getBeginDate_ms()) - Long.parseLong(nowtime);

					if (surveytype > 0) {
						// 不能进入 项目未开始
						residuetime = DateUtil
								.CalculateTime(
										mPersionItemListBean.getBeginDate_ms(),
										nowtime);
						residuetitle = "距离开启时间:";
					} else {
						if (!Util.isEmpty(mPersionItemListBean.getEndDate_ms())) {
							Log.i("itemadapter", "EndDate="
									+ mPersionItemListBean.getEndDate_ms()
									+ "nowtime=" + nowtime);
							residuetime = DateUtil.CalculateTime(
									mPersionItemListBean.getEndDate_ms(),
									nowtime);
							residuetitle = "距离关闭时间:";
						} else {
							residuetime = "";
							residuetitle = "距离关闭时间:";
						}

					}
				} else {
					starttime = "";
				}
				viewHoder.starttitle.setText("项目开启时间：");
				viewHoder.endtitle.setText("项目关闭时间：");

			} else if (mPersionItemListBean.getReturnType().equals("3")) {

				// 未进行
				type = context.getResources().getString(
						R.string.visit_state_interrupt);
				viewHoder.llresidue.setVisibility(View.VISIBLE);
				viewHoder.llendtime.setVisibility(View.VISIBLE);
				viewHoder.llstarttime.setVisibility(View.VISIBLE);

				// 有没有结束时间
				if (!Util.isEmpty(mPersionItemListBean.getEndDate_ms())) {
					endtime = DateUtil.getStrTime(
							mPersionItemListBean.getEndDate_ms(),
							"yyyy-MM-dd HH:mm");
				} else {
					endtime = "";
				}

				// 有没有开始时间
				if (!Util.isEmpty(mPersionItemListBean.getBeginDate_ms())) {
					starttime = DateUtil.getStrTime(
							mPersionItemListBean.getBeginDate_ms(),
							"yyyy-MM-dd HH:mm");

					long surveytype = Long.parseLong(mPersionItemListBean
							.getBeginDate_ms()) - Long.parseLong(nowtime);

					if (surveytype > 0) {
						// 不能进入 项目未开始
						residuetime = DateUtil
								.CalculateTime(
										mPersionItemListBean.getBeginDate_ms(),
										nowtime);
						residuetitle = "距离开启时间:";
					} else {
						if (!Util.isEmpty(mPersionItemListBean.getEndDate_ms())) {
							Log.i("itemadapter", "EndDate="
									+ mPersionItemListBean.getEndDate_ms()
									+ "nowtime=" + nowtime);
							residuetime = DateUtil.CalculateTime(
									mPersionItemListBean.getEndDate_ms(),
									nowtime);
							residuetitle = "距离关闭时间:";
						} else {
							residuetime = "";
							residuetitle = "距离关闭时间:";
						}

					}
				} else {

					starttime = "";
				}
				viewHoder.starttitle.setText("项目开启时间：");
				viewHoder.endtitle.setText("项目关闭时间：");

			}
		}

		viewHoder.project.setText(projiect);
		viewHoder.state.setText(type);
		// if (type.equals("已过期")) {
		// viewHoder.state.setTextColor(context.getResources().getColor(R.color.red));
		// }else {
		// viewHoder.state.setTextColor(context.getResources().getColor(R.color.back));
		// }

		viewHoder.startdata.setText(starttime);
		viewHoder.enddata.setText(endtime);

		if (!type.equals("已完成")) {
			if (residuetime.equals("已过期")) {
				viewHoder.state.setText("已过期");
				viewHoder.state.setTextColor(context.getResources().getColor(
						R.color.red));
				viewHoder.llstarttime.setVisibility(View.GONE);
				viewHoder.llresidue.setVisibility(View.GONE);
			} else {
				viewHoder.state.setTextColor(context.getResources().getColor(
						R.color.back));
			}
		}

		viewHoder.residue.setText(residuetime);
		viewHoder.residuetitle.setText(residuetitle);

		viewHoder.item_persions_llroot
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						// 跳转页面
						Log.d("surveyid the adapter", list.get(position)
								.getSurveyID());
						final Survey s = ma.dbService.getParameterSurvey(list
								.get(position).getSurveyID());
						// final Survey s =
						// ma.dbService.getAllDownloadedSurvey(ma.userId);
						if (Util.isEmpty(s.surveyId)) {
							Log.d("surveyid the adapter", s.surveyId);
							// 跳到下载项目的界面
							String text = "请先下载此项目！";
							DialogUtil.newdialog(context, text);
							Intent intent = new Intent(context,
									SubscibeActivity.class);
							context.startActivity(intent);
							return;
						}

						BaseLog.w("---" + list.get(position).getSurveyID());
						BaseLog.w("---" + ma.userId);
						BaseLog.w("---" + PID);

						final UploadFeed feed = ma.dbService
								.getAllXmlUploadFeed(list.get(position)
										.getSurveyID(), ma.userId, PID);

						if (Util.isEmpty(feed.getUuid())) {
							// String text = "此问卷数据不在本设备上！";
							// DialogUtil.newdialog(context, text);
							
							double d = 0;
							Intent it = new Intent(context,
									ChoiceModeActivity.class);
							
							
							feed.setTestMode(0);
							it.putExtra("uf", feed);
							it.putExtra("survey", s);
							it.putExtra("lat", d);
							it.putExtra("lng", d);
							it.putExtra("addr", "");
							it.putExtra("isweb", "true");
							it.putExtra("feedid", mPersionItemListBean.getFeedID());
							
							context.startActivity(it);
							return;

						} else {
							// 不是的判断一下是否过期
							// 项目开始时间为空 显示无期限 不为空 用开始时间减去现在时间
							// 如果大于0说明项目没开始不能进入，小于等于0的时候项目是开始的可以进入
							if (!Util.isEmpty(list.get(position)
									.getBeginDate_ms())) {
								long surveytype = Long.parseLong(list.get(
										position).getBeginDate_ms())
										- Long.parseLong(list.get(position)
												.getNowDate_ms());
								if (surveytype > 0) {
									// 不能进入
									String text = "此问卷暂未开始！";
									DialogUtil.newdialog(context, text);
									return;
								} else {
									// 判断过期没又
									String padtdue = context.getResources()
											.getString(R.string.item_PadtDue);
									if (residuetext.equals(padtdue)) {
										DialogUtil.newdialog(context, padtdue
												+ "!");
										return;
									}
								}
							}
						}

						if (viewHoder.residue.getText().toString()
								.equals("已过期")) {
							// 访问成功
							if (list.get(position).getReturnType().equals("2")) {

							}
							// 条件不符
							if (list.get(position).getReturnType().equals("1")) {

							}
							// 未访问
							if (list.get(position).getReturnType().equals("0")) {
								DialogUtil.newdialog(context, "该项目已过期！");
								return;
							}
						}

						String nomistext = "此问卷数据不在本设备上！";
						// 访问成功
						if (list.get(position).getReturnType().equals("2")) {
							if (feed.getIsUploaded() == 1) {
								// 传过

							} else if (feed.getIsUploaded() == -1) {
								// 未传
								DialogUtil.newdialog(context, nomistext);
								return;
							}
						}
						

						double d = 0;
						if (list.get(position).getReturnType().equals("2")) {

							Intent it = new Intent(context,
									ChoiceModeActivity.class);
							feed.setTestMode(0);
							it.putExtra("uf", feed);
							it.putExtra("survey", s);
							it.putExtra("lat", d);
							it.putExtra("lng", d);
							it.putExtra("addr", "");
							context.startActivity(it);
						} else if (list.get(position).getReturnType()
								.equals("1")) {

							Intent it = new Intent(context,
									ChoiceModeActivity.class);
							feed.setTestMode(0);
							it.putExtra("uf", feed);
							it.putExtra("survey", s);
							it.putExtra("lat", d);
							it.putExtra("lng", d);
							it.putExtra("addr", "");
							context.startActivity(it);
						} else {

							if (s.openGPS == 0) {
								Intent it = new Intent(context,
										ChoiceModeActivity.class);
								feed.setTestMode(0);
								it.putExtra("uf", feed);
								it.putExtra("survey", s);
								it.putExtra("lat", d);
								it.putExtra("lng", d);
								it.putExtra("addr", "");
								context.startActivity(it);
							} else {
								location = LocationUtil.getLocation();
								LocationUtil.isforceGPS(location, s, context,
										new ImPlement() {

											@Override
											public void implement() {
												// TODO Auto-generated method
												// stub
												Intent it = new Intent(
														context,
														ChoiceModeActivity.class);
												feed.setTestMode(0);
												feed.setLat(location
														.getLatitude() + "");
												feed.setLng(location
														.getLongitude() + "");
												feed.setVisitAddress(location
														.getAddrStr());

												it.putExtra("uf", feed);
												it.putExtra("survey", s);
												it.putExtra("lat",
														location.getLatitude());
												it.putExtra("lng",
														location.getLongitude());
												it.putExtra("addr",
														location.getAddrStr());
												context.startActivity(it);
											}
										}, ma);
							}
						}
					}
				});
		return convertView;
	}

	private ViewHoder viewHoder;

	private class ViewHoder {
		private LinearLayout item_persions_llroot;// root布局

		private TextView project;// 项目
		private TextView state;// 状态
		private TextView startdata;// 开始时间
		private TextView enddata;// 结束时间

		private TextView starttitle;// 结束时间
		private TextView endtitle;// 结束时间

		private LinearLayout llstarttime;// 开始布局
		private LinearLayout llendtime;// 结束布局
		private LinearLayout llresidue;// 剩余时间布局

		private TextView residue;// 剩余天数
		private TextView residuetitle;// 剩余天数

		public ViewHoder(View convertView) {
			super();
			this.item_persions_llroot = (LinearLayout) convertView
					.findViewById(R.id.item_persions_llroot);

			this.project = (TextView) convertView
					.findViewById(R.id.tv_item_persions_project);
			this.state = (TextView) convertView
					.findViewById(R.id.tv_item_persions_state);
			this.enddata = (TextView) convertView
					.findViewById(R.id.tv_item_persions_enddata);

			this.starttitle = (TextView) convertView
					.findViewById(R.id.tv_iten_starttile);
			this.endtitle = (TextView) convertView
					.findViewById(R.id.tv_iten_endtile);

			this.startdata = (TextView) convertView
					.findViewById(R.id.tv_item_persions_startdata);
			this.residuetitle = (TextView) convertView
					.findViewById(R.id.tv_item_residuetitle);
			this.residue = (TextView) convertView
					.findViewById(R.id.tv_item_persions_residue);

			this.llresidue = (LinearLayout) convertView
					.findViewById(R.id.ll_item_residuetiem);
			this.llendtime = (LinearLayout) convertView
					.findViewById(R.id.ll_item_endtime);
			this.llstarttime = (LinearLayout) convertView
					.findViewById(R.id.ll_item_starttime);
		}
	}

}
