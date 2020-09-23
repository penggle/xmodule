package com.penglecode.xmodule.master4j.spring.aop.advice;

import com.penglecode.xmodule.master4j.spring.aop.pointcut.ObjectMethodExcludedPointcut;
import org.aopalliance.aop.Advice;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 异常通知：又叫异常返回通知(After throwing advice)仅在连接点抛出异常后执行。
 *
 * ThrowsAdvice接口中并没有定义任何接口方法，其通知方法完全靠既定约定来维持，
 * Spring AOP在异常通知中寻找的第一通知方法是一个或多个被称为afterThrowing()的公共方法。
 * 方法的返回类型并不重耍，但最好是void，因为此方法不能返回任何有意义的值。
 *
 * 如果Advice实现中存在多个afterThrowing()方法，那么会：
 * 1、首先严格匹配afterThrowing()方法参数中的那个异常类型的参数，如果遇到异常类型完全匹配的方法那么就是候选执行方法，
 * 2、如果第1点所述的候选方法有多个，那么将按方法在类中的声明顺序先后来定，执行先声明的那个。
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 18:07
 */
public class ThrowsAdviceExample implements ThrowsAdvice {

    public void afterThrowing(IllegalArgumentException ex) {
        System.err.println(String.format("【%s】>>> ex = %s", getClass().getSimpleName(), ex));
    }

    public void afterThrowing(Method method, Object[] args, Object target, IllegalArgumentException ex) {
        System.err.println(String.format("【%s】>>> method = %s, args = %s, target = %s, ex = %s", getClass().getSimpleName(), method, Arrays.toString(args), target, ex));
    }

    private static void executeProxyTest(boolean useJdkProxy) throws Exception {
        System.out.println(String.format("==================================%s==================================", useJdkProxy ? "使用JDK动态代理" : "使用CGLIB动态代理"));
        TimeService targetService = new TimeServiceImpl();
        Advice advice = new ThrowsAdviceExample();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(targetService);
        if(useJdkProxy) {
            proxyFactory.setInterfaces(TimeService.class);
        }

        //1、直接添加通知，使用默认的切面DefaultPointcutAdvisor
        //proxyFactory.addAdvice(advice);
        //2、使用自定义的切面
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new ObjectMethodExcludedPointcut(), advice));

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

        System.out.println(proxyService.getNowTime(null));
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
