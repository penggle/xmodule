package com.penglecode.xmodule.master4j.spring.context.imports.registrar;

import com.penglecode.xmodule.master4j.spring.context.imports.common.MessageService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * ImportBeanDefinitionRegistrar示例，
 * 配合@Import()动态注册bean
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 20:54
 */
public class ImportBeanDefinitionRegistrarExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleConfiguration.class);

        System.out.println(">>> applicationContext = " + applicationContext);

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        System.out.println("=============================================All BeanDefinitions(" + beanNames.length + ")=================================================");
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition(beanName);
            System.out.println(beanDefinition);
        }

        MessageService messageService1 = (MessageService) applicationContext.getBean("compositeMessageService");
        messageService1.sendMessage("hello spring123!", "13812345678");

        MessageService messageService2 = applicationContext.getBean(CompositeMessageService.class);
        messageService2.sendMessage("hello spring456!", "admin123@qq.com");
    }

}
