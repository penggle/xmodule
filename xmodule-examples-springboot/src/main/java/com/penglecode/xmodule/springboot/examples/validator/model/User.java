package com.penglecode.xmodule.springboot.examples.validator.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import com.penglecode.xmodule.springboot.examples.validator.consts.ValidationGroups.*;

public class User {

	@Null(message="用户ID必须为空!", groups={Property1.class, Create.class})
	@NotNull(message="用户ID不能为空!", groups={Property1.class, Update.class})
	@Positive(message="用户ID不合法!", groups={Property1.class, Update.class})
	private Long userId;
	
	@NotBlank(message="用户名不能为空!", groups={Property2.class, Constraint1.class, Create.class, Update.class})
	@Pattern(regexp="[a-zA-Z]{1}[a-zA-Z0-9_]{2,19}", message="用户名必须由字母开头,3~20个字母、数字及下划线组成!", groups={Property2.class, Constraint2.class, Create.class, Update.class})
	@Size(min=3, max=20, message="用户名长度为3~20个字符!", groups={Property2.class, Constraint3.class, Create.class, Update.class})
	private String userName;
	
	@Null(message="密码必须为空!", groups={Property3.class, Update.class})
	@NotBlank(message="密码不能为空!", groups={Property3.class, Create.class})
	@Pattern(regexp="[a-zA-Z0-9]{6,20}", message="密码由6~20个字母或数字组成!", groups={Property3.class, Create.class})
	private String password;
	
	/**
	 * mobilePhone字段未指定组，那么：
	 * 1、如果方法级别的@Validated注解存在Default组，即：@Validated({Default.class, ...})，那么该字段的约束将会被执行
	 * 2、反之，如果@Validated注解不存在Default组，那么该字段的约束将不会被执行
	 * 此例见#UserService.updateUser()
	 */
	@NotBlank(message="手机号码不能为空!", groups={Property4.class, Default.class})
	@Pattern(regexp="1\\d{10}", message="手机号码不合法!", groups={Property4.class, Default.class})
	private String mobilePhone;
	
	private String createTime;

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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", password=" + password + ", mobilePhone="
				+ mobilePhone + ", createTime=" + createTime + "]";
	}
	
}
