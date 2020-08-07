package com.penglecode.xmodule.common.validation.support;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义的校验顺序
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/6 13:45
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
public @interface ValidationOrder {

    /**
     * 指示同一个bean中不同字段之间的验证顺序，值越小的优先执行校验
     * @return
     */
    int value() default Integer.MAX_VALUE;

    /**
     * 指示同一个property字段中不同约束之间之间的验证顺序，校验顺序按照数组顺序执行校验
     * @return
     */
    Class<? extends Annotation>[] constraints() default {};

}
