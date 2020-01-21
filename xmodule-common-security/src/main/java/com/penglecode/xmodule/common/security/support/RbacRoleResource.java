package com.penglecode.xmodule.common.security.support;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 基于RBAC权限体系的资源-角色配置关系
 * 
 * @author 	pengpeng
 * @date	2019年7月1日 上午11:37:09
 */
public class RbacRoleResource implements BaseModel<RbacRoleResource> {

	private static final long serialVersionUID = 1L;

	private String roleCode;
	
	private String resourceUrl;
	
	private String httpMethod;

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	
}
