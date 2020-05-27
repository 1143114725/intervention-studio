package com.investigate.newsupper.service;

import java.util.ArrayList;

/**
 * 问卷和apk的版本信息
 * 
 * @author xiaonan
 * 
 */
public class VersionInfo {

	String surupdate;// 问卷版本
	String vernum;// App版本
	String systemModel;// 手机型号
	String deviceBrand;// 手机厂商
	String systemVersion;// 系统版本
	ArrayList<String> arrList = new ArrayList<String>();
	String answerList = "";
	int VerifyFlag;// 是否为测试样本，0正常样本，1测试样本（手动、自动）

	public void setVerifyFlag(int verifyFlag) {
		VerifyFlag = verifyFlag;
	}

	public int getVerifyFlag() {
		return VerifyFlag;
	}

	public String getSystemModel() {
		return systemModel;
	}

	public void setSystemModel(String systemModel) {
		this.systemModel = systemModel;
	}

	public String getDeviceBrand() {
		return deviceBrand;
	}

	public void setDeviceBrand(String deviceBrand) {
		this.deviceBrand = deviceBrand;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public ArrayList<String> getArrList() {
		return arrList;
	}

	public void setArrList(ArrayList<String> arrList) {
		this.arrList = arrList;
	}

	public String getAnswerList() {
		return answerList;
	}

	public void setAnswerList(String answerList) {
		this.answerList = answerList;
	}

	public String getVernum() {
		return vernum;
	}

	public void setVernum(String vernum) {
		this.vernum = vernum;
	}

	public String getSurupdate() {
		return surupdate;
	}

	public void setSurupdate(String surupdate) {
		this.surupdate = surupdate;
	}

	@Override
	public String toString() {
		return "VersionInfo [surupdate=" + surupdate + ", vernum=" + vernum
				+ ", systemModel=" + systemModel + ", deviceBrand="
				+ deviceBrand + ", systemVersion=" + systemVersion
				+ ", arrList=" + arrList + ", answerList=" + answerList
				+ ", VerifyFlag=" + VerifyFlag + "]";
	}

}
