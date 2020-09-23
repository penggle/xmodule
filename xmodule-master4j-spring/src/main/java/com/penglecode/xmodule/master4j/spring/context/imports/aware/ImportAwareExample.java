package com.penglecode.xmodule.master4j.spring.context.imports.aware;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * ImportAware接口配合@Import及注解，用于设置注解的值示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 23:14
 */
public class ImportAwareExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleConfiguration.class);

        System.out.println(">>> applicationContext = " + applicationContext);

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        System.out.println("=============================================All BeanDefinitions(" + beanNames.length + ")=================================================");
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition(beanName);
            System.out.println(beanDefinition);
        }

        ApiClient apiClient = applicationContext.getBean(ApiClient.class);
        System.out.println(apiClient);
    }

}
