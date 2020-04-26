package com.investigate.newsupper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;



/**
 * 存放答案的
 */
/**
 * @author Administrator
 *
 */
public class Answer extends IBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2489596352664041006L;
	
	/**
	 * 题目在数据库中的id号
	 */
	public Integer id;
	
	/**
	 * 问卷号
	 */
	public String surveyId;
	
	/**
	 * 受访者帐号
	 */
	public String userId;
	
	/**
	 * 下标号
	 */
	public Integer qIndex;
	
	/**
	 * 顺序号
	 */
	public Integer qOrder;
	
	/**
	 * 题目的类型
	 */
	public int answerType;
	
	/**
	 * 音视频或照片的存放路径
	 */
	public String mediaPath;
	
	/**
	 * 音视频文件的名称
	 */
	public String mediaName;
	
	/**
	 * 返回类型
	 */
	public int returnType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getqIndex() {
		return qIndex;
	}

	public void setqIndex(Integer qIndex) {
		this.qIndex = qIndex;
	}

	public Integer getqOrder() {
		return qOrder;
	}

	public void setqOrder(Integer qOrder) {
		this.qOrder = qOrder;
	}

	public int getAnswerType() {
		return answerType;
	}

	public void setAnswerType(int answerType) {
		this.answerType = answerType;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public int getReturnType() {
		return returnType;
	}

	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int enable;
	
	/**
	 * 哪一个淡答卷的
	 * surveyId, userId, uuid
	 */
	public String uuid;
	
	private ArrayList<AnswerMap> answerMapArr = new ArrayList<AnswerMap>();
	
	private String answerMapStr;
	
	

	public ArrayList<AnswerMap> getAnswerMapArr() {
		return answerMapArr;
	}

	public void setAnswerMapArr(ArrayList<AnswerMap> answerMapArr) {
		this.answerMapArr = answerMapArr;
		if(!Util.isEmpty(answerMapArr)){
			String mapStr = XmlUtil.jsonArr2MapStr(answerMapArr);
			if(!Util.isEmpty(mapStr)){
				this.answerMapStr = mapStr;
			}
		}
		
	}

	public String getAnswerMapStr() {
		return answerMapStr;
	}

	public void setAnswerMapStr(String answerMapStr) {
		this.answerMapStr = answerMapStr;
		if(!Util.isEmpty(answerMapStr)){
			
			ArrayList<AnswerMap> maps = XmlUtil.mapStr2JsonArr(answerMapStr);
			if(!Util.isEmpty(maps)){
				this.answerMapArr = maps;
			}
		}
	}

	@Override
	public String toString() {
		return "Answer [id=" + id + ", surveyId=" + surveyId + ", userId=" + userId + ", qIndex=" + qIndex + ", qOrder=" + qOrder + ", answerType=" + answerType + ", mediaPath=" + mediaPath
				+ ", mediaName=" + mediaName + ", returnType=" + returnType + ", enable=" + enable + ", uuid=" + uuid + ", answerMapArr=" + answerMapArr + ", answerMapStr=" + answerMapStr + "]";
	}

	
	
}
