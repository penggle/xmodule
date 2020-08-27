package com.penglecode.xmodule.master4j.java.lang.reflect.proxy;

import java.util.Arrays;

/**
 * Cglib动态代理示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 18:24
 */
public class CglibProxyExample {

    public static void main(String[] args) {
        MessageSendService messageSendService = new SmsMessageSendServiceImpl();
        MessageSendService messageSendServiceProxy = new CglibProxy().newProxy(SmsMessageSendServiceImpl.class);
        messageSendServiceProxy.sendMessage("13812345678", "尊敬的用户您好，您的话费余额已不足10元!");

        System.out.println("---------------------------------------------------");

        Class<?> proxyClass = messageSendServiceProxy.getClass();
        System.out.println(proxyClass);
        System.out.println(proxyClass.getSuperclass());
        System.out.println(Arrays.toString(proxyClass.getInterfaces()));
    }

}
