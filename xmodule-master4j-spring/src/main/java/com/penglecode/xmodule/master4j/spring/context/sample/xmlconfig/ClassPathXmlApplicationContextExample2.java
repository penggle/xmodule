package com.penglecode.xmodule.master4j.spring.context.sample.xmlconfig;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ClassPathXmlApplicationContext的实例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/14 15:28
 */
public class ClassPathXmlApplicationContextExample2 {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"applicationContext2.xml"}, ClassPathXmlApplicationContextExample2.class);
        System.out.println(">>> applicationContext = " + applicationContext);
        /**
         * {@link ConfigurationClassPostProcessor}
         * {@link CommonAnnotationBeanPostProcessor}
         * {@link AutowiredAnnotationBeanPostProcessor}
         */
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        System.out.println("=============================================All BeanDefinitions(" + beanNames.length + ")=================================================");
        for (String beanName : beanNames) {
            System.out.println(applicationContext.getBeanFactory().getBeanDefinition(beanName));
        }

    }

}
