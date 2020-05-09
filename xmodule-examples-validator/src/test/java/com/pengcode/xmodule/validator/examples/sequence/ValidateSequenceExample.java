package com.pengcode.xmodule.validator.examples.sequence;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.validator.ValidationGroups.*;
import com.penglecode.xmodule.validator.examples.AbstractValidatorExample;

public class ValidateSequenceExample extends AbstractValidatorExample {

	public ValidateSequenceExample() {
		super(true);
	}

	public void validate4Create() {
		Customer customer = new Customer();
		customer.setCustomerId(null);
		customer.setCustomerName("张三qwe");
		customer.setBirthday("2012-12-12");
		
		Set<ConstraintViolation<Customer>> results = getValidator().validate(customer, ConstraintOrder.class);
		if(!CollectionUtils.isEmpty(results)) {
			results.forEach(System.out::println);
		}
	}
	
	public void validate4Update() {
		Customer customer = new Customer();
		customer.setCustomerId(1L);
		customer.setCustomerName("张三123");
		customer.setBirthday("2012-12-12");
		
		Set<ConstraintViolation<Customer>> results = getValidator().validate(customer, Update.class, ConstraintOrder.class);
		if(!CollectionUtils.isEmpty(results)) {
			results.forEach(System.out::println);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ValidateSequenceExample example = new ValidateSequenceExample();
		for(int i = 0; i < 10; i++) {
			example.validate4Create();
		}
	}
	
}
