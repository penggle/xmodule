package com.penglecode.xmodule.master4j.spring.context.config.property1;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * Spring属性配置文件加载，
 * {@link org.springframework.beans.factory.config.PropertyPlaceholderConfigurer} 已废弃
 * 推荐使用 {@link org.springframework.context.support.PropertySourcesPlaceholderConfigurer}
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 17:05
 */
public class PropertySourcesExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleConfiguration.class);

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
        System.out.println(">>> app.name = " + environment.getProperty("app.name"));
        System.out.println(">>> user.variant = " + environment.getProperty("user.variant"));
        System.out.println(">>> app.version = " + environment.getProperty("app.version", environment.resolvePlaceholders("${os.version}")));
        System.out.println("-----------------------------------");

        System.out.println(applicationContext.getBean(AppConfig.class));
    }

}
