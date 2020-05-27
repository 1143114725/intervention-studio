package com.investigate.newsupper.util;

import java.util.ArrayList;

import com.investigate.newsupper.activity.MyQuotaActivity;
import com.investigate.newsupper.bean.Answer;
import com.investigate.newsupper.bean.Option;
import com.investigate.newsupper.bean.Quota;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.MyApp;

import android.util.Log;
import android.view.LayoutInflater;

/**
 * 配额查询的工具类
 * @author EraJi
 *
 */
public class QuotaSucessUtil {

	public final static String TAG = "QuotaAdapter";

	public static int getSucessQuota(MyApp ma,ArrayList<Option> optionlist,
			String surveyId) {
		ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
		fs = ma.dbService.getAllQuotaUploadFeed(surveyId, ma.userId);
		//已经满足所有条件的
		int countsize = 0;
		if (0 < fs.size()) {
			for (int i = 0; i < fs.size(); i++) {
				UploadFeed feed = fs.get(i);
				// 满足了及格条件
				int optsize = 0;
				// 此次条件是否满足
				for (int j = 0; j < optionlist.size(); j++) {
					boolean isopt = false;
					
					Option opt = optionlist.get(j);
					// 得到关联的index
					String questionindex = opt.getQuestionindex();

					String myUuid = "";
					String uuid = feed.getUuid();
					myUuid += "_UUID='" + uuid + "'";
					// 初始化答案集合变量
					ArrayList<Answer> anlist = new ArrayList<Answer>();
					anlist = ma.dbService.getUserAllquotaAnswer(surveyId,
							questionindex, myUuid);// 条件中的要求的题目答案
					String[] optvalues = opt.getCondition().split(",");
					for (int m = 0; m < anlist.size(); m++) {
						Answer answer = anlist.get(m);
						int count = 0;
						for (int k = 0; k < answer.getAnswerMapArr().size(); k++) {
							String value = answer.getAnswerMapArr().get(k)
									.getAnswerValue();
							if (Util.isFormat(value, 10)) {
								for (String optvalue : optvalues) {
									if ((Integer.parseInt(value) + 1) == Integer
											.parseInt(optvalue)) {
										// count++;
										isopt = true;
									} else {
										isopt = false;
									}
								}
							}
						}
					}
					if (isopt) {
						//如果这个条件满足了size就++
						optsize++;
						if (optsize == optionlist.size()) {
							countsize++;
							optsize=0;
							isopt = false;
						}
					}
				}
			}
		}
		return countsize;
	}
}
