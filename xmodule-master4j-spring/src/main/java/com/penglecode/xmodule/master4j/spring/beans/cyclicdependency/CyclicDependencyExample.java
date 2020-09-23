package com.penglecode.xmodule.master4j.spring.beans.cyclicdependency;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassRelativeResourceLoader;

/**
 * Spring是如何解决循环依赖的？
 *
 * https://juejin.im/post/6844904180683898888
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/10 22:25
 */
public class CyclicDependencyExample {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        ClassRelativeResourceLoader resourceLoader = new ClassRelativeResourceLoader(CyclicDependencyExample.class);
        beanDefinitionReader.loadBeanDefinitions(resourceLoader.getResource("applicationContext1.xml")); //加载bean的定义

        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for(String beanName : beanNames) {
            System.out.println(beanName);
        }

        FooBean fooBean = (FooBean) beanFactory.getBean("fooBean");
        System.out.println(">>> fooBean = " + fooBean);

        BarBean barBean = (BarBean) beanFactory.getBean("barBean");
        System.out.println(">>> barBean = " + barBean);
    }

}
