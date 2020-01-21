package com.penglecode.xmodule.springboot.examples.validator.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.springframework.validation.annotation.Validated;

import com.penglecode.xmodule.springboot.examples.validator.model.User;
import com.penglecode.xmodule.springboot.examples.validator.consts.ValidationGroups.*;

public interface UserService {

	@Validated({Create.class, ConstraintOrder.class})
	public void createUser(@NotNull(message="参数不能为空!") @Valid User user);
	
	@Validated({Default.class, Update.class})
	public void updateUser(@NotNull(message="参数不能为空!") @Valid User user);
	
	public void deleteUserById(Long userId);
	
	public void modifyPassword(User user);
	
	public String resetPassword(Long userId, String password);
	
}
