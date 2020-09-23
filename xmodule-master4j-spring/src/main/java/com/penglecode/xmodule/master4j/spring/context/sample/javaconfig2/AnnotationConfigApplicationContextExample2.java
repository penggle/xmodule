package com.penglecode.xmodule.master4j.spring.context.sample.javaconfig2;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * AnnotationConfigApplicationContext示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 16:07
 */
public class AnnotationConfigApplicationContextExample2 {

    public static void main(String[] args) {
        /**
         * 1、仅仅通过指定的一个@Configuration注释的配置类Example1Configuration来启动AnnotationConfigApplicationContext
         * 2、如果Example1Configuration配置类中没有使用@ComponentScan那么将完全丧失自动的组件扫描能力
         *    解决办法两者选其一：1、Example1Configuration配置类上标注@ComponentScan
         *                    2、使用new AnnotationConfigApplicationContext(String... basePackages)
         */
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Example2Configuration.class);

        System.out.println(">>> applicationContext = " + applicationContext);

        /**
         * {@link ConfigurationClassPostProcessor}
         * {@link CommonAnnotationBeanPostProcessor}
         * {@link AutowiredAnnotationBeanPostProcessor}
         */
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        System.out.println("=============================================All BeanDefinitions(" + beanNames.length + ")=================================================");
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition(beanName);
            System.out.println(beanDefinition);
        }
    }

}
