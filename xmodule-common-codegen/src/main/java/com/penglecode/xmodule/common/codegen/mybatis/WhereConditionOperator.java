package com.penglecode.xmodule.common.codegen.mybatis;

/**
 * Where条件运算符
 * 
 * @author 	pengpeng
 * @date	2018年2月12日 下午1:43:45
 */
public enum WhereConditionOperator {

	EQ("eq", "="), LIKE("like", "like"), GT("gt", ">"), LT("lt", "<"), GTE("gte", ">="), LTE("lte", "<=");
	
	private String opName;
	
	private String opExpression;

	private WhereConditionOperator(String opName, String opExpression) {
		this.opName = opName;
		this.opExpression = opExpression;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}
	
	public String getOpExpression() {
		return opExpression;
	}

	public void setOpExpression(String opExpression) {
		this.opExpression = opExpression;
	}

	public static WhereConditionOperator getOperator(String opName) {
		for(WhereConditionOperator em : values()) {
			if(em.getOpName().equals(opName)) {
				return em;
			}
		}
		return null;
	}
	
}
