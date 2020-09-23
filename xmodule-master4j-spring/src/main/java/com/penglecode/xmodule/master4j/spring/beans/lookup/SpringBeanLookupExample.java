package com.penglecode.xmodule.master4j.spring.beans.lookup;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring @Lookup方法查找
 *
 * 建议仅在需要查找的bean的生命周期为prototype时才采用，这样避免频繁查找带来的性能损耗
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 20:11
 */
public class SpringBeanLookupExample {

    @Configuration
    @ComponentScan(basePackageClasses=SpringBeanLookupExample.class)
    public static class ExampleConfiguration {}

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleConfiguration.class);
        applicationContext.registerShutdownHook();
        System.out.println(applicationContext);

        int count = 10;

        EnvironmentService standardEnvironmentService = (EnvironmentService) applicationContext.getBean("standardEnvironmentService");
        for(int i = 0; i < count; i++) {
            standardEnvironmentService.getEnvironment("java.home");
        }

        System.out.println("---------------------------------------------------------------------");

        EnvironmentService lookupEnvironmentService = (EnvironmentService) applicationContext.getBean("lookupEnvironmentService");
        for(int i = 0; i < count; i++) {
            lookupEnvironmentService.getEnvironment("java.home");
        }
    }

}
