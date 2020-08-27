package com.penglecode.xmodule.master4j.java.lang.reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 基于JDK动态代理的代理逻辑
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 18:26
 */
public class JdkDynamicProxy implements InvocationHandler {

    private final Object target;

    public JdkDynamicProxy(Object target) {
        this.target = target;
    }

    public <T> T newProxy() {
        Class<?> clazz = target.getClass();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean success = false;
        try {
            System.out.println(String.format(">>> Send message: [%s] to [%s]", args[1], args[0]));
            success = (boolean) method.invoke(target, args);
            System.out.println(String.format("<<< Sent message: [%s] to [%s]", args[1], args[0]));
        } catch (Throwable e) {
            System.err.println(String.format(">>> Send message error: %s", e.getMessage()));
            e.printStackTrace();
        }
        return success;
    }

}
