package com.investigate.newsupper.bean;

import java.util.ArrayList;

import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;


/**
 * @author Administrator
 * 
 */
public class Survey extends IBean {

	/**
	 * 间隔时间
	 */
	public String IntervalTime;
	/**
	 * 间隔单位
	 * 
	 * 1年
	 * 2月
	 * 3周
	 * 4日
	 * 5时
	 * 6分
	 */
	public String IntervalTimeUnit;
	/**
	 * 浮动时间
	 */
	public String FloatingTime;
	/**
	 * 浮动单位
	 * 
	 * 1年
	 * 2月
	 * 3周
	 * 4日
	 * 5时
	 * 6分
	 */
	public String FloatingTimeUnit;

	public String getIntervalTime() {
		return IntervalTime;
	}

	public void setIntervalTime(String intervalTime) {
		IntervalTime = intervalTime;
	}

	public String getIntervalTimeUnit() {
		return IntervalTimeUnit;
	}

	public void setIntervalTimeUnit(String intervalTimeUnit) {
		IntervalTimeUnit = intervalTimeUnit;
	}

	public String getFloatingTime() {
		return FloatingTime;
	}

	public void setFloatingTime(String floatingTime) {
		FloatingTime = floatingTime;
	}

	public String getFloatingTimeUnit() {
		return FloatingTimeUnit;
	}

	public void setFloatingTimeUnit(String floatingTimeUnit) {
		FloatingTimeUnit = floatingTimeUnit;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Object getVh() {
		return vh;
	}

	public void setVh(Object vh) {
		this.vh = vh;
	}

	public int getShowpageStatus() {
		return showpageStatus;
	}

	public void setShowpageStatus(int showpageStatus) {
		this.showpageStatus = showpageStatus;
	}

	public String getBackPassword() {
		return backPassword;
	}

	public void setBackPassword(String backPassword) {
		this.backPassword = backPassword;
	}

	public int getAppAutomaticUpload() {
		return appAutomaticUpload;
	}

	public void setAppAutomaticUpload(int appAutomaticUpload) {
		this.appAutomaticUpload = appAutomaticUpload;
	}

	public int getVisitPreview() {
		return visitPreview;
	}

	public void setVisitPreview(int visitPreview) {
		this.visitPreview = visitPreview;
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

	public int getOneVisit() {
		return oneVisit;
	}

	public void setOneVisit(int oneVisit) {
		this.oneVisit = oneVisit;
	}

	public int getShowQindex() {
		return showQindex;
	}

	public void setShowQindex(int showQindex) {
		this.showQindex = showQindex;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public int getIsPhoto() {
		return isPhoto;
	}

	public void setIsPhoto(int isPhoto) {
		this.isPhoto = isPhoto;
	}

	public int getIsRecord() {
		return isRecord;
	}

	public void setIsRecord(int isRecord) {
		this.isRecord = isRecord;
	}

	public int getIsVideo() {
		return isVideo;
	}

	public void setIsVideo(int isVideo) {
		this.isVideo = isVideo;
	}

	public int getIsExpand() {
		return isExpand;
	}

	public void setIsExpand(int isExpand) {
		this.isExpand = isExpand;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public int getIsDowned() {
		return isDowned;
	}

	public void setIsDowned(int isDowned) {
		this.isDowned = isDowned;
	}

	public int getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(int isUpdate) {
		this.isUpdate = isUpdate;
	}

	public int getSurveyEnable() {
		return surveyEnable;
	}

	public void setSurveyEnable(int surveyEnable) {
		this.surveyEnable = surveyEnable;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getSurveyContent() {
		return surveyContent;
	}

	public void setSurveyContent(String surveyContent) {
		this.surveyContent = surveyContent;
	}

	public int getUpload() {
		return upload;
	}

	public void setUpload(int upload) {
		this.upload = upload;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}

	public int getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(int openStatus) {
		this.openStatus = openStatus;
	}

	public int getUnUploadCount() {
		return unUploadCount;
	}

	public void setUnUploadCount(int unUploadCount) {
		this.unUploadCount = unUploadCount;
	}

	public int getVisitMode() {
		return visitMode;
	}

	public void setVisitMode(int visitMode) {
		this.visitMode = visitMode;
	}

	public String getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(String userIdList) {
		this.userIdList = userIdList;
	}

	public int getIsTest() {
		return isTest;
	}

	public void setIsTest(int isTest) {
		this.isTest = isTest;
	}

	public int getInnerDone() {
		return innerDone;
	}

	public void setInnerDone(int innerDone) {
		this.innerDone = innerDone;
	}

	public int getGlobalRecord() {
		return globalRecord;
	}

	public void setGlobalRecord(int globalRecord) {
		this.globalRecord = globalRecord;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public int getEligible() {
		return eligible;
	}

	public void setEligible(int eligible) {
		this.eligible = eligible;
	}

	public String getStrLogicList() {
		return strLogicList;
	}

	public void setStrLogicList(String strLogicList) {
		this.strLogicList = strLogicList;
	}

	public int getDownload() {
		return download;
	}

	public void setDownload(int download) {
		this.download = download;
	}

	public int getOfflineImport() {
		return offlineImport;
	}

	public void setOfflineImport(int offlineImport) {
		this.offlineImport = offlineImport;
	}

	public int getOpenGPS() {
		return openGPS;
	}

	public void setOpenGPS(int openGPS) {
		this.openGPS = openGPS;
	}

	public int getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}

	public int getForceGPS() {
		return forceGPS;
	}

	public void setForceGPS(int forceGPS) {
		this.forceGPS = forceGPS;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6185055396453310303L;
	public String SCName;
	public String SCNum;

	public String getSCNextId() {
		return SCNextId;
	}

	public void setSCNextId(String sCNextId) {
		SCNextId = sCNextId;
	}

	public String SCNextId;

	public String getSCNum() {
		return SCNum;
	}

	public void setSCNum(String sCNum) {
		SCNum = sCNum;
	}

	public String SCID;

	public String getSCID() {
		return SCID;
	}

	public void setSCID(String sCID) {
		SCID = sCID;
	}

	public String getSCName() {
		return SCName;
	}

	public void setSCName(String sCName) {
		SCName = sCName;
	}

	/**
	 * 上一页隐藏
	 */
	public String backPageFlag;

	public String getBackPageFlag() {
		return backPageFlag;
	}

	public void setBackPageFlag(String backPageFlag) {
		this.backPageFlag = backPageFlag;
	}

	/**
	 * 在android数据库中的顺序号
	 */
	public Integer id;
	public Object vh;
	/**
	 * 分页还是不分页
	 */
	public int showpageStatus = 1;

	/**
	 * photoSource 0代表相机和相册 ， 1代表相机 如果没有该节点或节点值为空 也代表相机和相册
	 */
	public String photoSource;

	public String getPhotoSource() {
		return photoSource;
	}

	public void setPhotoSource(String photoSource) {
		this.photoSource = photoSource;
	}

	public String backPassword;
	/**
	 * 是否开启自动上传；0开启1不开启
	 */
	public int appAutomaticUpload;
	/**
	 * visitPreview(0或空为允许预览，1为不允许预览)
	 */
	public int visitPreview;
	/**
	 * AppModify（0或空为上传前允许修改，1为上传前不允许修改）
	 */
	public int appModify;
	/**
	 * AppPreview（0或空为提交数据前不预览，1为提交数据前预览）
	 */
	public int appPreview;
	/**
	 * 是否新建限制 0不限制 1限制
	 */
	public int oneVisit;
	/**
	 * 样本列表显示题答案的题号
	 */
	public int showQindex = -1;
	/**
	 * 调查的问卷号
	 */
	public String surveyId;

	/**
	 * 是否拍照
	 */
	public int isPhoto;

	/**
	 * 是否录音
	 */
	public int isRecord;

	/**
	 * 是否摄像 0为没摄像 1为摄像
	 */
	public int isVideo;

	/**
	 * 是否整张问卷显示
	 */
	public int isExpand;

	/**
	 * 调查问卷的标题
	 */
	public String surveyTitle;

	public String getSurveyTitle() {
		return surveyTitle;
	}

	public void setSurveyTitle(String surveyTitle) {
		this.surveyTitle = surveyTitle;
	}

	// 行 row
	// 列 column
	/**
	 * 问卷的网络下载地址
	 */
	public String downloadUrl;

	/**
	 * 是否下载过 0未下载1已下载2下载中
	 */
	public int isDowned;
	
	/**
	 * 是否是更新操作
	 */
	public int isUpdate;
	/**
	 * 问卷是否可用
	 */
	public int surveyEnable;

	/**
	 * 问卷的发布时间
	 */
	public String publishTime;

	/**
	 * 问卷的内容
	 */
	public String surveyContent;

	public int upload;

	public String upTime;

	public float version;

	public int openStatus;

	public int unUploadCount;
	/**
	 * 访问模式
	 */
	public int visitMode;

	public String userIdList;

	/**
	 * 是否为测试问卷
	 */
	public int isTest;

	/**
	 * 内部名单是否下载过
	 */
	public int innerDone;

	/**
	 * 是否为全局录音
	 */
	public int globalRecord;

	/**
	 * 地图类型,是谷歌还是百度
	 */
	public String mapType = "";

	public int eligible = -1;

	private String parameter1 = "";
	private String parameter2 = "";
	private String parameter3 = "";
	private String parameter4 = "";

	private String parameterName = "";// 命名规则属性

	// 逻辑次数跳转
	public String strLogicList = "";

	/**
	 * 密码
	 */
	private String password = "";
	private String word = "";// 访前说明

	private int noticeNew = 0;// 问卷提醒字段
	private String generatedTime = "";// 问卷提醒生成问卷时间
	private String lastGeneratedTime = "";// 问卷上次生成问卷时间

	public int download = -1;

	/** 离线导入 */
	public int offlineImport = -1;

	public String getLastGeneratedTime() {
		return lastGeneratedTime;
	}

	public void setLastGeneratedTime(String lastGeneratedTime) {
		this.lastGeneratedTime = lastGeneratedTime;
	}

	private int isCheck; // 0是未选中 1为选中
	// 访问状态开始
	private ArrayList<ReturnType> rlist = new ArrayList<ReturnType>();
	private String returnStr;

	public int openGPS;// 开启轨迹定位

	public int timeInterval;

	public int forceGPS;// 强制定位

	public int testType;// 0或空 未开启测试模式，1 开启测试模式

	public int getTestType() {
		return testType;
	}

	public void setTestType(int testType) {
		this.testType = testType;
	}

	public ArrayList<ReturnType> getRlist() {
		return rlist;
	}

	public void setTag(Object vh) {
		this.vh = vh;
	}

	public Object getTag() {
		return vh;
	}

	public void setRlist(ArrayList<ReturnType> rlist) {
		this.rlist = rlist;
		if (!Util.isEmpty(rlist)) {
			this.returnStr = XmlUtil.jsonArr2ReturnTypeStr(rlist);
		}
	}

	public String getReturnStr() {
		return returnStr;
	}

	public void setReturnStr(String returnStr) {
		this.returnStr = returnStr;
		if (!Util.isEmpty(returnStr)) {
			/**
			 * 从xml文件解析出逻辑的集合 然后转成json字符串 目的存在数据库中备以后拿出来用
			 */
			ArrayList<ReturnType> rs = XmlUtil.returnTypeStr2JsonArr(returnStr);
			if (!Util.isEmpty(rs)) {
				this.rlist = rs;
			}
		}
	}

	// 访问状态结束

	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getGeneratedTime() {
		return generatedTime;
	}

	public void setGeneratedTime(String generatedTime) {
		this.generatedTime = generatedTime;
	}

	public int getNoticeNew() {
		return noticeNew;
	}

	public void setNoticeNew(int noticeNew) {
		this.noticeNew = noticeNew;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Survey [id=" + id + ", vh=" + vh + ", showpageStatus="
				+ showpageStatus + "" + ", photoSource=" + photoSource
				+ ", backPassword=" + backPassword + ", appAutomaticUpload="
				+ appAutomaticUpload + ", visitPreview=" + visitPreview
				+ ", appModify=" + appModify + ", appPreview=" + appPreview
				+ ", oneVisit=" + oneVisit + ", showQindex=" + showQindex
				+ ", surveyId=" + surveyId + ", isPhoto=" + isPhoto
				+ ", isRecord=" + isRecord + ", isVideo=" + isVideo
				+ ", isExpand=" + isExpand + ", surveyTitle=" + surveyTitle
				+ ", downloadUrl=" + downloadUrl + ", isDowned=" + isDowned
				+ ", isUpdate=" + isUpdate + ", surveyEnable=" + surveyEnable
				+ ", publishTime=" + publishTime + ", surveyContent="
				+ surveyContent + ", upload=" + upload + ", upTime=" + upTime
				+ ", version=" + version + ", openStatus=" + openStatus
				+ ", unUploadCount=" + unUploadCount + ", visitMode="
				+ visitMode + ", userIdList=" + userIdList + ", isTest="
				+ isTest + ", innerDone=" + innerDone + ", globalRecord="
				+ globalRecord + ", mapType=" + mapType + ", eligible="
				+ eligible + ", parameter1=" + parameter1 + ", parameter2="
				+ parameter2 + ", parameter3=" + parameter3 + ", parameter4="
				+ parameter4 + ", parameterName=" + parameterName
				+ ", strLogicList=" + strLogicList + ", password=" + password
				+ ", word=" + word + ", noticeNew=" + noticeNew
				+ ", generatedTime=" + generatedTime + ", lastGeneratedTime="
				+ lastGeneratedTime + ", download=" + download
				+ ", offlineImport=" + offlineImport + ", isCheck=" + isCheck
				+ ", rlist=" + rlist + ", returnStr=" + returnStr
				+ ", openGPS=" + openGPS + ", timeInterval=" + timeInterval
				+ ", forceGPS=" + forceGPS + ", testType=" + testType
				+ ", backPageFlag=" + backPageFlag + ", SCID=" + SCID
				+ ", SCName=" + SCName + ", SCNum=" + SCNum + "]";
	}

	public String getParameter1() {
		return parameter1;
	}

	public void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}

	public String getParameter2() {
		return parameter2;
	}

	public void setParameter2(String parameter2) {
		this.parameter2 = parameter2;
	}

	public String getParameter3() {
		return parameter3;
	}

	public void setParameter3(String parameter3) {
		this.parameter3 = parameter3;
	}

	public String getParameter4() {
		return parameter4;
	}

	public void setParameter4(String parameter4) {
		this.parameter4 = parameter4;
	}

	public boolean isOfflineImport() {
		return offlineImport == YES;
	}
	
	
	ArrayList<AccessPanelBean> accessPanellist = new ArrayList<AccessPanelBean>();

	public ArrayList<AccessPanelBean> getAccessPanellist() {
		return accessPanellist;
	}

	public void setAccessPanellist(ArrayList<AccessPanelBean> accessPanellist) {
		this.accessPanellist = accessPanellist;
	}
	
	ArrayList<GroupsBean> groupsBeans = new ArrayList<GroupsBean>();

	public ArrayList<GroupsBean> getGroupsBeans() {
		return groupsBeans;
	}

	public void setGroupsBeans(ArrayList<GroupsBean> groupsBeans) {
		this.groupsBeans = groupsBeans;
	}

}
