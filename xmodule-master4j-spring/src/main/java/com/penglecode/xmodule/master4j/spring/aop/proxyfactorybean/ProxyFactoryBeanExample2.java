package com.penglecode.xmodule.master4j.spring.aop.proxyfactorybean;

import com.penglecode.xmodule.master4j.spring.aop.advice.TimeService;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassRelativeResourceLoader;

/**
 * 基于ProxyFactoryBean的Spring声明式AOP示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/20 12:14
 */
public class ProxyFactoryBeanExample2 {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        ClassRelativeResourceLoader resourceLoader = new ClassRelativeResourceLoader(ProxyFactoryBeanExample2.class);
        beanDefinitionReader.loadBeanDefinitions(resourceLoader.getResource("applicationContext2.xml")); //加载bean的定义

        TimeService cglibProxyTimeService = (TimeService) beanFactory.getBean("cglibProxyTimeService");
        System.out.println(cglibProxyTimeService.getNowTime(TimeService.LONDON));

        System.out.println();

        TimeService jdkProxyTimeService = (TimeService) beanFactory.getBean("jdkProxyTimeService");
        System.out.println(jdkProxyTimeService.getNowTime(null));

    }

}
