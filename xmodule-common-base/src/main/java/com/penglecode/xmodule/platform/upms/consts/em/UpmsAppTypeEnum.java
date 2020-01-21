package com.penglecode.xmodule.platform.upms.consts.em;

/**
 * 用户类型枚举
 * 
 * @author	  	pengpeng
 * @date	  	2015年10月28日 下午5:26:46
 * @version  	1.0
 */
public enum UpmsAppTypeEnum {
	
	APP_TYPE_CORE(0, "核心应用"), APP_TYPE_NORMAL(1, "普通应用");
	
	private Integer typeCode;
	
	private String typeName;

	private UpmsAppTypeEnum(Integer typeCode, String typeName) {
		this.typeCode = typeCode;
		this.typeName = typeName;
	}
	
	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public static UpmsAppTypeEnum getAppType(Integer typeCode) {
		for(UpmsAppTypeEnum em : values()){
			if(em.getTypeCode().equals(typeCode)){
				return em;
			}
		}
		return null;
	}
	
}
