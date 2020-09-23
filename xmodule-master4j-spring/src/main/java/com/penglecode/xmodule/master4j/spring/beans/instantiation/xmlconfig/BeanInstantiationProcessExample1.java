package com.penglecode.xmodule.master4j.spring.beans.instantiation.xmlconfig;

import com.penglecode.xmodule.master4j.spring.beans.instantiation.*;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.ClassRelativeResourceLoader;

/**
 * Spring Bean实例化过程示例 (基于XML配置)
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/11 21:50
 */
public class BeanInstantiationProcessExample1 {

    public static void main(String[] args) {
        ConfigurableConversionService conversionService = (ConfigurableConversionService) DefaultConversionService.getSharedInstance();
        conversionService.addConverter(new StringToDurationConverter());
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.setConversionService(conversionService); //设置全局类型转换ConversionService
        beanFactory.registerCustomEditor(ServerConfig.class, ServerConfigPropertyEditor.class); //注册自定义的属性编辑器
        beanFactory.addBeanPostProcessor(new BeanInstantiationBeanPostProcessor()); //添加Bean实例化相关的自定义BeanPostProcessor

        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        ClassRelativeResourceLoader resourceLoader = new ClassRelativeResourceLoader(BeanInstantiationProcessExample1.class);
        beanDefinitionReader.loadBeanDefinitions(resourceLoader.getResource("applicationContext1.xml")); //加载bean的定义

        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for(String beanName : beanNames) {
            System.out.println(beanFactory.getBeanDefinition(beanName));
        }

        String beanName = "tomcatWebServer";

        AbstractWebServer webServer = (AbstractWebServer) beanFactory.getBean(beanName);
        System.out.println(webServer);
        System.out.println(webServer.getSessionTimeout());

        beanFactory.destroySingleton(beanName);
    }

}
