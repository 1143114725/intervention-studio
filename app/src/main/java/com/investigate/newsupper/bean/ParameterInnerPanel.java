package com.investigate.newsupper.bean;

import java.util.ArrayList;


import com.alibaba.fastjson.JSON;
import com.investigate.newsupper.util.Util;

/**
 * 引用受访者参数
 * 
 * @author zz
 * 
 */
public class ParameterInnerPanel  extends IBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6314448967697205513L;

	// /**
	// * 答卷的id
	// */
	// private String feedId="";
	//
	// private String panelID = "";

	private ArrayList<Parameter> Parameters = new ArrayList<Parameter>();
	private String parametersStr;

	public String getParametersStr() {
		return parametersStr;
	}

	public void setParametersStr(String mparametersStr) {
		this.parametersStr = mparametersStr;
		if (!Util.isEmpty(mparametersStr)) {
			this.Parameters.clear();
			ArrayList<Parameter> tParameters = (ArrayList<Parameter>) JSON.parseArray(mparametersStr, Parameter.class);
			if (!Util.isEmpty(tParameters)) {
				this.Parameters.addAll(tParameters);
			}
		}
	}

	public ArrayList<Parameter> getParameters() {
		return Parameters;
	}

	public void setParameters(ArrayList<Parameter> mparameters) {
		this.Parameters = mparameters;
		if (!Util.isEmpty(mparameters)) {
			this.parametersStr = JSON.toJSONString(mparameters);
		}
	}

}
