package com.penglecode.xmodule.master4j.spring.aop.pointcut;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.util.Arrays;

/**
 * 基于AspectJ的AspectJExpressionPointcut示例
 * Spring 提供了AspectJExpressionPointcut类来通过AspectJ的表达式语言定义切入点。
 * 要在Spring中使用AspectJ切入点表达式， 需要在项目的类路径中包含两个AspectJ库文件，分别是aspecjrt.jar和aspectjweaver.jar
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 23:50
 */
public class AspectJExpressionPointcutExample implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println(String.format("【%s】>>> method = %s, arguments = %s, target = %s", getClass().getSimpleName(), invocation.getMethod(), Arrays.toString(invocation.getArguments()), invocation.getThis()));
        Object returnValue = invocation.proceed();
        System.out.println(String.format("【%s】<<< method = %s, returnVal = %s, target = %s", getClass().getSimpleName(), invocation.getMethod(), returnValue, invocation.getThis()));
        return returnValue;
    }

    public static void main(String[] args) {
        AspectJExpressionPointcutExample example = new AspectJExpressionPointcutExample();

        MailService gmailService = new GMailServiceImpl();
        MailService qmailService = new QMailServiceImpl();

        ProxyFactory proxyFactory;
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.penglecode.xmodule.master4j.spring.aop.pointcut.*.sendHtml*(..))");

        //创建qmailService对象的代理
        proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(MailService.class);
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(pointcut, example));
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
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(pointcut, example));
        proxyFactory.setTarget(gmailService);

        MailService proxyGmailService = (MailService) proxyFactory.getProxy();
        proxyGmailService.sendTextMail("admin123@163.com", "设备告警", "温度过高!");
        System.out.println("------------------------------------------------------------");
        proxyGmailService.sendHtmlMail("admin123@163.com", "设备告警", "温度过高!");
    }

}
