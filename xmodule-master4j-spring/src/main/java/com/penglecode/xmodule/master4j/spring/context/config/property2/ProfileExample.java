package com.penglecode.xmodule.master4j.spring.context.config.property2;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * Spring 环境 & 属性由四个部分组成：PropertySource、PropertyResolver、Profile 和 Environment。
 *
 * - PropertySource：属性源，key-value 属性对抽象，用于配置数据。
 * - PropertyResolver：属性解析器，用于解析属性配置，例如解析${}占位符等。
 * - Profile：剖面，只有激活的剖面的组件/配置才会注册到 Spring 容器，类似于 Spring Boot 中的 profile
 * - Environment：环境，PropertySource、Profile 、PropertyResolver 的组合。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 17:05
 */
public class ProfileExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.getEnvironment().setActiveProfiles("dev");
        applicationContext.register(ExampleConfiguration.class);
        applicationContext.refresh();

        System.out.println(">>> applicationContext = " + applicationContext);

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        System.out.println("=============================================All BeanDefinitions(" + beanNames.length + ")=================================================");
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition(beanName);
            System.out.println(beanDefinition);
        }

        System.out.println("=============================================ConfigurableEnvironment Test=============================================");
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources1 = environment.getPropertySources();

        for(PropertySource<?> propertySource : propertySources1) {
            System.out.println(String.format("【%s】>>> name = %s, source(%s) = %s", propertySource.getClass(), propertySource.getName(), propertySource.getSource().getClass(), propertySource.getSource()));
        }

        System.out.println("-----------------------------------");
        System.out.println(">>> spring.datasource.jdbcUrl = " + environment.getProperty("spring.datasource.jdbcUrl"));
        System.out.println(">>> spring.datasource.username = " + environment.getProperty("spring.datasource.username"));
        System.out.println(">>> spring.datasource.password = " + environment.getProperty("spring.datasource.password"));
        System.out.println("-----------------------------------");

        System.out.println(applicationContext.getBean(DataSourceProperties.class));
    }

}
