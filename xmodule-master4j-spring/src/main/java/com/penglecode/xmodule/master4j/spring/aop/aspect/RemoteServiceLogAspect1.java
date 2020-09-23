package com.penglecode.xmodule.master4j.spring.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * Spring AOP支持在切入点表达式中使用以下AspectJ切入点指示器(PCD)：
 *
 * ***target*** - 通过判断被代理类型是否是target(type)中指定类型type的子类型来匹配的切点，
 * 例如target(org.springframework.data.repository.CrudRepository)那么继承或实现CrudRepository的类的方法将会被应用通知。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/20 15:17
 */
@Aspect
public class RemoteServiceLogAspect1 {

    //只对RemoteService类型的子类方法进行拦截
    @Before(value="target(com.penglecode.xmodule.master4j.spring.aop.aspect.service.RemoteService)")
    public void beforeLogging(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println(String.format("【%s#beforeLogging】>>> Before: %s(%s)", getClass().getSimpleName(), methodName, Arrays.toString(args)));
    }

    //只对RemoteService类型的子类方法进行拦截
    @AfterReturning(value="target(com.penglecode.xmodule.master4j.spring.aop.aspect.service.RemoteService)", returning="returnValue")
    public void afterReturningLogging(JoinPoint jp, Object returnValue) {
        System.out.println(String.format("【%s#afterReturningLogging】 returnValue = %s", getClass().getSimpleName(), returnValue));
    }

    //只对RemoteService类型的子类方法进行拦截
    @AfterThrowing(value="target(com.penglecode.xmodule.master4j.spring.aop.aspect.service.RemoteService)", throwing="exception")
    public void afterThrowingLogging(JoinPoint jp, Throwable exception) {
        System.out.println(String.format("【%s#afterThrowingLogging】 exception = %s", getClass().getSimpleName(), exception));
    }

    @After(value="target(com.penglecode.xmodule.master4j.spring.aop.aspect.service.RemoteService)")
    public void afterFinallyLogging(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        System.out.println(String.format("【%s#afterFinallyLogging】<<< After: %s(%s)", getClass().getSimpleName(), methodName, Arrays.toString(args)));
    }

}
