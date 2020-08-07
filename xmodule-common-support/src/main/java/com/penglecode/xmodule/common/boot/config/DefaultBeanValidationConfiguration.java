package com.penglecode.xmodule.common.boot.config;

import com.penglecode.xmodule.common.util.ValidatorUtils;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.Validator;

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
	    return ValidatorUtils.createValidator(true, new SpringConstraintValidatorFactory(getApplicationContext().getAutowireCapableBeanFactory()));
	}
	
}
