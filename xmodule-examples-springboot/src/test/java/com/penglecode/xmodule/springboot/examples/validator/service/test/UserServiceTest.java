package com.penglecode.xmodule.springboot.examples.validator.service.test;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import com.penglecode.xmodule.springboot.examples.validator.consts.ValidationGroups.*;
import com.penglecode.xmodule.springboot.examples.validator.model.User;
import com.penglecode.xmodule.springboot.examples.validator.service.UserService;
import com.penglecode.xmodule.springboot.examples.validator.service.test.UserServiceTest.ValidatorTestConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes={ValidatorTestConfiguration.class})
public class UserServiceTest {

	@Autowired
	private Validator validator;
	
	@Autowired
	private UserService userService;
	
	@Test
	public void createUser() {
		User user = new User();
		user.setUserId(null);
		user.setUserName("p");
		user.setPassword("123456");
		user.setMobilePhone("15112345678");
		userService.createUser(user);
	}
	
	@Test
	public void updateUser() {
		User user = new User();
		user.setUserId(1L);
		user.setUserName("pengsan");
		user.setPassword(null);
		user.setMobilePhone(null);
		userService.updateUser(user);
	}
	
	@Test
	public void validateObject() {
		User user = new User();
		user.setUserId(1L);
		user.setUserName("p");
		user.setPassword("123456");
		user.setMobilePhone("15112345678");
		
		System.out.println(validator);
		for(int i = 0; i < 50; i++) {
			Set<ConstraintViolation<User>> constraints = validator.validate(user, Create.class, ValidationOrder.class);
			constraints.forEach(System.out::println);
		}
	}
	
	@Test
	public void validateProperty() {
		User user = new User();
		user.setUserId(null);
		user.setUserName("p");
		user.setPassword("123456");
		user.setMobilePhone("15112345678");
		
		System.out.println(validator);
		Set<ConstraintViolation<User>> constraints = validator.validateProperty(user, "userName", Create.class, ConstraintOrder.class);
		constraints.forEach(System.out::println);
	}
	
	@Configuration
	@ComponentScan(basePackages={"com.penglecode.xmodule.springboot.examples.validator"})
	public static class ValidatorTestConfiguration {

	}
	
}
