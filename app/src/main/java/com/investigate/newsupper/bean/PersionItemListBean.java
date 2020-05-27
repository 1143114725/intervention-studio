package com.investigate.newsupper.bean;

/**
 * 人员详情页面BeanList
 * 
 * @author EraJi
 * 
 */
public class PersionItemListBean {

	public PersionItemListBean() {
		super();
	}

	public PersionItemListBean(String surveyID, String surveyName,
			String regDate_ms, String returnType, String nowDate_ms,
			String beginDate_ms, String endDate_ms, String feedID) {
		super();
		SurveyID = surveyID;
		SurveyName = surveyName;
		RegDate_ms = regDate_ms;
		ReturnType = returnType;
		NowDate_ms = nowDate_ms;
		BeginDate_ms = beginDate_ms;
		EndDate_ms = endDate_ms;
		FeedID = feedID;
	}

	/**
	 * SurveyID
	 */
	public String SurveyID;
	/**
	 * 项目名
	 */
	public String SurveyName;
	/**
	 * 提交时间戳
	 */
	public String RegDate_ms;
	/**
	 * 0 未进行 1 条件不符 2 已完成
	 */
	public String ReturnType;
	/**
	 * 当前时间
	 */
	public String NowDate_ms;
	/**
	 * 基于上次随访本次的开始时间
	 */
	public String BeginDate_ms;
	/**
	 * 基于上次随访本次的结束时间
	 */
	public String EndDate_ms;
	/**
	 * 这个问卷的feedid
	 */
	public String FeedID;
	
	public String getSurveyID() {
		return SurveyID;
	}

	public void setSurveyID(String surveyID) {
		SurveyID = surveyID;
	}

	public String getSurveyName() {
		return SurveyName;
	}

	public void setSurveyName(String surveyName) {
		SurveyName = surveyName;
	}

	public String getRegDate_ms() {
		return RegDate_ms;
	}

	public void setRegDate_ms(String regDate_ms) {
		RegDate_ms = regDate_ms;
	}

	public String getReturnType() {
		return ReturnType;
	}

	public void setReturnType(String returnType) {
		ReturnType = returnType;
	}

	public String getNowDate_ms() {
		return NowDate_ms;
	}

	public void setNowDate_ms(String nowDate_ms) {
		NowDate_ms = nowDate_ms;
	}

	public String getBeginDate_ms() {
		return BeginDate_ms;
	}

	public void setBeginDate_ms(String beginDate_ms) {
		BeginDate_ms = beginDate_ms;
	}

	public String getEndDate_ms() {
		return EndDate_ms;
	}

	public void setEndDate_ms(String endDate_ms) {
		EndDate_ms = endDate_ms;
	}

	public String getFeedID() {
		return FeedID;
	}

	public void setFeedID(String feedID) {
		FeedID = feedID;
	}
	
	

}
