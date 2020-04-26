package com.investigate.newsupper.intervention;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.investigate.newsupper.bean.Answer;
import com.investigate.newsupper.bean.AnswerMap;
import com.investigate.newsupper.bean.QuestionItem;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.ListUtils;

/**
 * 干预
 * 
 * @author Administrator
 * 
 */

public class InterventionEP2 {
	public static final String TAG = "Intervention";
	private int surveyId;
	private MyApp ma;
	private String uuid;
	private final int INTERVENTION_D1 = 50;
	private final int INTERVENTION_D5 = 51;
	private final int INTERVENTION_D7 = 52;
	private final int INTERVENTION_C9 = 48;
	private final int INTERVENTION_D11 = 54;

	private final int INTERVENTION_D10 = 53;
	private final int INTERVENTION_D12 = 55;
/**************************************************/
	private final int INTERVENTION_G1 = 93;
	private final int INTERVENTION_G5 = 94;
	private final int INTERVENTION_G7 = 95;
	private final int INTERVENTION_B26 = 31;
	private final int INTERVENTION_G11 = 97;
	
	private final int INTERVENTION_G10 = 96;
	private final int INTERVENTION_G12 = 98;

	/**************************************************/
	private final int INTERVENTION_H1 = 101;
	private final int INTERVENTION_H5 = 102;
	private final int INTERVENTION_H7 = 103;
	
	private final int INTERVENTION_H11 = 105;
	
	private final int INTERVENTION_H10 = 104;
	private final int INTERVENTION_H12 = 106;
	
	

	public InterventionEP2(int surveyId, MyApp ma, String uuid) {
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
	public Answer geianswer(String index) {
		Answer p4aans = ma.dbService.getAnswer(uuid, index);
		if (p4aans != null && p4aans.getAnswerMapArr() != null) {
			return p4aans;
		}
		return null;

	}

	/**
	 * 根据index 和itemvalue 获取输入的数值
	 * 
	 * @param index
	 * @param position
	 * @return
	 */
	public float getansText(String index, int position) {
		Answer answer = geianswer(index);
		float ansvalue = 0;
		AnswerMap ansmap = answer.getAnswerMapArr().get(position);
		ansvalue = Float.parseFloat(ansmap.answerText);
		return ansvalue;
	}
	
	
	public void setansText(int index, LinearLayout bodyView) {
		BaseLog.v("index == " + index + "---->>INTERVENTION_D10  ==" + INTERVENTION_D10);
		switch (index) {
		case INTERVENTION_D10:
			insertsortD10(bodyView,INTERVENTION_D1,INTERVENTION_D5,INTERVENTION_C9);
			break;
		case INTERVENTION_D12:
			insertsortD12(bodyView,INTERVENTION_D1,INTERVENTION_D5,INTERVENTION_D7,INTERVENTION_C9,INTERVENTION_D11);
			break;
			
		case INTERVENTION_G10:
			insertsortD10(bodyView,INTERVENTION_G1,INTERVENTION_G5,INTERVENTION_B26);
			break;
		case INTERVENTION_G12:
			insertsortD12(bodyView,INTERVENTION_G1,INTERVENTION_G5,INTERVENTION_G7,INTERVENTION_B26,INTERVENTION_G11);
			break;
			
		case INTERVENTION_H10:
			insertsortD10(bodyView,INTERVENTION_H1,INTERVENTION_H5,INTERVENTION_B26);
			break;
		case INTERVENTION_H12:
			insertsortD12(bodyView,INTERVENTION_H1,INTERVENTION_H5,INTERVENTION_H7,INTERVENTION_B26,INTERVENTION_H11);
			break;
			
		
		default:
			break;
		}

	}

	public void insertsortD10(LinearLayout bodyView,int indexD1,int indexD5,int indexC9) {

		float D1 = getansText("" + indexD1, 0);
		float D2 = getansText("" + indexD1, 1);
		float D3 = getansText("" + indexD1, 2);
		float D4 = getansText("" + indexD1, 3);
		
		float D5 = getansText("" + indexD5, 0);
		float D6 = getansText("" + indexD5, 1);
		float C9 = getansText("" + indexC9, 0);
		float result = (D1 + D2 + D3 + D4 - D5 - D6) / C9;
		
		String ans = setansvalue(result);
		

		if (bodyView != null && bodyView.getChildCount() != 0) {
			LinearLayout tl = (LinearLayout) bodyView.getChildAt(0);// 获取表格布局
			LinearLayout la = (LinearLayout) tl.getChildAt(0);// 获取第一行布局
			LinearLayout lac = (LinearLayout) la.getChildAt(0);// 获取第一行第一列布局
			LinearLayout rightA = (LinearLayout) lac.getChildAt(1);// 获取第一行第一列右侧布局
			EditText eTexta = (EditText) rightA.getChildAt(0);
			eTexta.setText("" + (ans));
			eTexta.setFocusable(false);
			eTexta.setEnabled(false);
		}
	}
	
	

	public void insertsortD12(LinearLayout bodyView,int indexD1,int indexD5,int indexD7,int indexC9,int indexD11 ) {

		float D1 = getansText("" + indexD1, 0);
		float D2 = getansText("" + indexD1, 1);
		float D3 = getansText("" + indexD1, 2);
		float D4 = getansText("" + indexD1, 3);
		
		float D5 = getansText("" + indexD5, 0);
		float D6 = getansText("" + indexD5, 1);

		float D7 = getansText("" + indexD7, 0);
		float D8 = getansText("" + indexD7, 1);
		float D9 = getansText("" + indexD7, 2);

		float C9 = getansText("" + indexC9, 0);
		float D11 = getansText("" + indexD11, 0);
		float result = (D1 + D2 + D3 + D4 - D5 - D6) / C9 / D11;

		String ans = setansvalue(result);
		
		if (bodyView != null && bodyView.getChildCount() != 0) {
			LinearLayout tl = (LinearLayout) bodyView.getChildAt(0);// 获取表格布局
			LinearLayout la = (LinearLayout) tl.getChildAt(0);// 获取第一行布局
			LinearLayout lac = (LinearLayout) la.getChildAt(0);// 获取第一行第一列布局
			LinearLayout rightA = (LinearLayout) lac.getChildAt(1);// 获取第一行第一列右侧布局
			EditText eTexta = (EditText) rightA.getChildAt(0);
			eTexta.setText("" + (ans));
			eTexta.setFocusable(false);
			eTexta.setEnabled(false);
		}
		float result2 = (D1 + D2 + D3 + D4 - D5 - D6 - D7 - D8 - D9) / C9 / D11;
		String ans2 = setansvalue(result2);
		
		if (bodyView != null && bodyView.getChildCount() != 0) {
			LinearLayout tl = (LinearLayout) bodyView.getChildAt(0);// 获取表格布局
			LinearLayout la = (LinearLayout) tl.getChildAt(1);// 获取第一行布局
			LinearLayout lac = (LinearLayout) la.getChildAt(0);// 获取第一行第一列布局
			LinearLayout rightA = (LinearLayout) lac.getChildAt(1);// 获取第一行第一列右侧布局
			EditText eTexta = (EditText) rightA.getChildAt(0);
			eTexta.setText("" + (ans2));
			eTexta.setFocusable(false);
			eTexta.setEnabled(false);
		}
	}
	
	/**
	 * 吧计算出来的float 转成2位小数，如果是整数去掉小数点
	 * @param result2
	 * @return
	 */
	private String setansvalue(float result2){
		String ans2 = "";
		if(result2 == (int)result2){
			ans2 = (int)result2+"";
		}else{
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			//format 返回的是字符串
			ans2 = decimalFormat.format(result2);

		}
		return ans2;
	}

}
