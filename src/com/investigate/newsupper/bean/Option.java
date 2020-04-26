package com.investigate.newsupper.bean;

public class Option  extends IBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7808929358349741437L;
	/**
	 * 
	 */

	private String Questionindex;// 关联问题ID
	@Override
	public String toString() {
		return "Option [Questionindex=" + Questionindex + ", Type=" + Type
				+ ", Match=" + Match + ", Symbol=" + Symbol + ", Condition="
				+ Condition + "]";
	}
	private String Type;// 问题类型
	private String Match;// 逻辑关系
	private String Symbol;// 比较符号
	private String Condition;// 对应问题的对应选项
	
	public String getQuestionindex() {
		return Questionindex;
	}
	public void setQuestionindex(String questionindex) {
		Questionindex = questionindex;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getMatch() {
		return Match;
	}
	public void setMatch(String match) {
		Match = match;
	}
	public String getSymbol() {
		return Symbol;
	}
	public void setSymbol(String symbol) {
		Symbol = symbol;
	}
	public String getCondition() {
		return Condition;
	}
	public void setCondition(String condition) {
		Condition = condition;
	}
	
	



}
