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
@SuppressLint("ResourceAsColor")
public class Intervention {
	public static final String TAG = "Intervention";
	private int surveyId;
	private MyApp ma;
	private String uuid;

	public Intervention(int surveyId, MyApp ma, String uuid) {
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
	 * 
	 * @param vs	view 数组
	 * @param sortindex	排序index   Y9
	 * @param insertindex	插入位置index Q14
	 */
	public void insertsortO(ArrayList<View> vs, String sortindex,
			String insertindex) {
		// 获取Y9的答案
		Answer ansY9 = geianswer(sortindex);
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
			Answer ansQ14 = geianswer(insertindex);
			
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
	 * L8中检查逻辑 c＞b＞a＞d L8题：【序号:333 索引:229】
	 * 
	 * @param bodyView
	 * @return true:符合逻辑，false:不符合逻辑
	 */
	public boolean sortoption(LinearLayout bodyView, Context context) {
		// true 判断通过。false 判断没过
		boolean isjud = true;
		String text = "";
		if (bodyView != null && bodyView.getChildCount() != 0) {
			LinearLayout tl = (LinearLayout) bodyView.getChildAt(0);// 获取表格布局

			LinearLayout la = (LinearLayout) tl.getChildAt(0);// 获取第一行布局
			LinearLayout lac = (LinearLayout) la.getChildAt(0);// 获取第一行第一列布局
			LinearLayout rightA = (LinearLayout) lac.getChildAt(1);// 获取第一行第一列右侧布局
			Spinner eTexta = (Spinner) rightA.getChildAt(0);// 获取第一行第一列文本框

			LinearLayout lb = (LinearLayout) tl.getChildAt(1);
			LinearLayout lbc = (LinearLayout) lb.getChildAt(0);
			LinearLayout rightB = (LinearLayout) lbc.getChildAt(1);
			Spinner eTextb = (Spinner) rightB.getChildAt(0);

			LinearLayout lc = (LinearLayout) tl.getChildAt(2);
			LinearLayout lcc = (LinearLayout) lc.getChildAt(0);
			LinearLayout rightC = (LinearLayout) lcc.getChildAt(1);
			Spinner eTextc = (Spinner) rightC.getChildAt(0);

			LinearLayout ld = (LinearLayout) tl.getChildAt(3);
			LinearLayout ldc = (LinearLayout) ld.getChildAt(0);
			LinearLayout rightD = (LinearLayout) ldc.getChildAt(1);
			Spinner eTextd = (Spinner) rightD.getChildAt(0);

			int inta = eTexta.getSelectedItemPosition();
			int intb = eTextb.getSelectedItemPosition();
			int intc = eTextc.getSelectedItemPosition();
			int intd = eTextd.getSelectedItemPosition();

			
			
			if (intc <= intb) {
				text = "第三项需要 大于第二项";
				DialogUtil.newdialog(context, text);
				isjud = false;
				return isjud;
			}
			if (intb <= inta) {
				text = "第二项需要 大于第一项";
				isjud = false;
				DialogUtil.newdialog(context, text);
				return isjud;
			}
			if (inta <= intd) {
				text = "第一项需要大于第四项";
				isjud = false;
				DialogUtil.newdialog(context, text);
				return isjud;
			}
		}
		return isjud;
	}
}
