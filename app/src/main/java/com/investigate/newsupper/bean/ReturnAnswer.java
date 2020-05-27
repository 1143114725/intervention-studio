package com.investigate.newsupper.bean;

/**
 Created by EEH on 2018/9/7.
 */
public class ReturnAnswer {
    String State;//返回状态
    String FeedID;//问卷编号
    String PanelID;//受访者编号
    String SurveyID;//问卷id
    String SCID;//随访组Id

    public ReturnAnswer() {

       
    }
    public ReturnAnswer(String state, String feedID, String panelID, String surveyID, String SCID) {

        State = state;
        FeedID = feedID;
        PanelID = panelID;
        SurveyID = surveyID;
        this.SCID = SCID;
    }

    @Override
    public String toString() {

        return "ReturnAnswer{" + "State='" + State + '\'' + ", FeedID='" + FeedID + '\'' + ", PanelID='" + PanelID + '\'' + ", SurveyID='" + SurveyID + '\'' + ", SCID='" + SCID + '\'' + '}';
    }

    public String getState() {

        return State;
    }

    public void setState(String state) {

        State = state;
    }

    public String getFeedID() {

        return FeedID;
    }

    public void setFeedID(String feedID) {

        FeedID = feedID;
    }

    public String getPanelID() {

        return PanelID;
    }

    public void setPanelID(String panelID) {

        PanelID = panelID;
    }

    public String getSurveyID() {

        return SurveyID;
    }

    public void setSurveyID(String surveyID) {

        SurveyID = surveyID;
    }

    public String getSCID() {

        return SCID;
    }

    public void setSCID(String SCID) {

        this.SCID = SCID;
    }
}
