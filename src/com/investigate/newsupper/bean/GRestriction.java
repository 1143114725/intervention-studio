package com.investigate.newsupper.bean;

import java.util.ArrayList;

import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;

/**
 * 复杂逻辑：二级逻辑节点
 * @author Administrator
 *
 */
public class GRestriction  extends IBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4496238296649454548L;
	//gMatch="all" gindex="0" pqindex="0"

	private String gMatch;
	private Integer gindex;
	private Integer pqindex;
	private ArrayList<RID> rIDs;
	private String rIDArrStr;
	public Integer getGindex() {
		return gindex;
	}
	public void setGindex(Integer gindex) {
		this.gindex = gindex;
	}
	public String getGMatch() {
		return gMatch;
	}
	
	public void setGMatch(String gMatch) {
		this.gMatch = gMatch;
	}
	public Integer getPqindex() {
		return pqindex;
	}
	public void setPqindex(Integer pqindex) {
		this.pqindex = pqindex;
	}
	public void setRIDs(ArrayList<RID> rIDs) {
		this.rIDs=rIDs;
		if(!Util.isEmpty(rIDs)){
			this.rIDArrStr = XmlUtil.parserGRestRIDArr2Json(rIDs);
		}
	}
	public String getRIDArrStr() {
		return rIDArrStr;
	}
	public void setRIDArrStr(String rIDArrStr) {
		this.rIDArrStr = rIDArrStr;
		if(!Util.isEmpty(rIDArrStr)){
			ArrayList<RID> _rIDs = XmlUtil.parserJson2GRestRIDArr(rIDArrStr);
			if(!Util.isEmpty(_rIDs)){
				this.rIDs = _rIDs;
			}
		}
	}
	public ArrayList<RID>  getRIDs() {
		return rIDs;
	}
	@Override
	public String toString() {
		return "GRestriction [gindex=" + gindex + ", gMatch=" + gMatch + ", pqindex=" + pqindex + ", rIDs=" + rIDs
				+ ",rIDArrStr=" + rIDArrStr + "]";
	}

}
