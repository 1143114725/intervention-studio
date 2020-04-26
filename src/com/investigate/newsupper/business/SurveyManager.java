package com.investigate.newsupper.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.investigate.newsupper.activity.WelcomeActivity;
import com.investigate.newsupper.bean.Call;
import com.investigate.newsupper.bean.Question;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.SurveyQuestion;
import com.investigate.newsupper.db.DBService;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.FileUtil;
import com.investigate.newsupper.util.ThreadPoolUtils;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.Utilty;
import com.investigate.newsupper.util.XmlUtil;

import android.text.TextUtils;
import android.util.Log;

public final class SurveyManager {
	
	private static final String TAG = SurveyManager.class.getSimpleName();
	
	private static final String SURVEY_PATH = "Survey";
	
	private static final String getSurveyPath() {
		final String PATH = FileUtil.getAppExternalStorageDirectory() + "/" + SURVEY_PATH;
		File f = new File(PATH);
		if (!f.exists()) {
			f.mkdir();
		}
		return PATH;
	}
	
	/**
	 * 扫描本地Survey
	 */
	public static void scanLocalSurveys(final MyApp app) {
		/**
		 * 1、将zip包放入“SDCARD/DapSurvey/Survey”目录下；
		 * 2、扫描上述目录中“任意正整数.zip”格式的文件；
		 * 3、解压zip文件（所有的）到内部存储空间；
		 * 4、解析Survey的XML文件，并将问卷信息保存至数据库；
		 * 5、通知UI刷新；
		 */
		final String PATTERN = "^\\d+.zip$";
		ThreadPoolUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				File[] files = new File(getSurveyPath()).listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String filename) {
						if (TextUtils.isEmpty(filename)) {
							return false;
						}
						return Pattern.compile(PATTERN)
								.matcher(filename).matches();
					}
				});
				if (files == null) {
					return;
				}
				for (File file : files) {
					if (MyApp.DEBUG) {
						Log.d(TAG, "File name: " + file.getAbsolutePath() + ", userId: " + app.userId);
					}
					String surveyId = FileUtil.getFileSimpleName(file.getName());
					if (TextUtils.isEmpty(surveyId)) {
						continue;
					}
					boolean yes = false;
					try { 
						yes = Util.decompress(file.getAbsolutePath(),
								Util.getSurveyFilePath(MyApp.getAppContext(), surveyId), 
								surveyId, 
								null
								);
						if (yes) {	// 解压成功
							String xmlPath = Util.getSurveyXML(MyApp.getAppContext(), surveyId);
							FileInputStream fis = new FileInputStream(xmlPath);
							yes = fis != null;
							if (yes) {
								yes = false;
								SurveyQuestion sq = XmlUtil.getSurveyQuestion(fis, null);
								Survey s = app.dbService.getSurveyExsit(surveyId);
								final boolean isUserEmpty = TextUtils.isEmpty(app.userId);
								if (s == null) {
									s = new Survey();
									s.surveyId = surveyId;
									s.surveyTitle = sq.getSurveyTitle();
									if (!isUserEmpty) {
										s.userIdList = app.userId;
									}
									s.surveyEnable = 0;
									s.download = 1;
									s.offlineImport = Survey.YES;
									app.dbService.addSurvey(s);
								} else {
									if (TextUtils.isEmpty(s.userIdList)) {
										if (!isUserEmpty) {
											s.userIdList = app.userId;
										}
									} else {
										if (!isUserEmpty) {
											if (s.userIdList.indexOf(app.userId) < 0) {
												s.userIdList += "," + app.userId;
											}
										}
									}

									s.download = 1;
									s.surveyEnable = 0;
									s.offlineImport = Survey.YES;
									// 假如以前下载过了，就比较时间
									if (s.isDowned == 0) {
										s.isDowned = 1;// 告知这个问卷已经下载了
										String nowGeneratedTime = s.getGeneratedTime();// 现在
										String pastGeneratedTime = s.getGeneratedTime();// 原来
										long nowLongGeneratedTime = Util.getLongTime(nowGeneratedTime, 3);
										long pastLongGeneratedTime = Util.getLongTime(pastGeneratedTime, 3);
										// 假如现在的生成时间大于存入数据库的时间，证明有更新的了。
										if (MyApp.DEBUG) {
											Log.d(TAG, "nowLongGeneratedTime:" + nowLongGeneratedTime + ":pastLongGeneratedTime" + pastLongGeneratedTime);
										}
										if (nowLongGeneratedTime > pastLongGeneratedTime) {
											app.dbService.updateSurvey(s, 1);// 1代表不用更新提醒字段
										} else {
											app.dbService.updateSurvey(s, 0);// 0代表不用更新提醒字段
										}
									} else {
										app.dbService.updateSurvey(s, 0);// 0代表不用更新提醒字段
									}
								}
								ArrayList<Question> qs = sq.getQuestions();
								if (!Util.isEmpty(qs)) {
									for (int i = 0; i < qs.size(); i++) {
										Question q = qs.get(i);
										q.surveyId = s.surveyId;
										if (-1 != q.qOrder) {
											boolean qt = app.dbService.isQuestionExist(s.surveyId, q.qIndex);
											if (qt) {
												boolean u = app.dbService.updateQuestion(q);
												if (u) {
													if (MyApp.DEBUG) {
														Log.d(TAG, "" + q.qIndex + "更新成功.");
													}
												}
											} else {
												boolean b = app.dbService.addQuestion(q);
												System.out.println("grselectnum"+q.getgRSelectNum());
												System.out.println("getItemGR_Flag"+q.getItemGR_Flag());
												
												if (b) {
													if (MyApp.DEBUG) {
														Log.d(TAG, "" + q.qIndex + "插入成功.");
													}
												}
											}
										}
									}
								}
								if (yes) {
									HashMap<String, Integer> sMap = new HashMap<String, Integer>();
									sMap.put("testType", sq.getTestType());
									sMap.put("forceGPS", sq.getForceGPS());
									sMap.put("showQindex", sq.getShowQindex());
									app.dbService.surveyDownloaded(s.surveyId, 
											(null == sq) ? -1 : sq.getEligible(), "",
											(null == sq) ? 1 : sq.getshowpageStatus(),
											(null == sq) ? 0 : sq.getAppModify(),
											(null == sq) ? 0 : sq.getAppPreview(),
											s.getGeneratedTime(), 
											(null == sq) ? null : sq.getBackPassword(),
											(null == sq) ? null : sq.getVisitPreview(),
											(null == sq) ? 0 : sq.getAppAutomaticUpload(),
											(null == sq) ? 0 : sq.getOpenGPS(), 
											(null == sq) ? 0 : sq.getTimeInterval(),
											s.getPhotoSource(),s.getBackPageFlag(), sMap);
								}
							}
							yes = true;
						}
					} catch (Exception e) {
						if (MyApp.DEBUG) {
							e.printStackTrace();
						}
					} finally {
						if (yes) {
							file.renameTo(new File(getSurveyPath(), surveyId + "_scaned.zip"));
						}
						if (MyApp.DEBUG) {
							Log.d(TAG, surveyId + (yes ? "解析成功" : "解析失败"));
						}
					}
				}
			}
		});

	}
	
}
