package com.investigate.newsupper.util.limitlis;

import java.util.ArrayList;

/**
 * Created by EEH on 2018/4/16.
 */

public class AshingQidBean {

	public String Qid;         //属于那道题的(这个是index 是index  是index  q.qIndex)
	public ArrayList<AshingBean>  ashingBeans;         //属于那道题的
	
	public AshingQidBean(String Qid,ArrayList<AshingBean>  ashingBeans) {
		this.Qid = Qid;
		this.ashingBeans = ashingBeans;
	}
	
	public String getQid() {
		return Qid;
	}
	public void setQid(String qid) {
		Qid = qid;
	}
	public ArrayList<AshingBean>  getAshingBeans() {
		return ashingBeans;
	}
	public void setAshingBeans(ArrayList<AshingBean>  ashingBeans) {
		this.ashingBeans = ashingBeans;
	}
	@Override
	public String toString() {
		return "AshingQidBean [Qid=" + Qid + ", ashingBeans=" + ashingBeans.toString()
				+ "]";
	}
	
	
	
    
}
