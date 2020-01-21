package com.penglecode.xmodule.common.consts.em;

/**
 * 共通的 是/否 枚举
 * 
 * @author  pengpeng
 * @date 	 2016年4月1日 上午10:24:51
 * @version 1.0
 */
public enum YesNoEnum {

	YES("1", "是", true), NO("0", "否", false);
	
	private String code;
	
	private String name;
	
	private boolean bool;

	private YesNoEnum(String code, String name, boolean bool) {
		this.code = code;
		this.name = name;
		this.bool = bool;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public static YesNoEnum getEm(String code) {
		for(YesNoEnum em : values()){
			if(em.getCode().equals(code)){
				return em;
			}
		}
		return null;
	}
	
}
