package com.penglecode.xmodule.master4j.spring.beans.util;

import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.util.Assert;

import java.util.function.Consumer;

/**
 * 基于JavaConfig的BeanFactory的工具类
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 12:16
 */
public class BeanFactoryUtils {

    public static DefaultListableBeanFactory createBeanFactory(Class<?> ... javaConfigClasses) {
        return createBeanFactory(beanFactory -> {}, javaConfigClasses);
    }

    public static DefaultListableBeanFactory createBeanFactory(Consumer<DefaultListableBeanFactory> beanFactorySetting, Class<?> ... javaConfigClasses) {
        Assert.notEmpty(javaConfigClasses, "Parameter 'javaConfigClasses' must be required!");
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //首先注册@Configuration注解的配置类
        for(Class<?> javaConfigClass : javaConfigClasses) {
            beanFactory.registerBeanDefinition(javaConfigClass.getName(), new RootBeanDefinition(javaConfigClass));
        }
        //@Configuration注解处理器
        ConfigurationClassPostProcessor ccpp = new ConfigurationClassPostProcessor();
        //从已注册的被@Configuration注解的配置类中进一步注册bean
        ccpp.postProcessBeanDefinitionRegistry(beanFactory);
        //准备用CGLIB增强(代理)被@Configuration注解的配置类,以替换它们在运行时的获取bean的请求，具体是否代理取决@Configuration#proxyBeanMethods()
        ccpp.postProcessBeanFactory(beanFactory);

        //@Required校验用于约束哪些bean的setter方法必须要设置值
        RequiredAnnotationBeanPostProcessor rapp = new RequiredAnnotationBeanPostProcessor();
        rapp.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(rapp);
        beanFactorySetting.accept(beanFactory);
        beanFactory.freezeConfiguration(); //确定所有bean的定义都已经准备好了，冻结对所有bean定义的修改
        return beanFactory;
    }

}
