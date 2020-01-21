package com.penglecode.xmodule.common.consts.em;

/**
 * 通用状态枚举 - [启用/禁用]
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月28日 下午5:26:46
 * @version  	1.0
 */
public enum EnableDisableStatusEnum {
	
	COMMON_STATUS_ENABLED(1, "启用"), COMMON_STATUS_DISABLED(0, "禁用");
	
	private Integer statusCode;
	
	private String statusName;
	

	private EnableDisableStatusEnum(Integer statusCode, String statusName) {
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

	public static EnableDisableStatusEnum getStatus(Integer statusCode) {
		for(EnableDisableStatusEnum em : values()){
			if(em.getStatusCode().equals(statusCode)){
				return em;
			}
		}
		return null;
	}
	
}
