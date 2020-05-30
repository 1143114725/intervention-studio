package com.investigate.newsupper.intervention;

import android.content.Context;
import android.view.View;

import com.investigate.newsupper.bean.Answer;
import com.investigate.newsupper.bean.AnswerMap;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.Util;

import java.util.ArrayList;

/**
 * 常驻干预
 * @author EraJi
 * @date 2020年4月26日22:03:12
 *
 */
public class Interventionutil {
	public  int surveyId;
	public  MyApp ma;
	public  String uuid;


    /*********** 千金裘 *********************/
    public static final int SURVEY_ID_QJQ = 4166;

    public static final int INTERVENTION_SE10 = 158;
    public static final int INTERVENTION_RE10 = 748;
    public static final int INTERVENTION_E18 = 246;
    public static final int INTERVENTION_SU81 = 268;
    public static final int INTERVENTION_SU8a = 267;
    public static final int INTERVENTION_QU8a = 286;
    public static final int INTERVENTION_QU82 = 287;


    private static Interventionutil mInstance;

    public synchronized static Interventionutil getInstance(int surveyId, MyApp ma, String uuid) {

        if (mInstance == null) {
            mInstance = new Interventionutil(surveyId,ma,uuid);
        }else{
            mInstance.surveyId = surveyId;
            mInstance.ma = ma;
            mInstance.uuid = uuid;
        }
        return mInstance;
    }

    public Interventionutil(int surveyId, MyApp ma, String uuid) {
		super();
		this.surveyId = surveyId;
		this.ma = ma;
		this.uuid = uuid;
	}
	
	/**
	 * 获取答案
	 * 
	 * @param index
	 * @return
	 */
	public Answer getanswer(String index) {
		Answer p4aans = ma.dbService.getAnswer(uuid, index);
		if (p4aans != null && p4aans.getAnswerMapArr() != null) {
			return p4aans;
		}
		return null;

	}
	
	public String getRowText(String str){
		String rowtext = "";
		String s[] = str.split("@@");
		
		Answer ans = getanswer(s[0]);
		if (ans != null) {
			for (int i = 0; i < ans.getAnswerMapArr().size(); i++) {
				AnswerMap ansmaperlist = ans.getAnswerMapArr().get(i);
				if (ansmaperlist.getAnswerValue().equals(s[1])) {
					rowtext = ansmaperlist.getAnswerText();
				}
			}
		}
		return rowtext;
	}


    /**
     * 动态生成题干 后面调用
     * @param qIndex
     * @param vs
     */
    public void createQuestionBodyViewBefore(int qIndex, ArrayList<View> vs) {

        switch (surveyId) {
            case SURVEY_ID_QJQ:
                InterventionDTG(qIndex, vs);
                break;

            default:
                break;
        }
    }

    private void InterventionDTG(int qIndex,ArrayList<View> vs) {



        switch (qIndex) {
            case INTERVENTION_SU81:

                InterventionQjq.getInstance(surveyId,ma, uuid).insertsortO(vs, INTERVENTION_SU8a+"", INTERVENTION_SU81+"");

                break;
            case INTERVENTION_QU82:

                InterventionQjq.getInstance(surveyId,ma, uuid).insertsortO(vs, INTERVENTION_QU8a+"", INTERVENTION_QU82+"");

                break;


            default:
                break;
        }

    }




    /**
     * 下一页的时候判断
     */
    public boolean nextpage(int qIndex, ArrayList<View> vs, Context context, String uuid){

        boolean isresult = true;
        switch (surveyId) {
            case SURVEY_ID_QJQ:

                if (INTERVENTION_E18 == qIndex) {
                    String  result = InterventionDTG.getInstance(surveyId, ma, uuid)
                            .sortCorrespondence(INTERVENTION_E18, INTERVENTION_SE10,"Q1的评分和Q2的选择不一致!1111",1);
                    if(!Util.isEmpty(result)){
                        DialogUtil.newdialog(context, result);
                        isresult = false;
                    }
                }
                if (INTERVENTION_E18 == qIndex) {
                    String  result = InterventionDTG.getInstance(surveyId, ma, uuid)
                            .sortCorrespondence(INTERVENTION_E18, INTERVENTION_RE10,"Q1的评分和Q2的选择不一致!2222",1);
                    if(!Util.isEmpty(result)){
                        DialogUtil.newdialog(context, result);
                        isresult = false;
                    }
                }

                break;

            default:
                break;
        }
        return isresult;
    }
}
