package com.investigate.newsupper.adapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.MyQuotaActivity;
import com.investigate.newsupper.bean.Answer;
import com.investigate.newsupper.bean.Option;
import com.investigate.newsupper.bean.Parameter;
import com.investigate.newsupper.bean.Quota;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.QuotaSucessUtil;
import com.investigate.newsupper.util.Util;

public class QuotaAdapter extends BaseAdapter {
	String TAG = "QuotaAdapter";
	private MyQuotaActivity mContext;
	private ArrayList<Quota> qlist;
	private LayoutInflater inflater;
	private Survey survey;
	private MyApp ma;

	public QuotaAdapter(MyQuotaActivity _c, ArrayList<Quota> qlists,
			Survey _survey) {
		this.mContext = _c;
		this.qlist = qlists;
		this.survey = _survey;
		inflater = (LayoutInflater) _c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ma = (MyApp) _c.getApplication();
	}

	@Override
	public int getCount() {
		return qlist.size();
	}

	@Override
	public Object getItem(int position) {
		if (!Util.isEmpty(qlist)) {
			return qlist.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void add(ArrayList<Quota> qlists) {
		if (!Util.isEmpty(qlists)) {
			qlist.addAll(qlists);
			notifyDataSetChanged();
		}
	}

	public void refresh(ArrayList<Quota> quotas) {
		if (!Util.isEmpty(quotas)) {
			if (!Util.isEmpty(qlist)) {
				qlist.clear();
				qlist.addAll(quotas);
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.quota_list_item, null);
			vh = new ViewHolder();
			vh.tvQuotaName = (TextView) convertView
					.findViewById(R.id.quota_name_tv);
			vh.tvQuotasuccess = (TextView) convertView
					.findViewById(R.id.quota_success_tv);
			vh.tvQuotaText = (TextView) convertView
					.findViewById(R.id.quota_text_tv);
			vh.tvQuotains = (TextView) convertView
					.findViewById(R.id.ins_text_tv);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Quota quota = qlist.get(position);
		if (null != quota) {
			if (!Util.isEmpty(quota.getQuotaName())) {
				vh.tvQuotaName.setText(quota.getQuotaName());
			}
			int quotaSuccess = 0;
			if (!Util.isEmpty(quota.getOptionlist())) {
//				quotaSuccess = getQuotaSuccess(quota.getOptionlist(),
//						survey.surveyId);
				quotaSuccess = QuotaSucessUtil.getSucessQuota(ma, quota.getOptionlist(),survey.surveyId);
			}
			
			if (!Util.isEmpty(quota.getSucessCount())) {
				int cont = Integer.parseInt(quota.getSucessCount());
				vh.tvQuotaText.setText(cont + "/"+ String.valueOf(quotaSuccess));
			}else {
				vh.tvQuotaText.setText("0/"+ String.valueOf(quotaSuccess));
			}
			
			if (!Util.isEmpty(quota.getPlanCount())) {
				vh.tvQuotasuccess.setText(quota.getPlanCount());
			}
			
			if (!Util.isEmpty(quota.getQuotaIns())) {
				vh.tvQuotains.setText(quota.getQuotaIns());
			}
			
			if (!Util.isEmpty(quota.getPreQuoteFlag())) {
				if (quota.getPreQuoteFlag().equals("1")) {
					vh.tvQuotains.setTextColor(mContext.getResources().getColor(
							R.color.red));
					vh.tvQuotasuccess.setTextColor(mContext.getResources()
							.getColor(R.color.red));
					vh.tvQuotaText.setTextColor(mContext.getResources().getColor(
							R.color.red));
					vh.tvQuotaName.setTextColor(mContext.getResources().getColor(
							R.color.red));
				} else {
					vh.tvQuotains.setTextColor(mContext.getResources().getColor(
							R.color.back));
					vh.tvQuotasuccess.setTextColor(mContext.getResources()
							.getColor(R.color.back));
					vh.tvQuotaText.setTextColor(mContext.getResources().getColor(
							R.color.back));
					vh.tvQuotaName.setTextColor(mContext.getResources().getColor(
							R.color.back));
				}
			}
			
			


		}
		return convertView;
	}

	// 配额成功量获取方法
	/**
	 * 
	 * @param optionlist
	 *            单个配额的多个属性条件
	 * @param surveyId
	 * @return
	 */
	private int getQuotaSuccess(ArrayList<Option> optionlist, String surveyId) {
		Log.i(TAG, "111111");

		// 初始化答案集合变量
		ArrayList<Answer> anlist = new ArrayList<Answer>();
		// 初始化合格问题编号集合
		ArrayList<String> answerlist = new ArrayList<String>();
		// 最后合格的list
		ArrayList<String> successlist = new ArrayList<String>();
		// 用来排除重复选项的list
		ArrayList<String> excludelist = new ArrayList<String>();
		// 用来计数
		int success = 0;
		if (0 < optionlist.size()) {
			if ("Access".equals(optionlist.get(0).getType())) {
				ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
				fs = ma.dbService.getAllQuotaUploadFeed(surveyId, ma.userId);
				if (fs.size() > 0) {
					ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
					for (Option opt : optionlist) {
						if ("Equal".equals(opt.getSymbol())) {
							for (UploadFeed uploadFeed : fs) {
								if (!Util.isEmpty(uploadFeed.getReturnType()) && uploadFeed.getReturnType().equals("21")) {

								} else {
									String parametersStr = uploadFeed
											.getParametersStr();
									if (!Util.isEmpty(uploadFeed
											.getParametersStr())) {
										parameterList.clear();
										ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
												.parseArray(parametersStr,
														Parameter.class);
										if (!Util.isEmpty(tParameters)) {
											parameterList.addAll(tParameters);
											for (Parameter parameter : tParameters) {
												if (!Util.isEmpty(parameter
														.getContent())) {
													if (parameter
															.getSid()
															.equals(opt
																	.getQuestionindex())) {
														if (Util.isRespondentsMatching(
																parameter
																		.getContent(),
																"=",
																opt.getCondition())) {
															success++;
														}

													}
												}
											}
										}
									}
								}

							}
						}
						if ("MoreThan".equals(opt.getSymbol())) {
							for (UploadFeed uploadFeed : fs) {
								if (!Util.isEmpty(uploadFeed.getReturnType()) && uploadFeed.getReturnType().equals("21")) {

								} else {
									String parametersStr = uploadFeed
											.getParametersStr();
									if (!Util.isEmpty(uploadFeed
											.getParametersStr())) {
										parameterList.clear();
										ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
												.parseArray(parametersStr,
														Parameter.class);
										if (!Util.isEmpty(tParameters)) {
											parameterList.addAll(tParameters);
											for (Parameter parameter : tParameters) {
												if (!Util.isEmpty(parameter
														.getContent())) {
													// 假如是数字
													if (Util.isFormat(parameter
															.getContent(), 9)
															&& Util.isFormat(
																	opt.getCondition(),
																	9)) {
														if (Float
																.parseFloat(parameter
																		.getContent()) > Float
																.parseFloat(opt
																		.getCondition())
																&& parameter
																		.getSid()
																		.equals(opt
																				.getQuestionindex())) {
															success++;
														}
													}
													// 日期
													else if (parameter
															.getContent()
															.indexOf("-") != -1
															&& parameter
																	.getContent()
																	.length() > 5) {
														if (parameter
																.getSid()
																.equals(opt
																		.getQuestionindex())) {
															try {
																if (Util.getDateCompare(
																		parameter
																				.getContent(),
																		opt.getCondition(),
																		"<")) {
																	success++;
																}
															} catch (ParseException e) {
																e.printStackTrace();
															}
														}
													}
													// 文本格式
													else {
														// if(parameter.getSid().equals(opt.getQuestionindex())
														// ){
														// if
														// (Util.isRespondentsMatching(parameter.getContent(),"<"
														// ,opt.getCondition()
														// )) {
														// success++;
														// }
														//
														// }
													}
												}
											}
										}
									}
								}
							}
						}
						if ("LessThan".equals(opt.getSymbol())) {

							for (UploadFeed uploadFeed : fs) {
								if (!Util.isEmpty(uploadFeed.getReturnType()) && uploadFeed.getReturnType().equals("21")) {

								} else {
									String parametersStr = uploadFeed
											.getParametersStr();
									if (!Util.isEmpty(uploadFeed
											.getParametersStr())) {
										parameterList.clear();
										ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
												.parseArray(parametersStr,
														Parameter.class);
										if (!Util.isEmpty(tParameters)) {
											parameterList.addAll(tParameters);
											for (Parameter parameter : tParameters) {
												if (!Util.isEmpty(parameter
														.getContent())) {
													// 假如是数字
													if (Util.isFormat(parameter
															.getContent(), 9)
															&& Util.isFormat(
																	opt.getCondition(),
																	9)) {
														if (Float
																.parseFloat(parameter
																		.getContent()) < Float
																.parseFloat(opt
																		.getCondition())
																&& parameter
																		.getSid()
																		.equals(opt
																				.getQuestionindex())) {
															success++;
														}
													}
													// 日期
													else if (parameter
															.getContent()
															.indexOf("-") != -1
															&& parameter
																	.getContent()
																	.length() > 5) {
														if (parameter
																.getSid()
																.equals(opt
																		.getQuestionindex())) {
															try {
																if (Util.getDateCompare(
																		parameter
																				.getContent(),
																		opt.getCondition(),
																		">")) {
																	success++;
																}
															} catch (ParseException e) {
																e.printStackTrace();
															}
														}
													}
													// 文本格式
													else {
														// if(parameter.getSid().equals(opt.getQuestionindex())){
														// if
														// (Util.isRespondentsMatching(parameter.getContent(),
														// ">",opt.getCondition()
														// ))
														// {
														// success++;
														// }
														//
														// }
													}
												}
											}
										}
									}
								}

							}
						}
						if ("MoreEqual".equals(opt.getSymbol())) {
							for (UploadFeed uploadFeed : fs) {

								if (!Util.isEmpty(uploadFeed.getReturnType()) && uploadFeed.getReturnType().equals("21")) {

								} else {

									String parametersStr = uploadFeed
											.getParametersStr();
									if (!Util.isEmpty(uploadFeed
											.getParametersStr())) {
										parameterList.clear();
										ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
												.parseArray(parametersStr,
														Parameter.class);
										if (!Util.isEmpty(tParameters)) {
											parameterList.addAll(tParameters);
											for (Parameter parameter : tParameters) {
												if (!Util.isEmpty(parameter
														.getContent())) {
													// 假如是数字
													if (Util.isFormat(parameter
															.getContent(), 9)
															&& Util.isFormat(
																	opt.getCondition(),
																	9)) {
														if (Float
																.parseFloat(parameter
																		.getContent()) >= Float
																.parseFloat(opt
																		.getCondition())
																&& parameter
																		.getSid()
																		.equals(opt
																				.getQuestionindex())) {
															success++;
														}
													}
													// 日期
													else if (parameter
															.getContent()
															.indexOf("-") != -1
															&& parameter
																	.getContent()
																	.length() > 5) {
														if (parameter
																.getSid()
																.equals(opt
																		.getQuestionindex())) {
															try {
																if (Util.getDateCompare(
																		parameter
																				.getContent(),
																		opt.getCondition(),
																		"<=")) {
																	success++;
																}
															} catch (ParseException e) {
																e.printStackTrace();
															}
														}
													}
													// 文本格式
													else {
														// if(parameter.getSid().equals(opt.getQuestionindex())
														// ){
														// if
														// (Util.isRespondentsMatching(parameter.getContent(),
														// "<=",
														// opt.getCondition()))
														// {
														// success++;
														// }
														//
														// }
													}
												}
											}
										}
									}
								}
							}
						}
						if ("LessEqual".equals(opt.getSymbol())) {
							for (UploadFeed uploadFeed : fs) {
								if (!Util.isEmpty(uploadFeed.getReturnType()) && uploadFeed.getReturnType().equals("21")) {

								} else {
									String parametersStr = uploadFeed
											.getParametersStr();
									if (!Util.isEmpty(uploadFeed
											.getParametersStr())) {
										parameterList.clear();
										ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
												.parseArray(parametersStr,
														Parameter.class);
										if (!Util.isEmpty(tParameters)) {
											parameterList.addAll(tParameters);
											for (Parameter parameter : tParameters) {
												if (!Util.isEmpty(parameter
														.getContent())) {
													// 假如是数字
													if (Util.isFormat(parameter
															.getContent(), 9)
															&& Util.isFormat(
																	opt.getCondition(),
																	9)) {
														if (Float
																.parseFloat(parameter
																		.getContent()) <= Float
																.parseFloat(opt
																		.getCondition())
																&& parameter
																		.getSid()
																		.equals(opt
																				.getQuestionindex())) {
															success++;
														}
													}
													// 日期
													else if (parameter
															.getContent()
															.indexOf("-") != -1
															&& parameter
																	.getContent()
																	.length() > 5) {
														if (parameter
																.getSid()
																.equals(opt
																		.getQuestionindex())) {
															try {
																if (Util.getDateCompare(
																		parameter
																				.getContent(),
																		opt.getCondition(),
																		">=")) {
																	success++;
																}
															} catch (ParseException e) {
																e.printStackTrace();
															}
														}
													}
													// 文本格式
													else {
														// if(parameter.getSid().equals(opt.getQuestionindex())){
														// if
														// (Util.isRespondentsMatching(parameter.getContent(),">=",
														// opt.getCondition()))
														// {
														// success++;
														// }
														//
														// }
													}
												}
											}
										}
									}
								}
							}
						}
						// 包含
						if ("Include".equals(opt.getSymbol())) {
							for (UploadFeed uploadFeed : fs) {
								if (!Util.isEmpty(uploadFeed.getReturnType()) && uploadFeed.getReturnType().equals("21")) {

								} else {
									String parametersStr = uploadFeed
											.getParametersStr();
									if (!Util.isEmpty(uploadFeed
											.getParametersStr())) {
										parameterList.clear();
										ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON
												.parseArray(parametersStr,
														Parameter.class);
										if (!Util.isEmpty(tParameters)) {
											parameterList.addAll(tParameters);
											for (Parameter parameter : tParameters) {
												if (!Util.isEmpty(parameter
														.getContent())) {
													if (parameter
															.getSid()
															.equals(opt
																	.getQuestionindex())) {
														if (Util.isRespondentsMatching(
																parameter
																		.getContent(),
																"2",
																opt.getCondition())) {
															success++;
														}
													}
												}
											}
										}
									}
								}
							}
						}

					}
				} else {
				}

			} else {
				ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
				fs = ma.dbService.getAllQuotaUploadFeed(surveyId, ma.userId);
				if (0 < fs.size()) {
					for (Option opt : optionlist) {
						// 得到关联的index
						String questionindex = opt.getQuestionindex();
						// 根据index和项目ID得到答案集合
						// 计算拼串
						String myUuid = "(";
						for (int i = 0; i < fs.size(); i++) {
							if (!Util.isEmpty(fs.get(i).getReturnType()) && fs.get(i).getReturnType().equals("21")) {

							} else {
								UploadFeed f = fs.get(i);
								String uuid = f.getUuid();
								// 最后一个时候
								if (i == fs.size() - 1) {
									myUuid += "_UUID='" + uuid + "'";
								} else {
									myUuid += "_UUID='" + uuid + "' or ";
								}
							}
						}
						myUuid += ")";
						anlist = ma.dbService.getUserAllquotaAnswer(surveyId,
								questionindex, myUuid);// 条件中的要求的题目答案
						String[] optvalues = opt.getCondition().split(",");
						if (opt.getMatch().equals("all")) {
							for (Answer answer : anlist) {
								int count = 0;
								for (int i = 0; i < answer.getAnswerMapArr()
										.size(); i++) {
									String value = answer.getAnswerMapArr()
											.get(i).getAnswerValue();
									if (Util.isFormat(value, 10)) {
										for (String optvalue : optvalues) {
											if ((Integer.parseInt(value) + 1) == Integer
													.parseInt(optvalue)) {
												count++;
											}
										}
									}
								}
								if (count == optvalues.length) {
									answerlist.add(answer.uuid + "");
								}
							}
						} else {
							for (Answer answer : anlist) {
								int count = 0;
								for (int i = 0; i < answer.getAnswerMapArr()
										.size(); i++) {
									String value = answer.getAnswerMapArr()
											.get(i).getAnswerValue();
									if (Util.isFormat(value, 10)) {
										for (String optvalue : optvalues) {
											if ((Integer.parseInt(value) + 1) == Integer
													.parseInt(optvalue)) {
												count++;
											}
										}
									}
								}
								if (count > 0) {
									answerlist.add(answer.uuid + "");
								}
							}

						}
					}
				}

				Log.i(TAG, "optionlist==" + optionlist.size());
				Log.i(TAG, "optionlist==" + optionlist.toString());

				if (optionlist.size() > 1) {
					int count = 0;
					for (int i = 0; i < answerlist.size(); i++) {
						for (int j = 0; j < answerlist.size(); j++) {
							if (answerlist.get(i).equals(answerlist.get(j))) {
								count++;
							}
						}
						if (count >= optionlist.size()) {
							// 满足此条配额的所有条件
							excludelist.add(answerlist.get(i));
						}
						count = 0;
					}

					if (excludelist.size() >= 2) {
						for (String s : excludelist) {
							// frequency 过滤去重复的：在successlist集合中s的出现频率少于1才添加
							if (Collections.frequency(successlist, s) < 1)
								successlist.add(s);
						}
					}
				} else if (optionlist.size() == 1) {
					return answerlist.size();
				}

				return successlist.size();

			}
		}
		return success;
	}

	static class ViewHolder {

		private TextView tvQuotasuccess;// 配额编号
		private TextView tvQuotaName;// 配额名称
		private TextView tvQuotaText;// 配额/完成
		private TextView tvQuotains;// 配额说明
	}

	private class isSucsess extends
			AsyncTask<ArrayList<Option>, Void, ArrayList<Option>> {

		@Override
		protected ArrayList<Option> doInBackground(ArrayList<Option>... params) {
			ArrayList<Option> opList = params[0];
			if (!Util.isEmpty(opList)) {

			}

			return opList;

		}

	}

}
