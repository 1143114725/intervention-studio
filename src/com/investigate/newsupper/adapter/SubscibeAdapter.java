package com.investigate.newsupper.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.SubscibeActivity;
import com.investigate.newsupper.bean.Call;
import com.investigate.newsupper.bean.Data;
import com.investigate.newsupper.bean.HttpBean;
import com.investigate.newsupper.bean.InnerPanel;
import com.investigate.newsupper.bean.Intervention;
import com.investigate.newsupper.bean.OpenStatus;
import com.investigate.newsupper.bean.Parameter;
import com.investigate.newsupper.bean.ParameterInnerPanel;
import com.investigate.newsupper.bean.QGroup;
import com.investigate.newsupper.bean.Question;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.SurveyQuestion;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.TextSizeManager;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.NetService;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;
import com.investigate.newsupper.view.Toasts;

public class SubscibeAdapter extends BaseAdapter {

	private final String TAG;

	private ArrayList<Survey> ss;
	private SubscibeActivity context;
	private MyApp ma;
	private ArrayList<String> list = new ArrayList<String>();

	@Override
	public int getCount() {
		return ss.size();
	}

	public SubscibeAdapter(SubscibeActivity context, ArrayList<Survey> list, MyApp ma, String TAG) {
		super();
		this.TAG = TAG;
		this.ss = list;
		this.context = context;
		this.ma = ma;
	}

	@Override
	public Object getItem(int position) {
		return ss.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void refresh(ArrayList<Survey> surveys) {
		if (!Util.isEmpty(surveys)) {
			if (!Util.isEmpty(ss)) {
				ss.clear();
				ss.addAll(surveys);
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Survey s = ss.get(position);
		ViewHolder vh;
		convertView = (View) s.getTag();
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.subscibe_item, null);
			vh = new ViewHolder();
			vh.tvSurveyTitle = (UITextView) convertView.findViewById(R.id.textView1);
			TextSizeManager.getInstance().addTextComponent(TAG, vh.tvSurveyTitle);
			vh.tvLastTime = (UITextView) convertView.findViewById(R.id.textView4);
			TextSizeManager.getInstance().addTextComponent(TAG, vh.tvLastTime);
			vh.tvStartTime = (UITextView) convertView.findViewById(R.id.textView2);
			TextSizeManager.getInstance().addTextComponent(TAG, vh.tvStartTime);
			vh.tvContent = (UITextView) convertView.findViewById(R.id.textView3);
			TextSizeManager.getInstance().addTextComponent(TAG, vh.tvContent);
			vh.subscibe_ll = (LinearLayout) convertView.findViewById(R.id.subscibe_ll);
			vh.iv_ll = (LinearLayout) convertView.findViewById(R.id.iv_ll);
			vh.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
			vh.pbDownload = (ProgressBar) convertView.findViewById(R.id.author_list_progress);
			vh.download_state = (UITextView) convertView.findViewById(R.id.download_state);
			TextSizeManager.getInstance().addTextComponent(TAG, vh.download_state);

			convertView.setTag(vh);
			Drawable drawable = context.getResources().getDrawable(R.drawable.dark_gray_background_subscibe);
			if (position % 2 == 1) {
				vh.subscibe_ll.setBackgroundDrawable(drawable);
			}
		
			if (null != s) {
				//测试经纬度
				if (!Util.isEmpty(s.surveyTitle)) {
					if (1 == s.openStatus) {
						String tempStr = s.surveyTitle + context.getString(R.string.inner_name);
						SpannableStringBuilder style = new SpannableStringBuilder(tempStr);
						style.setSpan(new ForegroundColorSpan(Color.RED), s.surveyTitle.length(), tempStr.length(),
								Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // 设置指定位置文字的颜色
						vh.tvSurveyTitle.setText(style);
					} else {
						vh.tvSurveyTitle.setText(s.surveyTitle);
					}
				}
				
				
				
				
				
				// 图标更换
				if (s.isDowned == 1) {
					vh.iv_add.setImageResource(R.drawable.ic_checkmark_holo_light);

				} else if (s.isDowned == 2) {
					vh.iv_add.setVisibility(View.GONE);
					vh.pbDownload.setVisibility(View.VISIBLE);
					vh.download_state.setVisibility(View.GONE);//隐藏状态文本
				}
				//这个项目我下载过 显示最近更新时间
				if (null != s && 1 == s.isDowned) {
					if (!Util.isEmpty(s.getLastGeneratedTime())) {
						vh.tvLastTime.setVisibility(View.VISIBLE);
						vh.tvLastTime.setText("最近更新：" + s.getLastGeneratedTime());
						s.isUpdate = 1;
						//用时间判断是否要更新  显示图标
						if (!s.getLastGeneratedTime().equals(s.getGeneratedTime())) {
							vh.download_state.setText(R.string.download_survey_state3);
							vh.iv_add.setImageResource(R.drawable.ic_checkmark_holo_light_update);
						} else {
							vh.download_state.setText(R.string.download_survey_state5);
						}
						
					} else {
						vh.tvLastTime.setVisibility(View.GONE);
					}

				} else {
					s.isUpdate = 0;
					vh.tvLastTime.setVisibility(View.GONE);
				}
				
				
				
				
				
				
				
				if (!Util.isEmpty(s.getGeneratedTime())) {
					vh.tvStartTime.setText("最新发布：" + s.getGeneratedTime());
				}
				//详细说明
				if (!Util.isEmpty(s.getWord())) {
					Spannable sp = (Spannable) Html.fromHtml(s.getWord());
					vh.tvContent.setText(sp.toString());
				} else {
					vh.tvContent.setText(R.string.no_explain);
				}
				vh.iv_ll.setOnClickListener(new DownloadListenner(s, vh.pbDownload, vh.iv_add, vh.download_state,vh.iv_ll));
			}
			s.setTag(convertView);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	private class DownloadListenner implements OnClickListener {
		private Survey s;
		private ProgressBar pb;
		private ImageView iv;
		private UITextView tv;
		private LinearLayout iv_ll;

		public DownloadListenner(Survey survey, ProgressBar progressBar, ImageView iv_add, UITextView tv,
				LinearLayout iv_ll) {
			this.s = survey;
			this.pb = progressBar;
			this.iv = iv_add;
			this.tv = tv;
			this.iv_ll = iv_ll;
		}

		@Override
		public void onClick(View v) {
			iv_ll.setClickable(false);
			context.show();
			isDownloading = true;
			if (NetUtil.checkNet(context)) {
				s.isDowned = 2;
				new AuthorDownloadTask(s, pb, iv, tv, iv_ll).execute();
//				isDownloading = false;
//				context.dismiss();
			} else {
				isDownloading = false;
				context.dismiss();
				iv_ll.setClickable(true);
				Toasts.makeText(context, R.string.exp_net, Toast.LENGTH_LONG)
						.show();
			}
		}

	}

	public boolean isDownloading;
	private class AuthorDownloadTask extends AsyncTask<Void, Integer, Boolean> {
		private Survey s;
		private ProgressBar pb;
		private ImageView iv;
		private UITextView tv;
		SurveyQuestion sq;
		private LinearLayout iv_ll;

		public AuthorDownloadTask(Survey survey, ProgressBar progressBar, ImageView iv, UITextView tv,LinearLayout iv_ll) {
			this.s = survey;
			this.pb = progressBar;
			this.iv = iv;
			this.tv = tv;
			this.iv_ll=iv_ll;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean yes = false;
			try {
				Log.i("zrl1", s.downloadUrl + "downloadUrl:");
				HttpBean hb = NetService.obtainHttpBean(s.downloadUrl + "?" + new Random().nextInt(1000), null, "GET");
				if (200 == hb.code) {
					
					File file = new File(Util.getSurveySaveFilePath(context), s.surveyId + ".zip");
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[2048];
					int len = 0;
					int currentSize = 0;
					while ((len = hb.inStream.read(buffer)) != -1) {//下载的压缩包保存到文件
						fos.write(buffer, 0, len);
						currentSize += len;
						publishProgress(currentSize, hb.contentLength);
					}

					fos.flush();
					fos.close();
					hb.inStream.close();
					// String absPath = Util.getSurveyFileAbsolutePath(mContext,
					// s.surveyId);
					//解压下载到的压缩文件
					
					
					Log.i("zippath", "xxx="+file.getAbsolutePath()+"yyy="+Util.getSurveyFilePath(context, s.surveyId));
					
					yes = Util.decompress(file.getAbsolutePath(), Util.getSurveyFilePath(context, s.surveyId),
							s.surveyId, new Call() {

								@Override
								public void updateProgress(int curr, int total) {
									publishProgress(curr, total);
								}
							});
					if (yes) {
						file.delete();
						/**
						 * 假如是原生模式访问则解析原生XML问卷
						 */
						if (true) {
							String xml = Util.getSurveyXML(context, s.surveyId);
							FileInputStream inStream = new FileInputStream(xml);
							// 数据字典
							ArrayList<String> classIdList = new ArrayList<String>();

							if (null != inStream) {
								sq = XmlUtil.getSurveyQuestion(inStream, new Call() {
									@Override
									public void updateProgress(int curr, int total) {
									}
								});
								ArrayList<Question> qs = sq.getQuestions();
								// 显示类型
								// 数据字典
								classIdList = sq.getClassId();

								if (!Util.isEmpty(qs)) {
									/**
									 * 废除的题目
									 */
									ma.dbService.deleteQuestion(s.surveyId);
									for (int i = 0; i < qs.size(); i++) {
										Question q = qs.get(i);
										// q.qSign=1;//模拟单题签名
										q.surveyId = s.surveyId;
										if (-1 != q.qOrder) {
											boolean b = ma.dbService.addQuestion(q);
											if (b) {
												ma.dbService.updateAnswerOrder(q);
												String c = q.getCheckRepeat();
												System.out.println("" + q.qIndex + "插入成功."+c);
											}
										} else {
											/**
											 * 删除答案
											 */
											ma.dbService.deleteAnswer(q.surveyId, q.qIndex + "");
										}
										publishProgress((i + 1), qs.size());
									}

									/**
									 * 将问卷中所有的题组随机置空
									 */
									ma.dbService.updateQuestionGroup2Null(s.surveyId);

									/**
									 * 题组随机json字符串入库
									 */
									if (!Util.isEmpty(sq.getQgs())) {
										for (QGroup qg : sq.getQgs()) {
											ma.dbService.updateQuestionGroup(s.surveyId, qg.getRealIndex(),
													XmlUtil.parserQGroup2Json(qg));
										}
									}

									/**
									 * //逻辑跳转解析字符json串入库
									 */
									if (null != sq.getLogicList()) {
										ma.dbService.updateLogicListBySurvey(s.surveyId,
												XmlUtil.parserLogicList2Json(sq.getLogicList()));
									} else {
										ma.dbService.updateLogicListBySurvey(s.surveyId, "");
									}
								}
							}

							/**
							 * 先将干预的字符串标志置为空， 这样为了防止干预有改动
							 */
							ma.dbService.updateAllIntervention2Null(s.surveyId);

							/**
							 * 获取干预文件的绝对路径
							 */
							xml = Util.getSurveyIntervention(context, s.surveyId);
							File iiFile = new File(xml);
							if (iiFile.exists()) {
								ArrayList<Intervention> iis = XmlUtil.getInterventionList(iiFile, new Call() {
									@Override
									public void updateProgress(int curr, int total) {
										publishProgress(curr, total);
									}
								});
								for (int i = 0; i < iis.size(); i++) {
									// String json = ;
									Intervention ii = iis.get(i);
									ma.dbService.updateQuestionIntervention(s.surveyId, ii.getIndex(),
											XmlUtil.parserIntervention2Json(ii));
									publishProgress((i + 1), iis.size());
								}

								/**
								 * 处理完干预的xml后, 将其删除, 以免对下一次更改干预的xml造成影响
								 */
								iiFile.delete();
							}
							// 数据字典不为空
							if (!Util.isEmpty(classIdList)) {
								for (String classId : classIdList) {
									// 数据字典
									HashMap<String, Object> hmData = new HashMap<String, Object>();
									hmData.put(Cnt.USER_ID, ma.userId);
									hmData.put(Cnt.USER_PWD, ma.userPwd);
									hmData.put("classId", classId);
									InputStream inStreamData = NetService.openUrl(Cnt.DATA_URL, hmData, "GET");
									List<Data> dataList = XmlUtil.parseData(inStreamData);
									for (int z = 0; z < dataList.size(); z++) {
										Data data = dataList.get(z);
										if (!ma.dbService.IsExistData(data.getClassId())) {
											// 假如不存在就增加
											ma.dbService.addData(data);
										} else {
											// 假如存在就更新
											ma.dbService.updateData(data);
										}
									}
								}
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return yes;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				pb.setVisibility(View.GONE);
				iv.setVisibility(View.VISIBLE);
				HashMap<String, Integer> sMap = new HashMap<String, Integer>();
				sMap.put("forceGPS", sq.getForceGPS());
				sMap.put("testType", sq.getTestType());
				sMap.put("showQindex", sq.getShowQindex());
				
				ma.dbService.surveyDownloaded(s.surveyId, (null == sq) ? -1 : sq.getEligible(), "",
						(null == sq) ? 1 : sq.getshowpageStatus(), (null == sq) ? 0 : sq.getAppModify(),
						(null == sq) ? 0 : sq.getAppPreview(), s.getGeneratedTime(),
						(null == sq) ? null : sq.getBackPassword(), (null == sq) ? null : sq.getVisitPreview(),
						(null == sq) ? 0 : sq.getAppAutomaticUpload(), (null == sq) ? 0 : sq.getOpenGPS(),
						(null == sq) ? 0 : sq.getTimeInterval(),sq.getPhotoSource(),  sq.getBackPageFlag(),sMap);
				// 图标更换
				s.isDowned = 1;
				iv.setImageResource(R.drawable.ic_checkmark_holo_light);
				if (1 == s.isUpdate) {
					tv.setText(R.string.download_survey_state4);
				} else {
					tv.setText(R.string.download_survey_state2);
				}
					// 内部名单开始
					if (null == ma.cfg) {
						ma.cfg = new Config(context);
					}
					if (Util.isEmpty(ma.userId)) {
						ma.userId = ma.cfg.getString("UserId", "");
					}
					String authorId = ma.cfg.getString("authorId", "");

					if (Util.isEmpty(ma.userId) || Util.isEmpty(authorId)) {
						Toasts.makeText(context, R.string.app_data_invalidate,
								Toast.LENGTH_LONG).show();
						return;
					}
					
						new InnerTask(authorId, ma.userId, s, iv, pb, iv_ll)
						.execute();
					
				} else {
					iv_ll.setClickable(true);
					isDownloading = false;
					context.dismiss();
					Toasts.makeText(context, R.string.survey_add_complete,
							Toast.LENGTH_SHORT).show();
				}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			for (int i = 0; i < values.length; i++) {
				System.out.println("---+++values["+i+"]"+values[i]);
			}
			if (0 != values[1]) {
				pb.setProgress((int) (values[0] * 1000) / values[1]);
			}
		}

		@Override
		protected void onPreExecute() {
			iv.setVisibility(View.GONE);
			pb.setVisibility(View.VISIBLE);
			pb.setProgress(0);
			super.onPreExecute();
		}
	};

	// 内部名单方法
	private final class InnerTask extends AsyncTask<Void, Integer, Boolean> {

		private String authorId;
		private String userId;
		private Survey survey;
		private ImageView iv;
		private ProgressBar pb;// 进度条
		private LinearLayout iv_ll;
		public InnerTask(String _authorId, String _userId, Survey _survey, ImageView iv, ProgressBar pb,LinearLayout iv_ll) {
			this.authorId = _authorId;
			this.userId = _userId;
			this.survey = _survey;
			this.iv = iv;
			this.pb = pb;
			this.iv_ll=iv_ll;
			
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			BaseLog.w("InnerTask  doInBackground");
			// http://www.dapchina.cn/newsurvey/alisoft/DownloadUser.asp?AuthorID=1514&SurveyID=3076
			try {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("AuthorID", this.authorId);
				hm.put("SurveyID", survey.surveyId);
				InputStream inStream = NetService.openUrl(Cnt.INNER_URL, hm, "GET");
				// InputStream inStream =mContext.getAssets().open("ceshi.xml");
				OpenStatus os = XmlUtil.ParserInnerPanelList(inStream);
				// 遍历下载的内部名单panel号
				ArrayList<String> spList = new ArrayList<String>();
				for (int i = 0; i < os.getIps().size(); i++) {
					InnerPanel ip = os.getIps().get(i);
					// 引用受访者参数
					ParameterInnerPanel pip = os.getParameterIps().get(i);
					ArrayList<Parameter> parameters = pip.getParameters();
					pip.setParameters(parameters);
					// 引用受访者参数结束
					// 命名规则开始
					// 不为空判断
					String content = "";
					if (!Util.isEmpty(os.getParameterName())) {
						for (Parameter parameter : parameters) {
							if (parameter.getSid().equals(os.getParameterName())) {
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
					if (!Util.isEmpty(ip.getFeedId())) {
						spList.add(ip.getFeedId());
						String str = XmlUtil.paserInnerPanel2Json(ip);
						// System.out.println("inner_json==="+str);
						if (ma.dbService.isFeedExist(survey.surveyId, ip.getFeedId())) {
							// 假如服务器上有id 本地上也有id的更新。引用受访者参数
							// System.out.println("更新");
							ma.dbService.updateInnerUploadFeed(survey.surveyId, ip.getPanelID(), str, ip.getFeedId(),
									pip.getParametersStr());
						} else {
							// 假如服务器上有id 本地上没有id的增加。
							String uuid = UUID.randomUUID().toString();
							String path = Util.getXmlPath(context, survey.surveyId);
							// 增加pid 命名规则
							String AUTHORID = ma.cfg.getString(Cnt.AUTHORID, "");
							String name = Util.getXmlName(AUTHORID,userId, survey.surveyId, uuid, ip.getPanelID(), content);
							// System.out.println("增加 feedId=" + ip.getFeedId()
							// + ", uuid=" + uuid + ", path=" + path + ", name="
							// + name + ", userId=" + userId + ",
							// survey.surveyId=" + survey.surveyId);
							ma.dbService.addInnerUploadFeed(ip.getFeedId(), userId, survey.surveyId, uuid,
									System.currentTimeMillis(), path, name, survey.visitMode, str, ip.getPanelID(),
									pip.getParametersStr());
						}
					}
					// 进度更新
					publishProgress((i + 1), os.getIps().size());
				}
				ArrayList<String> dbList = ma.dbService.getListBySurveyId(survey.surveyId, ma.userId);
				/**
				 * 判断服务器上没有 本地上有这个panelid ,剩下的dblist就是剩下的服务器没有的。该删除的
				 * 把查出来的panelId删除(没数据的删除，有数据的不删除?但传不传,不传)
				 */
				for (int i = dbList.size() - 1; i >= 0; i--) {
					String temp = dbList.get(i).split(":::")[0];
					for (int j = 0; j < spList.size(); j++) {
						if (temp.equals(spList.get(j))) {
							dbList.remove(i);
							break;
						}
					}
				}
				/**
				 * 遍历数据,查出指定的路径xml文件。假如这个文件存在。设置为giveup为2。不存在的话giveup为1。
				 */
				// if (!Util.isEmpty(dbList)) {
				// for(int z=0;z<dbList.size();z++){
				// String deleteDB=dbList.get(z);
				// String panelId = deleteDB.split(":::")[0];
				// String path = deleteDB.split(":::")[1];
				// File file = new File(path);
				// int giveUp = 0;
				// // 存在还能做
				// if (file.exists()) {
				// giveUp = 2;
				// ma.dbService.updateGiveUpByPanelId(panelId, survey.surveyId,
				// giveUp);
				// }
				// // 不存在设置为放弃,查不出来
				// else {
				// giveUp = 1;
				// ma.dbService.updateGiveUpByPanelId(panelId, survey.surveyId,
				// giveUp);
				// }
				// //进度更新
				// publishProgress((z + 1), dbList.size());
				// }
				// }
				// 命名规则更改
				ma.dbService.updateSurveyOpenStatus(survey.surveyId, os.getParameter1(), os.getParameter2(),
						os.getParameter3(), os.getParameter4(), os.getParameterName());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPreExecute() {
			BaseLog.w("InnerTask  onPreExecute");
			iv.setVisibility(View.GONE);
			pb.setVisibility(View.VISIBLE);
			pb.setProgress(0);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			BaseLog.w("InnerTask  onPostExecute++"+result);
			if (result) {
				ma.dbService.updateSurveyInnerDone(survey.surveyId);
				iv.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE);
				Toasts.makeText(context, R.string.survey_add_complete, Toast.LENGTH_SHORT).show();
			} else {
				iv.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE);
				Toasts.makeText(context, R.string.inner_failure, Toast.LENGTH_LONG).show();
			}
			iv_ll.setClickable(true);
			isDownloading = false;
			context.dismiss();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			
			super.onProgressUpdate(values);
			BaseLog.w("InnerTask  onProgressUpdate++"+values);
			if (0 != values[1]) {
				pb.setProgress((int) (values[0] * 1000) / values[1]);
			}
		}
	}

	static class ViewHolder {
		/**
		 * 项目名称
		 */
		private UITextView tvSurveyTitle;
		/**
		 * 开始时间
		 */
		private UITextView tvStartTime;
		/**
		 * 上次更新时间
		 */
		private UITextView tvLastTime;
		/**
		 * 详细说明
		 */
		private UITextView tvContent;
		/**
		 * 颜色布局
		 */
		private LinearLayout subscibe_ll;
		/**
		 * 添加订阅
		 */
		private LinearLayout iv_ll;
		/**
		 * 下载进度
		 */
		private ProgressBar pbDownload;
		/**
		 * 添加按钮
		 */
		private ImageView iv_add;
		/**
		 * 下载状态
		 */
		private UITextView download_state;
	}

}
