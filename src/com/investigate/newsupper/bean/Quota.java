package com.investigate.newsupper.bean;

import java.util.ArrayList;

import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;



public class Quota  extends IBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -762459985077112187L;
	/**
	 * 
	 */

	private String quotaId;// 配额编号
	private String quotaName;// 配额名称
	private String quotaTime;// 创建时间
	private int quotaSuccess;// 成功量
	private String quotaIns;// 配额说明
	private ArrayList<Option> optionlist  = new ArrayList<Option>();//问题配额list
	private String optionstr ;//问题配额存数据字符串
	private String quota_Userid;// 配额用户编号
	private String quota_Surveyid;// 配额项目编号
	private String planCount;// 配额数量
	private String sucessCount;// 配额已完成数量
	private String PreQuoteFlag;//是否达到配额
	
	public String getPreQuoteFlag() {
		return PreQuoteFlag;
	}


	public void setPreQuoteFlag(String preQuoteFlag) {
		PreQuoteFlag = preQuoteFlag;
	}

	
	public String getPlanCount() {
		return planCount;
	}


	public void setPlanCount(String planCount) {
		this.planCount = planCount;
	}


	public String getSucessCount() {
		return sucessCount;
	}


	public void setSucessCount(String sucessCount) {
		this.sucessCount = sucessCount;
	}


	
	
	public String getQuota_Surveyid() {
		return quota_Surveyid;
	}


	public void setQuota_Surveyid(String quota_Surveyid) {
		this.quota_Surveyid = quota_Surveyid;
	}


	public String getQuota_Userid() {
		return quota_Userid;
	}


	public void setQuota_Userid(String quota_Userid) {
		this.quota_Userid = quota_Userid;
	}


	public ArrayList<Option> getOptionlist() {
		return optionlist;
	}


	public String getOptionstr() {
		return optionstr;
	}


	public void setOptionstr(String optionstr) {
		this.optionstr = optionstr;
		if(!Util.isEmpty(optionstr)){
			
			ArrayList<Option> optlist = XmlUtil.listStr2JsonArr(optionstr);
			if(!Util.isEmpty(optlist)){
				this.optionlist = optlist;
			}
		}
	}


	public void setOptionlist(ArrayList<Option> optionlist) {
		this.optionlist = optionlist;
		if(!Util.isEmpty(optionlist)){
			String optStr = XmlUtil.jsonArr2optionStr(optionlist);
			if(!Util.isEmpty(optStr)){
				this.optionstr = optStr;
			}
		}
	}


	public String getQuotaId() {
		return quotaId;
	}


	public void setQuotaId(String quotaId) {
		this.quotaId = quotaId;
	}


	public String getQuotaName() {
		return quotaName;
	}

	public void setQuotaName(String quotaName) {
		this.quotaName = quotaName;
	}

	public String getQuotaTime() {
		return quotaTime;
	}

	public void setQuotaTime(String quotaTime) {
		this.quotaTime = quotaTime;
	}

	public int getQuotaSuccess() {
		return quotaSuccess;
	}

	public void setQuotaSuccess(int quotaSuccess) {
		this.quotaSuccess = quotaSuccess;
	}

	public String getQuotaIns() {
		return quotaIns;
	}

	public void setQuotaIns(String quotaIns) {
		this.quotaIns = quotaIns;
	}

}
