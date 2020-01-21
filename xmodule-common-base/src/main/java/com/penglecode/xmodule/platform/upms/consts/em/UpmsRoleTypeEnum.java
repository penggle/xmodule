package com.penglecode.xmodule.platform.upms.consts.em;

/**
 * 角色类型枚举
 * 
 * @author	  	pengpeng
 * @date	  	2015年10月28日 下午5:26:46
 * @version  	1.0
 */
public enum UpmsRoleTypeEnum {
	
	ROLE_TYPE_SYSTEM(0, "系统角色"), ROLE_TYPE_NORMAL(1, "普通角色");
	
	private Integer typeCode;
	
	private String typeName;

	private UpmsRoleTypeEnum(Integer typeCode, String typeName) {
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

	public static UpmsRoleTypeEnum getType(Integer typeCode) {
		for(UpmsRoleTypeEnum em : values()){
			if(em.getTypeCode().equals(typeCode)){
				return em;
			}
		}
		return null;
	}
	
}
