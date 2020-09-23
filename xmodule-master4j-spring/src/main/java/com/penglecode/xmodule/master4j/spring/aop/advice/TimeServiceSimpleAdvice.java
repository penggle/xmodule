package com.penglecode.xmodule.master4j.spring.aop.advice;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/18 18:42
 */
public class TimeServiceSimpleAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println(String.format("【%s】>>> method = %s, args = %s, target = %s", getClass().getSimpleName(), method, Arrays.toString(args), target));
    }
}
