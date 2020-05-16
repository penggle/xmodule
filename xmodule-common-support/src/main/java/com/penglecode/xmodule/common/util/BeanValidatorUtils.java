package com.penglecode.xmodule.common.util;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;

/**
 * Java bean validator 工具类
 * 
 * @author 	pengpeng
 * @date 	2020年4月27日 下午5:35:45
 */
public class BeanValidatorUtils {

	/**
	 * 创建Validator
	 * @param failFast						- 是否快速失败模式
	 * @param constraintValidatorFactory	- {@link #ConstraintValidator} 接口的工厂
	 * @return
	 */
	public static Validator createValidator(boolean failFast, ConstraintValidatorFactory constraintValidatorFactory) {
		HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator.class).configure();
		if(constraintValidatorFactory != null) {
			configuration.constraintValidatorFactory(constraintValidatorFactory);
		}
		ValidatorFactory validatorFactory = configuration.failFast(failFast).buildValidatorFactory();
		return validatorFactory.getValidator();
	}
	
	/**
	 * 创建Validator
	 * @param failFast						- 是否快速失败模式
	 * @return
	 */
	public static Validator createValidator(boolean failFast) {
		return createValidator(failFast, null);
	}
	
}
