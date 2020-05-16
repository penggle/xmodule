package com.penglecode.xmodule.common.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 校验一个字符串是否是非负数
 * 
 * @author 	pengpeng
 * @date 	2020年4月27日 下午4:56:29
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=NonNegativeNumberConstraintValidator.class)
public @interface NonNegativeNumber {

	/**
	 * @return 错误消息模板
	 */
	String message() default "{javax.validation.constraints.NonNegativeNumber.message}";

	/**
	 * @return 约束所属的组
	 */
	Class<?>[] groups() default { };

	/**
	 * @return 与约束关联的有效负载
	 */
	Class<? extends Payload>[] payload() default { };
	
}
