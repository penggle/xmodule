package com.penglecode.xmodule.common.codegen.mybatis;

/**
 * Where条件运算符
 * 
 * @author 	pengpeng
 * @date	2018年2月12日 下午1:43:45
 */
public enum WhereConditionOperator {

	EQ("eq"), LIKE("like");
	
	private String opName;

	private WhereConditionOperator(String opName) {
		this.opName = opName;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
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
