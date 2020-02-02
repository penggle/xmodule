package com.penglecode.xmodule.security.oauth2.common.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OAuth2LoginUser {

	private Long userId;
	
	private String username;
	
	private String password;
	
	private Integer userType;
	
	private String email;
	
	private String mobilePhone;
	
	private Boolean enabled;
	
	private Integer loginTimes;
	
	private String lastLoginTime;
	
	private String createTime;
	
	private String updateTime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	public Map<String,List<String>> getKcUserAttributes() {
		Map<String,List<String>> attributes = new LinkedHashMap<>();
		attributes.put("userId", asList(userId));
		attributes.put("userType", asList(userType));
		attributes.put("email", asList(email));
		attributes.put("mobilePhone", asList(mobilePhone));
		attributes.put("enabled", asList(enabled));
		attributes.put("loginTimes", asList(loginTimes));
		attributes.put("lastLoginTime", asList(lastLoginTime));
		attributes.put("createTime", asList(createTime));
		attributes.put("updateTime", asList(updateTime));
		return attributes;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> asList(Object value) {
		if(value != null) {
			return Arrays.asList(value.toString());
		}
		return Collections.EMPTY_LIST;
	}
	
}