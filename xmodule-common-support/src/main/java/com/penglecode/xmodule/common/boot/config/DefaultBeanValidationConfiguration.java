package com.penglecode.xmodule.common.boot.config;

import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import com.penglecode.xmodule.common.util.BeanValidatorUtils;

/**
 * 默认Java bean validation配置
 * 
 * @author 	pengpeng
 * @date 	2020年4月27日 下午5:34:31
 */
@Configuration
@ConditionalOnClass(HibernateValidator.class)
public class DefaultBeanValidationConfiguration extends AbstractSpringConfiguration {

	@Bean
	public Validator defaultValidator() {
	    return BeanValidatorUtils.createValidator(true, new SpringConstraintValidatorFactory(getApplicationContext().getAutowireCapableBeanFactory()));
	}
	
}
