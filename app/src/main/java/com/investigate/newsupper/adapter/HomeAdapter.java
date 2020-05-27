package com.investigate.newsupper.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.FragmentMain;
import com.investigate.newsupper.activity.HomeActivity;
import com.investigate.newsupper.activity.LoginActivity;
import com.investigate.newsupper.activity.SubscibeActivity;
import com.investigate.newsupper.activity.VisitActivity;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.UIUtils;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.view.Toasts;

public class HomeAdapter extends BaseAdapter {
	private String TAG;
	private Activity activity;
	private ArrayList<Survey> list;
	private MyApp ma;
	private String language;
	int dialogBtnSize = (int) (UIUtils.getDimenPixelSize(R.dimen.sry_text_big) * TextSizeManager
			.getInstance().getRealScale());;
	int dialogMsg = (int) (UIUtils.getDimenPixelSize(R.dimen.sry_text_big) * TextSizeManager
			.getInstance().getRealScale());;

	public HomeAdapter(Activity activity, ArrayList<Survey> list, String TAG) {
		super();
		this.activity = activity;
		this.list = list;
		this.TAG = TAG;
		ma = (MyApp) this.activity.getApplication();
		language = activity.getResources().getConfiguration().locale
				.getLanguage();

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void refresh(ArrayList<Survey> surveys) {
		if (!Util.isEmpty(surveys)) {
			if (!Util.isEmpty(list)) {
				list.clear();
				list.addAll(surveys);
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (null == convertView) {
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.home_grid_item, null);
			vh = new ViewHolder();
			vh.grid_item_ll = (LinearLayout) convertView
					.findViewById(R.id.grid_item_ll);
			vh.grid_item_top = (LinearLayout) convertView
					.findViewById(R.id.grid_item_top);
			vh.grid_item_back = (LinearLayout) convertView
					.findViewById(R.id.grid_item_back);
			vh.grid_item_scsize = (UITextView) convertView
					.findViewById(R.id.grid_item_scsize);
			vh.grid_item_number = (UITextView) convertView
					.findViewById(R.id.grid_item_number);
			vh.grid_item_title = (UITextView) convertView
					.findViewById(R.id.grid_item_title);
			vh.grid_item_content = (UITextView) convertView
					.findViewById(R.id.grid_item_content);
			TextSizeManager.getInstance()
					.addTextComponent(TAG, vh.grid_item_number)
					.addTextComponent(TAG, vh.grid_item_title)
					.addTextComponent(TAG, vh.grid_item_content);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		Drawable background = activity.getResources().getDrawable(
				R.drawable.border);
		Drawable backgrounds = activity.getResources().getDrawable(
				R.drawable.borders);
		if (position % 4 == 1) {
			background = activity.getResources().getDrawable(R.drawable.border1);
			backgrounds = activity.getResources().getDrawable(R.drawable.border1s);
		} else if (position % 4 == 2) {
//			background = activity.getResources().getDrawable(R.drawable.new_feed_bac);
			background = activity.getResources().getDrawable(R.drawable.border2);
			backgrounds = activity.getResources().getDrawable(R.drawable.border2s);
		} else if (position % 4 == 3) {
			background = activity.getResources().getDrawable(R.drawable.border3);
			backgrounds = activity.getResources().getDrawable(R.drawable.border3s);
		}
		vh.grid_item_back.setBackgroundDrawable(backgrounds);
		vh.grid_item_top.setBackgroundDrawable(background);
		Survey s = list.get(position);
		if (null != s) {
			vh.grid_item_title.setText(s.surveyTitle);
			if (Util.isEmpty(ma.userId)) {
				ma.userId = ((null == ma.cfg) ? (ma.cfg = new Config(ma))
						: (ma.cfg)).getString("UserId",
						activity.getString(R.string.user_name_test));
			}
			vh.grid_item_number.setText(""
					+ ma.dbService.feedCompletedCount(s.surveyId, ma.userId));
			// 大树 访问前说明 添加
			if (!Util.isEmpty(s.getWord())) {
				Spannable sp = (Spannable) Html.fromHtml(s.getWord());
				vh.grid_item_content.setText(sp.toString());
			} else {
				vh.grid_item_content.setText(R.string.no_explain);
			}
		}
		initKeyTextView(vh.grid_item_title, vh.grid_item_content, position);
		vh.grid_item_ll.setOnClickListener(new GridListener(s));
		vh.grid_item_ll.setOnLongClickListener(new GridOnLongClick(s));

		if (!Util.isEmpty(s.getSCID())) {
			// app连续性项目的实现
			String SCnum = s.getSCNum();
			int number = SCnum.split(",").length;
			String showtext = "有" + number + "个子项目";
			vh.grid_item_scsize.setText(showtext);
			vh.grid_item_title.setText(s.getSCName());
		} else {
			vh.grid_item_scsize.setText("");
		}
		return convertView;
	}

	// 初始化宽度
	private void initKeyTextView(final View ll1, final View ll2,
			final int position) {
		ViewTreeObserver vto1 = ll1.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				ll1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				if (position % Cnt.GRIVEW_COLUMN_NUMS == 0) {
					Cnt.TITLE_HEIGHT = 0;
				}
				if (ll1.getHeight() > Cnt.TITLE_HEIGHT) {
					Cnt.TITLE_HEIGHT = ll1.getHeight();
				}
				setHeight(ll1, Cnt.TITLE_HEIGHT, 1);
			}
		});

		ViewTreeObserver vto2 = ll2.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				ll2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				if (position % Cnt.GRIVEW_COLUMN_NUMS == 0) {
					Cnt.INS_HEIGHT = 0;
				}
				if (ll2.getHeight() > Cnt.INS_HEIGHT) {
					Cnt.INS_HEIGHT = ll2.getHeight();
				}
				setHeight(ll2, Cnt.INS_HEIGHT, 2);
			}
		});
	}

	// 设置高度
	public void setHeight(View ll, int height, int flag) {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				height);
		layoutParams.leftMargin = activity.getResources()
				.getDimensionPixelSize(R.dimen.login_margin_top_small);
		layoutParams.topMargin = activity.getResources().getDimensionPixelSize(
				R.dimen.login_margin_top_small);
		layoutParams.rightMargin = activity.getResources()
				.getDimensionPixelSize(R.dimen.login_margin_top_small);
		if (2 == flag) {
			layoutParams.bottomMargin = activity.getResources()
					.getDimensionPixelSize(R.dimen.login_margin_top_small);
		}
		ll.setLayoutParams(layoutParams);
	}

	// viewholder
	static class ViewHolder {
		private UITextView grid_item_number;// 个数
		private UITextView grid_item_scsize;// 子项目个数
		private UITextView grid_item_title;// 题目
		private UITextView grid_item_content;// 说明
		private LinearLayout grid_item_ll;// 外边框
		private LinearLayout grid_item_back;// 局部背景
		private LinearLayout grid_item_top;// 上部背景
	}

	/**
	 * 点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private final class GridListener implements OnClickListener {
		private Survey s;

		public GridListener(Survey survey) {
			this.s = survey;
		}

		@Override
		public void onClick(View v) {
			
			

			System.out.println("scid----->>" + s.getSCID());
			// 正常跳转页面
			if (s.getSCID() == null || s.getSCID().equals("")) {

				if (NetUtil.checkNet(activity)) {
					if (!s.getLastGeneratedTime().equals(s.getGeneratedTime())) {
						String deleteMsg = activity
								.getString(R.string.update_alert);
						AlertDialog.Builder builder = new AlertDialog.Builder(
								activity, AlertDialog.THEME_HOLO_LIGHT);
						builder.setMessage(deleteMsg);
						builder.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (!Util.isEmpty(ma.userPwd)
												&& NetUtil.checkNet(ma)) {
											((HomeActivity) activity)
													.skipActivity(SubscibeActivity.class);
										} else if (!NetUtil.checkNet(ma)) {
											// 没网情况下
											Toasts.makeText(ma, R.string.exp_net,
													Toast.LENGTH_LONG).show();
										} else if (Util.isEmpty(ma.userPwd)) {
											// 用户名为空
											Toasts.makeText(ma, R.string.no_login,
													Toast.LENGTH_LONG).show();
											ma.cfg.putBoolean("isFirst", false); // 设置第一次访问
																					// 否
											((HomeActivity) activity).skipActivity(
													LoginActivity.class, 30);
										}
									}
								});
						builder.setNegativeButton(
								R.string.cancle,
								new android.content.DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});

						AlertDialog create = builder.create();
						create.setCancelable(false);
						create.show();
						// 修改提示框的字体大小和颜色
						TextView msgTv = ((TextView) create
								.findViewById(android.R.id.message));
						msgTv.setMinLines(2);
						msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogMsg);
						msgTv.setGravity(Gravity.CENTER_VERTICAL);
						create.getButton(create.BUTTON_NEGATIVE).setTextSize(
								TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
						create.getButton(create.BUTTON_POSITIVE).setTextSize(
								TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
						return;
					}
				}
				
				
				Intent it = new Intent();
				Bundle bundle = new Bundle();
				if ("zh".equals(language)) {
					// language
					it.setClass(activity, VisitActivity.class);
					s.mapType = "Baidu";
				} else {
					it.setClass(activity, VisitActivity.class);
					s.mapType = "Baidu";
				}
				bundle.putSerializable("s", s);
				bundle.putString("type", "2");
				it.putExtras(bundle);
				activity.startActivity(it);
				activity.overridePendingTransition(R.anim.zzright,
						R.anim.zzleft);
			} else {
				
				
				String scnum[] = s.getSCNum().split(",");
				for (int i = 0; i < scnum.length; i++) {
					
					s = ma.dbService.getSurvey(scnum[i]);
					if (s == null) {
						continue;
					}
					
					
					if (NetUtil.checkNet(activity)) {
						if (!Util.isEmpty(s.getLastGeneratedTime()) && !Util.isEmpty(s.getGeneratedTime())) {
							if (!s.getLastGeneratedTime().equals(s.getGeneratedTime())) {
								String deleteMsg = activity
										.getString(R.string.update_alert);
								AlertDialog.Builder builder = new AlertDialog.Builder(
										activity, AlertDialog.THEME_HOLO_LIGHT);
								builder.setMessage(deleteMsg);
								builder.setPositiveButton(R.string.ok,
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,
													int which) {
												if (!Util.isEmpty(ma.userPwd)
														&& NetUtil.checkNet(ma)) {
													((HomeActivity) activity)
															.skipActivity(SubscibeActivity.class);
												} else if (!NetUtil.checkNet(ma)) {
													// 没网情况下
													Toasts.makeText(ma, R.string.exp_net,
															Toast.LENGTH_LONG).show();
												} else if (Util.isEmpty(ma.userPwd)) {
													// 用户名为空
													Toasts.makeText(ma, R.string.no_login,
															Toast.LENGTH_LONG).show();
													ma.cfg.putBoolean("isFirst", false); // 设置第一次访问
																							// 否
													((HomeActivity) activity).skipActivity(
															LoginActivity.class, 30);
												}
											}
										});
								builder.setNegativeButton(
										R.string.cancle,
										new android.content.DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										});

								AlertDialog create = builder.create();
								create.setCancelable(false);
								create.show();
								// 修改提示框的字体大小和颜色
								TextView msgTv = ((TextView) create
										.findViewById(android.R.id.message));
								msgTv.setMinLines(2);
								msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogMsg);
								msgTv.setGravity(Gravity.CENTER_VERTICAL);
								create.getButton(create.BUTTON_NEGATIVE).setTextSize(
										TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
								create.getButton(create.BUTTON_POSITIVE).setTextSize(
										TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
								return;
							}
						}
						
					}
				}
				
				
				
				// 连续项目跳转页面
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				intent.setClass(activity, FragmentMain.class);
				s.mapType = "Baidu";
				bundle.putSerializable("s", s);
				bundle.putString("type", "1");
				intent.putExtras(bundle);
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.zzright,
						R.anim.zzleft);
			}

		}
	};

	/**
	 * 长按删除项目（假删除）
	 * 
	 * @author Administrator
	 */
	private final class GridOnLongClick implements OnLongClickListener {
		Survey survey;

		public GridOnLongClick(Survey s) {
			this.survey = s;
		}

		@Override
		public boolean onLongClick(View v) {

			// 都上传了就停
			String deleteMsg = activity.getString(R.string.delete_survey);
			AlertDialog.Builder builder = new AlertDialog.Builder(activity,
					AlertDialog.THEME_HOLO_LIGHT);
			builder.setMessage(deleteMsg);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// 有子项目的
							if (!Util.isEmpty(survey.SCID)) {
								// 获取所有已完成且未上传的数据数量
								long feedUnUploadCount = 0;
								String SCnum = survey.getSCNum();
								final String strscnum[] = SCnum.split(",");
								final int number = strscnum.length;
								if (number > 1) {
									for (int i = 0; i < strscnum.length; i++) {
										feedUnUploadCount += ma.dbService
												.feedUnUploadCount(survey.surveyId);
									}
								}

								// 单个项目
								if (0 < feedUnUploadCount) {
									String deleteMsg = activity
											.getString(R.string.delete_survey_warning);
									AlertDialog.Builder builder = new AlertDialog.Builder(
											activity);
									builder.setMessage(deleteMsg);
									builder.setPositiveButton(
											R.string.ok,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													if (number > 1) {
														for (int i = 0; i < strscnum.length; i++) {
															ma.dbService
																	.removeSurvey(strscnum[i]);
															HomeAdapter.this.list
																	.remove(survey);
															notifyDataSetChanged();
															dialog.dismiss();
														}
													}
												}
											});
									builder.setNegativeButton(
											R.string.cancle,
											new android.content.DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											});
									AlertDialog create = builder.create();
									create.setCancelable(false);
									create.show();
									// 修改提示框的字体大小和颜色
									TextView msgTv = ((TextView) create
											.findViewById(android.R.id.message));
									msgTv.setMinLines(2);
									msgTv.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											dialogMsg);
									msgTv.setTextColor(Color.RED);
									msgTv.setGravity(Gravity.CENTER_VERTICAL);
									create.getButton(
											DialogInterface.BUTTON_NEGATIVE)
											.setTextSize(
													TypedValue.COMPLEX_UNIT_PX,
													dialogBtnSize);
									create.getButton(
											DialogInterface.BUTTON_POSITIVE)
											.setTextSize(
													TypedValue.COMPLEX_UNIT_PX,
													dialogBtnSize);
								} else {
									if (number > 1) {
										for (int i = 0; i < strscnum.length; i++) {
											ma.dbService
													.removeSurvey(strscnum[i]);
											HomeAdapter.this.list
													.remove(survey);
											notifyDataSetChanged();
											dialog.dismiss();
										}
									}

								}

							} else {
								// 获取所有已完成且未上传的数据数量
								final long feedUnUploadCount = ma.dbService
										.feedUnUploadCount(survey.surveyId);
								// 单个项目
								if (0 < feedUnUploadCount) {
									String deleteMsg = activity
											.getString(R.string.delete_survey_warning);
									AlertDialog.Builder builder = new AlertDialog.Builder(
											activity);
									builder.setMessage(deleteMsg);
									builder.setPositiveButton(
											R.string.ok,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													ma.dbService
															.removeSurvey(survey.surveyId);
													HomeAdapter.this.list
															.remove(survey);
													notifyDataSetChanged();
													dialog.dismiss();
												}
											});
									builder.setNegativeButton(
											R.string.cancle,
											new android.content.DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											});
									AlertDialog create = builder.create();
									create.setCancelable(false);
									create.show();
									// 修改提示框的字体大小和颜色
									TextView msgTv = ((TextView) create
											.findViewById(android.R.id.message));
									msgTv.setMinLines(2);
									msgTv.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											dialogMsg);
									msgTv.setTextColor(Color.RED);
									msgTv.setGravity(Gravity.CENTER_VERTICAL);
									create.getButton(
											DialogInterface.BUTTON_NEGATIVE)
											.setTextSize(
													TypedValue.COMPLEX_UNIT_PX,
													dialogBtnSize);
									create.getButton(
											DialogInterface.BUTTON_POSITIVE)
											.setTextSize(
													TypedValue.COMPLEX_UNIT_PX,
													dialogBtnSize);
								} else {
									ma.dbService.removeSurvey(survey.surveyId);
									HomeAdapter.this.list.remove(survey);
									notifyDataSetChanged();
									dialog.dismiss();
								}
							}
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
			// 修改提示框的字体大小和颜色
			TextView msgTv = ((TextView) create
					.findViewById(android.R.id.message));
			msgTv.setMinLines(2);
			msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogMsg);
			msgTv.setGravity(Gravity.CENTER_VERTICAL);
			create.getButton(create.BUTTON_NEGATIVE).setTextSize(
					TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
			create.getButton(create.BUTTON_POSITIVE).setTextSize(
					TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
			return false;
		}
	}
}
