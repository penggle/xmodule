package com.penglecode.xmodule.master4j.java.lang.reflect.proxy;

import java.util.Arrays;

/**
 * JDK动态代理示例
 *
 * 运行前加入这个参数，生成$Proxy0.class，这个生成的位置：在idea中是在当前工作空间下面
 * 例如C:\workbench\Java\IdeaProjects\projects1\下
 * System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 18:24
 */
public class JdkDynamicProxyExample {

    public static void main(String[] args) {
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles","true");

        MessageSendService messageSendService = new SmsMessageSendServiceImpl();
        MessageSendService messageSendServiceProxy = new JdkDynamicProxy(messageSendService).newProxy();
        messageSendServiceProxy.sendMessage("13812345678", "尊敬的用户您好，您的话费余额已不足10元!");

        System.out.println("---------------------------------------------------");

        /**
         * 生成的代理类的声明大致如下：
         * public final class $Proxy0 extends Proxy implements MessageSendService  {
         *     ...
         * }
         */
        Class<?> proxyClass = messageSendServiceProxy.getClass();
        System.out.println(proxyClass); //class com.sun.proxy.$Proxy0
        System.out.println(proxyClass.getSuperclass()); //class java.lang.reflect.Proxy
        System.out.println(Arrays.toString(proxyClass.getInterfaces())); //[interface xxx.MessageSendService]
    }

}
