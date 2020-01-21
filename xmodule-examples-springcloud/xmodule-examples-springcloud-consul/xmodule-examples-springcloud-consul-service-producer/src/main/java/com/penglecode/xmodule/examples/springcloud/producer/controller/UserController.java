package com.penglecode.xmodule.examples.springcloud.producer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.examples.springcloud.api.model.User;
import com.penglecode.xmodule.examples.springcloud.api.service.UserApiService;

@RestController("userController")
public class UserController implements UserApiService {

	private final List<User> allUserStorages = new ArrayList<User>();
	
	@Override
	public Result<Long> registerUser(User user) {
		System.out.println(">>> registerUser");
		ValidationAssert.notNull(user, "参数不能为空!");
		ValidationAssert.notEmpty(user.getUserName(), "用户名不能为空!");
		ValidationAssert.notEmpty(user.getPassword(), "用户密码不能为空!");
		ValidationAssert.notEmpty(user.getMobilePhone(), "手机号码不能为空!");
		ValidationAssert.notEmpty(user.getEmail(), "Email不能为空!");
		user.setUserId(System.currentTimeMillis());
		user.setCreateTime(DateTimeUtils.formatNow());
		user.setUpdateTime(null);
		allUserStorages.add(user);
		return Result.success().message("OK").data(user.getUserId()).build();
	}

	@Override
	public Result<Boolean> modifyPassword(User user) {
		ValidationAssert.notNull(user, "参数不能为空!");
		ValidationAssert.notNull(user.getUserId(), "用户ID不能为空!");
		ValidationAssert.notEmpty(user.getPassword(), "用户密码不能为空!");
		allUserStorages.stream().filter(u -> u.getUserId().equals(user.getUserId())).forEach(u -> {
			u.setPassword(user.getPassword());
			u.setUpdateTime(DateTimeUtils.formatNow());
		});
		
		allUserStorages.add(user);
		return null;
	}

	@Override
	public Result<User> getUserById(Long userId) {
		ValidationAssert.notNull(userId, "用户ID不能为空!");
		User user = allUserStorages.stream().filter(u -> u.getUserId().equals(userId)).findFirst().orElse(null);
		return Result.success().message("OK").data(user).build();
	}

	@Override
	public Result<List<User>> getUserList(User condition) {
		System.out.println(">>> getUserList");
		List<User> dataList = allUserStorages.stream().filter(u -> {
			boolean matched = true;
			if(matched && !StringUtils.isEmpty(condition.getUserName())) {
				matched = matched && u.getUserName().contains(condition.getUserName());
			}
			if(matched && !StringUtils.isEmpty(condition.getMobilePhone())) {
				matched = matched && u.getMobilePhone().equals(condition.getMobilePhone());
			}
			if(matched && !StringUtils.isEmpty(condition.getEmail())) {
				matched = matched && u.getEmail().equals(condition.getEmail());
			}
			return matched;
		}).collect(Collectors.toList());
		return Result.success().message("OK").data(dataList).build();
	}

}
