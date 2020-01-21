package com.penglecode.xmodule.platform.upms.consts.em;
/**
 * 资源功能类型枚举
 * 
 * @author	  	pengpeng
 * @date	  	2019年10月28日 下午5:26:46
 * @version  	1.0
 */
public enum UpmsResourceActionTypeEnum {
	
	/**
	 * 侧边导航菜单 + 顶部导航菜单
	 */
	RESOURCE_ACTION_TYPE_MENU(0, "菜单"),
	
	/**
	 * 按钮、链接、子页面
	 */
	RESOURCE_ACTION_TYPE_BUTTON(1, "按钮");
	
	private Integer typeCode;
	
	private String typeName;

	private UpmsResourceActionTypeEnum(Integer typeCode, String typeName) {
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

	public static UpmsResourceActionTypeEnum getType(Integer typeCode) {
		for(UpmsResourceActionTypeEnum em : values()){
			if(em.getTypeCode().equals(typeCode)){
				return em;
			}
		}
		return null;
	}
	
}
