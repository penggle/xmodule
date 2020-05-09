package com.penglecode.xmodule.validator.examples;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;

public abstract class AbstractValidatorExample {

	private final ValidatorFactory validatorFactory;
	
	private final Validator validator;

	public AbstractValidatorExample(boolean failfast) {
		super();
		this.validatorFactory = createValidatorFactory(failfast);
		this.validator = createValidator();
	}
	
	protected ValidatorFactory createValidatorFactory(boolean failfast) {
		return Validation.byProvider(HibernateValidator.class)
				.configure()
				.failFast(failfast) // 快速验证模式，有第一个参数不满足条件直接返回
				.buildValidatorFactory();
	}
	
	protected Validator createValidator() {
		return validatorFactory.getValidator();
	}

	protected Validator getValidator() {
		return validator;
	}
	
}
