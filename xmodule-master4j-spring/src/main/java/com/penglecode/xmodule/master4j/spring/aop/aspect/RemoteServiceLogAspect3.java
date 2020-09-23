package com.penglecode.xmodule.master4j.spring.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * Spring AOP支持在切入点表达式中使用以下AspectJ切入点指示器(PCD)：
 *
 * *@target* - 通过判断被代理类型是否标注了@target(annotation)中指定类型annotation的注解来匹配的切点，
 * 且annotation注解只能加在targetClass上，即实现类上才有效果，annotation注解加在方法上或者接口上都是没有效果的。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/20 15:17
 */
@Aspect
public class RemoteServiceLogAspect3 {

    /**
     * @TargetService注解只加在接口(RemoteService、NewsService、WeatherService等)上面时是无效的，必须加在实现类上才有效果
     */
    @Before(value="@target(com.penglecode.xmodule.master4j.spring.aop.aspect.service.TargetService)")
    public void beforeLogging(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println(String.format("【%s#beforeLogging】>>> Before: %s(%s)", getClass().getSimpleName(), methodName, Arrays.toString(args)));
    }

    @AfterReturning(value="@target(com.penglecode.xmodule.master4j.spring.aop.aspect.service.TargetService)", returning="returnValue")
    public void afterReturningLogging(JoinPoint jp, Object returnValue) {
        System.out.println(String.format("【%s#afterReturningLogging】 returnValue = %s", getClass().getSimpleName(), returnValue));
    }

    @AfterThrowing(value="@target(com.penglecode.xmodule.master4j.spring.aop.aspect.service.TargetService)", throwing="exception")
    public void afterThrowingLogging(JoinPoint jp, Throwable exception) {
        System.out.println(String.format("【%s#afterThrowingLogging】 exception = %s", getClass().getSimpleName(), exception));
    }

    @After(value="@target(com.penglecode.xmodule.master4j.spring.aop.aspect.service.TargetService)")
    public void afterFinallyLogging(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println(String.format("【%s#afterFinallyLogging】<<< After: %s(%s)", getClass().getSimpleName(), methodName, Arrays.toString(args)));
    }

}
