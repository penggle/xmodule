package com.penglecode.xmodule.master4j.java.util.spi;

import java.net.URL;
import java.util.Enumeration;

/**
 * SPI的原理实现示例
 *
 * 也就是ServiceLoader是如何获取到SPI接口列表的，具体见java.util.ServiceLoader.LazyIterator#hasNextService()
 * 最终调用的还是ClassLoader#getResources()方法
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/24 19:16
 */
public class SPIActualExample {


    public static void paySpiServiceTest() throws Exception {
        String spiServiceName = "META-INF/services/" + IPay.class.getName();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> serviceFiles = classLoader.getResources(spiServiceName);
        while(serviceFiles.hasMoreElements()) {
            URL url = serviceFiles.nextElement();
            System.out.println(url);
            //file:/C:/workbench/GIT/xmodule/xmodule-master4j-java/target/classes/META-INF/services/com.penglecode.xmodule.master4j.java.util.spi.IPay
        }
    }

    public static void springSpiServiceTest() throws Exception {
        String spiServiceName = "META-INF/" + "spring.factories";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> serviceFiles = classLoader.getResources(spiServiceName);
        while(serviceFiles.hasMoreElements()) {
            URL url = serviceFiles.nextElement();
            System.out.println(url);
            //jar:file:/C:/Users/Pengle/.m2/repository/org/springframework/boot/spring-boot/2.3.4.RELEASE/spring-boot-2.3.4.RELEASE.jar!/META-INF/spring.factories
            //jar:file:/C:/Users/Pengle/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.3.4.RELEASE/spring-boot-autoconfigure-2.3.4.RELEASE.jar!/META-INF/spring.factories
            //jar:file:/C:/Users/Pengle/.m2/repository/org/springframework/spring-beans/5.2.9.RELEASE/spring-beans-5.2.9.RELEASE.jar!/META-INF/spring.factories
        }
    }

    public static void main(String[] args) throws Exception {
        paySpiServiceTest();
        springSpiServiceTest();
    }

}
