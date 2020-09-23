package com.penglecode.xmodule.master4j.spring.aop.advice;

import com.penglecode.xmodule.master4j.spring.aop.pointcut.ObjectMethodExcludedPointcut;
import org.aopalliance.aop.Advice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 前置通知：在连接点前面执行，前置通知不会影响连接点的执行，除非此处抛出异常。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 22:51
 */
public class MethodBeforeAdviceExample implements MethodBeforeAdvice {

    private static final String DEFAULT_TOKEN = "123456";

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println(String.format("【%s】>>> method = %s, args = %s, target = %s", getClass().getSimpleName(), method, Arrays.toString(args), target));
        if(target instanceof WeatherService) {
            if("getWeatherInfoByCity".equals(method.getName())) {
                String cityid = (String) args[0];
                String token = (String) args[1];
                Assert.hasText(cityid, "Parameter 'cityid' must be required!");
                Assert.hasText(token, "Parameter 'token' must be required!");
                Assert.isTrue(DEFAULT_TOKEN.equals(token), String.format("Parameter 'token' is invalid：%s", token));
            }
        }
    }

    private static void executeProxyTest(boolean useJdkProxy) throws Exception {
        System.out.println(String.format("==================================%s==================================", useJdkProxy ? "使用JDK动态代理" : "使用CGLIB动态代理"));
        WeatherService targetService = new WeatherServiceImpl();
        Advice advice = new MethodBeforeAdviceExample();
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
