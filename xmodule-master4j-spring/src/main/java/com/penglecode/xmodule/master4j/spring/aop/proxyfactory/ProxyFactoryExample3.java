package com.penglecode.xmodule.master4j.spring.aop.proxyfactory;

import com.penglecode.xmodule.master4j.spring.aop.advice.TimeService;
import com.penglecode.xmodule.master4j.spring.aop.advice.TimeServiceSimpleAdvice;
import com.penglecode.xmodule.master4j.spring.aop.advice.TimeServiceImpl;
import com.penglecode.xmodule.master4j.spring.aop.pointcut.ObjectMethodExcludedPointcut;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.util.Arrays;

import static com.penglecode.xmodule.master4j.spring.aop.advice.TimeServiceImpl.LONDON;
import static com.penglecode.xmodule.master4j.spring.aop.advice.TimeServiceImpl.SHANG_HAI;

/**
 * ProxyFactory是Spring AOP的核心，它是以手动编程方式实现AOP功能的核心类
 *
 * 本例展示一个最简单的入门级Spring AOP示例，同时说明ProxyFactory类中一些配置项的作用
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/18 18:33
 */
public class ProxyFactoryExample3 {

    private static void executeProxyTest(boolean useJdkProxy) throws Exception {
        System.out.println(String.format("==================================%s==================================", useJdkProxy ? "使用JDK动态代理" : "使用CGLIB动态代理"));
        TimeService targetService = new TimeServiceImpl();
        MethodBeforeAdvice advice = new TimeServiceSimpleAdvice();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(targetService);
        if(useJdkProxy) {
            proxyFactory.setInterfaces(TimeService.class);
        }

        //使用自定义的切面
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new ObjectMethodExcludedPointcut(), advice));
        //设置代理类是否实现Advised接口，该接口可以查询到代理的状态，默认为false意味着实现Advised接口
        proxyFactory.setOpaque(true);
        //这个优化指的是尽量走CGLIB动态代理而非JDK动态代理
        proxyFactory.setOptimize(false);
        /**
         * 这个用来冻结对proxyFactory的修改，即冻结了修改Advice/Advisor，如果(proxyFactory.isFrozen() && proxyFactory.isStatic())为true
         * 那么CGLIB动态代理将会采取优化措施，将会使用CglibAopProxy.FixedChainStaticTargetInterceptor来取代CglibAopProxy.DynamicAdvisedInterceptor
         */
        proxyFactory.setFrozen(true);

        TimeService proxyService = (TimeService) proxyFactory.getProxy();
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

        System.out.println(proxyService.getNowTime(SHANG_HAI));
        System.out.println();

        System.out.println(proxyService.getNowInstant(LONDON));
        System.out.println();
    }

    public static void cglibProxyTest() throws Exception {
        executeProxyTest(false);
    }

    public static void jdkProxyTest() throws Exception {
        executeProxyTest(true);
    }

    public static void main(String[] args) throws Exception {
        //cglibProxyTest();
        jdkProxyTest();
    }

}
