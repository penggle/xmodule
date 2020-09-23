package com.penglecode.xmodule.master4j.spring.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * Spring AOP支持在切入点表达式中使用以下AspectJ切入点指示器(PCD)：
 *
 * ***@within*** - 跟@target(..)效果一样，这并没有奇怪的，因为Spring AOP走的路线是基于CGLIB或JDK动态代理的而非AspectJ，
 * 而且只能代理方法，引入AspectJ切入点指示器(PCD)只是再尽量向标准靠拢，所以@target()、@within()语义上并没有完全实现标准AspjectJ的那套语义，
 * 所以形成了@target()与@within()行为基本一样的问题。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/20 15:17
 */
@Aspect
public class RemoteServiceLogAspect4 {

    @Before(value="@within(com.penglecode.xmodule.master4j.spring.aop.aspect.service.WithinService)")
    public void beforeLogging(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println(String.format("【%s#beforeLogging】>>> Before: %s(%s)", getClass().getSimpleName(), methodName, Arrays.toString(args)));
    }

    @AfterReturning(value="@within(com.penglecode.xmodule.master4j.spring.aop.aspect.service.WithinService)", returning="returnValue")
    public void afterReturningLogging(JoinPoint jp, Object returnValue) {
        System.out.println(String.format("【%s#afterReturningLogging】 returnValue = %s", getClass().getSimpleName(), returnValue));
    }

    @AfterThrowing(value="@within(com.penglecode.xmodule.master4j.spring.aop.aspect.service.WithinService)", throwing="exception")
    public void afterThrowingLogging(JoinPoint jp, Throwable exception) {
        System.out.println(String.format("【%s#afterThrowingLogging】 exception = %s", getClass().getSimpleName(), exception));
    }

    @After(value="@within(com.penglecode.xmodule.master4j.spring.aop.aspect.service.WithinService)")
    public void afterFinallyLogging(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println(String.format("【%s#afterFinallyLogging】<<< After: %s(%s)", getClass().getSimpleName(), methodName, Arrays.toString(args)));
    }

}
