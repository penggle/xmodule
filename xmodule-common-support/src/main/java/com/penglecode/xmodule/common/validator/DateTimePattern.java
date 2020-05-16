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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 校验一个字符串是否是指定的日期时间格式
 * 
 * @author 	pengpeng
 * @date 	2020年4月27日 下午4:55:46
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=DateTimePatternConstraintValidator.class)
public @interface DateTimePattern {

	/**
	 * @return 实际的日期时间类型
	 */
	Type type();
	
	/**
	 * @return 日期时间的格式
	 */
	String pattern();

	/**
	 * @return 错误消息模板
	 */
	String message() default "{javax.validation.constraints.DateTimePattern.message}";

	/**
	 * @return 约束所属的组
	 */
	Class<?>[] groups() default { };

	/**
	 * @return 与约束关联的有效负载
	 */
	Class<? extends Payload>[] payload() default { };
	
	enum Type {
		
		DATE(LocalDate.class), TIME(LocalTime.class), DATETIME(LocalDateTime.class);
		
		private Class<? extends TemporalAccessor> type;

		private Type(Class<? extends TemporalAccessor> type) {
			this.type = type;
		}

		public Class<? extends TemporalAccessor> getType() {
			return type;
		}

		public void setType(Class<? extends TemporalAccessor> type) {
			this.type = type;
		}

	}
	
}
