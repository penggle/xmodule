package com.penglecode.xmodule.common.util;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * 基于javax.validation的数据校验工具类
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/6 11:25
 */
public class ValidationUtils {

    /**
     * 校验整个bean
     * @param bean
     * @param groups
     * @param <T>
     * @see Validator#validate(Object, Class[])
     */
    public static <T> void validateBean(T bean, Class<?>... groups) {
        Set<ConstraintViolation<T>> results = getValidator().validate(bean, groups);
        raiseConstraintViolationExceptionIf(results);
    }

    /**
     * 校验指定bean的某个字段
     * @param bean
     * @param propertyName
     * @param groups
     * @param <T>
     * @see Validator#validateProperty(Object, String, Class[])
     */
    public static <T> void validateProperty(T bean, String propertyName, Class<?>... groups) {
        Set<ConstraintViolation<T>> results = getValidator().validateProperty(bean, propertyName, groups);
        raiseConstraintViolationExceptionIf(results);
    }

    /**
     * 使用指定的字段值value去校验放置在指定的beanType.propertyName字段上的所有约束类
     * @param beanType
     * @param propertyName
     * @param value
     * @param groups
     * @param <T>
     */
    public static <T> void validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
        Set<ConstraintViolation<T>> results = getValidator().validateValue(beanType, propertyName, value, groups);
        raiseConstraintViolationExceptionIf(results);
    }

    /**
     * 获取javax.validation.Validator校验器
     * @return
     */
    protected static Validator getValidator() {
        return ValidatorUtils.getGlobalValidator();
    }

    /**
     * 如果results不为空则触发ConstraintViolationException异常
     * @param results
     * @param <T>
     */
    protected static <T> void raiseConstraintViolationExceptionIf(Set<ConstraintViolation<T>> results) {
        if(!results.isEmpty()) {
            throw new ConstraintViolationException(results);
        }
    }

}
