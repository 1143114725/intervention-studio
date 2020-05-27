package com.investigate.newsupper.bean;

public class UserPosition {
	private String surveyId;
	private String userId;
	// private double userLat; // 纬
	// private double userLng; // 经
	private String times=""; // 时间
	private String addName; // 地点
	private int isUpload;// 是否上传了
	private String date;// 日期
	private String points="";//本次访问的所有定位字符串
	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	//
	// public double getUserLat() {
	// return userLat;
	// }
	//
	// public void setUserLat(double userLat) {
	// this.userLat = userLat;
	// }
	//
	// public double getUserLng() {
	// return userLng;
	// }
	//
	// public void setUserLng(double userLng) {
	// this.userLng = userLng;
	// }

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getAddName() {
		return addName;
	}

	public void setAddName(String addName) {
		this.addName = addName;
	}

	public int getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(int isUpload) {
		this.isUpload = isUpload;
	}

}
