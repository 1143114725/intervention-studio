package com.investigate.newsupper.bean;
/**
 * 人员列表模型
 * @author EraJi
 *<user PanelID="24833" UserName="李斯，0284576495" Phone="" BackSurveyID="9731" 
 *	BackReturnType="2" NextSurveyID="10260" NextSurveyName="1月随访"></user>
 */
public class UserList extends IBean{
	private String PanelID;//Pid
	private String UserName;//受访者姓名
	private String Phone;//受访者电话
	private String BackSurveyID;//上次项目的id
	private String BackReturnType;//上次项目的状态
	private String NextSurveyID;//下次项目的id
	private String NextSurveyName;//下次项目的项目名称
	
	public UserList() {
		super();
	}
	public UserList(String panelID, String userName, String phone,
			String backSurveyID, String backReturnType, String nextSurveyID,
			String nextSurveyName) {
		super();
		PanelID = panelID;
		UserName = userName;
		Phone = phone;
		BackSurveyID = backSurveyID;
		BackReturnType = backReturnType;
		NextSurveyID = nextSurveyID;
		NextSurveyName = nextSurveyName;
	}
	@Override
	public String toString() {
		return "UserList [PanelID=" + PanelID + ", UserName=" + UserName
				+ ", Phone=" + Phone + ", BackSurveyID=" + BackSurveyID
				+ ", BackReturnType=" + BackReturnType + ", NextSurveyID="
				+ NextSurveyID + ", NextSurveyName=" + NextSurveyName + "]";
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
	public String getBackSurveyID() {
		return BackSurveyID;
	}
	public void setBackSurveyID(String backSurveyID) {
		BackSurveyID = backSurveyID;
	}
	public String getBackReturnType() {
		return BackReturnType;
	}
	public void setBackReturnType(String backReturnType) {
		BackReturnType = backReturnType;
	}
	public String getNextSurveyID() {
		return NextSurveyID;
	}
	public void setNextSurveyID(String nextSurveyID) {
		NextSurveyID = nextSurveyID;
	}
	public String getNextSurveyName() {
		return NextSurveyName;
	}
	public void setNextSurveyName(String nextSurveyName) {
		NextSurveyName = nextSurveyName;
	}



}
