package com.investigate.newsupper.intervention;

import java.util.ArrayList;


import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.Util;

import android.content.Context;
import android.view.View;

/**
 * 干预 字符串 surveyID index
 * 
 * @author EraJi
 * @date 2020年4月26日22:03:12
 * 
 */
public class Interventionutil {

	/*********** 千金裘 *********************/
	public static final int SURVEY_ID_QJQ = 4112;

	public static final int INTERVENTION_QJQ_Q1 = 53;
	public static final int INTERVENTION_QJQ_Q2 = 54;
	
	
	public static final int INTERVENTION_QJQ_L6C = 568;

	public static final int INTERVENTION_QJQ_L6D = 569;
	
	public static final int INTERVENTION_QJQ_L7 = 141;

	public static final int INTERVENTION_QJQ_L8 = 142;

	public static final int INTERVENTION_QJQ_K4a = 154;
	public static final int INTERVENTION_QJQ_K4 = 218;

	public static final int INTERVENTION_QJQ_K13 = 168;
	public static final int INTERVENTION_QJQ_K13a = 220;

	public static final int INTERVENTION_QJQ_K14a = 169;
	public static final int INTERVENTION_QJQ_K14b = 170;

	public static final int INTERVENTION_QJQ_U1 = 173;
	public static final int INTERVENTION_QJQ_U1a = 222;
	
	public static final int INTERVENTION_QJQ_P5 = 6;
	public static final int INTERVENTION_QJQ_S1a = 15;

	/*********** 千金裘 *********************/
	
	
	private int surveyId;
	private MyApp ma;
	private String uuid;
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
    
	

	private Interventionutil(int surveyId, MyApp ma, String uuid) {
		super();
		this.surveyId = surveyId;
		this.ma = ma;
		this.uuid = uuid;
	}
    
	
	/**
	 * 动态生成题干 后面调用
	 * @param surveyId
	 * @param qIndex
	 * @param ma
	 * @param uuid
	 * @param vs
	 */
	public void createQuestionBodyViewBefore(int qIndex,ArrayList<View> vs) {
		
		switch (surveyId) {
		case SURVEY_ID_QJQ:
			InterventionQJQ(qIndex, vs);
			break;

		default:
			break;
		}
	}
	

	/**
	 * 下一页的时候判断
	 */
	public boolean nextpage(int qIndex,ArrayList<View> vs,Context context,String uuid){
		
		boolean isresult = true;
		switch (surveyId) {
		case SURVEY_ID_QJQ:
			
			if (INTERVENTION_QJQ_Q2 == qIndex) {
				String  result = InterventionQjq.getInstance(surveyId, ma, uuid)
						.sortCorrespondence(INTERVENTION_QJQ_Q2, INTERVENTION_QJQ_Q1,"Q1的评分和Q2的选择不一致!");
				if(!Util.isEmpty(result)){
					DialogUtil.newdialog(context, result);
					isresult = false;
				}
			}
			if (INTERVENTION_QJQ_L8 == qIndex) {
				String  result = InterventionQjq.getInstance(surveyId, ma, uuid)
						.sortCorrespondence(INTERVENTION_QJQ_L8, INTERVENTION_QJQ_L7,"L7评分和L8的选择顺序不一致!");
				if(!Util.isEmpty(result)){
					DialogUtil.newdialog(context, result);
					isresult = false;
				}
			}
			
			if (INTERVENTION_QJQ_L6D == qIndex) {
				String  result = InterventionQjq.getInstance(surveyId, ma, uuid)
						.sortCorrespondence(INTERVENTION_QJQ_L6D, INTERVENTION_QJQ_L6C,"L6C评分和L6D的选择顺序不一致!");
				if(!Util.isEmpty(result)){
					DialogUtil.newdialog(context, result);
					isresult = false;
				}
			}
			
			if (INTERVENTION_QJQ_S1a == qIndex) {
				String  result = InterventionQjq.getInstance(surveyId, ma, uuid)
						.chickSentence(INTERVENTION_QJQ_P5,INTERVENTION_QJQ_S1a,uuid);
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

	private void InterventionQJQ(int qIndex,ArrayList<View> vs) {
		
		
		
		switch (qIndex) {
		case INTERVENTION_QJQ_Q2:
			
			break;
		case INTERVENTION_QJQ_K4:
		
			InterventionQjq.getInstance(surveyId,ma, uuid).insertsortO(vs, INTERVENTION_QJQ_L8+"", INTERVENTION_QJQ_K4a+"");

			break;
		case INTERVENTION_QJQ_K13a:
			
			InterventionQjq.getInstance(surveyId,ma, uuid).insertsortO(vs, INTERVENTION_QJQ_K4+"", INTERVENTION_QJQ_K13+"");

			break;
		case INTERVENTION_QJQ_U1a:
			InterventionQjq.getInstance(surveyId,ma, uuid).insertsortO(vs, INTERVENTION_QJQ_L8+"", INTERVENTION_QJQ_U1+"");
			break;

		default:
			break;
		}

	}

}
