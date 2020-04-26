package com.investigate.newsupper.util.limitlis;

/**
 * Created by EEH on 2018/4/16.
 */

public class AshingBean {

    public String EtId;         //需要禁用的edittext 的itemvalue
    public String ContetnId;    //设置禁用的 的itemvalue
    public String Contetntext;    //设置禁用的 的itemtext
    public String Operator;     //禁用的判断条件
    public String Parameter;    //禁用的条件源
    public String Circuit;      //And / Or
    public boolean isAshing;     //是否禁用

    public AshingBean(String etId, String contetnId, String contetntext, String operator, String parameter, String circuit, boolean isAshing) {
        EtId = etId;
        ContetnId = contetnId;
        Contetntext = contetntext;
        Operator = operator;
        Parameter = parameter;
        Circuit = circuit;
        this.isAshing = isAshing;
    }

    public String getContetntext() {
        return Contetntext;
    }

    public void setContetntext(String contetntext) {
        Contetntext = contetntext;
    }
    public boolean getIsAshing() {
        return isAshing;
    }

    public void setIsAshing(boolean isAshing) {
        this.isAshing = isAshing;
    }

    public String getEtId() {
        return EtId;
    }

    public void setEtId(String etId) {
        EtId = etId;
    }

    public String getContetnId() {
        return ContetnId;
    }

    public void setContetnId(String contetnId) {
        ContetnId = contetnId;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public String getParameter() {
        return Parameter;
    }

    public void setParameter(String parameter) {
        Parameter = parameter;
    }

    public String getCircuit() {
        return Circuit;
    }

    public void setCircuit(String circuit) {
        Circuit = circuit;
    }

	@Override
	public String toString() {
		return "AshingBean [EtId=" + EtId + ", ContetnId=" + ContetnId
				+ ", Contetntext=" + Contetntext + ", Operator=" + Operator
				+ ", Parameter=" + Parameter + ", Circuit=" + Circuit
				+ ", isAshing=" + isAshing + "]";
	}
    
}
