package com.penglecode.xmodule.master4j.spring.aop.pointcut;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.util.Arrays;

/**
 * 基于注解的AnnotationMatchingPointcut示例
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 23:50
 */
public class AnnotationMatchingPointcutExample implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println(String.format("【%s】>>> method = %s, arguments = %s, target = %s", getClass().getSimpleName(), invocation.getMethod(), Arrays.toString(invocation.getArguments()), invocation.getThis()));
        Object returnValue = invocation.proceed();
        System.out.println(String.format("【%s】<<< method = %s, returnVal = %s, target = %s", getClass().getSimpleName(), invocation.getMethod(), returnValue, invocation.getThis()));
        return returnValue;
    }

    public static void main(String[] args) {
        AnnotationMatchingPointcutExample example = new AnnotationMatchingPointcutExample();

        MailService gmailService = new GMailServiceImpl();
        MailService qmailService = new QMailServiceImpl();

        ProxyFactory proxyFactory;

        //创建qmailService对象的代理
        proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(MailService.class);
        //在接口方法上加上过滤注解@Proxyable
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new AnnotationMatchingPointcut(null, Proxyable.class, true), example));
        proxyFactory.setTarget(qmailService);

        MailService proxyQmailService = (MailService) proxyFactory.getProxy();

        proxyQmailService.sendTextMail("admin123@qq.com", "余额告警", "余额不足!");
        System.out.println("------------------------------------------------------------");
        proxyQmailService.sendHtmlMail("admin123@qq.com", "余额告警", "余额不足!");

        System.out.println();
        System.out.println();

        //创建gmailService对象的代理
        proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(MailService.class);
        //在接口方法上加上过滤注解@Proxyable
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new AnnotationMatchingPointcut(null, Proxyable.class, true), example));
        proxyFactory.setTarget(gmailService);

        MailService proxyGmailService = (MailService) proxyFactory.getProxy();
        proxyGmailService.sendTextMail("admin123@163.com", "设备告警", "温度过高!");
        System.out.println("------------------------------------------------------------");
        proxyGmailService.sendHtmlMail("admin123@163.com", "设备告警", "温度过高!");
    }

}
