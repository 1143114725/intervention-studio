package com.investigate.newsupper.intervention;

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
import com.investigate.newsupper.bean.Question;
import com.investigate.newsupper.bean.QuestionItem;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.ListUtils;

/**
 * 干预-千金裘
 * 
 * @author EraJieZhang
 * @date 2020年4月26日22:03:12
 */
public class InterventionQjq {
	
	private int surveyId;
	private MyApp ma;
	private String uuid;

	private InterventionQjq(int surveyId, MyApp ma, String uuid) {
		super();
		this.surveyId = surveyId;
		this.ma = ma;
		this.uuid = uuid;
	}
	
	
	
    private static InterventionQjq mInstance;

    public synchronized static InterventionQjq getInstance(int surveyId, MyApp ma, String uuid) {

        if (mInstance == null) {
            mInstance = new InterventionQjq(surveyId,ma,uuid);
        }
        return mInstance;
    }
    
    

	/**
	 * 获取答案
	 * 
	 * @param index
	 * @return
	 */
	public Answer getAnswer(String index) {
		Answer p4aans = ma.dbService.getAnswer(uuid, index);
		if (p4aans != null && p4aans.getAnswerMapArr() != null) {
			return p4aans;
		}
		return null;

	}

	/**
	 * 插入一辆车后比较排序
	 * @param vs	view 数组
	 * @param sortindex	第一次排序index（单选矩阵）   
	 * @param insertindex	插入位置index （单选）
	 */
	public void insertsortO(ArrayList<View> vs, String sortindex,
			String insertindex) {
		// 获取Y9的答案
		Answer ansY9 = getAnswer(sortindex);
		if (ansY9 != null) {
			int size = ansY9.getAnswerMapArr().size();
			ListUtils.sort(ansY9.getAnswerMapArr(), true, "rol", "birthDate");
			
			for (int i = 0; i < size; i++) {
				BaseLog.v("ansY9 =" + ansY9.getAnswerMapArr().get(i));
			}
			
			
			List<Integer> cols = new ArrayList<Integer>();
			for (int i = 0; i < size; i++) {
				AnswerMap ansmap = ansY9.getAnswerMapArr().get(i);
				cols.add(ansmap.getCol());
			}
			// 获取Q14的答案
			Answer ansQ14 = getAnswer(insertindex);
			
			BaseLog.v("ansQ14 =" + ansQ14.getAnswerMapStr());
			
			// 根据Q14的答案插入O车的位置
			int Q14row = ansQ14.getAnswerMapArr().get(0).getRow();
			List<Integer> newcollist = new ArrayList<Integer>();
			// 循环吧O车后面的车序加1
			cols.add(Q14row, cols.size());
			newcollist.addAll(cols);
			// 最后的O车位置
			newcollist.add(Q14row);
			
			BaseLog.v("newcollist =" + newcollist.toString());
			// 排序后的顺序
			for (View view : vs) {
				if (view instanceof RadioButton) {
					RadioButton rb = (RadioButton) view;
					AnswerMap aMap = (AnswerMap) rb.getTag();
					int rbrow = aMap.getRow();
					int rbcol = aMap.getCol();
					int checkrbsize = newcollist.get(rbrow);
					if (rbcol == checkrbsize) {
						rb.setChecked(true);
					} else {
						rb.setChecked(false);
					}
					rb.setEnabled(false);
					rb.setFocusable(false);
				}
			}
		}
	}

	/**
	 * 题干插入N 或者N2
	 * @param question
	 */
	private String insertTitleAnstext(Question question){
		String title = question.qTitle;
		String newTitle = "";
		if(title.indexOf("【") != -1){
			String[] titles = title.split("【");
			
			// 这里获取插入的文字
			String insertText = "";
			Answer ansK13a = getAnswer("K13a_INDEX");
			if (ansK13a !=null) {
				
				ArrayList<AnswerMap> k13aansmao = ansK13a.getAnswerMapArr();
				int ki3asize = k13aansmao.size();
				for (int i = 0; i < ki3asize; i++) {
					
				}
				
				
				//最后输出的标题
				for (int i = 0; i < titles.length; i++) {
					newTitle += titles[i];
					if (i == 0) {
						newTitle += insertText;
					}
				}
			}
		}
		return newTitle;
	}
	
	/**
	 * 获取K13a 中比Y车高或者低的车是N 还是N2
	 * @param Ki3aIndex k13a的index
     * @param ishigh 排名 高还是低 于K14 true 高 false 低
	 */
	private void getAnstext(String Ki3aIndex,boolean ishigh){
			
			// 这里获取的文字
			String ansText = "";
			Answer ansK13a = getAnswer("K13a_INDEX");
			if (ansK13a !=null) {
				ArrayList<AnswerMap> k13aansmap = ansK13a.getAnswerMapArr();
				int ki3asize = k13aansmap.size();
				int carYRanking = -1;
				int carNRanking = -1;
				int carN2Ranking = -1;
				for (int i = 0; i < ki3asize; i++) {
					AnswerMap ansmap = k13aansmap.get(i);
					
					switch (ansmap.getCol() ) {
					case 1:
						//N车排名
						carNRanking = ansmap.getRow();
						break;
					case 5:
						//N2车排名
						carN2Ranking = ansmap.getRow();
						break;
					case 6:
						//Y车排名
						carYRanking = ansmap.getRow();
						break;
					default:
						break;
					}
					
					if(ishigh){
						int s = (carNRanking > carN2Ranking)?carN2Ranking:carNRanking;
					}else{
						int Y = (carNRanking > carN2Ranking)?carN2Ranking:carNRanking;
					}
					
					
				}
		}
	}
	
}
