package com.penglecode.xmodule.master4j.spring.aop.pointcut;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * StaticMethodMatcherPointcut示例
 *
 * - StaticMethodMatcher  该类只能在被拦截的方法声明上做拦截逻辑，无法在方法参数值上做拦截逻辑；并且该类的matchs(Method method, Class<?> targetClass)方法初次调用结果会被缓存起来，后面不会再次调用
 * - DynamicMethodMatcher  该类除了能在方法声明上做拦截逻辑，还可以在方法参数值上做拦截逻辑；并且该类的matchs(Method method, Class<?> targetClass， Object[] args)方法在每次调用代理对象上的代理方法时均会被调用一次
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 21:05
 */
public class StaticMethodMatcherPointcutExample extends StaticMethodMatcherPointcut implements MethodInterceptor {

    /**
     * 多次调用同一个代理对象上的代理方法，该matches()方法仅会被调用一次
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        System.out.println(String.format("【%s】>>> matches(%s, %s)", getClass().getSimpleName(), method, targetClass));
        return method.getName().startsWith("send");
    }

    @Override
    public ClassFilter getClassFilter() {
        System.out.println(String.format("【%s】>>> getClassFilter()", getClass().getSimpleName()));
        return clazz -> clazz.equals(GMailServiceImpl.class);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println(String.format("【%s】>>> method = %s, arguments = %s, target = %s", getClass().getSimpleName(), invocation.getMethod(), Arrays.toString(invocation.getArguments()), invocation.getThis()));
        Object returnValue = invocation.proceed();
        System.out.println(String.format("【%s】<<< method = %s, returnVal = %s, target = %s", getClass().getSimpleName(), invocation.getMethod(), returnValue, invocation.getThis()));
        return returnValue;
    }

    public static void main(String[] args) {
        StaticMethodMatcherPointcutExample example = new StaticMethodMatcherPointcutExample();

        MailService gmailService = new GMailServiceImpl();
        MailService qmailService = new QMailServiceImpl();

        ProxyFactory proxyFactory;

        //创建qmailService对象的代理
        proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(MailService.class);
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(example, example));
        proxyFactory.setTarget(qmailService);

        MailService proxyQmailService = (MailService) proxyFactory.getProxy();

        proxyQmailService.sendTextMail("admin123@qq.com", "余额告警", "余额不足!");
        System.out.println("------------------------------------------------------------");
        proxyQmailService.sendTextMail("admin123@qq.com", "余额告警", "余额不足!");

        System.out.println();
        System.out.println();

        //创建gmailService对象的代理
        proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(MailService.class);
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(example, example));
        proxyFactory.setTarget(gmailService);

        MailService proxyGmailService = (MailService) proxyFactory.getProxy();
        proxyGmailService.sendTextMail("admin123@163.com", "设备告警", "温度过高!");
        System.out.println("------------------------------------------------------------");
        proxyGmailService.sendTextMail("admin123@163.com", "设备告警", "温度过高!");
    }

}
