package com.penglecode.xmodule.master4j.spring.context.sample.xmlconfig;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * ClassPathXmlApplicationContext的实例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/14 15:28
 */
public class ClassPathXmlApplicationContextExample1 {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"applicationContext1.xml"}, ClassPathXmlApplicationContextExample1.class);
        System.out.println(">>> applicationContext = " + applicationContext);

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        System.out.println("=============================================All BeanDefinitions(" + beanNames.length + ")=================================================");
        for (String beanName : beanNames) {
            System.out.println(applicationContext.getBeanFactory().getBeanDefinition(beanName));
        }

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        System.out.println(environment);
    }

}
