package com.investigate.newsupper.bean;

import com.investigate.newsupper.util.Util;

/**
 * Created by EEH on 2018/3/31.
 */


/**
 * 时间列表
 * Created by EEH on 2018/3/31.
 *  <user PanelID="105026" UserName="" Phone="" BackSurveyID="10257" BackReturnType="2"
 *  BackRegDate_ms="1522396449000" NextSurveyID="10258" NextSurveyName="随访2月"
 *  NowDate_ms="1522483059593" NextBeginDate_ms="" NextEndDate_ms=""></user>
 */

public class TimeBean implements Comparable<TimeBean>{
    private String PanelID;//Pid
    private String UserName;//受访者姓名
    private String Phone;//受访者电话
    private String BackSurveyID;//上次随访的项目ID
    private String BackReturnType;//上次随访的项目的状态 0 未访问 1 条件不符 2 访问成功
    private String BackRegDate_ms;//上次随访的项目提交时间毫秒数
    private String NextSurveyID;//当前随访的项目ID
    private String NextSurveyName;//当前随访的项目名称
    private String NowDate_ms;//当前时间毫秒数
    private String NextBeginDate_ms;//当前项目的起始时间毫秒数
    private String NextEndDate_ms;//当前项目的截至时间毫秒数，没有浮动时间时，该项为空
    private String TypeTitle;//计算是不是需要悬浮的title

    public String getTypeTitle() {
        return TypeTitle;
    }

    public void setTypeTitle(String typeTitle) {
        TypeTitle = typeTitle;
    }

    @Override
    public String toString() {
        return "timebean{" +
                "PanelID='" + PanelID + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Phone='" + Phone + '\'' +
                ", BackSurveyID='" + BackSurveyID + '\'' +
                ", BackReturnType='" + BackReturnType + '\'' +
                ", BackRegDate_ms='" + BackRegDate_ms + '\'' +
                ", NextSurveyID='" + NextSurveyID + '\'' +
                ", NextSurveyName='" + NextSurveyName + '\'' +
                ", NowDate_ms='" + NowDate_ms + '\'' +
                ", NextBeginDate_ms='" + NextBeginDate_ms + '\'' +
                ", NextEndDate_ms='" + NextEndDate_ms + '\'' +
                ", TypeTitle='" + TypeTitle + '\'' +
                '}';
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

    public String getBackRegDate_ms() {
        return BackRegDate_ms;
    }

    public void setBackRegDate_ms(String backRegDate_ms) {
        BackRegDate_ms = backRegDate_ms;
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

    public String getNowDate_ms() {
        return NowDate_ms;
    }

    public void setNowDate_ms(String nowDate_ms) {
        NowDate_ms = nowDate_ms;
    }

    public String getNextBeginDate_ms() {
        return NextBeginDate_ms;
    }

    public void setNextBeginDate_ms(String nextBeginDate_ms) {
        NextBeginDate_ms = nextBeginDate_ms;
    }

    public String getNextEndDate_ms() {
        return NextEndDate_ms;
    }

    public void setNextEndDate_ms(String nextEndDate_ms) {
        NextEndDate_ms = nextEndDate_ms;
    }

    public TimeBean(){}
    public TimeBean(String panelID, String userName, String phone, String backSurveyID, String backReturnType, String backRegDate_ms, String nextSurveyID, String nextSurveyName, String nowDate_ms, String nextBeginDate_ms, String nextEndDate_ms,String TypeTitle) {
        this.PanelID = panelID;
        this.UserName = userName;
        this.Phone = phone;
        this.BackSurveyID = backSurveyID;
        this.BackReturnType = backReturnType;
        this.BackRegDate_ms = backRegDate_ms;
        this.NextSurveyID = nextSurveyID;
        this.NextSurveyName = nextSurveyName;
        this.NowDate_ms = nowDate_ms;
        this.NextBeginDate_ms = nextBeginDate_ms;
        this.NextEndDate_ms = nextEndDate_ms;
        this.TypeTitle = TypeTitle;
    }


    @Override
    public int compareTo(TimeBean o) {
    	Long begindata = 0l;
    	Long obegindata = 0l;
    	if (!Util.isEmpty(this.getNextBeginDate_ms())) {
    		begindata = Long.parseLong(this.getNextBeginDate_ms());
		}
    	if (!Util.isEmpty(this.getNextBeginDate_ms())) {
    		begindata = Long.parseLong(this.getNextBeginDate_ms());
		}
    	
		int i = (int)(begindata - obegindata);
        return i;
    }
}