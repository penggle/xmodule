package com.penglecode.xmodule.master4j.jvm.chapter9.dynamicproxy;

import sun.misc.ProxyGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/23 9:08
 */
public class JdkDynamicProxyExample {

    interface IHello {

        void sayHello();

    }

    static class Hello implements IHello {

        @Override
        public void sayHello() {
            System.out.println("hello world!");
        }

    }

    static class DynamicProxy implements InvocationHandler {

        private Object target;

        @SuppressWarnings("unchecked")
        public <T> T newProxy(Object target) {
            this.target = target;
            Class<?> clazz = target.getClass();
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("welcome");
            return method.invoke(target, args);
        }
    }

    public static void main(String[] args) {
        /**
         * 在main方法中加入这个参数，生成$Proxy0.class，这个生成的位置：在idea中是在当前工作空间下面
         * 例如C:\workbench\Java\IdeaProjects\projects1\下
         */
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        //生成动态代理类的真实代码java文件
        IHello hello = new DynamicProxy().newProxy(new Hello());
        hello.sayHello();
    }

}
