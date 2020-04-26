package com.investigate.newsupper.bean;

import java.util.ArrayList;


import com.alibaba.fastjson.JSON;
import com.investigate.newsupper.util.Util;

public class LogicList  extends IBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2394320764496767750L;
	// 逻辑次数跳转
	private ArrayList<Logic> logics = new ArrayList<Logic>();
	private String logicsStr;

	public ArrayList<Logic> getLogics() {
		return logics;
	}

	public void setLogics(ArrayList<Logic> logics) {
		this.logics = logics;
		this.logicsStr = null;
		if (!Util.isEmpty(logics)) {
			this.logicsStr = JSON.toJSONString(logics);
		}
	}

	public String getLogicsStr() {
		return logicsStr;
	}

	public void setLogicsStr(String logicsStr) {
		this.logicsStr = logicsStr;
		if (!Util.isEmpty(this.logics)) {
			this.logics.clear();
		}
		if (!Util.isEmpty(logicsStr)) {
			ArrayList<Logic> _logics = (ArrayList<Logic>) JSON.parseArray(logicsStr, Logic.class);
			if (!Util.isEmpty(_logics)) {
				this.logics.addAll(_logics);
			}
		}
	}

}
