package com.penglecode.xmodule.common.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Anyone自定义注解校验器
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/5 19:08
 */
public class EnumsConstraintValidator implements ConstraintValidator<Enums, Object> {

    private Set<String> enums;

    @Override
    public void initialize(Enums constraintAnnotation) {
        this.enums = new HashSet<>(Arrays.asList(constraintAnnotation.values()));
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean valid = true;
        if(value != null) {
            return enums.contains(value.toString());
        }
        return valid;
    }
}
