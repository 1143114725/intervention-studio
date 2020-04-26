package com.investigate.newsupper.bean;

import java.util.ArrayList;

public class TimeList extends IBean{
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public ArrayList<TimeBean> getTimeBean() {
		return timebean;
	}
	public void setTimeBean(ArrayList<TimeBean> timebean) {
		this.timebean = timebean;
	}
	public TimeList(String state, ArrayList<TimeBean> timebean) {
		super();
		this.state = state;
		this.timebean = timebean;
	}
	public TimeList() {
		super();
	}
	private String state;
	private ArrayList<TimeBean> timebean;
}
