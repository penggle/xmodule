package com.penglecode.xmodule.common.validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
