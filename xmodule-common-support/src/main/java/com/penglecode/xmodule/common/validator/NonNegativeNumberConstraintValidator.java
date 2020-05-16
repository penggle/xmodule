package com.penglecode.xmodule.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NonNegativeNumberConstraintValidator implements ConstraintValidator<NonNegativeNumber, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value != null) {
			try {
				Double number = Double.valueOf(value);
				return number != null && number >= 0;
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
	
}
