package com.investigate.newsupper.bean;

import java.util.ArrayList;

import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;



/**
 *	逻辑
 */
public class Restriction  extends IBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4087103758053265496L;

	private String match;

	private String rID;
	
	
	private Integer questionId = -1;
	
	private Integer matchNum = -1;//最少答得数量
	
	//private String valueArr;

	private String valueArrStr;
	
	private String matchCompare;//比较符
	
	private ArrayList<RestrictionValue> rvs = new ArrayList<RestrictionValue>();
	
	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}
	public String getRID() {
		return rID;
	}

	public void setRID(String rID) {
		this.rID = rID;
	}
	public String getMatchCompare() {
		return matchCompare;
	}

	public void setMatchCompare(String matchCompare) {
		this.matchCompare = matchCompare;
	}
	public Integer getMatchNum() {
		return matchNum;
	}

	public void setMatchNum(Integer matchNum) {
		this.matchNum = matchNum;
	}
	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getValueArrStr() {
		return valueArrStr;
	}

	public void setValueArrStr(String valueArrStr) {
		this.valueArrStr = valueArrStr;
		if(!Util.isEmpty(valueArrStr)){
			ArrayList<RestrictionValue> _rvs = XmlUtil.parserJson2RestValueArr(valueArrStr);
			if(!Util.isEmpty(_rvs)){
				this.rvs = _rvs;
			}
		}
	}

	public ArrayList<RestrictionValue> getRvs() {
		return rvs;
	}

	public void setRvs(ArrayList<RestrictionValue> rvs) {
		this.rvs = rvs;
		if(!Util.isEmpty(rvs)){
			this.valueArrStr = XmlUtil.parserRestValueArr2Json(rvs);
		}
	}
	
	@Override
	public String toString() {
		return "Restriction [match=" + match + ", rID=" + rID + ", questionId=" + questionId + ", matchNum=" + matchNum
				+ ", valueArrStr=" + valueArrStr + ", matchCompare=" + matchCompare + ", rvs=" + rvs + "]";
	}

	
}
