package com.penglecode.xmodule.master4j.spring.aop.proxyfactory;

import com.penglecode.xmodule.master4j.spring.aop.advice.TimeService;
import com.penglecode.xmodule.master4j.spring.aop.advice.TimeServiceSimpleAdvice;
import com.penglecode.xmodule.master4j.spring.aop.advice.TimeServiceImpl;
import com.penglecode.xmodule.master4j.spring.aop.pointcut.ObjectMethodExcludedPointcut;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.cglib.core.DebuggingClassWriter;

import java.util.Arrays;

import static com.penglecode.xmodule.master4j.spring.aop.advice.TimeServiceImpl.LONDON;
import static com.penglecode.xmodule.master4j.spring.aop.advice.TimeServiceImpl.SHANG_HAI;

/**
 * ProxyFactory是Spring AOP的核心，它是以手动编程方式实现AOP功能的核心类
 *
 * 本例展示一个最简单的入门级Spring AOP示例，它使用一个自定义的切面来取代默认的切面，
 * 本例通过自定义的Pointcut来排除对来在java.lang.Object类中的方法应用切面
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/18 18:33
 */
public class ProxyFactoryExample2 {

    private static void outputProxyClassFile() {
        //该设置用于输出cglib动态代理产生的类，我将其设为和JDK动态代理生成的$Proxy0的默认位置一样
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:/workbench/Java/IdeaProjects/projects1");
        //该设置用于输出jdk动态代理产生的类，本机生成位置如上面所示
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        //System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true"); //JDK8以上版本使用这个
    }

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

        TimeService proxyService = (TimeService) proxyFactory.getProxy();
        System.out.println();

        System.out.println(">>> proxyFactory = " + proxyFactory);
        System.out.println(">>> proxyTargetClass = " + proxyFactory.isProxyTargetClass());
        System.out.println(">>> targetService = " + targetService);
        /**
         * 此处打印代理对象proxyService，将导致TimeService#toString()方法被调用。
         * 对于toString()、clone()、finalize()方法的调用默认将会应用切面，也就是说通知默认会拦截这三个方法，
         * 如果不希望切面去处理这三个方法，那么必须替换DefaultPointcutAdvisor的默认切点(Pointcut.TRUE - 即应用到一切上)
         *
         * 此例由于使用了ObjectMethodExcludedPointcut，故此处打印proxyService将不会导致在proxyService.toString()方法上应用切面
         */
        System.out.println(">>> proxyService = " + proxyService);
        System.out.println(">>> proxyService == targetService ? " + (proxyService == targetService));
        /**
         * 调用代理对象proxyService的equals()方法将会被Spring AOP内部特定的拦截器拦截，
         * CGLIB动态代理或JDK动态代理都会特殊处理hashcode()和equals()方法，而不会调用目标对象或者代理对象上真正的hashcode()或equals()方法逻辑，
         * 取而代之的是特殊的处理逻辑：
         * - CGLIB动态代理见org.springframework.aop.framework.CglibAopProxy.EqualsInterceptor，org.springframework.aop.framework.CglibAopProxy.HashCodeInterceptor
         * - JDK动态代理见org.springframework.aop.framework.JdkDynamicAopProxy#equals()，org.springframework.aop.framework.JdkDynamicAopProxy#hashCode()
         */
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

        /**
         * Object中的剩余几个final的方法(getClass()、wait()、notify()等)，由于是final的，是无法做到被动态代理的，
         * 因为final的方法无法被重写，调用这些方法压根就不会进入到InvocationHandler#invoke()或者MethodInterceptor#intercept()方法中去，
         * 对这些方法的调用都会真实的落在实际对象上
         */
        synchronized (proxyService) {
            proxyService.wait(1000);
        }
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
