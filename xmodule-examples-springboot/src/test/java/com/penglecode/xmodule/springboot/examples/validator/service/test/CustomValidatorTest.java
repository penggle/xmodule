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

import com.penglecode.xmodule.springboot.examples.validator.model.CustomValidatorData;
import com.penglecode.xmodule.springboot.examples.validator.service.test.CustomValidatorTest.ValidatorTestConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes={ValidatorTestConfiguration.class})
public class CustomValidatorTest {

	@Autowired
	private Validator validator;
	
	@Test
	public void validate1() {
		CustomValidatorData data = new CustomValidatorData();
		data.setPublishDate("20121212");
		data.setPublishTime("12:12:12");
		data.setPublishDateTime("2012-12-12 12:12:12");
		Set<ConstraintViolation<CustomValidatorData>> constraints = validator.validate(data);
		constraints.stream().forEach(System.out::println);
	}
	
	@Configuration
	@ComponentScan(basePackages={"com.penglecode.xmodule.springboot.examples.validator"})
	public static class ValidatorTestConfiguration {

	}
	
}
