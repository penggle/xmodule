package com.penglecode.xmodule.common.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @DateTimePattern自定义注解校验器
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/5 19:08
 */
public class DateTimePatternConstraintValidator implements ConstraintValidator<DateTimePattern, String> {

	private DateTimePattern constraint;
	
	@Override
	public void initialize(DateTimePattern constraint) {
		this.constraint = constraint;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value != null) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(constraint.pattern());
				if(DateTimePattern.Type.DATE.equals(constraint.type())) {
					LocalDate date = LocalDate.parse(value, formatter);
					return date != null;
				} else if (DateTimePattern.Type.TIME.equals(constraint.type())) {
					LocalTime time = LocalTime.parse(value, formatter);
					return time != null;
				} else {
					LocalDateTime dateTime = LocalDateTime.parse(value, formatter);
					return dateTime != null;
				}
			} catch (Exception e) {}
			return false;
		}
		return true;
	}
	
}
