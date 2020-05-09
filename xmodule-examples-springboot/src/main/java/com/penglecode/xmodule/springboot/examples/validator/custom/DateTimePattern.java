package com.penglecode.xmodule.springboot.examples.validator.custom;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;

import javax.validation.Constraint;
import javax.validation.Payload;

@Repeatable(DateTimePattern.List.class)
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
	 * @return the error message template
	 */
	String message() default "{javax.validation.constraints.DateTimePattern.message}";

	/**
	 * @return the groups the constraint belongs to
	 */
	Class<?>[] groups() default { };

	/**
	 * @return the payload associated to the constraint
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
	
	/**
	 * Defines several {@link DateTimePattern} annotations on the same element.
	 *
	 * @see DateTimePattern
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		DateTimePattern[] value();
	}
	
}
