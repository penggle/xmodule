package com.penglecode.xmodule.master4j.spring.aop.proxyfactorybean;

import org.springframework.aop.AfterAdvice;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/20 13:25
 */
public class TimeServiceAfterAdvice implements ThrowsAdvice, AfterAdvice {

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) {
        System.err.println(String.format("【%s】>>> method = %s, args = %s, target = %s, ex = %s", getClass().getSimpleName(), method, Arrays.toString(args), target, ex));
    }

    //这个方法，在编程式中无法应用
    public void afterAdvice() {
        System.out.println(String.format("【%s】>>> AfterAdvice(After finlly advice)", getClass().getSimpleName()));
    }

}
