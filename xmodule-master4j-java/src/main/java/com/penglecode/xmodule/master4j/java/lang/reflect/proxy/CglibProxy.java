package com.penglecode.xmodule.master4j.java.lang.reflect.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * 基于Cglib代理的代理逻辑
 *
 * 注：此处用的是Spring将cglib合到自己jar包体系下的实现，只是包名不一样，实现代码及效果一样
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 19:02
 */
public class CglibProxy implements MethodInterceptor {

    public <T> T newProxy(Class<T> targetClass) {
        Assert.isTrue(targetClass.getSuperclass() != null, "Parameter 'targetClass' must be a class type!");
        Enhancer enhancer = new Enhancer();
        //设置即将生成的代理类的超类
        enhancer.setSuperclass(targetClass);
        //设置代理类的回调方法(即intercept(..)方法)
        enhancer.setCallback(this);
        //生成代理类
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        boolean success = false;
        try {
            System.out.println(String.format(">>> Send message: [%s] to [%s]", args[1], args[0]));
            //success = (boolean) method.invoke(obj, args); //不能调用此方法，相当于自己调自己形成递归死循环
            //success = (boolean) methodProxy.invoke(obj, args); //不能调用此方法，相当于自己调自己形成递归死循环
            success = (boolean) methodProxy.invokeSuper(obj, args); //相当于调用super.xxx(..)方法
            System.out.println(String.format("<<< Sent message: [%s] to [%s]", args[1], args[0]));
        } catch (Throwable e) {
            System.err.println(String.format(">>> Send message error: %s", e.getMessage()));
            e.printStackTrace();
        }
        return success;
    }
}
