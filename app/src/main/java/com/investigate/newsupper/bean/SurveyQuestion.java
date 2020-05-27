package com.investigate.newsupper.bean;

import java.util.ArrayList;


import com.alibaba.fastjson.JSON;
import com.investigate.newsupper.util.Util;

public class SurveyQuestion extends IBean {

	
	/**上一页隐藏**/
	public String getBackPageFlag() {
		return backPageFlag;
	}
	public void setBackPageFlag(String backPageFlag) {
		this.backPageFlag = backPageFlag;
	}

	private String backPageFlag;
	
	
/**组内随机开始**/
	
	public String itemGR_Flag;// 大组随机不随机
	public String gRSelectNum;//该Item属于哪个分组，组内不随机，组与组之间随机。
	public String getgRSelectNum() {
		return gRSelectNum;
	}

	public void setgRSelectNum(String gRSelectNum) {
		this.gRSelectNum = gRSelectNum;
	}

	public String getItemGR_Flag() {
		return itemGR_Flag;
	}

	public void setItemGR_Flag(String itemGR_Flag) {
		this.itemGR_Flag = itemGR_Flag;
	}


	/**组内随机结束**/
	/**
	 * 数据字典bine
	 */
	private static final long serialVersionUID = -3660436003495135273L;

	private int eligible = -1;

	private String qgsStr;

	private ArrayList<QGroup> qgs = new ArrayList<QGroup>();

	private ArrayList<Question> questions = new ArrayList<Question>();
	// 访前说明
	private String word;
	private int showpageStatus = 1;
	private int appModify = 0;
	private int appPreview = 0;
	private int visitPreview = 0;
	public String getPhotoSource() {
		return photoSource;
	}
	public void setPhotoSource(String photoSource) {
		this.photoSource = photoSource;
	}

	private String photoSource;
	
	private int testType = 0;// 是否为测试模式，0或空 未开启测试模式，1 开启测试模式
	private int showQindex = -1;// 指定要在样本列表显示答案的题号
	private String backPassword;
	private String surveyTitle;

	@Override
	public String toString() {
		return "SurveyQuestion [eligible=" + eligible + ", qgsStr=" + qgsStr + ", qgs=" + qgs + ", questions="
				+ questions + ", word=" + word + ", showpageStatus=" + showpageStatus + ", appModify=" + appModify
				+ ", appPreview=" + appPreview + ", visitPreview=" + visitPreview + ", testType=" + testType
				+ ", showQindex=" + showQindex + ", backPassword=" + backPassword + ", surveyTitle=" + surveyTitle
				+ ", logicList=" + logicList + ", classId=" + classId + ", appAutomaticUpload=" + appAutomaticUpload
				+ ", forceGPS=" + forceGPS + ", openGPS=" + openGPS + ", photoSource=" + photoSource + ", timeInterval=" + timeInterval + "]";
	}
	public int getShowQindex() {
		return showQindex;
	}
	
	public void setShowQindex(int showQindex) {
		this.showQindex = showQindex;
	}

	public int getTestType() {
		return testType;
	}

	public void setTestType(int testType) {
		this.testType = testType;
	}

	public String getBackPassword() {
		return backPassword;
	}

	public void setBackPassword(String backPassword) {
		this.backPassword = backPassword;
	}

	// 逻辑次数跳转
	private LogicList logicList;

	private ArrayList<String> classId = new ArrayList<String>();// 数据字典数组

	private int appAutomaticUpload, forceGPS;
	private int openGPS;
	private int timeInterval;

	public int getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}

	public int getOpenGPS() {
		return openGPS;
	}

	public void setOpenGPS(int openGPS) {
		this.openGPS = openGPS;
	}

	public ArrayList<String> getClassId() {
		return classId;
	}

	public void setClassId(ArrayList<String> classId) {
		this.classId = classId;
	}

	public LogicList getLogicList() {
		return logicList;
	}

	public void setLogicList(LogicList logicList) {
		this.logicList = logicList;
	}

	public String getWord() {
		return word;
	}

	public int getVisitPreview() {
		return visitPreview;
	}

	public void setVisitPreview(int visitPreview) {
		this.visitPreview = visitPreview;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void setshowpageStatus(int showpageStatus) {
		this.showpageStatus = showpageStatus;
	}

	public int getshowpageStatus() {
		return showpageStatus;
	}

	public int getAppModify() {
		return appModify;
	}

	public void setAppModify(int appModify) {
		this.appModify = appModify;
	}

	public int getAppPreview() {
		return appPreview;
	}

	public void setAppPreview(int appPreview) {
		this.appPreview = appPreview;
	}

	public int getEligible() {
		return eligible;
	}

	public void setEligible(int eligible) {
		this.eligible = eligible;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}

	public String getQgsStr() {
		return qgsStr;
	}

	public void setQgsStr(String qgsStr) {
		this.qgsStr = qgsStr;
		if (!Util.isEmpty(this.qgs)) {
			this.qgs.clear();
		}
		if (!Util.isEmpty(qgsStr)) {
			ArrayList<QGroup> _qgs = (ArrayList<QGroup>) JSON.parseArray(qgsStr, QGroup.class);
			if (!Util.isEmpty(_qgs)) {
				this.qgs.addAll(_qgs);
			}
		}
	}

	public ArrayList<QGroup> getQgs() {
		return qgs;
	}

	public void setQgs(ArrayList<QGroup> qgs) {
		this.qgs = qgs;
		this.qgsStr = null;
		if (!Util.isEmpty(qgs)) {
			this.qgsStr = JSON.toJSONString(qgs);
		}
	}

	public String getSurveyTitle() {
		return surveyTitle;
	}

	public void setSurveyTitle(String surveyTitle) {
		this.surveyTitle = surveyTitle;
	}

	public void setAppAutomaticUpload(int appAutomaticUpload) {
		this.appAutomaticUpload = appAutomaticUpload;
	}

	public int getAppAutomaticUpload() {
		return appAutomaticUpload;
	}

	public void setForceGPS(int forceGPS) {
		// TODO Auto-generated method stub
		this.forceGPS = forceGPS;
	}

	public int getForceGPS() {
		return forceGPS;
	}

}
