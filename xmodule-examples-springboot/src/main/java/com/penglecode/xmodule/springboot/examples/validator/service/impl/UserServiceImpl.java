package com.penglecode.xmodule.springboot.examples.validator.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.penglecode.xmodule.springboot.examples.validator.model.User;
import com.penglecode.xmodule.springboot.examples.validator.service.UserService;

@Validated
@Service("userService")
public class UserServiceImpl implements UserService {

	@Override
	public void createUser(User user) {
		System.out.println(">>> createUser : " + user);
	}

	@Override
	public void updateUser(User user) {
		System.out.println(">>> updateUser : " + user);
	}

	@Override
	public void deleteUserById(Long userId) {
		System.out.println(">>> deleteUserById : " + userId);
	}

	@Override
	public void modifyPassword(User user) {
		System.out.println(">>> modifyPassword : " + user);
	}

	@Override
	public String resetPassword(Long userId, String password) {
		System.out.println(">>> modifyPassword : " + userId + " , " + password);
		return null;
	}

}
