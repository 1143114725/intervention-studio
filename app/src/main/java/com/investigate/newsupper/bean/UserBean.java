package com.investigate.newsupper.bean;

import java.util.ArrayList;
/**
 * 人员Bean
 * @author EraJi
 *
 */
public class UserBean extends IBean{
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public ArrayList<UserList> getUserlist() {
		return userlist;
	}
	public void setUserlist(ArrayList<UserList> userlist) {
		this.userlist = userlist;
	}
	public UserBean(String state, ArrayList<UserList> userlist) {
		super();
		this.state = state;
		this.userlist = userlist;
	}
	public UserBean() {
		super();
	}
	private String state;
	private ArrayList<UserList> userlist;
}
