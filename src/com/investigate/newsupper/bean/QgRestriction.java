package com.investigate.newsupper.bean;


/**
 * 复杂逻辑:一级逻辑节点
 * @author Administrator
 *
 */
public class QgRestriction  extends IBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2073077370075157108L;
	private String qMatch;
	private Integer qindex=-1;

	public Integer getQindex() {
		return qindex;
	}

	public void setQindex(Integer qindex) {
		this.qindex = qindex;
	}

	public String getQMatch() {
		return qMatch;
	}

	public void setQMatch(String qMatch) {
		this.qMatch = qMatch;
	}
	
	@Override
	public String toString() {
		return "QgRestriction [qindex=" + qindex + ", qMatch=" + qMatch + "]";
	}

}
