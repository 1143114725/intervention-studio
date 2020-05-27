package com.investigate.newsupper.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.investigate.newsupper.bean.AccessPanelBean;
import com.investigate.newsupper.bean.Application;
import com.investigate.newsupper.bean.GroupsBean;
import com.investigate.newsupper.bean.Knowledge;
import com.investigate.newsupper.bean.Logic;
import com.investigate.newsupper.bean.Logo;
import com.investigate.newsupper.bean.MSG;
import com.investigate.newsupper.bean.Notice;
import com.investigate.newsupper.bean.Option;
import com.investigate.newsupper.bean.Quota;
import com.investigate.newsupper.bean.Repeat;
import com.investigate.newsupper.bean.Restriction;
import com.investigate.newsupper.bean.RestrictionValue;
import com.investigate.newsupper.bean.ReturnType;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.GsonUtil;
import com.investigate.newsupper.util.OutFile;
import com.investigate.newsupper.util.Util;

import android.util.Log;
import android.util.Xml;

/**
 * 负责Xml解析的
 */
public class XmlService {

	private static final String TAG = "XmlService";

	// 大树 重置 1
	public List<UploadFeed> getAll(InputStream inStream) throws Exception {
		List<UploadFeed> feeds = new ArrayList<UploadFeed>();
		UploadFeed feed = null;
		XmlPullParser parser = Xml.newPullParser();
		/** 为Pull解析器设置要解析的XML数据 **/
		parser.setInput(inStream, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				if ("feed".equals(parser.getName())) {
					String feedId = parser.getAttributeValue(0);
					feed = new UploadFeed();
					feed.setFeedId(feedId);
				}
				break;

			case XmlPullParser.END_TAG:
				if ("feed".equals(parser.getName())) {
					feeds.add(feed);
					feed = null;
				}
				break;
			}
			event = parser.next();
		}
		return feeds;
	}

	// 以上
	public ArrayList<Notice> getNotice(InputStream inStream) {
		ArrayList<Notice> notices = new ArrayList<Notice>();
		if (null == inStream) {
			return notices;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			NodeList noticeList = element.getElementsByTagName("notice");
			if (null != noticeList) {
				for (int i = 0; i < noticeList.getLength(); i++) {
					Node noticeNode = noticeList.item(i);
					if (null != noticeNode) {
						NamedNodeMap noticeMap = noticeNode.getAttributes();
						if (null != noticeMap) {
							Notice notice = new Notice();
							Node titleNode = noticeMap.getNamedItem("title");
							if (null != titleNode) {
								/**
								 * 名称
								 */
								notice.setTitle(titleNode.getNodeValue().trim());
							}

							Node contentNode = noticeMap
									.getNamedItem("content");
							if (null != contentNode) {
								/**
								 * 内容
								 */
								notice.setContent(contentNode.getNodeValue()
										.trim());
							}

							Node timeNode = noticeMap.getNamedItem("time");
							if (null != timeNode) {
								/**
								 * 时间
								 */
								notice.setTime(timeNode.getNodeValue().trim());
							}

							Node idNode = noticeMap.getNamedItem("id");
							if (null != idNode) {
								/**
								 * 时间
								 */
								notice.setId(idNode.getNodeValue().trim());
							}
							notices.add(notice);
						}
					}
				}
			} else {
				return notices;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return notices;
		}
		return notices;
	}

	// 解析知识库
	public ArrayList<Knowledge> getAllKnow(InputStream inStream) {
		ArrayList<Knowledge> klist = new ArrayList<Knowledge>();
		if (null == inStream) {
			return klist;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			NodeList knowledgeList = element.getElementsByTagName("knowledge");
			if (null != knowledgeList) {
				for (int i = 0; i < knowledgeList.getLength(); i++) {
					Node knowNode = knowledgeList.item(i);
					if (null != knowNode) {
						NamedNodeMap knowMap = knowNode.getAttributes();
						if (null != knowMap) {
							Knowledge knowledge = new Knowledge();
							Node titleNode = knowMap
									.getNamedItem("knowledgeTitle");
							if (null != titleNode) {
								/**
								 * 知识库名称
								 */
								knowledge.setTitle(titleNode.getNodeValue()
										.trim());
							}
							Node kidNode = knowMap.getNamedItem("knowledgeId");
							if (null != kidNode) {
								/**
								 * 知识库id
								 */
								knowledge.setId(kidNode.getNodeValue().trim());
							}

							Node pathNode = knowMap
									.getNamedItem("knowledgeSurvey");
							if (null != pathNode) {
								// 知识库分类
								knowledge.setKind(pathNode.getNodeValue()
										.trim());
							}
							Node knowledgeContentNode = knowMap
									.getNamedItem("knowledgeContent");
							if (null != knowledgeContentNode) {
								knowledge.setContent(knowledgeContentNode
										.getNodeValue().trim());
							}
							Node knowledgePathNode = knowMap
									.getNamedItem("knowledgePath");
							if (null != knowledgePathNode) {
								String paths = knowledgePathNode.getNodeValue()
										.trim();
								knowledge.setAttach(paths);
								if (Util.isEmpty(paths)) {
									// 下载路径为空,那名字也就是空
									knowledge.setFileName("");
								} else {
									String[] pathSplit = paths.split(";");
									String names = "";
									for (String path : pathSplit) {
										String name = path.substring(
												path.lastIndexOf("/") + 1,
												path.length());
										names += name + ";";
									}
									knowledge.setFileName(names);
								}

							}
							knowledge.setEnable("0");
							klist.add(knowledge);
						}
					}
				}
			} else {
				return klist;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return klist;
		}
		return klist;
	}

	/**
	 * EraJieZhang 这个就是请求项目界面的xml解析。（下载项目和更新项目）
	 * 
	 * @param inStream
	 * @return
	 */
	public ArrayList<Survey> getAllSurvey(InputStream inStream) {
		ArrayList<Survey> surveys = new ArrayList<Survey>();
		if (null == inStream) {
			return surveys;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			NodeList surveyList = element.getElementsByTagName("project");

			if (null != surveyList) {
				for (int i = 0; i < surveyList.getLength(); i++) {
					Node proNode = surveyList.item(i);
					if (null != proNode) {
						NamedNodeMap proMap = proNode.getAttributes();
						Survey survey = new Survey();
						if (null != proMap) {
							Node surveyIdNode = proMap.getNamedItem("SurveyID");
							if (null != surveyIdNode) {
								survey.surveyId = surveyIdNode.getNodeValue()
										.trim();
							}

							// 获取SC_ID，和SC_Name,SC_Num

							Node scidNode = proMap.getNamedItem("SC_ID");
							if (null != scidNode) {
								String SCID = scidNode.getNodeValue().trim();
								if (!Util.isEmpty(SCID)
										&& !Util.isNullStr(SCID)) {
									survey.setSCID(SCID);
								}
							}

							Node scnameNode = proMap.getNamedItem("SC_Name");
							if (null != scnameNode) {
								String SCName = scnameNode.getNodeValue()
										.trim();
								if (!Util.isEmpty(SCName)
										&& !Util.isNullStr(SCName)) {
									survey.setSCName(SCName);
								}
							}

							Node scnumNode = proMap.getNamedItem("SC_Num");
							if (null != scnumNode) {
								String SCNum = scnumNode.getNodeValue().trim();
								if (!Util.isEmpty(SCNum)
										&& !Util.isNullStr(SCNum)) {

									survey.setSCNum(SCNum);
									// 获取下一个项目的surveyid
									String[] num = SCNum.split(",");
									String nextnum = "";
									for (int j = 0; j < num.length; j++) {
										if (survey.surveyId.equals(num[j])) {
											if (j == num.length - 1) {
												nextnum = "";
											} else {
												nextnum = num[j + 1];
											}

										}
									}
									survey.setSCNextId(nextnum);
								}
							}

							/**************** 间隔时间 **********************/
							// 间隔时间
							Node IntervalTimeNode = proMap
									.getNamedItem("IntervalTime");
							if (null != IntervalTimeNode) {
								String IntervalTime = IntervalTimeNode
										.getNodeValue().trim();
								if (!Util.isEmpty(IntervalTime)
										&& !Util.isNullStr(IntervalTime)) {
									survey.setIntervalTime(IntervalTime);
									Log.d("xml解析", "IntervalTime="
											+ IntervalTime);
								}
							}
							// 间隔时间unit
							Node IntervalTimeUnitNode = proMap
									.getNamedItem("IntervalTimeUnit");
							if (null != IntervalTimeUnitNode) {
								String IntervalTimeUnit = IntervalTimeUnitNode
										.getNodeValue().trim();
								if (!Util.isEmpty(IntervalTimeUnit)
										&& !Util.isNullStr(IntervalTimeUnit)) {
									survey.setIntervalTimeUnit(IntervalTimeUnit);
									Log.d("xml解析", "IntervalTimeUnit="
											+ IntervalTimeUnit);
								}
							}
							// 浮动时间
							Node FloatingTimeNode = proMap
									.getNamedItem("FloatingTime");
							if (null != FloatingTimeNode) {
								String FloatingTime = FloatingTimeNode
										.getNodeValue().trim();
								if (!Util.isEmpty(FloatingTime)
										&& !Util.isNullStr(FloatingTime)) {
									survey.setFloatingTime(FloatingTime);
									Log.d("xml解析", "FloatingTime="
											+ FloatingTime);
								}
							}
							// 浮动时间unit
							Node FloatingTimeUnitNode = proMap
									.getNamedItem("FloatingTimeUnit");
							if (null != FloatingTimeUnitNode) {
								String FloatingTimeUnit = FloatingTimeUnitNode
										.getNodeValue().trim();
								if (!Util.isEmpty(FloatingTimeUnit)
										&& !Util.isNullStr(FloatingTimeUnit)) {
									survey.setFloatingTimeUnit(FloatingTimeUnit);
									Log.d("xml解析", "FloatingTimeUnit="
											+ FloatingTimeUnit);
								}
							}
							/******************* 间隔时间 ***********************/

							// Survey survey = new Survey();
							Node titleNode = proMap.getNamedItem("title");
							if (null != titleNode) {
								/**
								 * 问卷名称
								 */
								survey.surveyTitle = titleNode.getNodeValue()
										.trim();
							}
							Node pwdNode = proMap
									.getNamedItem("AndroidNewPSEnableChar");
							if (null != pwdNode) {
								/**
								 * 问卷名称
								 */
								// System.out.println("设置密码");
								survey.setPassword(pwdNode.getNodeValue()
										.trim());
							}
							Node pathNode = proMap.getNamedItem("path");
							if (null != pathNode) {
								survey.downloadUrl = pathNode.getNodeValue()
										.trim();
							}
							Node uploadNode = proMap.getNamedItem("uploadfile");
							if (null != uploadNode) {
								String upload = uploadNode.getNodeValue()
										.trim();
								if ("true".equals(upload)) {
									survey.upload = 1;
								} else if ("false".equals(upload)) {
									survey.upload = 0;
								}
								// System.out.println("getAllProject--->upload="+upload);
							}
							Node uptimeNode = proMap.getNamedItem("uptime");
							if (null != uptimeNode) {
								survey.publishTime = uptimeNode.getNodeValue()
										.trim();
							}
							// 问卷提醒解析
							Node generatedTimeNode = proMap
									.getNamedItem("generatedTime");
							if (null != generatedTimeNode) {
								survey.setGeneratedTime(generatedTimeNode
										.getNodeValue().trim());
							}

							Node NodesurveyId = proMap.getNamedItem("SurveyID");
							if (null != NodesurveyId) {
								survey.surveyId = NodesurveyId.getNodeValue()
										.trim();
							}

							Node versionNode = proMap.getNamedItem("version");
							if (null != versionNode) {
								String v = versionNode.getNodeValue().trim();
								if (!Util.isEmpty(v) && !Util.isNullStr(v)) {
									survey.version = Float.parseFloat(v);
								}
							}

							Node camerNode = proMap.getNamedItem("cameraFlag");
							if (null != camerNode) {
								String camer = camerNode.getNodeValue().trim();
								if (!Util.isEmpty(camer)
										&& !Util.isNullStr(camer)) {
									survey.isPhoto = Integer.parseInt(camer);
								}
							}

							Node recordNode = proMap.getNamedItem("recordFlag");
							if (null != recordNode) {
								String record = recordNode.getNodeValue()
										.trim();
								if (!Util.isEmpty(record)
										&& !Util.isNullStr(record)) {
									survey.isRecord = Integer.parseInt(record);
								}
							}
							// 摄像解析节点
							Node vidoeNode = proMap.getNamedItem("videoFlag");
							if (null != vidoeNode) {
								String video = vidoeNode.getNodeValue().trim();
								if (!Util.isEmpty(video)
										&& !Util.isNullStr(video)) {
									survey.isVideo = Integer.parseInt(video);
								}
							}
							// survey.isVideo=1;

							// 新建限制
							Node oneVisitNode = proMap.getNamedItem("oneVisit");
							if (null != oneVisitNode) {
								String oneVisit = oneVisitNode.getNodeValue()
										.trim();
								if (!Util.isEmpty(oneVisit)
										&& !Util.isNullStr(oneVisit)) {
									survey.oneVisit = Integer
											.parseInt(oneVisit);
								}
							}

							Node androidNode = proMap
									.getNamedItem("AndroidFlag");
							if (null != androidNode) {
								String android = androidNode.getNodeValue()
										.trim();
								if (!Util.isEmpty(android)
										&& !Util.isNullStr(android)) {
									survey.visitMode = Integer
											.parseInt(android);
								} else {
									survey.visitMode = 1;
								}
							} else {
								survey.visitMode = 1;
							}

							Node openNode = proMap.getNamedItem("openStatus");
							if (null != openNode) {
								String openStatus = openNode.getNodeValue()
										.trim();
								if (!Util.isEmpty(openStatus)
										&& !Util.isNullStr(openStatus)) {
									survey.openStatus = Integer
											.parseInt(openStatus);
								}
							}

							/**
							 * 是否为全局录音
							 */
							Node globalNode = proMap
									.getNamedItem("RecordedFlag");
							if (null != globalNode) {
								String global = globalNode.getNodeValue()
										.trim();
								if (!Util.isEmpty(global)
										&& !Util.isNullStr(global)) {
									survey.globalRecord = Integer
											.parseInt(global);
								}
							}
							// surveys.add(survey);
						}
						// 访问状态
						NodeList elementReturn = proNode.getChildNodes();
						if (null != elementReturn) {
							for (int z = 0; z < elementReturn.getLength(); z++) {
								// return节点
								Node item = elementReturn.item(z);
								if (null != item) {
									NodeList elementSid = item.getChildNodes();
									if (null != elementSid) {
										ArrayList<ReturnType> rList = new ArrayList<ReturnType>();
										for (int y = 0; y < elementSid
												.getLength(); y++) {
											Node itemSid = elementSid.item(y);
											NamedNodeMap attributeSid = itemSid
													.getAttributes();
											// 属性
											if (null != attributeSid) {
												ReturnType rt = new ReturnType();
												Node namedItemEnable = attributeSid
														.getNamedItem("enable");
												if (namedItemEnable != null) {
													if (0 == Integer
															.parseInt(namedItemEnable
																	.getNodeValue()
																	.trim())) {
														continue;
													} else {
														rt.setEnable(Integer
																.parseInt(namedItemEnable
																		.getNodeValue()
																		.trim()));
													}
												}
												Node namedItemSid = attributeSid
														.getNamedItem("sid");
												if (namedItemSid != null) {
													// System.out.println("sid:"+namedItemSid.getNodeValue().trim());
													if (1 == Integer
															.parseInt(namedItemSid
																	.getNodeValue()
																	.trim())
															|| 100 == Integer
																	.parseInt(namedItemSid
																			.getNodeValue()
																			.trim())) {
														continue;
													}
													rt.setReturnId(Integer
															.parseInt(namedItemSid
																	.getNodeValue()
																	.trim()));
												}
												Node namedItemNote = attributeSid
														.getNamedItem("note");
												if (namedItemNote != null) {
													rt.setReturnName(namedItemNote
															.getNodeValue()
															.trim());
												}
												if (Util.isEmpty(rt
														.getReturnName())) {
													rt.setReturnName(itemSid
															.getTextContent()
															.trim());
												}
												rList.add(rt);
											}
										}
										// 存取成json
										survey.setRlist(rList);
									}
								}
							}
						}

						/**
						 * 公司组解析
						 */

						ArrayList<GroupsBean> groupsBeans = new ArrayList<GroupsBean>();
						NodeList Groupsllist = element
								.getElementsByTagName("Groups");
						if (Groupsllist != null) {
							for (int iq = 0, size = Groupsllist.getLength(); iq < size; iq++) {
								Element GroupsElement = (Element) Groupsllist
										.item(iq);

								NodeList groupllist = GroupsElement
										.getElementsByTagName("group");
								Log.i(TAG, "ParseXml: subllist.getLength()="
										+ groupllist.getLength());
								if (null != groupllist) {
									for (int j = 0, groupsize = groupllist
											.getLength(); j < groupsize; j++) {
										GroupsBean groupsBean = new GroupsBean();
										Element subElement = (Element) groupllist
												.item(j);
										NamedNodeMap groupMap = subElement
												.getAttributes();

										if (subElement != null) {
											String groupRuleName = subElement
													.getTextContent();
											groupsBean
													.setGroupName(groupRuleName);
											Log.i(TAG,
													"ParseXml: -----------groupRuleName--------"
															+ groupRuleName);
										}
										if (null != groupMap) {
											Node groupsidNode = groupMap
													.getNamedItem("sid");
											if (null != groupsidNode) {
												String sid = groupsidNode
														.getNodeValue().trim();
												groupsBean.setSid(sid);
												Log.i(TAG,
														"ParseXml: groupsidNode"
																+ sid);
											}
										}
										groupsBeans.add(groupsBean);
									}
								}
							}
						}
						survey.setGroupsBeans(groupsBeans);

						/**
						 * 名单信息解析
						 */

						ArrayList<AccessPanelBean> accessPanellist = new ArrayList<AccessPanelBean>();

						NodeList AccessPanellist = element
								.getElementsByTagName("AccessPanel");
						if (AccessPanellist != null) {

							Log.i(TAG, "ParseXml: AccessPanellist.getLength()="
									+ AccessPanellist.getLength());
							for (int ii = 0, size = AccessPanellist.getLength(); ii < size; ii++) {
								Element AccessPaneElement = (Element) AccessPanellist
										.item(ii);
								NamedNodeMap AccessPaneMap = AccessPaneElement
										.getAttributes();
								if (null != AccessPaneMap) {
									Node AccessPaneNode = AccessPaneMap
											.getNamedItem("CustomerID");
									if (null != AccessPaneNode) {
										String customerID = AccessPaneNode
												.getNodeValue().trim();
										Log.i(TAG, "ParseXml: CustomerID"
												+ customerID);
										// accessPanel.setCustomerID(customerID);
									}
								}
								NodeList subllist = AccessPaneElement
										.getElementsByTagName("sub");
								Log.i(TAG, "ParseXml: subllist.getLength()="
										+ subllist.getLength());
								if (null != subllist) {
									for (int j = 0, subsize = subllist
											.getLength(); j < subsize; j++) {

										AccessPanelBean accessPanel = new AccessPanelBean();

										Element subElement = (Element) subllist
												.item(j);
										NamedNodeMap subMap = subElement
												.getAttributes();

										if (subElement != null) {
											String showtext = subElement
													.getTextContent();
											accessPanel.setShowtext(showtext);
											Log.i(TAG,
													"ParseXml: -----------showtext--------"
															+ showtext);
										}

										if (null != subMap) {
											Node sidNode = subMap
													.getNamedItem("sid");
											if (null != sidNode) {
												String sid = sidNode
														.getNodeValue().trim();
												accessPanel.setSid(sid);
												Log.i(TAG, "ParseXml: sidNode"
														+ sidNode
																.getNodeValue()
																.trim());
											}
											Node lenNode = subMap
													.getNamedItem("len");
											if (null != lenNode) {

												String len = lenNode
														.getNodeValue().trim();
												Log.i(TAG, "ParseXml: lenNode"
														+ len);
											}

											Node noteNode = subMap
													.getNamedItem("note");
											if (null != noteNode) {
												String note = noteNode
														.getNodeValue().trim();
												accessPanel.setNote(note);
												Log.i(TAG, "ParseXml: noteNode"
														+ note);
											}

											Node modifyNode = subMap
													.getNamedItem("modify");
											if (null != modifyNode) {
												String modify = modifyNode
														.getNodeValue().trim();
												Log.i(TAG,
														"ParseXml: modifyNode"
																+ modify);
											}

											Node enableNode = subMap
													.getNamedItem("enable");
											if (null != enableNode) {
												String enable = enableNode
														.getNodeValue().trim();
												Log.i(TAG,
														"ParseXml: enableNode"
																+ enable);
											}

											Node PresetValueNode = subMap
													.getNamedItem("PresetValue");
											if (null != PresetValueNode) {
												String PresetValue = PresetValueNode
														.getNodeValue().trim();
												accessPanel
														.setPresetValue(PresetValue);
												Log.i(TAG,
														"ParseXml: PresetValueNode"
																+ PresetValue);
											}

											Node PermissionNode = subMap
													.getNamedItem("Permission");
											if (null != PermissionNode) {
												String Permission = PermissionNode
														.getNodeValue().trim();
												accessPanel
														.setPermission(Permission);
												Log.i(TAG,
														"ParseXml: PermissionNode"
																+ Permission);
											}

											Node AppointPersonNode = subMap
													.getNamedItem("AppointPerson");
											if (null != AppointPersonNode) {
												String AppointPerson = AppointPersonNode
														.getNodeValue().trim();
												Log.i(TAG,
														"ParseXml: AppointPersonNode"
																+ AppointPerson);
											}
										}
										accessPanellist.add(accessPanel);
									}
								}

							}
						}

						survey.setAccessPanellist(accessPanellist);
						surveys.add(survey);
						// 访问状态结束
					}
				}

			} else {
				return surveys;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return surveys;
		}
		return surveys;
	}

	public Survey getSurvey(InputStream inStream) {
		Survey survey = new Survey();
		if (null == inStream) {
			return null;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			NodeList surveyList = element.getElementsByTagName("project");
			if (null != surveyList) {
				for (int i = 0; i < surveyList.getLength(); i++) {
					Node proNode = surveyList.item(i);
					if (null != proNode) {
						NamedNodeMap proMap = proNode.getAttributes();
						if (null != proMap) {
							survey = new Survey();
							Node titleNode = proMap.getNamedItem("title");
							if (null != titleNode) {
								/**
								 * 问卷名称
								 */
								survey.surveyTitle = titleNode.getNodeValue()
										.trim();
							}
							Node pwdNode = proMap
									.getNamedItem("AndroidNewPSEnableChar");
							if (null != pwdNode) {
								/**
								 * 问卷名称
								 */
								// System.out.println("设置密码");
								survey.setPassword(pwdNode.getNodeValue()
										.trim());
							}

							Node pathNode = proMap.getNamedItem("path");
							if (null != pathNode) {
								survey.downloadUrl = pathNode.getNodeValue()
										.trim();
							}
							Node uploadNode = proMap.getNamedItem("uploadfile");
							if (null != uploadNode) {
								String upload = uploadNode.getNodeValue()
										.trim();
								if ("true".equals(upload)) {
									survey.upload = 1;
								} else if ("false".equals(upload)) {
									survey.upload = 0;
								}
								// System.out.println("getAllProject--->upload="+upload);
							}
							Node uptimeNode = proMap.getNamedItem("uptime");
							if (null != uptimeNode) {
								survey.publishTime = uptimeNode.getNodeValue()
										.trim();
							}
							// 问卷提醒解析
							Node generatedTimeNode = proMap
									.getNamedItem("generatedTime");
							if (null != generatedTimeNode) {
								survey.setGeneratedTime(generatedTimeNode
										.getNodeValue().trim());
							}

							Node surveyIdNode = proMap.getNamedItem("SurveyID");
							if (null != surveyIdNode) {
								survey.surveyId = surveyIdNode.getNodeValue()
										.trim();
							}

							Node versionNode = proMap.getNamedItem("version");
							if (null != versionNode) {
								String v = versionNode.getNodeValue().trim();
								if (!Util.isEmpty(v) && !Util.isNullStr(v)) {
									survey.version = Float.parseFloat(v);
								}
							}

							Node camerNode = proMap.getNamedItem("cameraFlag");
							if (null != camerNode) {
								String camer = camerNode.getNodeValue().trim();
								if (!Util.isEmpty(camer)
										&& !Util.isNullStr(camer)) {
									survey.isPhoto = Integer.parseInt(camer);
								}
							}

							Node recordNode = proMap.getNamedItem("recordFlag");
							if (null != recordNode) {
								String record = recordNode.getNodeValue()
										.trim();
								if (!Util.isEmpty(record)
										&& !Util.isNullStr(record)) {
									survey.isRecord = Integer.parseInt(record);
								}
							}
							// 摄像解析节点
							Node vidoeNode = proMap.getNamedItem("videoFlag");
							if (null != vidoeNode) {
								String video = vidoeNode.getNodeValue().trim();
								if (!Util.isEmpty(video)
										&& !Util.isNullStr(video)) {
									survey.isVideo = Integer.parseInt(video);
								}
							}
							// survey.isVideo=1;

							// 新建限制
							Node oneVisitNode = proMap.getNamedItem("oneVisit");
							if (null != oneVisitNode) {
								String oneVisit = oneVisitNode.getNodeValue()
										.trim();
								if (!Util.isEmpty(oneVisit)
										&& !Util.isNullStr(oneVisit)) {
									survey.oneVisit = Integer
											.parseInt(oneVisit);
								}
							}

							Node androidNode = proMap
									.getNamedItem("AndroidFlag");
							if (null != androidNode) {
								String android = androidNode.getNodeValue()
										.trim();
								if (!Util.isEmpty(android)
										&& !Util.isNullStr(android)) {
									survey.visitMode = Integer
											.parseInt(android);
								} else {
									survey.visitMode = 1;
								}
							} else {
								survey.visitMode = 1;
							}

							Node openNode = proMap.getNamedItem("openStatus");
							if (null != openNode) {
								String openStatus = openNode.getNodeValue()
										.trim();
								if (!Util.isEmpty(openStatus)
										&& !Util.isNullStr(openStatus)) {
									survey.openStatus = Integer
											.parseInt(openStatus);
								}
							}

							/**
							 * 是否为全局录音
							 */
							Node globalNode = proMap
									.getNamedItem("RecordedFlag");
							if (null != globalNode) {
								String global = globalNode.getNodeValue()
										.trim();
								if (!Util.isEmpty(global)
										&& !Util.isNullStr(global)) {
									survey.globalRecord = Integer
											.parseInt(global);
								}
							}

						}
					}
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return survey;
	}

	/**
	 * EraJieZhang 登陆的xml解析
	 * 
	 * @param is
	 * @return
	 */
	public HashMap<String, String> parseLoginXml(InputStream is) {

		if (is == null) {
			Log.i("登陆接口返回值：", "没有返回值!");
		} else {
			Log.i("登陆接口返回值：", "有返回值!");
		}
		// 打印的tag
		String TAG = "HttpForLoginResult:";
		// 登陆后返回的百度Bos参数
		String AccessKeyID, SecretAccessKey, EndPoint, BucketName;
		// 登陆后的用户信息
		String authorID, name, apkUrl;
		// 返回的数组
		HashMap<String, String> rMap = new HashMap<String, String>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(is);
			Element element = document.getDocumentElement();
			Element stateElement = (Element) element.getElementsByTagName(
					"state").item(0);
			String state = stateElement.getTextContent();
			rMap.put("state", state);
			Log.e(TAG, "onResponse: state" + state);
			if (state.equals("100")) {

				Element authorIDElement = (Element) element
						.getElementsByTagName("authorID").item(0);
				if (authorIDElement != null) {
					authorID = authorIDElement.getTextContent();
					Log.e(TAG, "onResponse: authorID" + authorID);
					rMap.put("authorID", authorID);

				}

				Element nameElement = (Element) element.getElementsByTagName(
						"name").item(0);
				if (nameElement != null) {
					name = nameElement.getTextContent();
					Log.e(TAG, "onResponse: name" + name);
					rMap.put("name", name);
				}

				Element AKElement = (Element) element.getElementsByTagName(
						"Access_Key").item(0);
				if (AKElement != null) {
					AccessKeyID = AKElement.getTextContent();
					Log.e(TAG, "onResponse: AccessKeyID" + AccessKeyID);
					rMap.put("AccessKeyID", AccessKeyID);
				}

				Element SKElement = (Element) element.getElementsByTagName(
						"Secret_Key").item(0);
				if (SKElement != null) {
					SecretAccessKey = SKElement.getTextContent();
					Log.e(TAG, "onResponse: SecretAccessKey" + SecretAccessKey);
					rMap.put("SecretAccessKey", SecretAccessKey);
				}

				Element EndpointElement = (Element) element
						.getElementsByTagName("endpoint").item(0);
				if (EndpointElement != null) {
					EndPoint = EndpointElement.getTextContent();
					Log.e(TAG, "onResponse: EndPoint" + EndPoint);
					rMap.put("EndPoint", EndPoint);
				}

				Element BNameElement = (Element) element.getElementsByTagName(
						"Bucket_Name").item(0);
				if (BNameElement != null) {
					BucketName = BNameElement.getTextContent();
					Log.e(TAG, "onResponse: BucketName" + BucketName);
					rMap.put("BucketName", BucketName);
				}
			}
			Element nameElement = (Element) element
					.getElementsByTagName("name").item(0);
			if (nameElement != null) {
				name = nameElement.getTextContent();
				Log.e(TAG, "onResponse: name" + name);
				rMap.put("name", name);
			}
			if (state.equals("101")) {
				// <name>直接更新访问专家</name><apkUrl>http://www.survey-expert.cn/newsurvey/config/DapSurvey_JLSJ.apk</apkUrl>

				Element apkUrlElement = (Element) element.getElementsByTagName(
						"apkUrl").item(0);
				if (apkUrlElement != null) {
					apkUrl = apkUrlElement.getTextContent();
					Log.e(TAG, "onResponse: apkUrl" + apkUrl);
					rMap.put("apkUrl", apkUrl);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// HashMap<String, String> rMap = new HashMap<String, String>();
		// DocumentBuilderFactory factory =
		// DocumentBuilderFactory.newInstance();
		// try {
		// DocumentBuilder builder = factory.newDocumentBuilder();
		// Document document = builder.parse(is);
		// Element element = document.getDocumentElement();
		// NodeList ndL = element.getChildNodes();
		// for (int i = 0; i < ndL.getLength(); i++) {
		// Node nd = ndL.item(i);
		// if (null != nd) {
		// if (nd.getNodeType() == Node.ELEMENT_NODE) {
		// if (null != nd.getFirstChild()) {
		// rMap.put(nd.getNodeName(), nd.getFirstChild()
		// .getNodeValue());
		// }
		// }
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return rMap;
	}

	/** 问卷的解析 **/
	public Application getApplication(InputStream inStream) throws Exception {
		Application app = null;
		XmlPullParser parser = Xml.newPullParser();
		/** 为Pull解析器设置要解析的XML数据 **/
		parser.setInput(inStream, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				app = new Application();
				break;

			case XmlPullParser.START_TAG:
				if ("application".equals(parser.getName())) {
					/**
					 * 此处要注意,服务器端提供的版本号必须为double格式字符串 否则将会出异常
					 * **/
					String v = parser.getAttributeValue(0);// parser.getAttributeValue(1);
					Log.i("a", "v" + v);
					app.setVersion(Double.parseDouble(v));
				}
				if ("path".equals(parser.getName())) {
					String path = parser.nextText();
					app.setPath(path);
				}

				if ("content".equals(parser.getName())) {
					String content = parser.nextText();
					app.setContent(content);
				}

				if ("notice".equals(parser.getName())) {
					String notice = parser.nextText();
					app.setNotice(notice);
				}
				break;

			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}
		return app;
	}

	/**
	 * 注册解析获取是否注册成功
	 * 
	 * <?xml version="1.0" encoding="UTF-8" ?> - <returnanswer>
	 * <state>98</state> </returnanswer>
	 */
	public int getRegistResponse(InputStream inStream, String charset)
			throws Exception {
		int state = 0;
		XmlPullParser parser = XmlPullParserFactory.newInstance()
				.newPullParser();
		parser.setInput(inStream, charset);
		int eventType = parser.getEventType();
		while (XmlPullParser.END_DOCUMENT != eventType) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				String nodeName = parser.getName();
				if ("state".equals(nodeName)) {
					state = Integer.parseInt(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}

		return state;
	}

	/**
	 * 自定义logo功能 解析
	 */
	public Logo getLogo(InputStream inStream) throws Exception {
		Logo logo = null;
		XmlPullParser parser = Xml.newPullParser();
		/** 为Pull解析器设置要解析的XML数据 **/
		parser.setInput(inStream, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				logo = new Logo();
				break;

			case XmlPullParser.START_TAG:
				if ("name".equals(parser.getName())) {
					/**
					 * 此处要注意,服务器端提供的版本号必须为double格式字符串 否则将会出异常
					 * **/
					String v = parser.nextText();// parser.getAttributeValue(1);
					Log.i("a", "v" + v);
					logo.setName(v);
				}
				if ("state".equals(parser.getName())) {
					String state = parser.nextText();
					logo.setState(state);

				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}
		return logo;
	}

	/**
	 * 配额是否需要更新
	 * 
	 * @param quotaStream
	 * @return
	 */
	public String getListQuotaTime(InputStream inStream) {

		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(inStream, "UTF-8");// 设置数据源编码
			int eventType = parser.getEventType();// 获取事件类型
			String time = "";
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
					break;
				case XmlPullParser.START_TAG:// 开始读取某个标签
					// 通过getName判断读到哪个标签，然后通过nextText()获取文本节点值，或通过getAttributeValue(i)获取属性节点值
					String name = parser.getName();
					if ("RegDate".equals(name)) {
						time = parser.nextText();
					}

					break;
				case XmlPullParser.END_TAG:// 结束元素事件
					break;
				}
				eventType = parser.next();
			}
			inStream.close();
			return time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 配额解析 EraJieZhang
	 * 
	 * @param quotaStream
	 * @return
	 */
	public ArrayList<Quota> getListQuota(InputStream inStream) {

		Quota quota = null;
		ArrayList<Quota> quotalist = null;
		Option opt = null;
		ArrayList<Option> optlist = null;
		// String xml =
		// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><returnanswer><state>100</state>"
		// +
		// "<MaxRegDate>2017/06/21 16:17:24</MaxRegDate><panels><panel RuleID=\"25867\">"
		// +
		// "<RuleName>燕京</RuleName><RegDate>2017/06/21 16:17:24</RegDate><planCount>1</planCount>"
		// +
		// "<sucessCount>0</sucessCount><Content><Option Index=\"1\" Type=\"RadioButton\" "
		// +
		// "Match=\"all\" Symbol=\"Equal\" Condition=\"3\"/><Option Index=\"3\" Type=\"CheckBox\" "
		// +
		// "Match=\"all\" Symbol=\"Equal\" Condition=\"1\"/></Content><ContentStr>Q1  等于  "
		// + "选项<b>[　　C.燕京]</b> (所有条件必须满足)Q2  等于  选项<b>[A.朋友聚会]</b> "
		// +
		// "(所有条件必须满足)</ContentStr></panel><panel RuleID=\"25866\"><RuleName>雪花</RuleName>"
		// +
		// "<RegDate>2017/06/21 16:09:03</RegDate><planCount>2</planCount><sucessCount>2</sucessCount>"
		// +
		// "<Content><Option Index=\"1\" Type=\"RadioButton\" Match=\"one\" Symbol=\"Equal\" Condition=\"2\"/>"
		// +
		// "<Option Index=\"15\" Type=\"RadioButton\" Match=\"one\" Symbol=\"Equal\" Condition=\"1\"/></Content>"
		// +
		// "<ContentStr>Q1  等于  选项<b>[　　B.雪花]</b> (至少有一个条件满足)Q13  等于  选项<b>[北京]</b> "
		// +
		// "(至少有一个条件满足)</ContentStr></panel><panel RuleID=\"25865\"><RuleName>青岛</RuleName>"
		// +
		// "<RegDate>2017/06/21 16:08:13</RegDate><planCount>1</planCount><sucessCount>1</sucessCount>"
		// +
		// "<Content><Option Index=\"1\" Type=\"RadioButton\" Match=\"one\" Symbol=\"Equal\" Condition=\"1\"/>"
		// +
		// "<Option Index=\"15\" Type=\"RadioButton\" Match=\"one\" Symbol=\"Equal\" Condition=\"1\"/></Content>"
		// +
		// "<ContentStr>Q1  等于  选项<b>[　　A.青岛]</b> (至少有一个条件满足)Q13  等于  选项<b>[北京]</b> "
		// + "(至少有一个条件满足)</ContentStr></panel></panels></returnanswer>";
		//
		// InputStream is = new ByteArrayInputStream(xml.getBytes());
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			Element stateElement = (Element) element.getElementsByTagName(
					"state").item(0);
			String state = stateElement.getTextContent();

			if (state.equals("100")) {
				quotalist = new ArrayList<Quota>(); // 实例化集合类

				NodeList panel = element.getElementsByTagName("panel");
				if (panel != null) {
					for (int j = 0; j < panel.getLength(); j++) {

						Element personElement = (Element) panel.item(j);
						String id = personElement.getAttribute("RuleID");

						quota = new Quota();
						// 添加配额编号
						quota.setQuotaId(id);

						Element nameElement = (Element) personElement
								.getElementsByTagName("RuleName").item(0);
						if (nameElement != null) {
							String RuleName = nameElement.getTextContent();
							// 配额名称
							quota.setQuotaName(RuleName);
						}

						Element ageElement = (Element) personElement
								.getElementsByTagName("RegDate").item(0);
						if (ageElement != null) {
							String RegDate = ageElement.getTextContent();
							// 创建时间
							quota.setQuotaTime(RegDate);
						}
						Element planCountElement = (Element) personElement
								.getElementsByTagName("planCount").item(0);

						if (planCountElement != null) {
							String planCount = planCountElement
									.getTextContent();
							// 配额数量
							quota.setPlanCount(planCount);
						}
						Element sucessCountElement = (Element) personElement
								.getElementsByTagName("sucessCount").item(0);
						if (sucessCountElement != null) {
							String sucessCount = sucessCountElement
									.getTextContent();
							// 配额成功数量
							quota.setSucessCount(sucessCount);
						}

						Element ContentStrElement = (Element) personElement
								.getElementsByTagName("ContentStr").item(0);
						if (ContentStrElement != null) {
							String ContentStr = ContentStrElement
									.getTextContent();
							// 配额说明
							quota.setQuotaIns(ContentStr);
						}

						Element PreQuoteFlagElement = (Element) personElement
								.getElementsByTagName("PreQuoteFlag").item(0);
						if (PreQuoteFlagElement != null) {
							String PreQuoteFlag = PreQuoteFlagElement
									.getTextContent();
							// 是否达到配额
							quota.setPreQuoteFlag(PreQuoteFlag);
						}

						Element ContentElement = (Element) personElement
								.getElementsByTagName("Content").item(0);
						if (ContentElement != null) {
							NodeList Contentnode = ContentElement
									.getElementsByTagName("Option");
							for (int i = 0; i < Contentnode.getLength(); i++) {
								optlist = new ArrayList<Option>();
								opt = new Option();
								Element OptionElement = (Element) Contentnode
										.item(i);
								String Index = OptionElement
										.getAttribute("Index");
								String Type = OptionElement
										.getAttribute("Type");
								String Match = OptionElement
										.getAttribute("Match");
								String Symbol = OptionElement
										.getAttribute("Symbol");
								String Condition = OptionElement
										.getAttribute("Condition");
								opt.setCondition(Condition);
								opt.setType(Type);
								opt.setMatch(Match);
								opt.setSymbol(Symbol);
								opt.setQuestionindex(Index);
								optlist.add(opt);
								quota.setOptionlist(optlist);
							}
						}
						quotalist.add(quota);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quotalist;

	}

	public static List<HashMap<String, String>> parserXml(
			InputStream inputStream, String encoding, String startNode,
			String... nodes) {

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = null;

		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();

			parser.setInput(inputStream, encoding);

			int evenType = parser.getEventType();

			while (evenType != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();

				switch (evenType) {
				case XmlPullParser.START_TAG:
					if (startNode.equals(nodeName)) {
						map = new HashMap<String, String>();
					}
					for (int i = 0; i < nodes.length; i++) {
						if (nodes[i].equalsIgnoreCase(nodeName)) {
							String temp = parser.nextText();
							map.put(nodes[i], temp);
						}
					}
					break;

				case XmlPullParser.END_TAG:
					if (startNode.equals(nodeName) && map != null) {
						list.add(map);
					}
					break;
				}

				evenType = parser.next();

			}

		} catch (XmlPullParserException e) {
		} catch (IOException e) {
		}

		return list;
	}

	
	
	/**
	 * 解析查重上传后的返回数据
	 */
	//	<R><S>100</S><FID>13</FID><RTP>100</RTP><MSG>json</MSG></R>
    public static Repeat DomXml(String xml) {
        //创建一个DocumentBuilderFactory的对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //创建一个DocumentBuilder的对象
        Repeat repeat = new Repeat();
        try {
            //创建DocumentBuilder对象
            DocumentBuilder db = dbf.newDocumentBuilder();
            //通过DocumentBuilder对象的parser方法加载books.xml文件到当前项目下
            ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());

            Document document = db.parse(is);
            //获取S节点的list
            NodeList RList = document.getElementsByTagName("R");
            //就一个所以就取第0个，获得他的自己点集合
            NodeList childNodes = RList.item(0).getChildNodes();
            for (int k = 0; k < childNodes.getLength(); k++) {
            	
                //区分出text类型的node以及element类型的node
                if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                    //获取了element类型节点的节点名
                	if (null == childNodes.item(k).getFirstChild()) {
                		BaseLog.w("childNodes" + k+"为空");
					}else{
						System.out.print("第" + (k + 1) + "个节点的节点名：" + childNodes.item(k).getNodeName());
	                    System.out.println("--节点值是：" + childNodes.item(k).getFirstChild().getNodeValue());
	                    
	                    
	                    
	                    if (childNodes.item(k).getNodeName().equals("S")) {
	                    	repeat.setS(childNodes.item(k).getFirstChild().getNodeValue());
						}else 
	                    if (childNodes.item(k).getNodeName().equals("FID")) {
	                    	repeat.setFID(childNodes.item(k).getFirstChild().getNodeValue());
						}else
	                    if (childNodes.item(k).getNodeName().equals("RTP")) {
	                    	repeat.setRTP(childNodes.item(k).getFirstChild().getNodeValue());
						}else
	                    if (childNodes.item(k).getNodeName().equals("MSG")) {
	                    	MSG msg = GsonUtil.GsonToBean(childNodes.item(k).getFirstChild().getNodeValue(), MSG.class);
	                    	repeat.setMSG(msg);
						}
					}
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return repeat;
    }
}
