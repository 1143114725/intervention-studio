package com.investigate.newsupper.bean;

/**
 * 添加到数据库的bean
 * @author EraJieZhang
 * @data 2018/11/27
 */
public class FeedAnsBean {

    /**
     * 用户名
     */
    String userId;
    /**
     * 项目编号
     */
    String surveyId;
    /**
     * 问卷唯一标识
     */
    String uuId;
    /**
     * 题目索引
     */
    String questionIndex;
    /**
     * 答案内容
     */
    String answerMap;

    public FeedAnsBean() {

    }

    public FeedAnsBean(String userId, String surveyId, String uuId, String questionIndex, String answerMap) {

        this.userId = userId;
        this.surveyId = surveyId;
        this.uuId = uuId;
        this.questionIndex = questionIndex;
        this.answerMap = answerMap;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public String getSurveyId() {

        return surveyId;
    }

    public void setSurveyId(String surveyId) {

        this.surveyId = surveyId;
    }

    public String getUuId() {

        return uuId;
    }

    public void setUuId(String uuId) {

        this.uuId = uuId;
    }

    public String getQuestionIndex() {

        return questionIndex;
    }

    public void setQuestionIndex(String questionIndex) {

        this.questionIndex = questionIndex;
    }

    public String getAnswerMap() {

        return answerMap;
    }

    public void setAnswerMap(String answerMap) {

        this.answerMap = answerMap;
    }

    @Override
    public String toString() {

        return "FeedAnsBean{" + "userId='" + userId + '\'' + ", surveyId='" + surveyId + '\'' + ", uuId='" + uuId + '\'' + ", questionIndex='" + questionIndex + '\'' + ", answerMap='" + answerMap + '\'' + '}';
    }
}
