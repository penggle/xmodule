package com.penglecode.xmodule.keycloak.storage.user.model;

/**
 * 本地RBAC用户权限体系的角色表
 * 
 * @author 	pengpeng
 * @date	2019年12月19日 下午5:05:33
 */
public class UpmsRole {

	/** 角色id */
    private Long roleId;

    /** 角色名称 */
    private String roleName;

    /** 角色代码,由字母、下划线组成 */
    private String roleCode;

    /** 角色类型：0-系统角色,1-普通角色 */
    private Integer roleType;

    /** 角色描述 */
    private String description;

    /** 创建时间 */
    private String createTime;

    /** 创建者,用户表的id */
    private Long createBy;

    /** 最近更新时间 */
    private String updateTime;

    /** 最近更新者,用户表的id */
    private Long updateBy;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public Integer getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	@Override
	public String toString() {
		return "UpmsRole [roleId=" + roleId + ", roleName=" + roleName + ", roleCode=" + roleCode + ", roleType="
				+ roleType + ", description=" + description + ", createTime=" + createTime + ", createBy=" + createBy
				+ ", updateTime=" + updateTime + ", updateBy=" + updateBy + "]";
	}
	
}