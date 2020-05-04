package com.investigate.newsupper.intervention;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.investigate.newsupper.util.Util;

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

	public synchronized static InterventionQjq getInstance(int surveyId,
			MyApp ma, String uuid) {

		if (mInstance == null) {
			mInstance = new InterventionQjq(surveyId, ma, uuid);
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
	 * 
	 * @param vs
	 *            view 数组
	 * @param sortindex
	 *            第一次排序index（单选矩阵）
	 * @param insertindex
	 *            插入位置index （单选）
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
		} else {
			BaseLog.v("insertsortO = null");
		}
	}

	/**
	 * Q1的喜欢程度，要和Q2相对应
	 * 
	 * @param sortResultIndex
	 *            Q2
	 * @param sortDepend
	 *            Q1
	 * @param showtext
	 *            返回文字
	 */
	public String sortCorrespondence(int sortResultIndex, int sortDepend,
			String showtext) {
		Answer ans = getAnswer(sortDepend + "");
		int sort = 5;
		if (ans != null) {
			// col:row
			Map<Integer, String> map = new HashMap<>();
			for (int i = 0; i < ans.getAnswerMapArr().size(); i++) {
				int col = ans.getAnswerMapArr().get(i).getCol();
				int row = ans.getAnswerMapArr().get(i).getRow();
				String rowCar = row + "";
				// 通过Map.entrySet()遍历key和value(推荐使用)
				for (Map.Entry<Integer, String> entry : map.entrySet()) {
					// System.out.println(entry.getKey() + entry.getValue());
					if (entry.getKey() == col) {
						rowCar += ("," + entry.getValue());
					}
				}
				map.put(col, rowCar);
			}

			// Q2答案
			Answer ansSortResult = getAnswer(sortResultIndex + "");
			if (ansSortResult != null) {

				ansSortResult.getAnswerMapArr();

				ListUtils.sort(ansSortResult.getAnswerMapArr(), false, "row",
						"birthDate");
				int indexIten = 0;
				for (int i = 0; i < 10; i++) {
					String rowCarstr = map.get(i);
					if (!Util.isEmpty(rowCarstr)) {
						if (rowCarstr.indexOf(",") == -1) {
							if (!(ansSortResult.getAnswerMapArr()
									.get(indexIten).getCol() + "")
									.equals(rowCarstr)) {
								return showtext;
							}
							indexIten++;
						} else {
							String[] rowstrmap = rowCarstr.split(",");
							for (int j = 0; j < rowstrmap.length; j++) {
								if (rowCarstr.indexOf(ansSortResult
										.getAnswerMapArr().get(indexIten)
										.getCol()
										+ "") != -1) {
									indexIten++;
								} else {
									// 错
									return showtext;
								}

							}
						}

					}
				}
			}
		}
		return "";
	}

	/**
	 * 题干插入N 或者N2
	 * 
	 * @param question
	 */
	public String insertTitleAnstext(Question question, String K13a_INDEX, int y) {
		String title = question.qTitle;
		String newTitle = "";
		if (title.indexOf("【") != -1) {
			String[] titles = title.split("【");

			// 这里获取插入的文字
			String insertText = "";
			Answer ansK13a = getAnswer(K13a_INDEX);
			if (ansK13a != null) {

				ArrayList<AnswerMap> k13aansmap = ansK13a.getAnswerMapArr();
				int ki3asize = k13aansmap.size();

				int carYRanking = -1;
				int carNRanking = -1;
				int carN2Ranking = -1;

				for (int i = 0; i < ki3asize; i++) {
					AnswerMap ansmap = k13aansmap.get(i);

					switch (ansmap.getCol()) {
					case 1:
						// N车排名
						carNRanking = ansmap.getRow();
						break;
					case 5:
						// N2车排名
						carN2Ranking = ansmap.getRow();
						break;
					default:
						break;
					}

					newTitle = (carNRanking < carN2Ranking) ? "车型N" : "车型N2";

				}
			}
		}
		return newTitle;
	}

	/**
	 * 看看是问K14a 还是K14b 1 K14a 2 K14b
	 * 
	 * @param Ki3aIndex
	 *            k13a的index
	 * @param ishigh
	 *            排名 高还是低 于K14 true 高 false 低
	 */
	public int getAnstext(String Ki3aIndex) {
		int y = 0;
		// 这里获取的文字
		String ansText = "";
		Answer ansK13a = getAnswer(Ki3aIndex);
		if (ansK13a != null) {
			ArrayList<AnswerMap> k13aansmap = ansK13a.getAnswerMapArr();
			int ki3asize = k13aansmap.size();
			int carYRanking = -1;
			int carNRanking = -1;
			int carN2Ranking = -1;
			for (int i = 0; i < ki3asize; i++) {
				AnswerMap ansmap = k13aansmap.get(i);

				switch (ansmap.getCol()) {
				case 1:
					// N车排名
					carNRanking = ansmap.getRow();
					break;
				case 5:
					// N2车排名
					carN2Ranking = ansmap.getRow();
					break;
				case 6:
					// Y车排名
					carYRanking = ansmap.getRow();
					break;
				default:
					break;
				}

				int n = (carNRanking < carN2Ranking) ? carNRanking
						: carN2Ranking;
				y = (carYRanking > n) ? 1 : 2;
			}
		}
		return y;
	}

	public String chickSentence(int P5, int S1a,String uuid) {
		this.uuid = uuid;
		Answer ansp5 = getAnswer(P5 + "");
		Answer ansS1a = getAnswer(S1a + "");
		if (ansp5 != null && ansS1a != null) {
BaseLog.v(ansp5.getAnswerMapStr());
			// 选1
			if (0 == (ansp5.getAnswerMapArr().get(0).getRow())) {
				// 序号 6-15，18中选择5-7分的数量大于等于8【总共有11句】；错误提示【句子6-15、18中至少8句是5-7分】
				Integer[] nums1 = new Integer[] { 5, 6, 7, 8, 9, 10, 11, 12,
						13, 14, 17 };
				int result1 = getchickList(ansS1a, nums1, 1);
				if (8 > result1) {
					return "句子6-15、18中至少8句是5-7分";
				}
				// 序号19-21 选择1-4分的数量大于等于2【总共3句】；错误提示【句子19-21中至少2句是1-4分】
				Integer[] nums2 = new Integer[] { 18, 19, 20 };
				int result2 = getchickList(ansS1a, nums2, 2);
				if (2 > result2) {
					return "句子19-21中至少2句是1-4分";
				} 
				// 序号22，24 选择 5-7分，【两个都得是 5-7】；错误提示【句子22、24是5-7分】
				Integer[] nums3 = new Integer[] { 21, 23 };
				int result3 = getchickList(ansS1a, nums3, 1);
				if (2 != result3) {
					return "句子22、24是5-7分";
				}
				// 序号23 选择1-4分 错误提示【句子23是1-4分】
				Integer[] nums4 = new Integer[] { 22 };
				int result4 = getchickList(ansS1a, nums4, 2);
				if (1 != result4) {
					return "句子23是1-4分";
				}
			} else {
				// 选2
				// 序号 1-5，19 选择 5-7分的数量大于等于5【总共6句】 错误提示【句子1-5、19中至少5句是5-7分】
				Integer[] numsA = new Integer[] { 0, 1, 2, 3, 4, 18 };
				int resultA = getchickList(ansS1a, numsA, 1);
				if (5 > resultA) {
					return "句子1-5、19中至少5句是5-7分";
				}
				// 序号16-18，选择1-4分的数量大于等于2【总共3句】 错误提示【句子16-18中至少2句是1-4分】
				Integer[] numsB = new Integer[] { 15, 16, 17 };
				int resultB = getchickList(ansS1a, numsB, 2);
				if (2 > resultB) {
					return "句子16-18中至少2句是1-4分";
				}

			}
		}
		return "";

	}

	/**
	 * 获取选中指定区域值得数量
	 * 
	 * @param ans
	 *            答案
	 * @param nums
	 *            row集合
	 * @param type
	 *            选择区域 1：5-7 2:1-4
	 * @return 符合条件的数量
	 */
	private int getchickList(Answer ans, Integer[] nums, int type) {
		// TODO Auto-generated method stub
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < nums.length; i++) {
			list.add(ans.getAnswerMapArr().get(nums[i]).getCol());
		}
		BaseLog.v("getchickList---->>" + list.toString());
		int result = 0;

		int max = 0;
		int min = 0;

		switch (type) {
		case 1:
			max = 6;
			min = 4;
			break;
		case 2:
			max = 3;
			min = 0;
			break;

		default:
			break;
		}

		for (int i = 0; i < list.size(); i++) {
			int item = list.get(i);
			if (min <= item && item <= max) {
				result++;
			}
		}
		BaseLog.v("result---->>" + result);
		return result;

	}
}
