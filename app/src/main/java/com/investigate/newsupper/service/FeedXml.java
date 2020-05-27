package com.investigate.newsupper.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlSerializer;

import com.investigate.newsupper.bean.Answer;
import com.investigate.newsupper.bean.AnswerMap;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.util.Util;

import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.util.Xml;

/**
 * 拼接XML文件类
 */
public class FeedXml {

	public FeedXml() {

	}

	/**
	 * 将一份问卷拼接成xml
	 * 
	 * @param hidMap
	 *            hid节点所需的内容
	 * @param aswList
	 *            答案集合
	 * @param versionInfo
	 * @param feeds
	 *            文件集合
	 * @return 命名规则 sid pid name
	 */
	public String write2AnswerString(
			HashMap<String, String> hidMap, //
			ArrayList<Answer> aswList, //
			ArrayList<UploadFeed> records, //
			String dateList,
			String pointList, //
			String pleaceList, String sid, String pid, String name,
			VersionInfo versionInfo,String isbaidubos,String uploadtype) {
		// StringBuilder sb = new StringBuilder("");
		StringWriter writer = new StringWriter();
		try {
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			/**
			 * 答卷的开始节点
			 */
			serializer.startTag("", "response");
			
			serializer.startTag("", "hidden");

			
			if (null != hidMap && !hidMap.isEmpty()) {
				Iterator<Entry<String, String>> iter = hidMap.entrySet()
						.iterator();
				while (iter.hasNext()) {
					/**
					 * 拼接hidden中的所有节点
					 */
					Map.Entry<String, String> entry = iter.next();
					String key = entry.getKey();
					String value = entry.getValue();
					serializer.startTag("", "answer");
					serializer.startTag("", "name");
					serializer.text(key);
					serializer.endTag("", "name");
					serializer.startTag("", "value");
					if (!Util.isEmpty(value)) {
						serializer.text(value);
					}
					serializer.endTag("", "value");
					serializer.endTag("", "answer");
				}
			}
			serializer.endTag("", "hidden");

			if (!Util.isEmpty(records)) {
				/**
				 * 写PNG|MP3
				 */
				serializer.startTag("", "files");
				for (UploadFeed record : records) {
					System.out
							.println("++++++++++++++++++++++++++++++++++++++++++"
									+ record.getName()
									+ "+++++++++++++"
									+ record.getName().lastIndexOf("_")
									+ 1
									+ "+++++++++++++++++"
									+ record.getName().lastIndexOf("."));
					String checkString = record.getName().substring(
							record.getName().lastIndexOf("_") + 1,
							record.getName().lastIndexOf("."));
					if ("thumbnail".equals(checkString)) {
					} else {
						if (Cnt.FILE_TYPE_PNG == record.getType()) {
							serializer.startTag("", "photo");
						} else if (Cnt.FILE_TYPE_MP3 == record.getType()) {
							serializer.startTag("", "record");
						}else if (Cnt.FILE_TYPE_HIDE_PNG == record.getType()) {
							serializer.startTag("", "photo");
						} 
						// 摄像视频节点。
						else if (Cnt.FILE_TYPE_MP4 == record.getType()) {
							serializer.startTag("", "video");
						}
						if (!Util.isEmpty(record.getQuestionId())) {
							serializer.attribute("", "questionID",
									record.getQuestionId());
						} else {
							serializer.attribute("", "questionID", "");
						}
						serializer.attribute("", "startDate",
								Util.getTime(record.getStartTime(), 0));
						serializer.attribute("", "regDate",
								Util.getTime(record.getRegTime(), 0));
						serializer.attribute("", "size",
								String.valueOf(record.getSize()));
						// 命名规则开始
						String[] fileStr = name.split("_");
						String date = fileStr[2];
						// 不能解析 原来的第三位是日期 ，判断最早的访问专家
						if (Util.getLongTime(date, 5) == 0) {
							String newDate = fileStr[3];
							// 判断ipsos的,假如原先第4位不是日期，就是最新的
							if (Util.getLongTime(newDate, 5) == 0) {
								serializer.text(sid + File.separator
										+ record.getFeedId() + File.separator
										+ record.getName());
							} else {
								serializer.text(record.getName());
							}
						} else {
							serializer.text(record.getName());
						}
						// 命名规则结束
						if (Cnt.FILE_TYPE_PNG == record.getType()) {
							serializer.endTag("", "photo");
						} else if (Cnt.FILE_TYPE_MP3 == record.getType()) {
							serializer.endTag("", "record");
						} else if (Cnt.FILE_TYPE_HIDE_PNG == record.getType()) {
							serializer.endTag("", "photo");
						}
						
						
						
						// 摄像视频结束节点。
						else if (Cnt.FILE_TYPE_MP4 == record.getType()) {
							serializer.endTag("", "video");
						}
					}
				}
				serializer.endTag("", "files");
			}

			if (!Util.isEmpty(aswList)) {
				for (Answer asw : aswList) {
					serializer.startTag("", "question");
					serializer.attribute("", "index",
							String.valueOf(asw.qIndex));
					if (!Util.isEmpty(asw.getAnswerMapArr())) {
						for (AnswerMap am : asw.getAnswerMapArr()) {
							if ((null != am)
									&& !Util.isEmpty(am.getAnswerValue())) {
								String anValue = am.getAnswerValue();

								serializer.startTag("", "answer");
								serializer.attribute("", "type", "item");
								serializer.startTag("", "name");
								serializer.text(am.getAnswerName());
								serializer.endTag("", "name");
								serializer.startTag("", "value");
								if (0 == anValue.indexOf("sort")) {
									serializer.text(anValue.substring(4));
								} else {
									serializer.text(am.getAnswerValue());
								}
								serializer.endTag("", "value");
								serializer.endTag("", "answer");
							}
						}
					}
					serializer.endTag("", "question");
				}
			}
			/**
			 * 答卷的结尾节点
			 */

			/**
			 * 时间列表
			 */
			if (!Util.isEmpty(dateList)) {
				/**
				 * 将最后一个分号去掉
				 */
				String list = dateList.substring(0, dateList.length() - 1);
				if (0 < list.length()) {
					/**
					 * 获取每一次答卷的开始时间和结束时间
					 */
					String[] dateTime = list.split(";");
					serializer.startTag("", "times");
					for (String day : dateTime) {
						String[] datetime = day.split(",");
						serializer.startTag("", "datetime");

						if (Util.isEmpty(datetime)) {
							serializer.attribute("", "startDate", "");
							serializer.attribute("", "regtDate", "");
						} else if (2 == datetime.length) {
							serializer.attribute("", "startDate", datetime[0]);
							serializer.attribute("", "regtDate", datetime[1]);
						} else {
							serializer.attribute("", "startDate", "");
							serializer.attribute("", "regtDate", "");
						}

						serializer.text("");
						serializer.endTag("", "datetime");
					}
					serializer.endTag("", "times");
				}
			}

			/**
			 * 经纬度列表
			 */
			if (!Util.isEmpty(pointList)) {
				/**
				 * 将最后一个分号去掉
				 */
				String list = pointList.substring(0, pointList.length() - 1);
				if (0 < list.length()) {
					/**
					 * 获取每一次答卷的开始时间和结束时间
					 */
					String[] points = list.split(";");
					serializer.startTag("", "points");
					for (String point : points) {
						String[] p = point.split(",");
						serializer.startTag("", "point");
						if (Util.isEmpty(p)) {
							serializer.attribute("", "lat", "");
							serializer.attribute("", "lng", "");
						} else if (2 == p.length) {
							serializer.attribute("", "lat", p[0]);
							serializer.attribute("", "lng", p[1]);
						} else {
							serializer.attribute("", "lat", "");
							serializer.attribute("", "lng", "");
						}
						serializer.text("");
						serializer.endTag("", "point");
					}
					serializer.endTag("", "points");
				}
			}

			/**
			 * 地点列表
			 */
			if (!Util.isEmpty(pleaceList)) {
				/**
				 * 获取每一次答卷的开始时间和结束时间
				 */
				String[] places = pleaceList.split(",");
				serializer.startTag("", "places");
				for (String place : places) {
					serializer.startTag("", "place");
					serializer.text(place);
					serializer.endTag("", "place");
				}
				serializer.startTag("", "provinces");
				serializer.text("");
				serializer.endTag("", "provinces");
				serializer.endTag("", "places");
			}
			if (null != versionInfo) {
				serializer.startTag("", "vernum");
				serializer.text(versionInfo.getVernum());
				serializer.endTag("", "vernum");
				serializer.startTag("", "surupdate");
				serializer.text(versionInfo.getSurupdate());
				serializer.endTag("", "surupdate");
				serializer.startTag("", "allList");
				serializer.text(versionInfo.getArrList().toString());
				serializer.endTag("", "allList");
				serializer.startTag("", "answerList");
				serializer.text(versionInfo.getAnswerList());
				serializer.endTag("", "answerList");
				serializer.startTag("", "DeviceBrand");
				serializer.text(versionInfo.getDeviceBrand());
				serializer.endTag("", "DeviceBrand");
				serializer.startTag("", "SystemModel");
				serializer.text(versionInfo.getSystemModel());
				serializer.endTag("", "SystemModel");
				serializer.startTag("", "SystemVersion");
				serializer.text(versionInfo.getSystemVersion());
				serializer.endTag("", "SystemVersion");
				serializer.startTag("", "VerifyFlag");
				serializer.text(versionInfo.getVerifyFlag() + "");
				serializer.endTag("", "VerifyFlag");
				
				
			}
			serializer.startTag("", "device");
			serializer.text("Android");
			serializer.endTag("", "device");

			serializer.startTag("", "baiduBos");
			
			serializer.text(isbaidubos);
			serializer.endTag("", "baiduBos");
			
			serializer.startTag("", "uploadtype");
			
			serializer.text(uploadtype);
			serializer.endTag("", "uploadtype");
			
			
			serializer.endTag("", "response");
			serializer.endDocument();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return writer.toString();
	}

	/**
	 * 写入文件 加密
	 * 
	 * @param path
	 *            文件的目录
	 * @param name
	 *            文件的名称
	 * @param data
	 *            写入的字符串
	 * @param isBase64以base64写入
	 * @return
	 */
	public boolean write2Xml(String path, String name, String data,
			boolean isBase64) {
		boolean is = false;
		OutputStream os = null;
		try {
			os = new FileOutputStream(path + File.separator + name);
			if (isBase64) {
				OutputStreamWriter osw = new OutputStreamWriter(
						new Base64OutputStream(os, Base64.DEFAULT));
				osw.write(data);
				osw.close();
				os.close();
			} else {
				OutputStreamWriter osw = new OutputStreamWriter(os);
				osw.write(data);
				osw.close();
				os.close();
			}
			is = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return is;
	}

	/**
	 * 一次性写文件
	 * 
	 * @param path
	 *            文件存储的目录
	 * @param name
	 *            文件存储的名字
	 * @param hidMap
	 *            hid节点中的内容
	 * @param aswList
	 *            问题
	 * @param feeds
	 *            文件
	 * @return 命名规则 sid pid
	 */
	
	
//	String isbaidubos
	public boolean write2Xml(String path,
			String name,
			HashMap<String, String> hidMap, //
			ArrayList<Answer> aswList,
			ArrayList<UploadFeed> feeds,//
			String dateList,
			String pointList, //
			String pleaceList, boolean isBase64, String sid, String pid,
			VersionInfo versionInfo,String isbaidubos,String uploadtype) {
		// 命名规则
		String data = write2AnswerString(hidMap, aswList, feeds, dateList,
				pointList, pleaceList, sid, pid, name, versionInfo, isbaidubos,uploadtype);
		if (!Util.isEmpty(data)) {
			return write2Xml(path, name, data, isBase64);
		} else {
			return false;
		}
	}
}
