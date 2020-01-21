package com.penglecode.xmodule.platform.upms.consts.em;

/**
 * enable/disable状态枚举
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月28日 下午5:26:46
 * @version  	1.0
 */
public enum UpmsUserStatusEnum {
	
	USER_STATUS_ENABLED(1, "启用"), USER_STATUS_DISABLED(0, "禁用");
	
	private Integer statusCode;
	
	private String statusName;

	private UpmsUserStatusEnum(Integer statusCode, String statusName) {
		this.statusCode = statusCode;
		this.statusName = statusName;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public static UpmsUserStatusEnum getStatus(Integer statusCode) {
		for(UpmsUserStatusEnum em : values()){
			if(em.getStatusCode().equals(statusCode)){
				return em;
			}
		}
		return null;
	}
	
}
