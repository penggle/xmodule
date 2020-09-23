package com.penglecode.xmodule.master4j.spring.aop.advice;

import com.penglecode.xmodule.master4j.spring.aop.pointcut.ObjectMethodExcludedPointcut;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.util.Arrays;

/**
 * 环绕通知：环绕通知围绕在连接点前后，比如一个方法调用的前后。这是最强大的通知类型，能在方法调用前后自定义一些操作。
 * 环绕通知还需要负责决定是继续处理join point(调用ProceedingJoinPoint的proceed方法)还是压根就绕过原方法。
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 18:30
 */
public class MethodIntercepterExample implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println(String.format("【%s】>>> method = %s, arguments = %s, target = %s", getClass().getSimpleName(), invocation.getMethod().getName(), Arrays.toString(invocation.getArguments()), invocation.getThis()));
        Object returnValue = invocation.proceed();
        System.out.println(String.format("【%s】<<< method = %s, returnVal = %s, target = %s", getClass().getSimpleName(), invocation.getMethod().getName(), returnValue, invocation.getThis()));
        return returnValue;
    }

    private static void executeProxyTest(boolean useJdkProxy) throws Exception {
        System.out.println(String.format("==================================%s==================================", useJdkProxy ? "使用JDK动态代理" : "使用CGLIB动态代理"));
        WeatherService targetService = new WeatherServiceImpl();
        Advice advice = new MethodIntercepterExample();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(targetService);
        if(useJdkProxy) {
            proxyFactory.setInterfaces(WeatherService.class);
        }

        //1、直接添加通知，使用默认的切面DefaultPointcutAdvisor
        //proxyFactory.addAdvice(advice);
        //2、使用自定义的切面
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new ObjectMethodExcludedPointcut(), advice));

        WeatherService proxyService = (WeatherService) proxyFactory.getProxy();
        System.out.println();

        System.out.println(">>> proxyFactory = " + proxyFactory);
        System.out.println(">>> proxyTargetClass = " + proxyFactory.isProxyTargetClass());
        System.out.println(">>> targetService = " + targetService);
        System.out.println(">>> proxyService = " + proxyService);
        System.out.println(">>> proxyService == targetService ? " + (proxyService == targetService));
        System.out.println(">>> proxyService.equals(targetService)  ? " + (proxyService.equals(targetService)));
        System.out.println();

        Class<?> proxyClass = proxyService.getClass();
        Class<?> proxySuperClass = proxyClass.getSuperclass();
        Class<?>[] proxyInterfaces = proxyClass.getInterfaces();
        System.out.println(">>> proxyClass = " + proxyClass);
        System.out.println(">>> proxySuperClass = " + proxySuperClass);
        System.out.println(">>> proxyInterfaces = " + Arrays.toString(proxyInterfaces));
        System.out.println();

        System.out.println(proxyService.getWeatherInfoByCity("101190101", "123123"));
        System.out.println();

    }

    public static void cglibProxyTest() throws Exception {
        executeProxyTest(false);
    }

    public static void jdkProxyTest() throws Exception {
        executeProxyTest(true);
    }

    public static void main(String[] args) throws Exception {
        cglibProxyTest();
        //jdkProxyTest();
    }

}
