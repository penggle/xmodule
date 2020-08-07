package com.penglecode.xmodule.common.validation.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 枚举自定义校验注解
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/5 19:05
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy=EnumsConstraintValidator.class)
public @interface Enums {

    /**
     * 枚举值
     * @return
     */
    String[] values();

    /**
     * @return the error message template
     */
    String message() default "{javax.validation.constraints.Enums.message}";

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default { };

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default { };

}
