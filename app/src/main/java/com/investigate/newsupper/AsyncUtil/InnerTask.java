package com.investigate.newsupper.AsyncUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.bean.InnerPanel;
import com.investigate.newsupper.bean.OpenStatus;
import com.investigate.newsupper.bean.Parameter;
import com.investigate.newsupper.bean.ParameterInnerPanel;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.NetService;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;
import com.investigate.newsupper.view.Toasts;

// 内部名单方法
public class InnerTask extends AsyncTask<Void, Integer, Boolean> {
	private String authorId;
	private String userId;
	private Survey survey;
	
	private Context mActivity;
	private MyApp ma;
	
	public InnerTask(String _authorId, String _userId, Survey _survey,Context mActivity,MyApp ma) {
		this.authorId = _authorId;
		this.userId = _userId;
		this.survey = _survey;
		this.mActivity = mActivity;
		this.ma = ma;

	}
	

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			System.out.println("调用内部名单方法");
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("AuthorID", this.authorId);
			hm.put("SurveyID", survey.surveyId);
			InputStream inStream = NetService.openUrl(Cnt.INNER_URL, hm, "GET");
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
					System.out.println("feedid是----"+ip.getFeedId());
					spList.add(ip.getFeedId());
					String str = XmlUtil.paserInnerPanel2Json(ip);
					// System.out.println("inner_json==="+str);
					if (ma.dbService.isFeedExist(survey.surveyId,
							ip.getFeedId())) {
						// 假如服务器上有id 本地上也有id的更新。引用受访者参数
						// System.out.println("更新");
						ma.dbService.updateInnerUploadFeed(survey.surveyId,
								ip.getPanelID(), str, ip.getFeedId(),
								pip.getParametersStr());
					} else {
						// 假如服务器上有id 本地上没有id的增加。
						String uuid = UUID.randomUUID().toString();
						String path = Util.getXmlPath(mActivity, survey.surveyId);
						// 增加pid 命名规则
						String AUTHORID = ma.cfg.getString(Cnt.AUTHORID, "");
						String name = Util.getXmlName(AUTHORID, userId, survey.surveyId,
										uuid, ip.getPanelID(), content);
						ma.dbService.addInnerUploadFeed(ip.getFeedId(), userId,
								survey.surveyId, uuid,
								System.currentTimeMillis(), path, name,
								survey.visitMode, str, ip.getPanelID(),
								pip.getParametersStr());
					}
				}
				// 进度更新
				publishProgress((i + 1), os.getIps().size());
			}
			ArrayList<String> dbList = ma.dbService.getListBySurveyId(
					survey.surveyId, ma.userId);
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
			
			// 命名规则更改
			ma.dbService.updateSurveyOpenStatus(survey.surveyId,
					os.getParameter1(), os.getParameter2(), os.getParameter3(),
					os.getParameter4(), os.getParameterName());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			ma.dbService.updateSurveyInnerDone(survey.surveyId);
//			Toasts.makeText(mActivity, R.string.survey_add_complete,
//					Toast.LENGTH_SHORT).show();
			
		} else {
			Toasts.makeText(mActivity, R.string.inner_failure, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
}
