package com.penglecode.xmodule.examples.springcloud.api.model;

import com.penglecode.xmodule.common.support.BaseModel;

public class User implements BaseModel<User> {

	private static final long serialVersionUID = 1L;

	private Long userId;
	
	private String userName;
	
	private String password;
	
	private String mobilePhone;
	
	private String email;
	
	private String createTime;
	
	private String updateTime;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", password=" + password + ", mobilePhone="
				+ mobilePhone + ", email=" + email + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
