package com.penglecode.xmodule.master4j.spring.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * Spring AOP支持在切入点表达式中使用以下AspectJ切入点指示器(PCD)：
 *
 * ***@annotation*** - 通过判断被代理对象上被调用的代理方法有没有标注@annotation(annotation)中指定的注解annotation来匹配切点，
 * 且annotation注解只能加在targetClass上，即实现类上才有效果，annotation注解加在方法上或者接口上都是没有效果的。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/20 15:17
 */
@Aspect
public class RemoteServiceLogAspect5 {

    @Before(value="@annotation(com.penglecode.xmodule.master4j.spring.aop.aspect.service.MyAnnotation)")
    public void beforeLogging(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println(String.format("【%s#beforeLogging】>>> Before: %s(%s)", getClass().getSimpleName(), methodName, Arrays.toString(args)));
    }

    @AfterReturning(value="@annotation(com.penglecode.xmodule.master4j.spring.aop.aspect.service.MyAnnotation)", returning="returnValue")
    public void afterReturningLogging(JoinPoint jp, Object returnValue) {
        System.out.println(String.format("【%s#afterReturningLogging】 returnValue = %s", getClass().getSimpleName(), returnValue));
    }

    @AfterThrowing(value="@annotation(com.penglecode.xmodule.master4j.spring.aop.aspect.service.MyAnnotation)", throwing="exception")
    public void afterThrowingLogging(JoinPoint jp, Throwable exception) {
        System.out.println(String.format("【%s#afterThrowingLogging】 exception = %s", getClass().getSimpleName(), exception));
    }

    @After(value="@annotation(com.penglecode.xmodule.master4j.spring.aop.aspect.service.MyAnnotation)")
    public void afterFinallyLogging(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println(String.format("【%s#afterFinallyLogging】<<< After: %s(%s)", getClass().getSimpleName(), methodName, Arrays.toString(args)));
    }

}
