package com.penglecode.xmodule.master4j.spring.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * Spring AOP支持在切入点表达式中使用以下AspectJ切入点指示器(PCD)：
 *
 * ***within*** - 简单地通过判断当前被执行方法是否是within(type)中指定类型type中声明的方法来匹配切点，
 * 例如within(com.xxx.FooServiceImpl)只会将通知应用到FooServiceImpl实现类中的方法上，换成接口FooService则没有效果。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/20 15:17
 */
@Aspect
public class RemoteServiceLogAspect2 {

    //这个有效果
    @Before(value="within(com.penglecode.xmodule.master4j.spring.aop.aspect.service.*)")
    public void beforeLogging(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println(String.format("【%s#beforeLogging】>>> Before: %s(%s)", getClass().getSimpleName(), methodName, Arrays.toString(args)));
    }

    //这个没效果
    @AfterReturning(value="within(com.penglecode.xmodule.master4j.spring.aop.aspect.service.NewsService)", returning="returnValue")
    public void afterReturningLogging(JoinPoint jp, Object returnValue) {
        System.out.println(String.format("【%s#afterReturningLogging】 returnValue = %s", getClass().getSimpleName(), returnValue));
    }

    //这个有效果
    @AfterThrowing(value="within(com.penglecode.xmodule.master4j.spring.aop.aspect.service.WeatherServiceImpl)", throwing="exception")
    public void afterThrowingLogging(JoinPoint jp, Throwable exception) {
        System.out.println(String.format("【%s#afterThrowingLogging】 exception = %s", getClass().getSimpleName(), exception));
    }

    //这个有效果
    @After(value="within(com.penglecode.xmodule.master4j.spring.aop.aspect.service.NewsServiceImpl)")
    public void afterFinallyLogging(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println(String.format("【%s#afterFinallyLogging】<<< After: %s(%s)", getClass().getSimpleName(), methodName, Arrays.toString(args)));
    }

}
