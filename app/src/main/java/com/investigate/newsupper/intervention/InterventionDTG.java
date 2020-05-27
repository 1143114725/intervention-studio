package com.investigate.newsupper.intervention;

import com.investigate.newsupper.bean.Answer;
import com.investigate.newsupper.bean.AnswerMap;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.ListUtils;
import com.investigate.newsupper.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 干预-电台港
 * 
 * @author EraJieZhang
 * @date 2020年5月28日07:06:04
 */
public class InterventionDTG {

	private int surveyId;
	private MyApp ma;
	private String uuid;

	private InterventionDTG(int surveyId, MyApp ma, String uuid) {
		super();
		this.surveyId = surveyId;
		this.ma = ma;
		this.uuid = uuid;
	}
	
	
	
    private static InterventionDTG mInstance;

    public synchronized static InterventionDTG getInstance(int surveyId, MyApp ma, String uuid) {

        if (mInstance == null) {
            mInstance = new InterventionDTG(surveyId,ma,uuid);
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
                                     String showtext,int type) {
        Answer ans = getAnswer(sortDepend + "");

        if (ans != null) {
            // col:row
            Map<Integer, String> map = new HashMap<Integer, String>();
            for (int i = 0; i < ans.getAnswerMapArr().size(); i++) {
                int col = ans.getAnswerMapArr().get(i).getCol();
                int row = ans.getAnswerMapArr().get(i).getRow();
                String rowCar = row + "";
                // 通过Map.entrySet()遍历key和value(推荐使用)
                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    if (entry.getKey() == col) {
                        rowCar += ("," + entry.getValue());
                    }
                }
                map.put(col, rowCar);
            }

            // Q2答案
            Answer ansSortResult = getAnswer(sortResultIndex + "");
            if (ansSortResult != null) {
                ArrayList<AnswerMap> result = new ArrayList<>();
                //            三辆车
                if(type == 0 ){
                    for (int i = 0, size = ansSortResult.getAnswerMapArr().size(); i < size; i++) {
                        if (ansSortResult.getAnswerMapArr().get(i).getRow() < 3){
                            result.add(ansSortResult.getAnswerMapArr().get(i));
                        }
                    }

                }else{
                    for (int i = 0, size = ansSortResult.getAnswerMapArr().size(); i < size; i++) {
                        if (ansSortResult.getAnswerMapArr().get(i).getRow() >= 3){
                            result.add(ansSortResult.getAnswerMapArr().get(i));
                        }
                    }
                }


                ListUtils.sort(result, false, "row",
                        "birthDate");
                int indexIten = 0;
                for (int i = 0; i < result.size(); i++) {
                    String rowCarstr = map.get(i);
                    if (! Util.isEmpty(rowCarstr)) {
                        if (! rowCarstr.contains(",")) {
                            if (!(result
                                    .get(indexIten).getCol() + "")
                                    .equals(rowCarstr)) {
                                return showtext;
                            }
                            indexIten++;
                        } else {
                            String[] rowstrmap = rowCarstr.split(",");
                            for (int j = 0; j < rowstrmap.length; j++) {
                                if (rowCarstr.contains(result.get(indexIten).getCol() + "")) {
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

	
}
