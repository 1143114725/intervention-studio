package com.investigate.newsupper.bean;
/**
 * 逻辑节点 对应restriction 节点RID属性值
 * @author Administrator
 *
 */
public class RID {
	//gMatch="all" gindex="0" pqindex="0"

	private String value;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "RID [value=" + value + "]";
	}
	
}
