package com.penglecode.xmodule.platform.upms.consts.em;

/**
 * 资源类型枚举
 * 
 * @author	  	pengpeng
 * @date	  	2015年10月28日 下午5:26:46
 * @version  	1.0
 */
public enum UpmsResourceTypeEnum {
	
	RESOURCE_TYPE_SYSTEM(0, "系统资源"), RESOURCE_TYPE_NORMAL(1, "普通资源");
	
	private Integer typeCode;
	
	private String typeName;

	private UpmsResourceTypeEnum(Integer typeCode, String typeName) {
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

	public static UpmsResourceTypeEnum getType(Integer typeCode) {
		for(UpmsResourceTypeEnum em : values()){
			if(em.getTypeCode().equals(typeCode)){
				return em;
			}
		}
		return null;
	}
	
}
