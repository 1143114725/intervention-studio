package com.investigate.newsupper.util;

import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.investigate.newsupper.bean.Answer;
import com.investigate.newsupper.bean.AnswerMap;
import com.investigate.newsupper.bean.Call;
import com.investigate.newsupper.bean.Data;
import com.investigate.newsupper.bean.GRestriction;
import com.investigate.newsupper.bean.Group;
import com.investigate.newsupper.bean.InnerPanel;
import com.investigate.newsupper.bean.Intervention;
import com.investigate.newsupper.bean.InterventionItem;
import com.investigate.newsupper.bean.Logic;
import com.investigate.newsupper.bean.LogicItem;
import com.investigate.newsupper.bean.LogicList;
import com.investigate.newsupper.bean.OpenStatus;
import com.investigate.newsupper.bean.Option;
import com.investigate.newsupper.bean.Parameter;
import com.investigate.newsupper.bean.ParameterInnerPanel;
import com.investigate.newsupper.bean.PersionItemBean;
import com.investigate.newsupper.bean.PersionItemListBean;
import com.investigate.newsupper.bean.QGroup;
import com.investigate.newsupper.bean.QgRestriction;
import com.investigate.newsupper.bean.Question;
import com.investigate.newsupper.bean.QuestionItem;
import com.investigate.newsupper.bean.RID;
import com.investigate.newsupper.bean.Restriction;
import com.investigate.newsupper.bean.RestrictionValue;
import com.investigate.newsupper.bean.ReturnType;
import com.investigate.newsupper.bean.SurveyQuestion;
import com.investigate.newsupper.bean.TimeBean;
import com.investigate.newsupper.bean.TimeList;
import com.investigate.newsupper.bean.UserBean;
import com.investigate.newsupper.bean.UserList;
import com.investigate.newsupper.global.Cnt;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlUtil {

	private XmlUtil() {
	}

	/**
	 * 解析问卷中的题目
	 * 
	 * @param surveyXml
	 *            传入问卷的xml
	 * @return 返回问题列表
	 */
	public static SurveyQuestion getSurveyQuestion(InputStream surveyXml,
			Call call) {
		/**
		 * 是否有分页符
		 */
		boolean ISPAGE = false;
		SurveyQuestion sq = new SurveyQuestion();
		// 数据字典
		ArrayList<String> classIdList = new ArrayList<String>();
		// ArrayList<Question> questions = new ArrayList<Question>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(surveyXml);
			Element element = document.getDocumentElement();
			NodeList ndL = element.getChildNodes();
			if (null != ndL) {// 1.如果文档中的节点列表不为空
				int length = ndL.getLength();
				boolean isCall = (null != call);
				for (int i = 0; i < length; i++) {
					if (isCall) {
						call.updateProgress(i, length);
					}
					Node nd = ndL.item(i);
					if (null != nd) {// 2.如果遍历的节点不为空
						String name = nd.getNodeName();
						if ("question".equals(name)) { // question问题节点
							Question q = new Question();
							// q.id = i;
							// System.out.println(name + ":");
							ArrayList<QuestionItem> rows = new ArrayList<QuestionItem>();
							ArrayList<Restriction> restrs = new ArrayList<Restriction>();
							ArrayList<QgRestriction> qgRestArr = new ArrayList<QgRestriction>();
							ArrayList<GRestriction> gRestrs = new ArrayList<GRestriction>();
							NamedNodeMap nnm = nd.getAttributes();
							if (null != nnm) {// 节点属性值不为空
								Node nodeOrder = nnm.getNamedItem("order");
								if (null != nodeOrder) {
									String order = nodeOrder.getNodeValue();
									if (null != order && 0 < order.length()) {
										// int o = Integer.parseInt(order);
										// if (-1 == o) {
										// /**
										// * 无效的问题不需要再往下解析
										// */
										// continue;
										// } else {
										// q.qOrder = o;
										// }
										q.qOrder = Integer.parseInt(order);
									}
									System.out.println("\torder="
											+ nodeOrder.getNodeValue());
								}

								Node nodeIndex = nnm.getNamedItem("index");
								if (null != nodeIndex) {
									String index = nodeIndex.getNodeValue();
									System.out.println("\tindex="
											+ nodeIndex.getNodeValue());
									if (null != index && 0 < index.length()) {
										q.qIndex = Integer.parseInt(index);
										if (-1 == q.qOrder) {
											sq.getQuestions().add(q);
											continue;
										}
									}
								}

								Node nodeType = nnm.getNamedItem("type");
								if (null != nodeType) {
									// TODO 解析出是什么题型
									// System.out.println("\ttype="
									// + nodeType.getNodeValue());
									q.qTypeStr = nodeType.getNodeValue();
									if (Cnt.HEADER.equals(q.qTypeStr)) {
										q.qType = Cnt.TYPE_HEADER;
									} else if (Cnt.RADIO_BUTTON
											.equals(q.qTypeStr)) {
										q.qType = Cnt.TYPE_RADIO_BUTTON;
									} else if (Cnt.CHECK_BOX.equals(q.qTypeStr)) {
										q.qType = Cnt.TYPE_CHECK_BOX;
									} else if (Cnt.DROP_DOWN_LIST
											.equals(q.qTypeStr)) {
										q.qType = Cnt.TYPE_DROP_DOWN_LIST;
									} else if (Cnt.FREE_TEXT_BOX
											.equals(q.qTypeStr)) {
										q.qType = Cnt.TYPE_FREE_TEXT_BOX;
									} else if (Cnt.FREE_TEXT_AREA
											.equals(q.qTypeStr)) {
										q.qType = Cnt.TYPE_FREE_TEXT_AREA;
									} else if (Cnt.MATRIX_RADIO_BUTTON
											.equals(q.qTypeStr)) {
										q.qType = Cnt.TYPE_MATRIX_RADIO_BUTTON;
									} else if (Cnt.MATRIX_CHECK_BOX
											.equals(q.qTypeStr)) {
										q.qType = Cnt.TYPE_MATRIX_CHECK_BOX;
									} else if (Cnt.IMAGE.equals(q.qTypeStr)) {
										q.qType = Cnt.TYPE_MEDIA;
									} else if (Cnt.PAGE.equals(q.qTypeStr)) {
										q.qType = Cnt.TYPE_PAGE;
										if (!ISPAGE) {
											ISPAGE = true;
											sq.setshowpageStatus(0);
										}
									}
								}

								Node qidType = nnm.getNamedItem("qid");
								if (null != qidType) {
									// TODO 解析出是什么题型
									// System.out.println("\tqid="
									// + qidType.getNodeValue());
									q.qid = qidType.getNodeValue();

								}

								Node nodeRequired = nnm
										.getNamedItem("required");
								if (null != nodeRequired) {
									// TODO 解析出是什么题型
									String required = nodeRequired
											.getNodeValue();
									if (null != required
											&& 0 < required.length()) {
										// System.out.println("\trequired="
										// + required);
										q.qRequired = Boolean
												.parseBoolean(required) ? 1 : 0;
									}

								}
								/**
								 * 单选矩阵星评显示
								 */
								Node nodeStarCheck = nnm
										.getNamedItem("starCheck");
								if (null != nodeStarCheck) {
									// TODO 解析出是什么题型
									String starCheck = nodeStarCheck
											.getNodeValue();
									if (null != starCheck
											&& 0 < starCheck.length()) {
										// System.out.println("starCheck="
										// + starCheck);
										q.qStarCheck = Integer
												.parseInt(starCheck);
									}

								}
								// /**
								// * 复选排序标记
								// */
								// Node nodeSortCheck =
								// nnm.getNamedItem("sortCheck");
								// if (null != nodeSortCheck) {
								// // TODO 解析出是什么题型
								// String sortCheck =
								// nodeSortCheck.getNodeValue();
								// if (null != sortCheck && 0 <
								// sortCheck.length()) {
								// System.out.println("\\sortCheck" +
								// sortCheck);
								// q.qSortCheck =
								// Boolean.parseBoolean(sortCheck) ? 1 : 0;
								// }
								// }

								// 触发强制拍照的功能设计
								Node nodephotocheck = nnm
										.getNamedItem("exclude_photo_check");
								if (null != nodephotocheck) {
									// TODO 解析出是什么题型
									String photocheck = nodephotocheck
											.getNodeValue();
									if (null != photocheck
											&& 0 < photocheck.length()) {
										System.out
												.println("触发强制拍照的功能设计--photocheck"
														+ photocheck);
										if (!Util.isEmpty(photocheck)) {
											q.setPhotocheck(photocheck);
										} else {
											q.setPhotocheck("");
										}

									}
								}

								/**
								 * 大树排序 获取字段 sortChecked=true 代表 排序
								 */
								Node nodeSortChecked = nnm
										.getNamedItem("sortChecked");
								if (null != nodeSortChecked) {
									// TODO 解析出是什么题型
									String sortChecked = nodeSortChecked
											.getNodeValue();
									if (null != sortChecked
											&& 0 < sortChecked.length()) {
										// System.out.println("\\sortChecked"
										// + sortChecked);
										q.qSortChecked = Boolean
												.parseBoolean(sortChecked) ? 1
												: 0;
									}
								}
								// 单复选矩阵固定
								Node nodeFixed = nnm.getNamedItem("isFixed");
								if (null != nodeFixed) {
									// TODO 解析出是什么题型
									String isFixed = nodeFixed.getNodeValue();
									if (null != isFixed && 0 < isFixed.length()) {
										// System.out.println("\tisFixed="
										// + isFixed);
										q.isFixed = Boolean
												.parseBoolean(isFixed) ? 1 : 0;
									}
								}
								// 单复选矩阵固定

								/**
								 * zz添加单题拍照属性
								 */
								Node nodeCamera = nnm.getNamedItem("photo");
								if (null != nodeCamera) {
									// TODO 解析出是什么题型
									String camera = nodeCamera.getNodeValue();
									if (null != camera && 0 < camera.length()) {
										// System.out.println("\tQCamera="
										// + camera);
										q.qCamera = Boolean
												.parseBoolean(camera) ? 1 : 0;
									}
								}
								/**
								 * zz结束单题拍照属性
								 */
								/**
								 * 三级联动解析
								 */
								Node nodeLinkage = nnm.getNamedItem("linkage");
								if (null != nodeLinkage) {
									// TODO 解析出是什么题型
									String linkage = nodeLinkage.getNodeValue();
									if (null != linkage && 0 < linkage.length()) {
										// System.out.println("\tQlinkage="
										// + linkage);
										q.qLinkage = Boolean
												.parseBoolean(linkage) ? 1 : 0;
									}
								}

								/**
								 * 题外关联 之和 字段的解析
								 */
								Node nodeAssocite = nnm
										.getNamedItem("parentAssociatedCheck");
								if (null != nodeAssocite) {
									// Log.i("zrl1",
									// nodeAssocite.getNodeValue());
									String value = nodeAssocite.getNodeValue();
									if (null != value && value.length() > 0) {
										q.qParentAssociatedCheck = value.trim();
									}
								}
								/**
								 * EraJie查重功能
								 */
								Node nodeRepeat = nnm
										.getNamedItem("checkRepeat");
								if (null != nodeRepeat) {
									// TODO 解析出是什么题型
									String Repeat = nodeRepeat.getNodeValue();
									if (null != Repeat && 0 < Repeat.length()) {
										System.out
												.println("\tRepeat=" + Repeat);
										q.setCheckRepeat(Repeat);
									}

								}

								/**
								 * EraJie隐藏拍照
								 */
								Node nodehide_photo_check = nnm
										.getNamedItem("hide_photo_check");
								if (null != nodehide_photo_check) {
									// TODO 解析出是什么题型
									String hide_photo_check = nodehide_photo_check
											.getNodeValue();
									if (null != hide_photo_check
											&& 0 < hide_photo_check.length()) {
										q.hidePhotoCheck = hide_photo_check;
									}
								}

								/**
								 * zz添加单题录音
								 */
								Node nodeRecord = nnm
										.getNamedItem("itemRecording");
								if (null != nodeRecord) {
									// TODO 解析出是什么题型
									String record = nodeRecord.getNodeValue();
									if (null != record && 0 < record.length()) {
										// System.out.println("\tQSign=" +
										// sign);
										q.itemRecording = Boolean
												.parseBoolean(record) ? 1 : 0;
									}

								}
								/**
								 * zz添加单题签名
								 */
								Node nodeSign = nnm.getNamedItem("sign");
								if (null != nodeSign) {
									// TODO 解析出是什么题型
									String sign = nodeSign.getNodeValue();
									if (null != sign && 0 < sign.length()) {
										// System.out.println("\tQSign=" +
										// sign);
										q.qSign = Boolean.parseBoolean(sign) ? 1
												: 0;
									}

								}
								/**
								 * 随机
								 */
								Node nodeRadomed = nnm.getNamedItem("radomed");
								if (null != nodeRadomed) {
									// TODO 解析出是什么题型
									String radomed = nodeRadomed.getNodeValue();
									if (null != radomed && 0 < radomed.length()) {
										// System.out.println("\tradomed="
										// + radomed);
										q.qRadomed = Boolean
												.parseBoolean(radomed) ? 1 : 0;
										System.out.println(Boolean
												.parseBoolean(radomed) ? 1
												: 0 + "");
									}

								}

								/** 组内随机开始 **/
								Node gRSelectNum = nnm
										.getNamedItem("gRSelectNum");
								if (null != gRSelectNum) {
									q.setgRSelectNum(gRSelectNum.getNodeValue());
									System.out.println("gRSelectNum------>"
											+ gRSelectNum.getNodeValue());
								}

								Node itemGR_Flag = nnm
										.getNamedItem("itemGR_Flag");
								if (null != itemGR_Flag) {
									q.setItemGR_Flag(itemGR_Flag.getNodeValue());
									System.out.println("itemGR_Flag--------->"
											+ itemGR_Flag.getNodeValue());
								}
								/** 组内随机结束 **/

								Node nodeWeightChecked = nnm
										.getNamedItem("weightChecked");
								if (null != nodeWeightChecked) {
									// TODO 解析出是什么题型
									String weightChecked = nodeWeightChecked
											.getNodeValue();
									if (null != weightChecked
											&& 0 < weightChecked.length()) {
										// System.out.println("\tweightChecked="
										// + weightChecked);
										q.qWeightChecked = Boolean
												.parseBoolean(weightChecked) ? 1
												: 0;
									}

								}

								Node nodeScoreChecked = nnm
										.getNamedItem("scoreChecked");
								if (null != nodeScoreChecked) {
									// TODO 解析出是什么题型
									String scoreChecked = nodeScoreChecked
											.getNodeValue();
									if (null != scoreChecked
											&& 0 < scoreChecked.length()) {
										// System.out.println("\tscoreChecked="
										// + scoreChecked);
										q.qScoreChecked = Boolean
												.parseBoolean(scoreChecked) ? 1
												: 0;
									}
								}

								Node nodeDragchecked = nnm
										.getNamedItem("dragchecked");
								if (null != nodeDragchecked) {
									// TODO 解析出是什么题型
									String dragchecked = nodeDragchecked
											.getNodeValue();
									if (null != dragchecked
											&& 0 < dragchecked.length()) {
										// System.out.println("\tdragchecked="
										// + dragchecked);
										q.qDragChecked = Boolean
												.parseBoolean(dragchecked) ? 1
												: 0;
									}
								}

								Node nodeMatchQuestion = nnm
										.getNamedItem("MatchQuestion");
								if (null != nodeMatchQuestion) {
									String matchQuestion = nodeMatchQuestion
											.getNodeValue();
									if (null != matchQuestion
											&& 0 < matchQuestion.length()) {
										// System.out.println("\tMatchQuestion="
										// + matchQuestion);
										// q.qMatchQuestion =
										// Boolean.parseBoolean(matchQuestion)?1:0;
										if ("all".equals(matchQuestion)) {
											q.qMatchQuestion = 1;
										} else if ("one".equals(matchQuestion)) {
											q.qMatchQuestion = 0;
										} else if ("custom"
												.equals(matchQuestion)) {
											q.qMatchQuestion = 2;
										}
									}
								}

								Node nodeTFrom = nnm.getNamedItem("titleFrom");
								if (null != nodeTFrom) {
									String tileFrom = nodeTFrom.getNodeValue();
									if (!Util.isEmpty(tileFrom)) {
										q.setTitleFrom(tileFrom);
									}
								}
								Node preSelectQuestion = nnm
										.getNamedItem("preSelect");
								if (null != preSelectQuestion) {
									String preSelect = preSelectQuestion
											.getNodeValue();
									if (null != preSelect
											&& 0 < preSelect.length()) {
										// System.out.println("\tpreSelect="
										// + preSelect);
										q.qPreSelect = Boolean
												.parseBoolean(preSelect) ? 1
												: 0;
									}
								}
								/**
								 * 哑题解析
								 */
								Node dumbFlagQuestion = nnm
										.getNamedItem("dumbChecked");
								if (null != dumbFlagQuestion) {
									String dumbFlag = dumbFlagQuestion
											.getNodeValue();
									if (null != dumbFlag
											&& 0 < dumbFlag.length()) {
										// System.out.println("\tdumbFlag="
										// + dumbFlag);
										q.qDumbFlag = Boolean
												.parseBoolean(dumbFlag) ? 1 : 0;
									}
								}

								Node nodeSiteOption = nnm
										.getNamedItem("siteOption");
								if (null != nodeSiteOption) {
									// TODO 解析出是什么题型
									String siteOption = nodeSiteOption
											.getNodeValue();
									if (null != siteOption
											&& 0 < siteOption.length()) {
										// System.out.println("\tsiteOption="
										// + siteOption);
										q.qSiteOption = siteOption.trim();
									}
								}
								/**
								 * 大树 添加 双引用
								 */
								Node nodeSiteOption2 = nnm
										.getNamedItem("siteOption2");
								if (null != nodeSiteOption2) {
									// TODO 解析出是什么题型
									String siteOption2 = nodeSiteOption2
											.getNodeValue();
									if (null != siteOption2
											&& 0 < siteOption2.length()) {
										Log.i("zrl1", "siteOption2:"
												+ siteOption2);
										q.qSiteOption2 = siteOption2.trim();
									}
								}

								Node nodeInclusion = nnm
										.getNamedItem("inclusion");
								if (null != nodeInclusion) {
									// TODO 解析出是什么题型
									String inclusion = nodeInclusion
											.getNodeValue();
									if (null != inclusion
											&& 0 < inclusion.length()) {
										// System.out.println("\tinclusion="
										// + inclusion);
										q.qInclusion = inclusion.trim();
									}
								}
								Node nodeContinuousText = nnm
										.getNamedItem("continuousText");
								if (null != nodeContinuousText) {
									// TODO 解析出是什么题型
									String continuousText = nodeContinuousText
											.getNodeValue();
									if (null != continuousText
											&& 0 < continuousText.length()) {
										q.qContinuousText = continuousText
												.trim();
									}
								}
								Node nodeContinuous = nnm
										.getNamedItem("continuous");
								if (null != nodeContinuous) {
									// TODO 解析出是什么题型
									String continuous = nodeContinuous
											.getNodeValue();
									if (null != continuous
											&& 0 < continuous.length()) {
										// System.out.println("\tcontinuous="
										// + continuous);
										q.qContinuous = Integer
												.parseInt(continuous);
									}
								}

								Node qScoreNode = nnm.getNamedItem("QScore");
								if (null != qScoreNode) {
									String score = qScoreNode.getNodeValue();
									if (null != score && 0 < score.length()) {
										// System.out.print("\tQScore=" +
										// score);
										q.qScore = score;
									}
								}

								Node qUpperNode = nnm
										.getNamedItem("upperBound");
								if (null != qUpperNode) {
									String upper = qUpperNode.getNodeValue();
									if (null != upper && 0 < upper.length()) {
										// System.out.print("\tUpper=" + upper);
										// q.qScore = score;
										q.upperBound = Integer.parseInt(upper);
									}
								}

								Node qLowerNode = nnm
										.getNamedItem("lowerBound");
								if (null != qLowerNode) {
									String lower = qLowerNode.getNodeValue();
									if (null != lower && 0 < lower.length()) {
										// System.out.print("\tLower=" + lower);
										q.lowerBound = Integer.parseInt(lower);
									}
								}
								/**
								 * 每题停留时间
								 */
								Node qStopTime = nnm.getNamedItem("StopTime");
								if (null != qStopTime) {
									String stopTime = qStopTime.getNodeValue();
									if (null != stopTime
											&& 0 < stopTime.length()) {
										// System.out.println("\timage_posison="
										// + stopTime);
										// q.qContinuous =
										// Integer.parseInt(continuous);
										q.qStopTime = Integer.parseInt(stopTime
												.trim());
									}
								}
								Node qPosition = nnm.getNamedItem("position");
								if (null != qPosition) {
									// TODO 解析出是什么题型
									String position = qPosition.getNodeValue();
									if (null != position
											&& 0 < position.length()) {
										// System.out.println("\timage_posison="
										// + position);
										// q.qContinuous =
										// Integer.parseInt(continuous);
										q.qMediaPosition = position;
									}
								}

								// Node qLInkNode=nnm.getNamedItem("linkMove");

							} // 节点属性值不为空

							// 遍历quesiton里面的控件属性
							NodeList qChildrenList = nd.getChildNodes();
							if (null != qChildrenList) {// quesiion子节点列表不为空的
								for (int j = 0; j < qChildrenList.getLength(); j++) {
									Node qChildrenNode = qChildrenList.item(j);
									if (null != qChildrenNode) {// quesiion子节点不为空的
										// System.out.println("控件类型--->"
										// + qChildrenNode.getNodeName());
										String tag = qChildrenNode
												.getNodeName();
										if ("text".equals(tag)) {// 表示题目question的标题
											NamedNodeMap chdMap = qChildrenNode
													.getAttributes();
											if (null != chdMap) {// 假如text属性值不为空
												Node disableNode = chdMap
														.getNamedItem("disable");
												if (null != disableNode) {// disable属性
													String disable = disableNode
															.getNodeValue();
													if (null != disable
															&& 0 < disable
																	.length()) {
														// System.out
														// .print("\ttext_disable="
														// + disable);
														q.qTitleDisable = Boolean
																.parseBoolean(disable) ? 1
																: 0;
													}
												} // disable属性

												Node posNode = chdMap
														.getNamedItem("position");
												if (null != posNode) {// position属性
													String pos = posNode
															.getNodeValue();
													if (null != pos
															&& 0 < pos.length()) {
														// System.out
														// .print("\ttext_posistion="
														// + pos);
														q.qTitlePosition = pos
																.trim();
													}
												} // position属性
											}
											String title = qChildrenNode
													.getTextContent();
											if (null != title
													&& 0 < title.length()) {
												// System.out.println("\ttitle="
												// + title);
												q.qTitle = title;
											}
										} else if ("freeInput".equals(tag)) {// 其他项
											QuestionItem item = new QuestionItem();
											NamedNodeMap inputMap = qChildrenNode
													.getAttributes();
											if (null != inputMap) {// 假如text属性值不为空
												Node valueNode = inputMap
														.getNamedItem("value");
												if (null != valueNode) {// value属性
													String value = valueNode
															.getNodeValue();
													if (null != value
															&& 0 < value
																	.length()) {
														// q.freeInputValue =
														// Integer.parseInt(value);
														item.itemValue = Integer
																.parseInt(value);
														// System.out
														// .print("\tfreeInput_value="
														// + value);
													}
													item.isOther = 1;// 其他项
													q.haveOther = 1;// 这个题目有其他项
												} // value属性

												Node excludeNode = inputMap
														.getNamedItem("exclude");
												if (null != excludeNode) {// exclude属性
													String exclude = excludeNode
															.getNodeValue();
													if (null != exclude
															&& 0 < exclude
																	.length()) {
														// q.freeInputExclude =
														// exclude;
														item.exclude = exclude
																.trim();
														// System.out
														// .print("\tfreeInput_exclude="
														// + exclude);
													}
												} // exclude属性

												Node sizeWidthNode = inputMap
														.getNamedItem("sizeWidth");
												if (null != sizeWidthNode) {// sizeWidth属性
													String sizeWidth = sizeWidthNode
															.getNodeValue();
													if (null != sizeWidth
															&& 0 < sizeWidth
																	.length()) {
														q.sizeWidth = Integer
																.parseInt(sizeWidth);
														// System.out
														// .print("\tfreeInput_sizeWidth="
														// + sizeWidth);
													}
												} // sizeWidth属性
													// 条件隐藏选项
												Node hideListNode = inputMap
														.getNamedItem("hideList");
												if (null != hideListNode) {
													String hideList = hideListNode
															.getNodeValue();
													if (null != hideList
															&& 0 < hideList
																	.length()) {
														item.hideList = hideList;
													}
												}
												Node itemHideType = inputMap
														.getNamedItem("hideType");
												if (null != itemHideType) {
													String hideType = itemHideType
															.getNodeValue();
													if (!Util.isEmpty(hideType)) {
														item.hideType = Integer
																.parseInt(hideType);
													}
												}
											}

											String inputText = qChildrenNode
													.getTextContent();
											if (null != inputText
													&& 0 < inputText.length()) {
												// q.freeInputText = inputText;
												item.itemText = inputText;
												// System.out
												// .println("\tfreeInput_freeInputText="
												// + inputText);
											}
											// freeInputItems.add(item);
											// 添加其他项
											if (1 == item.isOther) {
												rows.add(item);// 是其他项
											}
										} else if ("comment".equals(tag)) {
											NamedNodeMap chdMap = qChildrenNode
													.getAttributes();
											if (null != chdMap) {// 假如text属性值不为空
												Node posNode = chdMap
														.getNamedItem("position");
												if (null != posNode) {// disable属性
													String pos = posNode
															.getNodeValue();
													if (null != pos
															&& 0 < pos.length()) {
														// System.out
														// .println("\tcomment_position="
														// + pos);
														q.qCommentPosition = pos
																.trim();
													}
												} // disable属性
											}
											String comment = qChildrenNode
													.getTextContent();
											if (null != comment
													&& 0 < comment.length()) {
												// System.out.println("\tcomment="
												// + comment);
												q.qComment = comment;
											}
										} else if ("caption".equals(tag)) {
											String caption = qChildrenNode
													.getTextContent();
											if (null != caption
													&& 0 < caption.length()) {
												// System.out.println("\tcaption="
												// + caption);
												q.qCaption = caption;
											}

										} else if ("freeTextBox".equals(tag)) {
											NamedNodeMap ftbMap = qChildrenNode
													.getAttributes();
											if (null != ftbMap) {// 假如FreeTextBox属性值不为空
												Node columnNode = ftbMap
														.getNamedItem("column");
												if (null != columnNode) {// disable属性
													String column = columnNode
															.getNodeValue();
													if (null != column
															&& 0 < column
																	.length()) {
														// System.out
														// .println("\tfreeTextBox_column="
														// + column);
														q.freeTextColumn = Integer
																.parseInt(column);
													}
												} // column属性
											}
											NodeList freeNodeList = qChildrenNode
													.getChildNodes();
											if (null != freeNodeList) {// 假如FreeTextBox标签下的子节点列表不为空
												ArrayList<QuestionItem> items = new ArrayList<QuestionItem>();
												for (int k = 0; k < freeNodeList
														.getLength(); k++) {
													Node freeNode = freeNodeList
															.item(k);
													if (null != freeNode) {// 假如子节点不为空
														String freeTag = freeNode
																.getNodeName();
														if (null != freeTag
																&& 0 < freeTag
																		.length()) {
															if ("sort"
																	.equals(freeTag)) {

																NamedNodeMap sortMap = freeNode
																		.getAttributes();
																if (null != sortMap) {
																	Node nodeSumIndex = sortMap
																			.getNamedItem("sumIndex");
																	if (null != nodeSumIndex) {
																		String sumIndex = nodeSumIndex
																				.getNodeValue();
																		if (!Util
																				.isEmpty(sumIndex)) {
																			q.sumIndex = sumIndex
																					.trim();
																		}
																	}
																	Node nodeSum = sortMap
																			.getNamedItem("sumNumber");
																	if (null != nodeSum) {
																		String sum = nodeSum
																				.getNodeValue();
																		if (!Util
																				.isEmpty(sum)) {
																			q.freeSumNumber = sum
																					.trim();
																		}
																	}

																	Node nodeSyb = sortMap
																			.getNamedItem("Symbol");
																	if (null != nodeSyb) {
																		String syb = nodeSyb
																				.getNodeValue();
																		if (!Util
																				.isEmpty(syb)) {
																			q.freeSymbol = syb
																					.trim();
																		}
																	}

																	Node nodeMax = sortMap
																			.getNamedItem("maxNumber");
																	if (null != nodeMax) {
																		String max = nodeMax
																				.getNodeValue();
																		if (!Util
																				.isEmpty(max)) {
																			q.freeMaxNumber = max
																					.trim();
																		}
																	}

																	Node nodeMin = sortMap
																			.getNamedItem("minNumber");
																	if (null != nodeMin) {
																		String min = nodeMin
																				.getNodeValue();
																		if (!Util
																				.isEmpty(min)) {
																			q.freeMinNumber = min
																					.trim();
																		}
																	}

																	// 特殊值功能
																	Node nodespecialNum = sortMap
																			.getNamedItem("specialNum");
																	if (null != nodespecialNum) {
																		String special = nodespecialNum
																				.getNodeValue();
																		if (!Util
																				.isEmpty(special)) {
																			q.special = special
																					.trim();
																		}
																	}

																	Node nodeNoRepeat = sortMap
																			.getNamedItem("noRepeat");
																	if (null != nodeNoRepeat) {
																		if ("true"
																				.equals(nodeNoRepeat
																						.getNodeValue())) {
																			q.freeNoRepeat = 1;
																		}
																	}
																}

																String sort = freeNode
																		.getTextContent();
																if (null != sort
																		&& 0 < sort
																				.length()) {
																	q.freeTextSort = sort
																			.trim();
																	// System.out
																	// .println("\tfree_sort="
																	// + sort);
																}
															} else if ("item"
																	.equals(freeTag)) {
																QuestionItem item = new QuestionItem();
																NamedNodeMap freeMap = freeNode
																		.getAttributes();
																if (null != freeMap) {
																	Node sizeNode = freeMap
																			.getNamedItem("size");
																	if (null != sizeNode) {// size属性节点
																		String size = sizeNode
																				.getNodeValue();
																		if (null != size
																				&& 0 < size
																						.length()) {
																			item.itemSize = Integer
																					.parseInt(size
																							.trim());
																			// System.out
																			// .println("item_size="
																			// +
																			// item.itemSize);
																		}
																	} // size属性节点

																	Node dateNode = freeMap
																			.getNamedItem("datecheck");
																	if (null != dateNode) {
																		String date = dateNode
																				.getNodeValue();
																		if (null != date
																				&& 0 < date
																						.length()) {
																			if ("true"
																					.equals(date
																							.trim())) {
																				item.dateCheck = 1;
																			}
																		}
																	}

																	Node dateSelectNode = freeMap
																			.getNamedItem("dateselect");
																	if (null != dateSelectNode) {
																		String dateSelect = dateSelectNode
																				.getNodeValue();
																		if (null != dateSelect
																				&& 0 < dateSelect
																						.length()) {
																			item.dateSelect = Integer
																					.parseInt(dateSelect);
																		}
																	}

																	/**
																	 * 选项类型
																	 */
																	Node typeNode = freeMap
																			.getNamedItem("type");
																	if (null != typeNode) {
																		String type = typeNode
																				.getNodeValue();
																		if (!Util
																				.isEmpty(type)) {
																			if ("date"
																					.equals(type)) {
																				item.type = 1;
																			} else if ("figure"
																					.equals(type)) {
																				item.type = 2;
																			} else if ("alphabet"
																					.equals(type)) {
																				item.type = 3;
																			} else if ("data"
																					.equals(type)) {
																				item.type = 4;
																			} else if ("email"
																					.equals(type)) {
																				item.type = 5;
																			}
																			// 维码用扫描
																			else if ("scanningCode"
																					.equals(type)) {
																				item.type = 6;
																			}
																			// 维码用扫描
																			else {
																				item.type = 0;
																			}
																		}
																	}
																	/**
																	 * 该选项是不是必填的
																	 */
																	Node requiredNode = freeMap
																			.getNamedItem("required");
																	if (null != requiredNode) {
																		String itemRequired = requiredNode
																				.getNodeValue();
																		if (!Util
																				.isEmpty(itemRequired)
																				&& "true"
																						.equals(itemRequired)) {
																			item.required = true;
																		}
																	}

																	/**
																	 * 选项启用拖动条
																	 */
																	Node dragNode = freeMap
																			.getNamedItem("dragchecked");
																	if (null != dragNode) {
																		String drag = dragNode
																				.getNodeValue();
																		if (!Util
																				.isEmpty(drag)
																				&& "true"
																						.equals(drag)) {
																			item.dragChecked = true;
																		}
																	}

																	/**
																	 * 数字体是否允许输入小数点
																	 */
																	Node floatNode = freeMap
																			.getNamedItem("float");
																	if (null != floatNode) {
																		String _float = floatNode
																				.getNodeValue();
																		if (!Util
																				.isEmpty(_float)
																				&& "true"
																						.equals(_float)) {
																			item.isFloat = true;
																		}
																	}

																	/**
																	 * 选项最大值
																	 */
																	Node maxNumberNode = freeMap
																			.getNamedItem("maxNumber");
																	if (null != maxNumberNode) {
																		String max = maxNumberNode
																				.getNodeValue();
																		if (!Util
																				.isEmpty(max)) {
																			item.maxNumber = max;
																		}
																	}

																	/**
																	 * 选项最小值
																	 */
																	Node minNumberNode = freeMap
																			.getNamedItem("minNumber");
																	if (null != minNumberNode) {
																		String min = minNumberNode
																				.getNodeValue();
																		if (!Util
																				.isEmpty(min)) {
																			item.minNumber = min;
																		}
																	}
																	// 特殊值功能
																	Node nodespecialNum = freeMap
																			.getNamedItem("specialNum");
																	if (null != nodespecialNum) {
																		String special = nodespecialNum
																				.getNodeValue();
																		if (!Util
																				.isEmpty(special)) {
																			item.specialNumber = special;
																		}
																	}
																	// 是否强制判断数字范围
																	Node nodeRange = freeMap
																			.getNamedItem("range");
																	if (null != nodeRange) {
																		String range = nodeRange
																				.getNodeValue();
																		if (!Util
																				.isEmpty(range)) {
																			item.range = range;
																		}
																	}
																	// 单行文本框是否可以多行显示
																	Node nodeNewLine = freeMap
																			.getNamedItem("newLine");
																	if (null != nodeNewLine) {
																		String newLine = nodeNewLine
																				.getNodeValue();
																		if (!Util
																				.isEmpty(newLine)) {
																			item.newLine = newLine
																					.trim();
																		}
																	}
																	// 字符串长度限制
																	// len="1"
																	// lensymbol="="
																	Node nodeLen = freeMap
																			.getNamedItem("len");
																	if (null != nodeLen) {
																		String len = nodeLen
																				.getNodeValue();
																		if (!Util
																				.isEmpty(len)) {
																			item.len = len
																					.trim();
																		}
																	}
																	/**
																	 * 小数位数限制
																	 */
																	Node nodefloatNum = freeMap
																			.getNamedItem("floatNum");
																	if (null != nodefloatNum) {
																		String floatNum = nodefloatNum
																				.getNodeValue();
																		if (!Util
																				.isEmpty(floatNum)) {
																			item.floatNum = floatNum
																					.trim();
																		}
																	}
																	// 字符串长度限制
																	// len="1"
																	// lensymbol="="
																	Node nodeLensymbol = freeMap
																			.getNamedItem("lensymbol");
																	if (null != nodeLensymbol) {
																		String lensymbol = nodeLensymbol
																				.getNodeValue();
																		if (!Util
																				.isEmpty(lensymbol)) {
																			item.lensymbol = lensymbol
																					.trim();
																		}
																	}

																	// 禁用逻辑
																	Node nodelimitList = freeMap
																			.getNamedItem("limitList");
																	if (null != nodelimitList) {
																		String limitList = nodelimitList
																				.getNodeValue();
																		if (!Util
																				.isEmpty(limitList)) {
																			item.limitList = limitList
																					.trim();
																			System.out
																					.println("limitList===="
																							+ limitList);
																		}
																	}

																	// 引用选项的文本
																	Node nodeIncludeIndex = freeMap
																			.getNamedItem("includeIndex");
																	if (null != nodeIncludeIndex) {
																		String includeIndex = nodeIncludeIndex
																				.getNodeValue();
																		if (!Util
																				.isEmpty(includeIndex)) {
																			item.includeIndex = Integer
																					.parseInt(includeIndex
																							.trim());
																		}
																	}

																	/**
																	 * EraJieZhang
																	 * 引用单行文本题答案
																	 */
																	Node nodeIncludefromItem = freeMap
																			.getNamedItem("includefromItem");
																	if (null != nodeIncludefromItem) {
																		String IncludefromItem = nodeIncludefromItem
																				.getNodeValue();
																		if (!Util
																				.isEmpty(IncludefromItem)) {
																			item.includefromItem = IncludefromItem
																					.trim();
																			BaseLog.w("xml解析结果includefromItem="
																					+ IncludefromItem
																							.trim());
																		}
																	}

																	// 数据字典
																	Node classIdNode = freeMap
																			.getNamedItem("classid");
																	if (null != classIdNode) {// size属性节点
																		String classId = classIdNode
																				.getNodeValue();
																		if (null != classId
																				&& 0 < classId
																						.length()) {
																			item.classid = classId;
																			if (!classIdList
																					.contains(classId)) {
																				classIdList
																						.add(classId);
																			}
																		}
																	}
																	// 数据字典类型
																	Node classTypeNode = freeMap
																			.getNamedItem("classtype");
																	if (null != classTypeNode) {// size属性节点
																		String classType = classTypeNode
																				.getNodeValue();
																		if (null != classType
																				&& 0 < classType
																						.length()) {
																			item.classtype = classType
																					.trim();
																		}
																	}
																	/**
																	 * 选项引用某一道题的某一个选项进行大小比较
																	 */
																	Node titlefromNode = freeMap
																			.getNamedItem("titlefrom");
																	if (null != titlefromNode) {
																		String titlefrom = titlefromNode
																				.getNodeValue();
																		if (!Util
																				.isEmpty(titlefrom)) {
																			item.titlefrom = titlefrom;
																		}
																	}

																	/**
																	 * 选项引用某一道题的某一个选项进行大小比较
																	 */
																	Node symbolNode = freeMap
																			.getNamedItem("Symbol");
																	if (null != symbolNode) {
																		String symbol = symbolNode
																				.getNodeValue();
																		if (!Util
																				.isEmpty(symbol)) {
																			item.symbol = symbol
																					.trim();
																		}
																	}
																	// 隐藏选项
																	Node hideNode = freeMap
																			.getNamedItem("hide");
																	if (null != hideNode) {
																		String hide = hideNode
																				.getNodeValue();
																		if (null != hide
																				&& 0 < hide
																						.length()) {
																			item.hide = Integer
																					.parseInt(hide);
																			// System.out
																			// .print("\tcheckBox_item_hide="
																			// +
																			// item.hide);
																		}
																	}

																	// 条件隐藏选项
																	Node hideListNode = freeMap
																			.getNamedItem("hideList");
																	if (null != hideListNode) {
																		String hideList = hideListNode
																				.getNodeValue();
																		if (null != hideList
																				&& 0 < hideList
																						.length()) {
																			item.hideList = hideList;
																		}
																	}
																	Node itemHideType = freeMap
																			.getNamedItem("hideType");
																	if (null != itemHideType) {
																		String hideType = itemHideType
																				.getNodeValue();
																		if (!Util
																				.isEmpty(hideType)) {
																			item.hideType = Integer
																					.parseInt(hideType);
																		}
																	}
																}
																NodeList itemList = freeNode
																		.getChildNodes();
																if (null != itemList) {
																	for (int l = 0; l < itemList
																			.getLength(); l++) {
																		Node node = itemList
																				.item(l);
																		if (null != node) {
																			String itemTag = node
																					.getNodeName();
																			if ("leftside-word"
																					.equals(itemTag)) {
																				String leftWord = node
																						.getTextContent();
																				if (null != leftWord
																						&& 0 < leftWord
																								.length()) {
																					// 三级联动更改处理@@符号
																					if (1 == q.qLinkage) {
																						item.leftsideWord = leftWord;
																						Log.i("zrl1",
																								item.leftsideWord);
																					} else {
																						// 非三级联动
																						item.leftsideWord = leftWord;
																					}
																				}
																				// System.out
																				// .println("\titem_leftside-word="
																				// +
																				// leftWord);
																			} else if ("rightside-word"
																					.equals(itemTag)) {
																				String rightWord = node
																						.getTextContent();
																				if (null != rightWord
																						&& 0 < rightWord
																								.length()) {
																					item.rightsideWord = rightWord;
																				}
																				// System.out
																				// .println("\titem_rightside-word="
																				// +
																				// rightWord);
																			}
																			// 旧数据字典解析
																			else if ("data"
																					.equals(itemTag)) {
																				String data = node
																						.getTextContent();
																				if (null != data
																						&& 0 < data
																								.length()) {
																					item.dataDictionary = data;
																				}
																				// System.out
																				// .println("\titem_dataDictionary="
																				// +
																				// data);
																			}
																		}
																	}
																}
																if (!Util
																		.isEmpty(item.leftsideWord)
																		|| null != freeMap) {
																	items.add(item);
																}
																// q.getColumnsItemArr().add(item);
															}
														}
													}
												}
												q.setColItemArr(items);
											}
										} else if ("radioButton".equals(tag)) {// radioButton

											NamedNodeMap radioMap = qChildrenNode
													.getAttributes();
											if (null != radioMap) {// 解析radioButton节点的属性值
												Node rowsnumNode = radioMap
														.getNamedItem("rowsnum");
												if (null != rowsnumNode) {
													String rowsnum = rowsnumNode
															.getNodeValue();
													if (null != rowsnum
															&& 0 < rowsnum
																	.length()) {
														q.rowsNum = Integer
																.parseInt(rowsnum);
														// System.out
														// .print("\tradioButton_rowsNum="
														// + q.rowsNum);
													}
												}

												Node deploymentNode = radioMap
														.getNamedItem("deployment");
												if (null != deploymentNode) {
													String deployment = deploymentNode
															.getNodeValue();
													if (null != deployment
															&& 0 < deployment
																	.length()) {
														q.deployment = deployment
																.trim();
														// System.out
														// .println("\tradioButton_deployment="
														// + q.deployment);
													}
												}
											} // 解析radioButton节点的属性值

											// 解析radioButton子节点值
											NodeList radioNodeList = qChildrenNode
													.getChildNodes();
											if (null != radioNodeList) {// 假如radioButton的子节点列表不为空则进行遍历
												// ArrayList<Item> items = new
												// ArrayList<Item>();
												ArrayList<QuestionItem> childRows = new ArrayList<QuestionItem>();
												for (int k = 0; k < radioNodeList
														.getLength(); k++) {
													Node radioNode = radioNodeList
															.item(k);
													if (null != radioNode) {// 假如子节点不为空
														String radioNodeName = radioNode
																.getNodeName();
														if ("item"
																.equals(radioNodeName)) {// 普通选项
															QuestionItem item = new QuestionItem();
															NamedNodeMap itemMap = radioNode
																	.getAttributes();
															if (null != itemMap) {// item节点的属性值不为空时
																Node valueNode = itemMap
																		.getNamedItem("value");
																if (null != valueNode) {
																	String value = valueNode
																			.getNodeValue();
																	if (null != value
																			&& 0 < value
																					.length()) {
																		item.itemValue = Integer
																				.parseInt(value);
																		// System.out
																		// .print("\tradioButton_item_value="
																		// +
																		// item.itemValue);
																	}
																}

																/** 组内随机开始 **/
																Node gRandomNum = itemMap
																		.getNamedItem("gRandomNum");
																if (null != gRandomNum) {
																	item.setgRandomNum(gRandomNum
																			.getNodeValue());
																	System.out
																			.println("gRandomNum------->"
																					+ gRandomNum
																							.getNodeValue());
																}
																/** 组内随机结束 **/
																/**
																 * 固定显示
																 */
																Node showNode = itemMap
																		.getNamedItem("show");
																if (null != showNode) {
																	String show = showNode
																			.getNodeValue();
																	if (null != show
																			&& 0 < show
																					.length()) {
																		item.itemShow = Integer
																				.valueOf(show);
																	}
																}
																/**
																 * 选项置顶 置底 题外关联
																 * 之 字段解析 大树 添加
																 * 1
																 */

																Node paddingNode = itemMap
																		.getNamedItem("padding");
																if (null != paddingNode) {
																	String padding = paddingNode
																			.getNodeValue();
																	System.out
																			.println("padding-------->"
																					+ padding);
																	if (null != padding
																			&& 0 < padding
																					.length()) {
																		item.padding = Integer
																				.valueOf(padding);
																		if (item.padding != 0) {
																			q.isItemStick = "1";
																		}
																	}
																}

																Node captionNode = itemMap
																		.getNamedItem("caption");
																if (null != captionNode) {
																	String cap = captionNode
																			.getNodeValue();
																	if (null != cap
																			&& 0 < cap
																					.length()) {
																		item.caption = cap;
																		q.isHaveItemCap = 1;
																		// System.out
																		// .print("\tradioButton_item_caption"
																		// +
																		// cap);
																	}
																}
																// 追加说明方法解析
																Node captionCheckNode = itemMap
																		.getNamedItem("caption_check");
																if (null != captionCheckNode) {
																	String capCheck = captionCheckNode
																			.getNodeValue();
																	if (null != capCheck
																			&& 0 < capCheck
																					.length()) {
																		item.caption_check = Integer
																				.parseInt(capCheck);
																	}
																}
																// 预选解析
																Node deftNode = itemMap
																		.getNamedItem("default");
																if (null != deftNode) {
																	String deft = deftNode
																			.getNodeValue();
																	if (null != deft
																			&& "checked"
																					.equals(deft)) {
																		item.deft = 1;
																	} else {
																		item.deft = 0;
																	}

																}

																/**
																 * 哑题每个选项的逻辑关系
																 */
																Node dumbListNode = itemMap
																		.getNamedItem("dumbList");
																if (null != dumbListNode) {
																	String dumbList = dumbListNode
																			.getNodeValue();
																	if (null != dumbList
																			&& 0 < dumbList
																					.length()) {
																		item.dumbList = dumbList
																				.trim();
																		// System.out
																		// .print("\tdumbList"
																		// +
																		// dumbList);
																	}
																}
																// 隐藏选项
																Node hideNode = itemMap
																		.getNamedItem("hide");
																if (null != hideNode) {// value属性节点不为空
																	String hide = hideNode
																			.getNodeValue();
																	if (null != hide
																			&& 0 < hide
																					.length()) {
																		item.hide = Integer
																				.parseInt(hide
																						.trim());
																		// System.out
																		// .print("\tmatrix_rows_item_hide="
																		// +
																		// hide);
																	}
																}
																// 条件隐藏选项
																Node hideListNode = itemMap
																		.getNamedItem("hideList");
																if (null != hideListNode) {
																	String hideList = hideListNode
																			.getNodeValue();
																	if (null != hideList
																			&& 0 < hideList
																					.length()) {
																		item.hideList = hideList;
																	}
																}
																Node itemHideType = itemMap
																		.getNamedItem("hideType");
																if (null != itemHideType) {
																	String hideType = itemHideType
																			.getNodeValue();
																	if (!Util
																			.isEmpty(hideType)) {
																		item.hideType = Integer
																				.parseInt(hideType
																						.trim());
																	}
																}
																/**
																 * 预设题组的开始位置
																 */
																Node itemactualAllBigSetsFirstIndex = itemMap
																		.getNamedItem("actualAllBigSetsFirstIndex");
																if (null != itemactualAllBigSetsFirstIndex) {
																	String actualAllBigSetsFirstIndex = itemactualAllBigSetsFirstIndex
																			.getNodeValue();
																	if (!Util
																			.isEmpty(actualAllBigSetsFirstIndex)) {
																		item.actualAllBigSetsFirstIndex = actualAllBigSetsFirstIndex
																				.trim();
																	}
																}
																/**
																 * 预设题组的结束位置
																 */
																Node itemactualAllBigSetslastIndex = itemMap
																		.getNamedItem("actualAllBigSetslastIndex");
																if (null != itemactualAllBigSetslastIndex) {
																	String actualAllBigSetslastIndex = itemactualAllBigSetslastIndex
																			.getNodeValue();
																	if (!Util
																			.isEmpty(actualAllBigSetslastIndex)) {
																		item.actualAllBigSetslastIndex = actualAllBigSetslastIndex
																				.trim();
																	}
																}
																/**
																 * 预设题组的各小题组名称
																 */
																Node itemtheoryAllBigSetsName = itemMap
																		.getNamedItem("theoryAllBigSetsName");
																if (null != itemtheoryAllBigSetsName) {
																	String theoryAllBigSetsName = itemtheoryAllBigSetsName
																			.getNodeValue();
																	if (!Util
																			.isEmpty(theoryAllBigSetsName)) {
																		item.theoryAllBigSetsName = theoryAllBigSetsName
																				.trim();
																	}
																}
																/**
																 * 是否有弹窗说明
																 */
																Node itemPopupType = itemMap
																		.getNamedItem("Popup");
																if (null != itemPopupType) {
																	String popupType = itemPopupType
																			.getNodeValue();
																	if (!Util
																			.isEmpty(popupType)) {
																		item.popUp = popupType
																				.trim();
																	}
																}

															} // item节点的属性值不为空时

															String itemText = radioNode
																	.getTextContent();
															if (null != itemText
																	&& 0 < itemText
																			.length()) {
																item.itemText = itemText;
																// System.out
																// .println("\tradioButton_item_text="
																// +
																// item.itemText);
															}
															// items.add(item);
															// q.getMatrixRowsItems().add(item);
															// q.getRowsItemArr().add(item);
															rows.add(item);
														} else if ("childitem"
																.equals(radioNodeName)) {// configration子选项
															QuestionItem item = new QuestionItem();
															item.isChild = true;
															NamedNodeMap itemMap = radioNode
																	.getAttributes();
															if (null != itemMap) {// item节点的属性值不为空时
																Node parentItem = itemMap
																		.getNamedItem("parentItem");
																if (null != parentItem) {
																	String value = parentItem
																			.getNodeValue();
																	if (null != value
																			&& 0 < value
																					.length()) {
																		item.parentItem = Integer
																				.parseInt(value
																						.trim());
																		// System.out
																		// .print("\tradioButton_item_value="
																		// +
																		// item.itemValue);
																	}
																}
																Node valueNode = itemMap
																		.getNamedItem("value");
																if (null != valueNode) {
																	String value = valueNode
																			.getNodeValue();
																	if (null != value
																			&& 0 < value
																					.length()) {
																		item.itemValue = Integer
																				.parseInt(value
																						.trim());
																		// System.out
																		// .print("\tradioButton_item_value="
																		// +
																		// item.itemValue);
																	}
																}
																Node captionNode = itemMap
																		.getNamedItem("caption");
																if (null != captionNode) {
																	String cap = captionNode
																			.getNodeValue();
																	if (null != cap
																			&& 0 < cap
																					.length()) {
																		item.caption = cap;
																		q.isHaveItemCap = 1;
																		// System.out
																		// .print("\tradioButton_item_caption"
																		// +
																		// cap);
																	}
																}
																/**
																 * 是否有弹窗说明
																 */
																Node itemPopupType = itemMap
																		.getNamedItem("Popup");
																if (null != itemPopupType) {
																	String popupType = itemPopupType
																			.getNodeValue();
																	if (!Util
																			.isEmpty(popupType)) {
																		item.popUp = popupType
																				.trim();
																	}
																}
															}

															String itemText = radioNode
																	.getTextContent();
															if (null != itemText
																	&& 0 < itemText
																			.length()) {
																item.itemText = itemText;
																// System.out
																// .println("\tradioButton_item_text="
																// +
																// item.itemText);
															}
															childRows.add(item);
														}
													} // 假如子节点不为空
												}
												// System.out.println("childRows="
												// + childRows.size());
												for (int r = 0; r < rows.size(); r++) {
													QuestionItem rItem = rows
															.get(r);
													int itemValue = rItem.itemValue;
													for (int cr = 0; cr < childRows
															.size(); cr++) {
														QuestionItem crItem = childRows
																.get(cr);
														if (null != crItem) {
															int parentItem = crItem.parentItem;
															if (itemValue == parentItem) {
																crItem.parentText = rItem.itemText;
																rItem.getChildRows()
																		.add(crItem);
																// childRows.remove(parentItem);
															}
														}
													}
												}
												// q.setRowItemArr(items);
											} // 假如radioButton的子节点列表不为空则进行遍历
										} else if ("checkBox".equals(tag)) {// checkBox
											NamedNodeMap checkMap = qChildrenNode
													.getAttributes();
											if (null != checkMap) {// 解析radioButton节点的属性值
												Node rowsnumNode = checkMap
														.getNamedItem("rowsnum");
												if (null != rowsnumNode) {
													String rowsnum = rowsnumNode
															.getNodeValue();
													if (null != rowsnum
															&& 0 < rowsnum
																	.length()) {
														q.rowsNum = Integer
																.parseInt(rowsnum);
														// System.out
														// .print("\tcheckBox_rowsNum="
														// + q.rowsNum);
													}
												}

												Node deploymentNode = checkMap
														.getNamedItem("deployment");
												if (null != deploymentNode) {
													String deployment = deploymentNode
															.getNodeValue();
													if (null != deployment
															&& 0 < deployment
																	.length()) {
														q.deployment = deployment
																.trim();
														// System.out
														// .println("\tcheckBox_deployment="
														// + q.deployment);
													}
												}
											} // 解析radioButton节点的属性值

											// 解析radioButton子节点值
											NodeList radioNodeList = qChildrenNode
													.getChildNodes();
											if (null != radioNodeList) {// 假如radioButton的子节点列表不为空则进行遍历
												// ArrayList<Item> items = new
												// ArrayList<Item>();
												for (int k = 0; k < radioNodeList
														.getLength(); k++) {
													Node radioNode = radioNodeList
															.item(k);
													if (null != radioNode) {// 假如子节点不为空
														QuestionItem item = new QuestionItem();
														NamedNodeMap itemMap = radioNode
																.getAttributes();
														if (null != itemMap) {// item节点的属性值不为空时
															Node valueNode = itemMap
																	.getNamedItem("value");
															if (null != valueNode) {
																String value = valueNode
																		.getNodeValue();
																if (null != value
																		&& 0 < value
																				.length()) {
																	item.itemValue = Integer
																			.parseInt(value);
																	// System.out
																	// .print("\tcheckBox_item_value="
																	// +
																	// item.itemValue);
																}
															}

															/** 组内随机开始 **/
															Node gRandomNum = itemMap
																	.getNamedItem("gRandomNum");
															if (null != gRandomNum) {
																item.setgRandomNum(gRandomNum
																		.getNodeValue());
																System.out
																		.println("gRandomNum------->"
																				+ gRandomNum
																						.getNodeValue());
															}
															/** 组内随机结束 **/

															/**
															 * 固定显示
															 */
															Node showNode = itemMap
																	.getNamedItem("show");
															if (null != showNode) {
																String show = showNode
																		.getNodeValue();
																if (null != show
																		&& 0 < show
																				.length()) {
																	item.itemShow = Integer
																			.valueOf(show);
																}
															}

															/**
															 * 选项置顶 置底 题外关联 之
															 * 字段解析 在复选框中的 体现 大树
															 * 添加 2
															 */

															Node paddingNode = itemMap
																	.getNamedItem("padding");
															if (null != paddingNode) {
																String padding = paddingNode
																		.getNodeValue();
																if (null != padding
																		&& 0 < padding
																				.length()) {
																	item.padding = Integer
																			.valueOf(padding);
																	if (item.padding != 0) {
																		q.isItemStick = "1";
																	}
																}
															}
															Node captionNode = itemMap
																	.getNamedItem("caption");
															if (null != captionNode) {
																String cap = captionNode
																		.getNodeValue();
																if (null != cap
																		&& 0 < cap
																				.length()) {
																	item.caption = cap;
																	q.isHaveItemCap = 1;
																	// System.out
																	// .print("\tcheckBox_item_caption"
																	// + cap);
																}
															}
															// 追加说明方法解析
															Node captionCheckNode = itemMap
																	.getNamedItem("caption_check");
															if (null != captionCheckNode) {
																String capCheck = captionCheckNode
																		.getNodeValue();
																if (null != capCheck
																		&& 0 < capCheck
																				.length()) {
																	item.caption_check = Integer
																			.parseInt(capCheck);
																}
															}
															// 预选解析
															Node deftNode = itemMap
																	.getNamedItem("default");
															if (null != deftNode) {
																String deft = deftNode
																		.getNodeValue();
																if (null != deft
																		&& "checked"
																				.equals(deft)) {
																	item.deft = 1;
																} else {
																	item.deft = 0;
																}
															}
															/**
															 * 哑题每个选项的逻辑关系
															 */
															Node dumbListNode = itemMap
																	.getNamedItem("dumbList");
															if (null != dumbListNode) {
																String dumbList = dumbListNode
																		.getNodeValue();
																if (null != dumbList
																		&& 0 < dumbList
																				.length()) {
																	item.dumbList = dumbList;
																	// System.out
																	// .print("\tdumbList"
																	// +
																	// dumbList);
																}
															}
															// 隐藏选项
															Node hideNode = itemMap
																	.getNamedItem("hide");
															if (null != hideNode) {
																String hide = hideNode
																		.getNodeValue();
																if (null != hide
																		&& 0 < hide
																				.length()) {
																	item.hide = Integer
																			.parseInt(hide);
																	// System.out
																	// .print("\tcheckBox_item_hide="
																	// +
																	// item.hide);
																}
															}

															// 条件隐藏选项
															Node hideListNode = itemMap
																	.getNamedItem("hideList");
															if (null != hideListNode) {
																String hideList = hideListNode
																		.getNodeValue();
																if (null != hideList
																		&& 0 < hideList
																				.length()) {
																	item.hideList = hideList;
																}
															}
															Node itemHideType = itemMap
																	.getNamedItem("hideType");
															if (null != itemHideType) {
																String hideType = itemHideType
																		.getNodeValue();
																if (!Util
																		.isEmpty(hideType)) {
																	item.hideType = Integer
																			.parseInt(hideType);
																}
															}
															// 排斥
															Node excludeNode = itemMap
																	.getNamedItem("exclude");
															if (null != excludeNode) {
																String exclude = excludeNode
																		.getNodeValue();
																if (null != exclude
																		&& 0 < exclude
																				.length()) {
																	item.exclude = exclude
																			.trim();
																	// System.out
																	// .print("\tcheckBox_item_value="
																	// +
																	// item.exclude);
																}
															}
															// 选项排他
															Node excludeInNode = itemMap
																	.getNamedItem("exclude_in");
															if (null != excludeInNode) {
																String excludeIn = excludeInNode
																		.getNodeValue();
																if (null != excludeIn
																		&& 0 < excludeIn
																				.length()) {
																	item.excludeIn = excludeIn
																			.trim();
																}
															}
														} // item节点的属性值不为空时

														String itemText = radioNode
																.getTextContent();
														if (null != itemText
																&& 0 < itemText
																		.length()) {
															item.itemText = itemText;
															// System.out
															// .println("\tcheckBox_item_text="
															// + item.itemText);
														}

														// items.add(item);
														if ("item"
																.equals(radioNode
																		.getNodeName())) {
															rows.add(item);
														}
														// q.getRowsItemArr().add(item);
													} // 假如子节点不为空
												}
												// q.setRowItemArr(items);
											} // 假如radioButton的子节点列表不为空则进行遍历
												// checkBox
										} else if ("matrixRadioButton"
												.equals(tag)
												|| "matrixCheckBox".equals(tag)) {// matrixRadioButton单选矩阵题
											NodeList matxNodeList = qChildrenNode
													.getChildNodes();
											if (null != matxNodeList) {// matrix有子节点列表
												for (int k = 0; k < matxNodeList
														.getLength(); k++) {
													Node matxNode = matxNodeList
															.item(k);
													if (null != matxNode) {// 假如遍历的子节点不为空
														String matRadioTag = matxNode
																.getNodeName();
														if ("rows"
																.equals(matRadioTag)) {
															NodeList rowsList = matxNode
																	.getChildNodes();
															if (null != rowsList) {// rows子节点不为空
																// ArrayList<Item>
																// rows = new
																// ArrayList<Item>();
																for (int l = 0; l < rowsList
																		.getLength(); l++) {
																	Node itemNode = rowsList
																			.item(l);
																	if (null != itemNode) {
																		QuestionItem item = new QuestionItem();
																		NamedNodeMap itemMap = itemNode
																				.getAttributes();
																		if (null != itemMap) {// 属性对象不为空
																			Node valueNode = itemMap
																					.getNamedItem("value");
																			if (null != valueNode) {// value属性节点不为空
																				String value = valueNode
																						.getNodeValue();
																				if (null != value
																						&& 0 < value
																								.length()) {
																					item.itemValue = Integer
																							.parseInt(value);
																				}
																			}
																			/**
																			 * 固定显示
																			 */
																			Node showNode = itemMap
																					.getNamedItem("show");
																			if (null != showNode) {
																				String show = showNode
																						.getNodeValue();
																				if (null != show
																						&& 0 < show
																								.length()) {
																					item.itemShow = Integer
																							.valueOf(show);
																				}
																			}
																			/**
																			 * 复选矩阵排他项
																			 * 复选矩阵行识别exclude
																			 */
																			Node excludeNode = itemMap
																					.getNamedItem("exclude");
																			if (null != excludeNode) {
																				String exclude = excludeNode
																						.getNodeValue();
																				if (null != exclude
																						&& 0 < exclude
																								.length()) {
																					item.exclude = exclude
																							.trim();
																				}
																			}
																			Node itemSingleNode = itemMap
																					.getNamedItem("item_single");
																			if (null != itemSingleNode) {
																				String itemSingle = itemSingleNode
																						.getNodeValue();
																				if (null != itemSingle
																						&& 0 < itemSingle
																								.length()) {
																					item.itemSingle = itemSingle
																							.trim();
																				}
																			}

																			/** 组内随机开始 **/
																			Node gRandomNum = itemMap
																					.getNamedItem("gRandomNum");
																			if (null != gRandomNum) {
																				item.setgRandomNum(gRandomNum
																						.getNodeValue());
																				System.out
																						.println("gRandomNum------->"
																								+ gRandomNum
																										.getNodeValue());
																			}
																			/** 组内随机结束 **/

																			/**
																			 * 选项置顶
																			 * 置底
																			 * 题外关联
																			 * 之
																			 * 字段解析
																			 * 在单选矩阵中的
																			 * 体现
																			 * 大树
																			 * 添加
																			 * 3
																			 */

																			Node paddingNode = itemMap
																					.getNamedItem("padding");
																			if (null != paddingNode) {
																				String padding = paddingNode
																						.getNodeValue();
																				if (null != padding
																						&& 0 < padding
																								.length()) {
																					item.padding = Integer
																							.valueOf(padding);
																					if (item.padding != 0) {
																						q.isItemStick = "1";
																					}
																				}
																			}

																			String itemText = itemNode
																					.getTextContent();
																			if (null != itemText
																					&& 0 < itemText
																							.length()) {
																				item.itemText = itemText;
																				// System.out
																				// .println("\tmatrix_rows_item_text="
																				// +
																				// itemText);
																			}

																			// 隐藏选项
																			Node hideNode = itemMap
																					.getNamedItem("hide");
																			if (null != hideNode) {// value属性节点不为空
																				String hide = hideNode
																						.getNodeValue();
																				if (null != hide
																						&& 0 < hide
																								.length()) {
																					item.hide = Integer
																							.parseInt(hide);
																					// System.out
																					// .print("\tmatrix_rows_item_hide="
																					// +
																					// hide);
																				}
																			}
																			// 条件隐藏选项
																			Node hideListNode = itemMap
																					.getNamedItem("hideList");
																			if (null != hideListNode) {
																				String hideList = hideListNode
																						.getNodeValue();
																				if (null != hideList
																						&& 0 < hideList
																								.length()) {
																					item.hideList = hideList;
																				}
																			}
																			Node itemHideType = itemMap
																					.getNamedItem("hideType");
																			if (null != itemHideType) {
																				String hideType = itemHideType
																						.getNodeValue();
																				if (!Util
																						.isEmpty(hideType)) {
																					item.hideType = Integer
																							.parseInt(hideType);
																				}
																			}
																		}
																		if ("item"
																				.equals(itemNode
																						.getNodeName())) {
																			rows.add(item);
																		}
																		// q.getRowsItemArr().add(item);
																	}
																}
																// q.setRowsItemArr(rows);
															}
														} else if ("columns"
																.equals(matRadioTag)) {// columns节点
															NamedNodeMap colMp = matxNode
																	.getAttributes();
															if (null != colMp) {
																Node dirNode = colMp
																		.getNamedItem("direction");
																if (null != dirNode) {
																	String direction = dirNode
																			.getNodeValue();
																	if (null != direction
																			&& 0 < direction
																					.length()) {
																		q.qColumnsDirection = direction;
																		// System.out
																		// .println("\tmatrix_columns_direction="
																		// +
																		// direction);
																	}
																}
															}

															NodeList columnsList = matxNode
																	.getChildNodes();
															if (null != columnsList) {// columns子节点不为空
																ArrayList<QuestionItem> columns = new ArrayList<QuestionItem>();
																for (int l = 0; l < columnsList
																		.getLength(); l++) {
																	Node itemNode = columnsList
																			.item(l);
																	if (null != itemNode) {
																		QuestionItem item = new QuestionItem();
																		NamedNodeMap itemMap = itemNode
																				.getAttributes();
																		if (null != itemMap) {// 属性对象不为空
																			Node comparisonNode = itemMap
																					.getNamedItem("Comparison");
																			if (null != comparisonNode) {// value属性节点不为空
																				String comparison = comparisonNode
																						.getNodeValue();
																				if (null != comparison
																						&& 0 < comparison
																								.length()) {
																					item.comparison = Integer
																							.parseInt(comparison
																									.trim());
																					// System.out
																					// .println("\tmatrix_columns_item_comparison="
																					// +
																					// comparison);
																				}
																			}
																			Node valueNode = itemMap
																					.getNamedItem("value");
																			if (null != valueNode) {// value属性节点不为空
																				String value = valueNode
																						.getNodeValue();
																				if (null != value
																						&& 0 < value
																								.length()) {
																					item.itemValue = Integer
																							.parseInt(value);
																					// System.out
																					// .print("\tmatrix_columns_item_value="
																					// +
																					// value);
																				}
																			}
																			// 排斥
																			Node excludeNode = itemMap
																					.getNamedItem("exclude");
																			if (null != excludeNode) {
																				String exclude = excludeNode
																						.getNodeValue();
																				if (null != exclude
																						&& 0 < exclude
																								.length()) {
																					item.exclude = exclude
																							.trim();
																					// System.out
																					// .print("\tcheckBox_item_value="
																					// +
																					// item.exclude);
																				}
																			}
																			// 预设选中
																			Node deftNode = itemMap
																					.getNamedItem("default");
																			if (null != deftNode) {
																				String deft = deftNode
																						.getNodeValue();
																				if (null != deft
																						&& "checked"
																								.equals(deft
																										.trim())) {
																					item.deft = 1;

																				} else {
																					item.deft = 0;
																					Log.i("zrl",
																							"item.deft=="
																									+ item.deft);
																				}

																			}
																			// 选项排他
																			Node excludeInNode = itemMap
																					.getNamedItem("exclude_in");
																			if (null != excludeInNode) {
																				String excludeIn = excludeInNode
																						.getNodeValue();
																				if (null != excludeIn
																						&& 0 < excludeIn
																								.length()) {
																					item.excludeIn = excludeIn
																							.trim();
																				}
																			}
																		}
																		String itemText = itemNode
																				.getTextContent();
																		if (null != itemText
																				&& 0 < itemText
																						.length()) {
																			item.itemText = itemText;
																			// System.out
																			// .println("\tmatrixR_columns_item_text="
																			// +
																			// itemText);
																		}
																		if (!Util
																				.isEmpty(itemText)
																				|| null != itemMap) {
																			columns.add(item);
																		}
																		// q.getColumnsItemArr().add(item);
																	}
																}
																q.setColItemArr(columns);
															}
														}
														// 矩阵右侧
														else if ("rowsRight".equals(matRadioTag)) {
															QuestionItem item = new QuestionItem();
															ArrayList<QuestionItem> colItemArr = q.getColItemArr();
															colItemArr.add(item);
															q.setColItemArr(colItemArr);
															NodeList rowsList = matxNode.getChildNodes();

															if (null != rowsList) {
																q.isRight = 1;
																int r = 0;
																for (int l = 0; l < rowsList.getLength(); l++) {
																	Node itemNode = rowsList.item(l);

																	if (null != itemNode&& "item".equals(itemNode.getNodeName())) {
																		if (r < rows.size()) {
																			QuestionItem questionItem = rows.get(r);
																			r++;
																			if (!Util.isEmpty(itemNode.getTextContent())) {
																				String itemText = itemNode.getTextContent();
																				if (null != itemText&& 0 < itemText.length()) {
																					questionItem.itemTextRight = itemText;

																				}
																			}
																		}
																	}
																}
															}
														}
														// 矩阵右侧结束
													}
												}
											}
											// matrix矩阵题

										} else if ("freeTextArea".equals(tag)) {// freeTextArea
											NamedNodeMap areaMap = qChildrenNode
													.getAttributes();
											if (null != areaMap) {
												Node rowNode = areaMap
														.getNamedItem("rows");
												if (null != rowNode) {
													String rs = rowNode
															.getNodeValue();
													if (null != rs
															&& 0 < rs.length()) {
														q.textAreaRows = Integer
																.parseInt(rs);
														// System.out
														// .println("freeTextArea_rows="
														// + rs);
													}
												}
											}
											NodeList areaNodeList = qChildrenNode
													.getChildNodes();
											if (null != areaNodeList) {
												// ArrayList<Item> items = new
												// ArrayList<Item>();
												for (int k = 0; k < areaNodeList
														.getLength(); k++) {
													Node itemNode = areaNodeList
															.item(k);
													if (null != itemNode) {
														QuestionItem item = new QuestionItem();
														String itemText = itemNode
																.getTextContent();
														if (null != itemText
																&& 0 < itemText
																		.length()) {
															item.itemText = itemText;
															// System.out
															// .print("\tfreeTextArea_text="
															// + itemText);
														}
														if ("item"
																.equals(itemNode
																		.getNodeName())) {
															rows.add(item);
														}
													}
												}
												// q.setRowItemArr(items);
											}
										} else if ("dropDownList".equals(tag)) {
											NamedNodeMap dropMap = qChildrenNode
													.getAttributes();
											if (null != dropMap) {
												Node ignoreNode = dropMap
														.getNamedItem("ignoreFirstItem");
												if (null != ignoreNode) {
													String ignore = ignoreNode
															.getNodeValue();
													if (null != ignore
															&& 0 < ignore
																	.length()) {
														q.ignoreFirstItem = Boolean
																.parseBoolean(ignore) ? 1
																: 0;
														// System.out
														// .println("dropDownList_ignore="
														// + ignore);
													}
												}
												Node firstTextNode = dropMap
														.getNamedItem("firstText");
												if (null != firstTextNode) {
													String firstText = firstTextNode
															.getNodeValue();
													if (null != firstText
															&& 0 < firstText
																	.length()) {
														q.firstText = firstText
																.trim();
														// System.out
														// .println("dropDownList_firstText="
														// + firstText);
													}
												}
											}
											NodeList itemNodeList = qChildrenNode
													.getChildNodes();
											if (null != itemNodeList) {
												ArrayList<QuestionItem> items = new ArrayList<QuestionItem>();
												for (int k = 0; k < itemNodeList
														.getLength(); k++) {
													Node itemNode = itemNodeList
															.item(k);
													if (null != itemNode) {
														QuestionItem item = new QuestionItem();
														NamedNodeMap itemMap = itemNode
																.getAttributes();
														if (null != itemMap) {
															Node valueNode = itemMap
																	.getNamedItem("value");
															if (null != valueNode) {
																String value = valueNode
																		.getNodeValue();
																if (null != value
																		&& 0 < value
																				.length()) {
																	item.itemValue = Integer
																			.parseInt(value);
																	// System.out
																	// .print("\tdropDownList_item_value="
																	// + value);
																}
															}

															// 预设选中
															Node deftNode = itemMap
																	.getNamedItem("default");
															if (null != deftNode) {
																String deft = deftNode
																		.getNodeValue();
																if (null != deft
																		&& "checked"
																				.equals(deft)) {
																	item.deft = 1;

																} else {
																	item.deft = 0;
																	Log.i("zrl",
																			"item.deft=="
																					+ item.deft);
																}

															}
														}
														String itemText = itemNode
																.getTextContent();
														if (null != itemText
																&& 0 < itemText
																		.length()) {
															item.itemText = itemText;
															// System.out
															// .println("\tdropDownList_item_text="
															// + itemText);
														}
														// q.getColumnsItemArr().add(item);
														if (null != itemMap) {
															items.add(item);
														}
													}
												}
												q.setColItemArr(items);
											}
										} else if ("restriction".equals(tag)) {// 引用其它问题的答案
											Restriction rest = new Restriction();
											NamedNodeMap restrMap = qChildrenNode
													.getAttributes();
											if (null != restrMap) {// map
												Node matchNode = restrMap
														.getNamedItem("Match");
												if (null != matchNode) {
													String m = matchNode
															.getNodeValue();
													if (null != m
															&& 0 < m.length()) {
														rest.setMatch(m);
													}
												}
												Node RIDNode = restrMap
														.getNamedItem("RID");
												if (null != RIDNode) {
													String RID = RIDNode
															.getNodeValue();
													if (null != RID
															&& 0 < RID.length()) {
														rest.setRID(RID);
													}
												}
												Node qIdNode = restrMap
														.getNamedItem("questionID");
												if (null != qIdNode) {
													String qid = qIdNode
															.getNodeValue();
													if (null != qid
															&& 0 < qid.length()) {
														rest.setQuestionId(Integer
																.parseInt(qid));
													}
												}
												Node matchCompareNode = restrMap
														.getNamedItem("MatchCompare");
												if (null != matchCompareNode) {
													String mc = matchCompareNode
															.getNodeValue();
													if (null != mc
															&& 0 < mc.length()) {
														rest.setMatchCompare(mc);
													}
												}

												Node matchNumNode = restrMap
														.getNamedItem("MatchNum");
												if (null != matchNumNode) {
													String matchNum = matchNumNode
															.getNodeValue();
													if (null != matchNum
															&& 0 < matchNum
																	.length()) {
														rest.setMatchNum(Integer
																.parseInt(matchNum));

														System.out
																.println("xml穿过来的值++++++++++++++++++++++"
																		+ matchNum);
													}
												}
											} // map

											NodeList valueNodeList = qChildrenNode
													.getChildNodes();
											if (null != valueNodeList) {
												ArrayList<RestrictionValue> rvs = new ArrayList<RestrictionValue>();
												for (int k = 0; k < valueNodeList
														.getLength(); k++) {
													Node valueNode = valueNodeList
															.item(k);
													if (null == valueNode
															|| !"value"
																	.equals(valueNode
																			.getNodeName())) {
														continue;
													}
													RestrictionValue rv = new RestrictionValue();
													NamedNodeMap valueNNM = valueNode
															.getAttributes();
													if (null != valueNNM) {
														Node rowsNode = valueNNM
																.getNamedItem("rows");
														if (null != rowsNode
																&& !Util.isEmpty(rowsNode
																		.getNodeValue())) {
															rv.setRows(Integer
																	.parseInt(rowsNode
																			.getNodeValue()));
														}
														Node equalText1Node = valueNNM
																.getNamedItem("EqualText1");
														if (null != equalText1Node
																&& !Util.isEmpty(equalText1Node
																		.getNodeValue())) {
															String et1Value = equalText1Node
																	.getNodeValue();
															String regexp = "\'";
															et1Value = et1Value
																	.replaceAll(
																			regexp,
																			"");
															rv.setEqualText1(et1Value);
														}
														Node equalText2Node = valueNNM
																.getNamedItem("EqualText2");
														if (null != equalText2Node
																&& !Util.isEmpty(equalText2Node
																		.getNodeValue())) {
															String et2Value = equalText2Node
																	.getNodeValue();
															String regexp = "\'";
															et2Value = et2Value
																	.replaceAll(
																			regexp,
																			"");
															rv.setEqualText2(et2Value);
														}
														Node equal1Node = valueNNM
																.getNamedItem("Equal1");
														if (null != equal1Node
																&& !Util.isEmpty(equal1Node
																		.getNodeValue())) {
															rv.setEqual1(equal1Node
																	.getNodeValue());
														}
														Node equal2Node = valueNNM
																.getNamedItem("Equal2");
														if (null != equal2Node
																&& !Util.isEmpty(equal2Node
																		.getNodeValue())) {
															rv.setEqual2(equal2Node
																	.getNodeValue());
														}
														Node matchEqualNode = valueNNM
																.getNamedItem("MatchEqual");
														if (null != matchEqualNode
																&& !Util.isEmpty(matchEqualNode
																		.getNodeValue())) {
															rv.setMatchEqual(matchEqualNode
																	.getNodeValue());
														}

														// 非逻辑
														Node exclusiveNode = valueNNM
																.getNamedItem("exclusive");
														if (null != exclusiveNode
																&& !Util.isEmpty(exclusiveNode
																		.getNodeValue())) {
															String exclusiveValue = exclusiveNode
																	.getNodeValue();
															rv.setExclusive(Boolean
																	.parseBoolean(exclusiveValue));
														}
													}
													System.out
															.println("valueNode.getTextContent()="
																	+ valueNode
																			.getTextContent());
													if (!Util.isEmpty(valueNode
															.getTextContent())) {
														rv.setValue(valueNode
																.getTextContent());
													}
													rvs.add(rv);
												}
												rest.setRvs(rvs);
											}
											if (null != rest) {
												// System.out.println("rest--->"
												// + rest.getQuestionId());
												restrs.add(rest);
											}
										} else if ("qgRestriction".equals(tag)) {// 复杂逻辑
																					// qgRestriction
											QgRestriction qgRest = new QgRestriction();
											NamedNodeMap qgRestrMap = qChildrenNode
													.getAttributes();// 节点属性
																		// qindex="1"
																		// qMatch="all"
											if (null != qgRestrMap) {
												Node qindexNode = qgRestrMap
														.getNamedItem("qindex");
												if (null != qindexNode) {
													String qindex = qindexNode
															.getNodeValue();
													if (null != qindex
															&& 0 < qindex
																	.length()) {
														qgRest.setQindex(Integer
																.parseInt(qindex));
													}
												}
												Node qMatchNode = qgRestrMap
														.getNamedItem("qMatch");
												if (null != qMatchNode) {
													String qMatch = qMatchNode
															.getNodeValue();
													if (null != qMatch
															&& 0 < qMatch
																	.length()) {
														qgRest.setQMatch(qMatch);
													}
												}
											}
											NodeList gRestNodeList = qChildrenNode
													.getChildNodes();// 子节点
											if (null != gRestNodeList) {
												for (int k = 0; k < gRestNodeList
														.getLength(); k++) {
													Node gRestNode = gRestNodeList
															.item(k);
													if (null == gRestNode) {
														continue;
													}
													GRestriction gRest = new GRestriction();
													NamedNodeMap gRestNNM = gRestNode
															.getAttributes();
													if (null != gRestNNM) { // gMatch="all"
																			// gindex="0"
																			// pqindex="0"
														Node gMatchNode = gRestNNM
																.getNamedItem("gMatch");
														if (null != gMatchNode
																&& !Util.isEmpty(gMatchNode
																		.getNodeValue())) {
															gRest.setGMatch(gMatchNode
																	.getNodeValue());
														}
														Node gindexNode = gRestNNM
																.getNamedItem("gindex");
														if (null != gindexNode
																&& !Util.isEmpty(gindexNode
																		.getNodeValue())) {
															gRest.setGindex(Integer
																	.parseInt(gindexNode
																			.getNodeValue()));
														}
														Node pqindexNode = gRestNNM
																.getNamedItem("pqindex");
														if (null != pqindexNode
																&& !Util.isEmpty(pqindexNode
																		.getNodeValue())) {
															gRest.setPqindex(Integer
																	.parseInt(pqindexNode
																			.getNodeValue()));
														}
													}
													NodeList RIDNodeList = gRestNode
															.getChildNodes();// 子节点
													if (null != RIDNodeList) {
														ArrayList<RID> RIDs = new ArrayList<RID>();
														for (int l = 0; l < RIDNodeList
																.getLength(); l++) {
															Node RIDNode = RIDNodeList
																	.item(l);
															if (null == RIDNode) {
																continue;
															}
															RID rid = new RID();
															if (!Util
																	.isEmpty(RIDNode
																			.getTextContent())) {
																rid.setValue(RIDNode
																		.getTextContent());
															}
															RIDs.add(rid);
														}
														gRest.setRIDs(RIDs);
													}
													if (null != gRest) {
														// System.out
														// .println("grest--->"
														// +
														// gRest.getPqindex());
														gRestrs.add(gRest);
													}
												}
											}
											if (null != qgRest) {
												// System.out.println("qgRest--->"
												// + qgRest.getQindex());
												qgRestArr.add(qgRest);
											}
										} else if ("image".equals(tag)) {// 图片题型
											NamedNodeMap imgMap = qChildrenNode
													.getAttributes();
											if (null != imgMap) {// src
												Node srcNode = imgMap
														.getNamedItem("src");
												if (null != srcNode) {
													String src = srcNode
															.getNodeValue();
													if (null != src
															&& 0 < src.length()) {
														q.qMediaSrc = src;
													}
												}
											} // src
											NodeList sizeList = qChildrenNode
													.getChildNodes();
											if (null != sizeList) {
												for (int k = 0; k < sizeList
														.getLength(); k++) {
													Node sizeItemNode = sizeList
															.item(k);
													if (null != sizeItemNode) {
														// "height".equals()
														String sizeTag = sizeItemNode
																.getNodeName();
														if ("height"
																.equals(sizeTag)) {
															String h = sizeItemNode
																	.getTextContent();
															if (null != h
																	&& 0 < h.length()) {
																q.qMediaHeight = Integer
																		.parseInt(h);
															}
														} else if ("width"
																.equals(sizeTag)) {
															String w = sizeItemNode
																	.getTextContent();
															if (null != w
																	&& 0 < w.length()) {
																q.qMediaWidth = Integer
																		.parseInt(w);
															}
														}
													}
												}
											}
										} // 图片题型

									} // quesiion子节点不为空的
								}
							} // quesiion子节点列表不为空的
							/**
							 * 假如其他项
							 */
							// q.setFreeItemArr(freeInputItems);
							q.setRowItemArr(rows);
							q.setResctItemArr(restrs);
							q.setGRestArr(gRestrs);
							q.setQgRestItemArr(qgRestArr);
							sq.getQuestions().add(q);
							// 数据字典
							sq.setClassId(classIdList);
						} else if ("property".equals(name)) {// property节点
							NodeList pptyNL = nd.getChildNodes();
							if (null != pptyNL) {// if (null != pptyNL) {
								for (int j = 0; j < pptyNL.getLength(); j++) {// for
									Node propertyChild = pptyNL.item(j);

									if (null == propertyChild) {
										continue;
									}
									Util.Log("name="
											+ propertyChild.getNodeName()
											+ ", value="
											+ propertyChild.getTextContent());
									final String propertyChildName = propertyChild
											.getNodeName();
									// 访前说明
									if ("homepage".equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										// System.out.println("wordvalue:"+value);
										sq.setWord(value);
									} else if ("showpageStatus"
											.equals(propertyChildName)) {
										if (ISPAGE) {
											sq.setshowpageStatus(0);
										} else {
											String value = propertyChild
													.getTextContent();
											// System.out.println("wordvalue:"+value);
											int showpage = Util.isEmpty(value) ? 1
													: Integer.parseInt(value);
											sq.setshowpageStatus(showpage);
										}
									} else if ("backPassword"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										// System.out.println("wordvalue:"+value);
										sq.setBackPassword(value);
									}

									else if ("photoSource"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();

										System.out
												.println("photoSource--------++++++------>>>>"
														+ value);
										sq.setPhotoSource(value);
									} else if ("backPageFlag"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();

										System.out
												.println("backPageFlag--------++++++------>>>>"
														+ value);
										sq.setBackPageFlag(value);
									} else if ("AppModify"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										// System.out.println("wordvalue:"+value);
										int appModify = Util.isEmpty(value) ? 0
												: Integer.parseInt(value);
										sq.setAppModify(appModify);
									} else if ("AppPreview"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										// System.out.println("wordvalue:"+value);
										int appPreview = Util.isEmpty(value) ? 0
												: Integer.parseInt(value);
										sq.setAppPreview(appPreview);
									} else if ("visitPreview"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										// System.out.println("wordvalue:"+value);
										int visitPreview = Util.isEmpty(value) ? 0
												: Integer.parseInt(value);
										sq.setVisitPreview(visitPreview);
									} else if ("AppAutomaticUpload"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										int appAutomaticUpload = Util
												.isEmpty(value) ? 0 : Integer
												.parseInt(value);
										sq.setAppAutomaticUpload(appAutomaticUpload);
									} else if ("openGPS"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										int openGPS = Util.isEmpty(value) ? 0
												: Integer.parseInt(value);
										sq.setOpenGPS(openGPS);
									} else if ("timeInterval"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										int timeInterval = Util.isEmpty(value) ? 0
												: Integer.parseInt(value);
										sq.setTimeInterval(timeInterval);
									} else if ("forceGPS"
											.equals(propertyChildName)) {// 强制定位
										String value = propertyChild
												.getTextContent();
										// 空或0为不强制定位，1为开启强制定位
										int forceGPS = Util.isEmpty(value) ? 0
												: Integer.parseInt(value);
										sq.setForceGPS(forceGPS);
									}
									// 访前说明
									else if ("eligible"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										sq.setEligible(Util.isEmpty(value) ? -1
												: Integer.parseInt(value));
										// break;
									} else if ("title"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										sq.setSurveyTitle(value);
									} else if ("TestType"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										// 空或0为未开启测试模式，1 开启测试模式
										int testType = Util.isEmpty(value) ? 0
												: Integer.parseInt(value);
										sq.setTestType(testType);
									} else if ("showQindex"
											.equals(propertyChildName)) {
										String value = propertyChild
												.getTextContent();
										// 指定要在样本列表显示答案的题号
										int showQindex = Util.isEmpty(value) ? -1
												: Integer.parseInt(value);
										sq.setShowQindex(showQindex);
									}

									// 设置密码节点
									// else
									// if("AndroidNewPSEnableChar".equals(propertyChild.getNodeName())){
									// String pwdValue =
									// propertyChild.getTextContent();
									// sq.setPassword(Util.isEmpty(pwdValue)?"":pwdValue);
									// }
									else if ("qgroup".equals(propertyChildName)) {// 题组随机解析
										/**
										 * 大题组节点
										 */
										NamedNodeMap qgroupNNM = propertyChild
												.getAttributes();
										if (null == qgroupNNM) {
											continue;
										}
										QGroup qg = new QGroup();
										Node orderNode = qgroupNNM
												.getNamedItem("order");
										if (null != orderNode) {// order
											if (!Util.isEmpty(orderNode
													.getNodeValue())) {
												qg.setOrder(Integer.parseInt(orderNode
														.getNodeValue()));
											}
										} // order

										Node qgroupNameNode = qgroupNNM
												.getNamedItem("qgroupName");
										if (null != qgroupNameNode) {// qgroupName
											qg.setGroupName(qgroupNameNode
													.getNodeValue());
										} // qgroupName

										Node randomNode = qgroupNNM
												.getNamedItem("random");
										if (null != randomNode) {// random
											if (!Util.isEmpty(randomNode
													.getNodeValue())) {
												qg.setRandom(Boolean
														.parseBoolean(randomNode
																.getNodeValue()));
											}
										} // random

										Node groupTypeNode = qgroupNNM
												.getNamedItem("qgroupType");
										if (null != groupTypeNode) {// qgroupType
											if (!Util.isEmpty(groupTypeNode
													.getNodeValue())) {
												/**
												 * 不循环
												 */
												if ("none".equals(groupTypeNode
														.getNodeValue())) {
													qg.setGroupType(QGroup.GROUP_TYPE_NONE);
												} else if ("auto"
														.equals(groupTypeNode
																.getNodeValue())) {
													/**
													 * 自动循环
													 */
													qg.setGroupType(QGroup.GROUP_TYPE_AUTO);
												} else if ("hand"
														.equals(groupTypeNode
																.getNodeValue())) {
													/**
													 * 手动循环
													 */
													qg.setGroupType(QGroup.GROUP_TYPE_HAND);
												}
											}
										} // qgroupType
										Node reQgroupNode = qgroupNNM
												.getNamedItem("reqgroup");
										if (null != reQgroupNode) {// qgroupType
											if (!Util.isEmpty(reQgroupNode
													.getNodeValue())) {
												if ("checked"
														.equals(reQgroupNode
																.getNodeValue()
																.trim())) {
													qg.setReQgroup(1);
												}
											}
										} // qgroupType
										/**
										 * 处理qgroup的子几点group
										 */
										NodeList groupNodes = propertyChild
												.getChildNodes();
										if (null == groupNodes) {
											continue;
										}
										for (int k = 0; k < groupNodes
												.getLength(); k++) {
											Node goupNode = groupNodes.item(k);
											if (null == goupNode) {
												continue;
											}
											NamedNodeMap groupNNM = goupNode
													.getAttributes();
											if (null == groupNNM) {
												continue;
											}
											Group group = new Group();
											Node gorderNode = groupNNM
													.getNamedItem("order");
											if (null != gorderNode) {// order
												if (!Util.isEmpty(gorderNode
														.getNodeValue())) {
													/**
													 * 小题组随机的顺序号
													 */
													group.setOrder(Integer
															.parseInt(gorderNode
																	.getNodeValue()));
												}
											} // order

											Node grandomNode = groupNNM
													.getNamedItem("random");
											if (null != grandomNode) {// random
												if (!Util.isEmpty(grandomNode
														.getNodeValue())) {
													/**
													 * 小题组随机是否随机
													 */
													group.setRandom(Boolean
															.parseBoolean(grandomNode
																	.getNodeValue()));
												}
											} // random

											Node groupNameNode = groupNNM
													.getNamedItem("groupname");
											if (null != groupNameNode) {// groupname
												if (!Util.isEmpty(groupNameNode
														.getNodeValue())) {
													group.setGroupName(groupNameNode
															.getNodeValue());
												}
											} // groupname

											Node globalgroupNode = groupNNM
													.getNamedItem("globalgroup_");
											if (null != globalgroupNode) {// globalgroup_
												if (!Util
														.isEmpty(globalgroupNode
																.getNodeValue())) {
													group.setGlobalGroup(globalgroupNode
															.getNodeValue());
												}
											} // globalgroup_

											NodeList indexNodes = goupNode
													.getChildNodes();
											if (null != indexNodes) {
												for (int l = 0; l < indexNodes
														.getLength(); l++) {
													Node indexNode = indexNodes
															.item(l);
													if (null == indexNode) {
														continue;
													}
													if (!Util.isEmpty(indexNode
															.getTextContent())) {
														String textIndex = indexNode
																.getTextContent();
														int tempOrder = -1;
														ArrayList<Question> questions = sq
																.getQuestions();
														for (Question q : questions) {
															String tempIndex = q.qIndex
																	+ "";
															if (tempIndex
																	.equals(textIndex)) {
																tempOrder = q.qOrder;
																break;
															}
														}
														if (0 == k && 0 == l) {
															/**
															 * 指定是哪一道题出现题组随机
															 * 小题组的第一个
															 * 第一个序列号就是这个大组随机的位置
															 */
															if (-1 != tempOrder) {
																qg.setRealIndex(indexNode
																		.getTextContent());
																qg.setIndex(tempOrder
																		+ "");
															}

														}
														group.getIndexArr()
																.add(tempOrder);
													}
												}
												group.setIndexArr(group
														.getIndexArr());
											}
											qg.getGroups().add(group);
										} // 循环group的节点
											// 此处set为了转json串
										qg.setGroups(qg.getGroups());
										sq.getQgs().add(qg);
									} // 题组随机解析
										// 逻辑跳转解析开始
									else if ("logicJumps".equals(propertyChild
											.getNodeName())) {
										/**
										 * 处理logic
										 */
										// 实例化
										LogicList logicList = new LogicList();
										NodeList logicNodes = propertyChild
												.getChildNodes();
										if (null == logicNodes) {
											continue;
										}
										for (int k = 0; k < logicNodes
												.getLength(); k++) {
											Node logicNode = logicNodes.item(k);
											if (null == logicNode) {
												continue;
											}
											NamedNodeMap logicNNM = logicNode
													.getAttributes();
											if (null == logicNNM) {
												continue;
											}
											// 实体
											Logic logic = new Logic();
											Node jumpIndexNode = logicNNM
													.getNamedItem("jumpIndex");
											if (null != jumpIndexNode) {// order
												if (!Util.isEmpty(jumpIndexNode
														.getNodeValue())) {
													/**
													 * 要跳到哪个题目
													 */
													Log.i("kjy",
															"要跳到哪个题目:"
																	+ Integer
																			.parseInt(jumpIndexNode
																					.getNodeValue()));
													logic.setJumpIndex(Integer
															.parseInt(jumpIndexNode
																	.getNodeValue()));
													// group.setOrder(Integer.parseInt(jumpIndexNode.getNodeValue()));
												}
											}
											Node countJumpNode = logicNNM
													.getNamedItem("countJump");
											if (null != countJumpNode) {// order
												if (!Util.isEmpty(countJumpNode
														.getNodeValue())) {
													/**
													 * 要跳到哪个题目
													 */
													Log.i("kjy",
															"几道题目后跳:"
																	+ Integer
																			.parseInt(countJumpNode
																					.getNodeValue()));
													logic.setCountJump(Integer
															.parseInt(countJumpNode
																	.getNodeValue()));
													// group.setOrder(Integer.parseInt(jumpIndexNode.getNodeValue()));
												}
											}
											NodeList itemNodes = logicNode
													.getChildNodes();
											if (null == itemNodes) {
												continue;
											}
											for (int l = 0; l < itemNodes
													.getLength(); l++) {
												Node itemNode = itemNodes
														.item(l);
												if (null == itemNode) {
													continue;
												}
												NamedNodeMap itemNNM = itemNode
														.getAttributes();
												if (null == itemNNM) {
													continue;
												}
												LogicItem logicItem = new LogicItem();
												// 实体
												Node logicIndexNode = itemNNM
														.getNamedItem("logicIndex");
												if (null != logicIndexNode) {// order
													if (!Util
															.isEmpty(logicIndexNode
																	.getNodeValue())) {
														/**
														 * 要跳到哪个题目
														 */
														Log.i("kjy",
																"这个逻辑中包含的index:"
																		+ Integer
																				.parseInt(logicIndexNode
																						.getNodeValue()));
														logicItem
																.setLogicIndex(Integer
																		.parseInt(logicIndexNode
																				.getNodeValue()));
														// group.setOrder(Integer.parseInt(jumpIndexNode.getNodeValue()));
													}
												}
												Node logicValueNode = itemNNM
														.getNamedItem("logicValue");
												if (null != logicValueNode) {// order
													if (!Util
															.isEmpty(logicValueNode
																	.getNodeValue())) {
														/**
														 * 要跳到哪个题目
														 */
														Log.i("kjy",
																"题目的值是:"
																		+ logicValueNode
																				.getNodeValue());
														logicItem
																.setLogicValue(logicValueNode
																		.getNodeValue());
														// group.setOrder(Integer.parseInt(jumpIndexNode.getNodeValue()));
													}
												}
												logic.getLogicItem().add(
														logicItem);
											}
											logic.setLogicItem(logic
													.getLogicItem());
											logicList.getLogics().add(logic);
										}
										logicList.setLogics(logicList
												.getLogics());
										sq.setLogicList(logicList);
									}
									// 逻辑跳转解析结束
								} // for
							} // if (null != pptyNL) {
								// property节点end.
							sq.setQgs(sq.getQgs());
							// 逻辑跳转解析
						} // question问题节点

					} // 2.如果遍历的节点不为空
				}
			} // 1.如果文档中的节点列表不为空
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sq;
	}

	/**
	 * 将json字符串转成json集合
	 * 
	 * @param json
	 * @return
	 */
	public static ArrayList<QuestionItem> str2JsonArr(String json) {
		return (ArrayList<QuestionItem>) JSON.parseArray(json,
				QuestionItem.class);
	}

	/**
	 * 将json集合转成json字符串
	 * 
	 * @param items
	 * @return
	 */
	public static String jsonArr2Str(ArrayList<QuestionItem> items) {
		return GsonUtil.GsonToString(items);
	}

	/**
	 * 将json字符串转成json集合
	 * 
	 * @param json
	 * @return
	 */
	public static ArrayList<AnswerMap> mapStr2JsonArr(String json) {
		return (ArrayList<AnswerMap>) JSON.parseArray(json, AnswerMap.class);
	}

	/**
	 * 将json字符串转成json集合
	 * 
	 * @param json
	 * @return
	 */
	public static ArrayList<Option> listStr2JsonArr(String json) {
		return (ArrayList<Option>) JSON.parseArray(json, Option.class);
	}

	/**
	 * 将json集合转成json字符串
	 * 
	 * @param items
	 * @return
	 */
	public static String jsonArr2MapStr(ArrayList<AnswerMap> maps) {
		return GsonUtil.GsonToString(maps);
	}

	public static String jsonArr2optionStr(ArrayList<Option> opt) {
		return GsonUtil.GsonToString(opt);
	}

	public static String jsonArr2RestrictionStr(ArrayList<Restriction> rs) {
		return GsonUtil.GsonToString(rs);
	}

	public static String jsonArr2QgRestrictionStr(ArrayList<QgRestriction> qgrs) {
		return GsonUtil.GsonToString(qgrs);
	}

	public static ArrayList<Restriction> restrictionStr2JsonArr(String json) {
		return (ArrayList<Restriction>) JSON
				.parseArray(json, Restriction.class);
	}

	public static ArrayList<QgRestriction> qGRestrictionStr2JsonArr(String json) {
		return (ArrayList<QgRestriction>) JSON.parseArray(json,
				QgRestriction.class);
	}

	// 访问状态
	public static ArrayList<ReturnType> returnTypeStr2JsonArr(String json) {
		return (ArrayList<ReturnType>) JSON.parseArray(json, ReturnType.class);
	}

	public static String jsonArr2ReturnTypeStr(ArrayList<ReturnType> rs) {
//		return JSON.toJSONString(rs);
		return GsonUtil.GsonToString(rs);
	}

	// 访问状态结束

	/**
	 * 将内部名单转成json字符串
	 * 
	 * @param ip
	 * @return
	 */
	public static String paserInnerPanel2Json(InnerPanel ip) {
		if (null == ip) {
			return null;
		}
		return GsonUtil.GsonToString(ip);
	}

	/**
	 * 将json字符串转换成InnerPanel对象
	 * 
	 * @param str
	 * @return
	 */
	public static InnerPanel parserJson2InnerPanel(String str) {
		if (Util.isEmpty(str)) {
			return null;
		}
		return (InnerPanel) JSON.parseObject(str, InnerPanel.class);
	}

	public static String parserRestValueArr2Json(ArrayList<RestrictionValue> vs) {
		return GsonUtil.GsonToString(vs);
	}

	public static ArrayList<RestrictionValue> parserJson2RestValueArr(
			String json) {
		return (ArrayList<RestrictionValue>) JSON.parseArray(json,
				RestrictionValue.class);
	}

	public static String parserGRestRIDArr2Json(ArrayList<RID> vs) {
		return GsonUtil.GsonToString(vs);
	}

	public static ArrayList<RID> parserJson2GRestRIDArr(String json) {
		return (ArrayList<RID>) JSON.parseArray(json, RID.class);
	}

	public static String parserGRestArr2Json(ArrayList<GRestriction> vs) {
		return GsonUtil.GsonToString(vs);
	}

	public static ArrayList<GRestriction> parserJson2GRestArr(String json) {
		return (ArrayList<GRestriction>) JSON.parseArray(json,
				GRestriction.class);
	}

	/**
	 * 干预
	 * 
	 * @param vs
	 * @return
	 */
	public static String parserIntervention2Json(Intervention intervention) {
		return GsonUtil.GsonToString(intervention);
	}

	/**
	 * 干预
	 * 
	 * @param <E>
	 * @param json
	 * @return
	 */
	public static Intervention parserJson2Intervention(String json) {
		return (Intervention) JSON.parseObject(json, Intervention.class);
	}

	/**
	 * 逻辑次数跳转将json字符串转换成LogicList对象
	 * 
	 * @param str
	 * @return
	 */
	public static LogicList parserJsonToLogicList(String str) {
		if (Util.isEmpty(str)) {
			return null;
		}
		return (LogicList) JSON.parseObject(str, LogicList.class);
	}

	/**
	 * 将指定问题的题组随机串转换成QGroup对象
	 * 
	 * @param json
	 * @return
	 */
	public static QGroup parserJson2QGroup(String json) {
		return JSON.parseObject(json, QGroup.class);
	}

	/**
	 * 将QGroup转换成题组随机json串
	 * 
	 * @param qg
	 * @return
	 */
	public static String parserQGroup2Json(QGroup qg) {
		return JSON.toJSONString(qg);
	}

	/**
	 * 逻辑次数跳转 将LogicList转换成题组随机json串
	 * 
	 * @param qg
	 * @return
	 */
	public static String parserLogicList2Json(LogicList logicList) {
		return JSON.toJSONString(logicList);
	}

	public static LogicList parserJson2LogicList(String json) {
		return JSON.parseObject(json, LogicList.class);
	}

	/**
	 * 逻辑跳转结束(大哥 在那复制的啊 这都解析xml了 还玩逻辑呢) 解析名单接口
	 * 
	 * @param inStream
	 * @return
	 */

	public static OpenStatus ParserInnerPanelList(InputStream inStream) {
		OpenStatus os = new OpenStatus();
		if (null == inStream) {
			return os;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			// 命名规则开始
			String nameType = element.getAttribute("nameType");
			if (!Util.isEmpty(nameType)) {
				os.setParameterName(nameType);
			}
			// 命名规则结束
			NodeList users = element.getChildNodes();
			if (null == users) {
				return os;
			}
			for (int i = 0; i < users.getLength(); i++) {
				Node user = users.item(i);
				if (null == user) {
					continue;
				}
				NamedNodeMap uMap = user.getAttributes();
				InnerPanel ip = new InnerPanel();
				// 新建 引用引用受访者参数 不影响之前的入库
				ParameterInnerPanel parameterIps = new ParameterInnerPanel();
				if (null != uMap) {
					Node feedid = uMap.getNamedItem("FeedID");
					if (null != feedid) {
						ip.setFeedId((null == feedid.getNodeValue()) ? ""
								: feedid.getNodeValue());
					}

					Node panelid = uMap.getNamedItem("PanelID");
					if (null != panelid) {
						ip.setPanelID((null == panelid.getNodeValue()) ? ""
								: panelid.getNodeValue());
					}
				}
				NodeList nlUser = user.getChildNodes();
				if (null != nlUser) {
					for (int j = 0; j < nlUser.getLength(); j++) {
						Node node = nlUser.item(j);
						if (null == node) {
							continue;
						}

						NamedNodeMap nnm = node.getAttributes();
						if (null == nnm) {
							continue;
						}
						if (Util.isEmpty(os.getParameter1())
								&& "Parameter1".equals(node.getNodeName())) {
							Node pnode = nnm.getNamedItem("note");
							os.setParameter1(null == pnode.getNodeValue() ? ""
									: pnode.getNodeValue());
						} else if (Util.isEmpty(os.getParameter2())
								&& "Parameter2".equals(node.getNodeName())) {
							Node pnode = nnm.getNamedItem("note");
							os.setParameter2(null == pnode.getNodeValue() ? ""
									: pnode.getNodeValue());
						} else if (Util.isEmpty(os.getParameter3())
								&& "Parameter3".equals(node.getNodeName())) {
							Node pnode = nnm.getNamedItem("note");
							os.setParameter3(null == pnode.getNodeValue() ? ""
									: pnode.getNodeValue());
						} else if (Util.isEmpty(os.getParameter4())
								&& "Parameter4".equals(node.getNodeName())) {
							Node pnode = nnm.getNamedItem("note");
							os.setParameter4(null == pnode.getNodeValue() ? ""
									: pnode.getNodeValue());
						} else if ("电话1".equals(node.getNodeName())) {
							Node phone1 = nnm.getNamedItem("note");
							Log.i("电话111111",
									null == phone1.getNodeValue() ? "" : phone1
											.getNodeValue());
						}

						Node psid = nnm.getNamedItem("sid");
						// 引用受访者参数
						if ("Parameter1".equals(node.getNodeName())
								|| "Parameter2".equals(node.getNodeName())
								|| "Parameter3".equals(node.getNodeName())
								|| "Parameter4".equals(node.getNodeName())
								|| "电话1".equals(node.getNodeName())) {
							ip.getPsMap().put(
									node.getNodeName(),
									new Parameter(node.getNodeName(),
											null == psid ? "" : psid
													.getNodeValue(), node
													.getTextContent()));
						}
						// 新建 引用引用受访者参数 不影响之前的入库
						if (null != psid) {
							parameterIps.getParameters().add(
									new Parameter(psid.getNodeValue(), node
											.getTextContent()));
						}
						// ip.getPs().add(new Parameter(node.getNodeName(),
						// null==pnode?"":pnode.getNodeValue(),
						// null==psid?"":psid.getNodeValue(),
						// node.getTextContent()));
					}
				}
				os.getIps().add(ip);
				// 加进去 引用引用受访者参数 不影响之前的入库
				os.getParameterIps().add(parameterIps);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return os;
	}

	public static HashMap<Integer, Answer> getFeedAnswer(File feedXml) {
		HashMap<Integer, Answer> ans = new HashMap<Integer, Answer>();
		if (null == feedXml || !feedXml.exists()) {
			System.out.println(feedXml.getAbsolutePath());
			return ans;
		}
		try {
			InputStream base = new Base64InputStream(new FileInputStream(
					feedXml), Base64.DEFAULT);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(base);
			Element element = document.getDocumentElement();
			NodeList qs = element.getElementsByTagName("question");
			if (null == qs) {
				return ans;
			}
			for (int i = 0; i < qs.getLength(); i++) {
				Node q = qs.item(i);
				// System.out.println(an.getFirstChild().getFirstChild().getTextContent());
				// System.out.println(an.getFirstChild().getLastChild().getTextContent());
				if (null == q) {
					continue;
				}

				Answer an = new Answer();
				NamedNodeMap nnm = q.getAttributes();
				if (null != nnm) {
					Node indexNode = nnm.getNamedItem("index");
					if (null == indexNode) {
						continue;
					}
					if (Util.isEmpty(indexNode.getNodeValue())) {
						continue;
					} else {
						an.qIndex = Integer.parseInt(indexNode.getNodeValue());
					}
				}

				NodeList answList = q.getChildNodes();
				if (null == answList) {
					continue;
				}
				for (int j = 0; j < answList.getLength(); j++) {
					Node node = answList.item(j);
					if (null == node) {
						continue;
					}
					AnswerMap am = new AnswerMap();
					// if("name".equals(node.getNodeName())){
					// //an.getAnswerMapArr().add(object)
					// am.setAnswerName(null==node.getTextContent()?"":node.getTextContent());
					// }else if("value".equals(node.getNodeName())){
					// am.setAnswerValue(null==node.getTextContent()?"":node.getTextContent());
					// }
					// node.getFirstChild().getFirstChild().getTextContent()
					am.setAnswerName(null == node.getFirstChild() ? ""
							: (null == node.getFirstChild().getFirstChild() ? ""
									: node.getFirstChild().getFirstChild()
											.getTextContent()));
					// node.getLastChild().getLastChild().getTextContent()
					am.setAnswerValue(null == node.getLastChild() ? ""
							: (null == node.getLastChild().getLastChild() ? ""
									: node.getLastChild().getLastChild()
											.getTextContent()));
					an.getAnswerMapArr().add(am);
				}
				ans.put(an.qIndex, an);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static ArrayList<Intervention> getInterventionList(File file,
			Call call) {

		ArrayList<Intervention> ils = new ArrayList<Intervention>();
		try {
			FileInputStream fis = new FileInputStream(file);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fis);
			Element element = document.getDocumentElement();
			NodeList ndL = element.getChildNodes();
			if (null == ndL) {
				return ils;
			}
			for (int i = 0; i < ndL.getLength(); i++) {
				Node itvNode = ndL.item(i);
				if (null == itvNode) {
					continue;
				}
				if ("intervention".equals(itvNode.getNodeName())) {
					NamedNodeMap itvNNM = itvNode.getAttributes();
					NodeList qsNL = itvNode.getChildNodes();
					if (null == itvNNM || null == qsNL) {
						continue;
					}

					Intervention intervention = new Intervention();
					Node qIndexNode = itvNNM.getNamedItem("qIndex");
					if (null != qIndexNode
							&& !Util.isEmpty(qIndexNode.getNodeValue())) {
						intervention.setIndex(qIndexNode.getNodeValue());
					}

					Node symbolNode = itvNNM.getNamedItem("symbol");
					if (null != symbolNode
							&& !Util.isEmpty(symbolNode.getNodeValue())) {
						intervention.setSymbol(symbolNode.getNodeValue());
					}

					Node singleNode = itvNNM.getNamedItem("single");
					if (null != singleNode
							&& !Util.isEmpty(singleNode.getNodeValue())) {
						intervention.setSingle(Boolean.parseBoolean(singleNode
								.getNodeValue()));
					}

					Node ruleNode = itvNNM.getNamedItem("rule");
					if (null != ruleNode
							&& !Util.isEmpty(ruleNode.getNodeValue())) {
						intervention.setRule(ruleNode.getNodeValue());
					}
					for (int j = 0; j < qsNL.getLength(); j++) {
						Node iqNode = qsNL.item(j);
						NamedNodeMap iqNNM = iqNode.getAttributes();
						if (null == iqNode || null == iqNNM) {
							continue;
						}
						InterventionItem ii = new InterventionItem();
						Node iqIndexNode = iqNNM.getNamedItem("qIndex");
						if (null != iqIndexNode
								&& !Util.isEmpty(iqIndexNode.getNodeValue())) {
							ii.setIndex(iqIndexNode.getNodeValue());
						}

						Node rowsNode = iqNNM.getNamedItem("rows");
						if (null != rowsNode
								&& !Util.isEmpty(rowsNode.getNodeValue())) {
							ii.setRows(Integer.parseInt(rowsNode.getNodeValue()));
						}

						Node iTextNode = iqNNM.getNamedItem("iText");
						if (null != iTextNode
								&& !Util.isEmpty(iTextNode.getNodeValue())) {
							ii.setiText(iTextNode.getNodeValue());
						}

						Node regexNode = iqNNM.getNamedItem("regex");
						if (null != regexNode
								&& !Util.isEmpty(regexNode.getNodeValue())) {
							ii.setRegex(regexNode.getNodeValue());
						}
						intervention.getIisMap().put(ii.getIndex(), ii);
					}
					ils.add(intervention);
					if (null != call) {
						call.updateProgress((1 + i), ndL.getLength());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ils;
	}

	/**
	 * 数据字典
	 * 
	 * @param inStream
	 * @return
	 */

	public static ArrayList<Data> parseData(InputStream inStream) {
		ArrayList<Data> dataList = new ArrayList<Data>();
		if (null == inStream) {
			return dataList;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			NodeList dictionarys = element.getChildNodes();
			if (null == dictionarys) {
				return dataList;
			}
			for (int i = 0; i < dictionarys.getLength(); i++) {
				Node dictionary = dictionarys.item(i);
				if (null == dictionary) {
					continue;
				}
				NamedNodeMap uMap = dictionary.getAttributes();
				Data data = new Data();
				if (null != uMap) {
					Node dictionaryClassID = uMap
							.getNamedItem("DictionaryClassID");
					if (null != dictionaryClassID) {
						data.setClassId((null == dictionaryClassID
								.getNodeValue()) ? "" : dictionaryClassID
								.getNodeValue());
					}
					Node dictionaryClassName = uMap
							.getNamedItem("DictionaryClassName");
					if (null != dictionaryClassName) {
						data.setClassName((null == dictionaryClassName
								.getNodeValue()) ? "" : dictionaryClassName
								.getNodeValue());
					}
					data.setDatas((null == dictionary.getTextContent()) ? ""
							: dictionary.getTextContent());
				}
				dataList.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataList;
	}

	/**
	 * 人员列表
	 * 
	 * @param inStream
	 * @return
	 */
	public static UserBean PersionList(InputStream inStream) {
		UserBean ub = new UserBean();
		if (null == inStream) {
			System.out.println("inStream ==null:");
			return ub;

		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			// 命名规则开始
			String state = element.getAttribute("state");
			if (!Util.isEmpty(state)) {
				System.out.println("state:" + state);
				if (!state.equals("100")) {
					return ub;
				}
			}
			// 命名规则结束
			NodeList users = element.getChildNodes();
			if (null == users) {
				return ub;
			}
			ArrayList<UserList> userlist = new ArrayList<UserList>();
			for (int i = 0; i < users.getLength(); i++) {
				Node user = users.item(i);
				if (null == user) {
					continue;
				}
				NamedNodeMap uMap = user.getAttributes();
				UserList us = new UserList();
				if (null != uMap) {
					// pid
					Node PanelID = uMap.getNamedItem("PanelID");
					if (null != PanelID) {
						us.setPanelID((null == PanelID.getNodeValue()) ? ""
								: PanelID.getNodeValue());
						System.out.println("PanelID:" + us.getPanelID());
					}
					// UserName
					Node UserName = uMap.getNamedItem("UserName");
					if (null != PanelID) {
						us.setUserName((null == UserName.getNodeValue()) ? ""
								: UserName.getNodeValue());
						System.out.println("UserName:" + us.getUserName());
					}
					// Phone
					Node Phone = uMap.getNamedItem("Phone");
					if (null != Phone) {
						us.setPhone((null == Phone.getNodeValue()) ? "" : Phone
								.getNodeValue());
						System.out.println("Phone:" + us.getPhone());
					}
					// BackSurveyID
					Node BackSurveyID = uMap.getNamedItem("BackSurveyID");
					if (null != BackSurveyID) {
						us.setBackSurveyID((null == BackSurveyID.getNodeValue()) ? ""
								: BackSurveyID.getNodeValue());
						System.out.println("BackSurveyID:"
								+ us.getBackSurveyID());
					}
					// BackReturnType
					Node BackReturnType = uMap.getNamedItem("BackReturnType");
					if (null != BackReturnType) {
						us.setBackReturnType((null == BackReturnType
								.getNodeValue()) ? "" : BackReturnType
								.getNodeValue());
						System.out.println("BackReturnType:"
								+ us.getBackReturnType());
					}

					// NextSurveyID
					Node NextSurveyID = uMap.getNamedItem("NextSurveyID");
					if (null != NextSurveyID) {
						us.setNextSurveyID((null == NextSurveyID.getNodeValue()) ? ""
								: NextSurveyID.getNodeValue());
						System.out.println("NextSurveyID:"
								+ us.getNextSurveyID());
					}

					// NextSurveyName
					Node NextSurveyName = uMap.getNamedItem("NextSurveyName");
					if (null != NextSurveyName) {
						us.setNextSurveyName((null == NextSurveyName
								.getNodeValue()) ? "" : NextSurveyName
								.getNodeValue());
						System.out.println("NextSurveyName:"
								+ us.getNextSurveyName());
					}

				}
				userlist.add(us);
				ub.setUserlist(userlist);

			}
		} catch (Exception e) {
			System.out.println("Exception:");
			e.printStackTrace();
		}

		return ub;
	}

	/**
	 * 时间列表
	 * 
	 * @param inStream
	 * @return
	 */
	public static TimeList TimeListxml(InputStream inStream) {
		TimeList timelist = new TimeList();
		if (null == inStream) {
			System.out.println("inStream ==null:");
			return timelist;

		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			// 命名规则开始
			String state = element.getAttribute("state");
			if (!Util.isEmpty(state)) {
				System.out.println("state:" + state);
				if (!state.equals("100")) {
					return timelist;
				} else {
					timelist.setState(state);
				}
			}
			// 命名规则结束
			NodeList users = element.getChildNodes();
			if (null == users) {
				return timelist;
			}
			ArrayList<TimeBean> tbs = new ArrayList<TimeBean>();
			for (int i = 0; i < users.getLength(); i++) {
				Node user = users.item(i);
				if (null == user) {
					continue;
				}
				NamedNodeMap uMap = user.getAttributes();
				TimeBean time = new TimeBean();
				if (null != uMap) {
					// pid
					Node PanelID = uMap.getNamedItem("PanelID");
					if (null != PanelID) {
						time.setPanelID((null == PanelID.getNodeValue()) ? ""
								: PanelID.getNodeValue());
						System.out.println("PanelID:" + time.getPanelID());
					}
					// UserName
					Node UserName = uMap.getNamedItem("UserName");
					if (null != PanelID) {
						time.setUserName((null == UserName.getNodeValue()) ? ""
								: UserName.getNodeValue());
						System.out.println("UserName:" + time.getUserName());
					}
					// Phone
					Node Phone = uMap.getNamedItem("Phone");
					if (null != Phone) {
						time.setPhone((null == Phone.getNodeValue()) ? ""
								: Phone.getNodeValue());
						System.out.println("Phone:" + time.getPhone());
					}
					// BackSurveyID
					Node BackSurveyID = uMap.getNamedItem("BackSurveyID");
					if (null != BackSurveyID) {
						time.setBackSurveyID((null == BackSurveyID
								.getNodeValue()) ? "" : BackSurveyID
								.getNodeValue());
						System.out.println("BackSurveyID:"
								+ time.getBackSurveyID());
					}
					// BackReturnType
					Node BackReturnType = uMap.getNamedItem("BackReturnType");
					if (null != BackReturnType) {
						time.setBackReturnType((null == BackReturnType
								.getNodeValue()) ? "" : BackReturnType
								.getNodeValue());
						System.out.println("BackReturnType:"
								+ time.getBackReturnType());
					}

					// NextSurveyID
					Node NextSurveyID = uMap.getNamedItem("NextSurveyID");
					if (null != NextSurveyID) {
						time.setNextSurveyID((null == NextSurveyID
								.getNodeValue()) ? "" : NextSurveyID
								.getNodeValue());
						System.out.println("NextSurveyID:"
								+ time.getNextSurveyID());
					}

					// NextSurveyName
					Node NextSurveyName = uMap.getNamedItem("NextSurveyName");
					if (null != NextSurveyName) {
						time.setNextSurveyName((null == NextSurveyName
								.getNodeValue()) ? "" : NextSurveyName
								.getNodeValue());
						System.out.println("NextSurveyName:"
								+ time.getNextSurveyName());
					}

					// BackRegDate_ms
					Node BackRegDate_ms = uMap.getNamedItem("BackRegDate_ms");
					if (null != BackRegDate_ms) {
						time.setBackRegDate_ms((null == BackRegDate_ms
								.getNodeValue()) ? "" : BackRegDate_ms
								.getNodeValue());
						System.out.println("NextSurveyName:"
								+ time.getNextSurveyName());
					}

					// NextBeginDate_ms
					Node NextBeginDate_ms = uMap
							.getNamedItem("NextBeginDate_ms");
					if (null != NextBeginDate_ms) {
						time.setNextBeginDate_ms((null == NextBeginDate_ms
								.getNodeValue()) ? "" : NextBeginDate_ms
								.getNodeValue());
						System.out.println("NextBeginDate_ms:"
								+ time.getNextBeginDate_ms());
					}
					// NowDate_ms
					Node NowDate_ms = uMap.getNamedItem("NowDate_ms");
					if (null != NowDate_ms) {
						time.setNowDate_ms((null == NowDate_ms.getNodeValue()) ? ""
								: NowDate_ms.getNodeValue());
						System.out
								.println("NowDate_ms:" + time.getNowDate_ms());
					}
					// NextEndDate_ms
					Node NextEndDate_ms = uMap.getNamedItem("NextEndDate_ms");
					if (null != NextEndDate_ms) {
						time.setNextEndDate_ms((null == NextEndDate_ms
								.getNodeValue()) ? "" : NextEndDate_ms
								.getNodeValue());
						System.out.println("NextEndDate_ms:"
								+ time.getNextEndDate_ms());

					}

					time.setTypeTitle("");

				}
				tbs.add(time);
				timelist.setTimeBean(tbs);

			}
		} catch (Exception e) {
			System.out.println("Exception:");
			e.printStackTrace();
		}

		return timelist;
	}

	/**
	 * 人员详情
	 * 
	 * @param inStream
	 * @return
	 */
	public static PersionItemBean PeraionItemListxml(InputStream inStream) {
		PersionItemBean mPersionItemBean = new PersionItemBean();
		if (null == inStream) {
			System.out.println("inStream ==null:");
			return mPersionItemBean;

		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inStream);
			Element element = document.getDocumentElement();
			// 命名规则开始
			String state = element.getAttribute("state");
			if (!Util.isEmpty(state)) {
				System.out.println("state:" + state);
				if (!state.equals("100")) {
					return mPersionItemBean;
				}
			}

			// 命名规则结束
			NodeList users = element.getChildNodes();
			if (null == users) {
				return mPersionItemBean;
			}

			for (int i = 0; i < users.getLength(); i++) {
				ArrayList<PersionItemListBean> mPersionItemListBean = new ArrayList<PersionItemListBean>();
				Node user = users.item(i);
				if (null == user) {
					continue;
				}
				NamedNodeMap uMap = user.getAttributes();

				if (null != uMap) {
					// pid
					Node PanelID = uMap.getNamedItem("PanelID");
					if (null != PanelID) {
						mPersionItemBean.setPanelID((null == PanelID
								.getNodeValue()) ? "" : PanelID.getNodeValue());
						System.out.println("PanelID:"
								+ mPersionItemBean.getPanelID());
					}
					// UserName
					Node UserName = uMap.getNamedItem("UserName");
					if (null != PanelID) {
						mPersionItemBean
								.setUserName((null == UserName.getNodeValue()) ? ""
										: UserName.getNodeValue());
						System.out.println("UserName:"
								+ mPersionItemBean.getUserName());
					}
					// Phone
					Node Phone = uMap.getNamedItem("Phone");
					if (null != Phone) {
						mPersionItemBean
								.setPhone((null == Phone.getNodeValue()) ? ""
										: Phone.getNodeValue());
						System.out.println("Phone:"
								+ mPersionItemBean.getPhone());
					}
					
					NodeList surveys = user.getChildNodes();
					if (null == surveys) {
						return mPersionItemBean;
					}
					for (int j = 0; j < surveys.getLength(); j++) {
						PersionItemListBean pilb = new PersionItemListBean();
						Node survey = surveys.item(j);
						if (null == survey) {
							continue;
						}
						NamedNodeMap sMap = survey.getAttributes();
						if (null != sMap) {
							// SurveyID
							Node SurveyID = sMap.getNamedItem("SurveyID");
							if (null != SurveyID) {
								pilb.setSurveyID((null == SurveyID
										.getNodeValue()) ? "" : SurveyID
										.getNodeValue());
								System.out.println("SurveyID:"
										+ SurveyID.getNodeValue());
							}
							// SurveyName
							Node SurveyName = sMap.getNamedItem("SurveyName");
							if (null != SurveyName) {
								pilb.setSurveyName((null == SurveyName
										.getNodeValue()) ? "" : SurveyName
										.getNodeValue());
								System.out.println("SurveyName:"
										+ SurveyName.getNodeValue());
							}
							
							// FeedID
							Node FeedID = sMap.getNamedItem("FeedID");
							if (null != FeedID) {
								pilb.setFeedID((null == FeedID
										.getNodeValue()) ? "" : FeedID
										.getNodeValue());
								System.out.println("FeedID:"
										+ FeedID.getNodeValue());
							}
							
							// RegDate_ms
							Node RegDate_ms = sMap.getNamedItem("RegDate_ms");
							if (null != RegDate_ms) {
								pilb.setRegDate_ms((null == RegDate_ms
										.getNodeValue()) ? "" : RegDate_ms
										.getNodeValue());
								System.out.println("RegDate_ms:"
										+ RegDate_ms.getNodeValue());
							}
							// ReturnType
							Node ReturnType = sMap.getNamedItem("ReturnType");
							if (null != ReturnType) {
								pilb.setReturnType((null == ReturnType
										.getNodeValue()) ? "" : ReturnType
										.getNodeValue());
								System.out.println("ReturnType:"
										+ ReturnType.getNodeValue());
							}
							// NowDate_ms
							Node NowDate_ms = sMap.getNamedItem("NowDate_ms");
							if (null != NowDate_ms) {
								pilb.setNowDate_ms((null == NowDate_ms
										.getNodeValue()) ? "" : NowDate_ms
										.getNodeValue());
								System.out.println("NowDate_ms:"
										+ NowDate_ms.getNodeValue());
							}
							// BeginDate_ms
							Node BeginDate_ms = sMap
									.getNamedItem("BeginDate_ms");
							if (null != BeginDate_ms) {
								pilb.setBeginDate_ms((null == BeginDate_ms
										.getNodeValue()) ? "" : BeginDate_ms
										.getNodeValue());
								System.out.println("BeginDate_ms:"
										+ BeginDate_ms.getNodeValue());
							}
							// EndDate_ms
							Node EndDate_ms = sMap.getNamedItem("EndDate_ms");
							if (null != RegDate_ms) {
								pilb.setEndDate_ms((null == EndDate_ms
										.getNodeValue()) ? "" : EndDate_ms
										.getNodeValue());
								System.out.println("EndDate_ms:"
										+ EndDate_ms.getNodeValue());
							}
						}
						mPersionItemListBean.add(pilb);
					}
				}
				mPersionItemBean.setmPersionItemListBean(mPersionItemListBean);
			}
		} catch (Exception e) {
			System.out.println("Exception:");
			e.printStackTrace();
		}

		return mPersionItemBean;
	}
}
