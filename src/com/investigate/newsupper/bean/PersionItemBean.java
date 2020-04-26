package com.investigate.newsupper.bean;

import java.util.ArrayList;

/**
 * 人员详情页面BeanList
 * @author EraJi
 *
 */
public class PersionItemBean extends IBean{
	public String PanelID;//PID
	public String UserName;//受访者名
	public String Phone;//受访者电话
	/**
	 * 内部数组
	 */
	public ArrayList<PersionItemListBean> mPersionItemListBean = new ArrayList<PersionItemListBean>();
	
	public PersionItemBean(){
		super();
	}
	
	public PersionItemBean(String panelID, String userName, String phone,
			ArrayList<PersionItemListBean> mPersionItemListBean) {
		super();
		PanelID = panelID;
		UserName = userName;
		Phone = phone;
		this.mPersionItemListBean = mPersionItemListBean;
	}
	
	
	public String getPanelID() {
		return PanelID;
	}
	public void setPanelID(String panelID) {
		PanelID = panelID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public ArrayList<PersionItemListBean> getmPersionItemListBean() {
		return mPersionItemListBean;
	}
	public void setmPersionItemListBean(
			ArrayList<PersionItemListBean> mPersionItemListBean) {
		this.mPersionItemListBean = mPersionItemListBean;
	}
	

}
