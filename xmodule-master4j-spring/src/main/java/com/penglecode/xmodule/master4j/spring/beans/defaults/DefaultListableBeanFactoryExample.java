package com.penglecode.xmodule.master4j.spring.beans.defaults;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.master4j.spring.common.model.News;
import com.penglecode.xmodule.master4j.spring.common.service.NewsService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassRelativeResourceLoader;

import java.util.List;

/**
 * DefaultListableBeanFactory使用示例
 *
 * XmlBeanFactory ，它根据XML文件中的定义加载beans。该容器从XML 文件读取配置元数据并用它去创建一个完全配置的系统或应用。
 * 这个类从Spring3.1的开始就已经废弃了，取而代之的是建议使用DefaultListableBeanFactory + XmlBeanDefinitionReader
 * 来构造一个DefaultListableBeanFactory实例。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/10 22:25
 */
public class DefaultListableBeanFactoryExample {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        ClassRelativeResourceLoader resourceLoader = new ClassRelativeResourceLoader(DefaultListableBeanFactoryExample.class);
        beanDefinitionReader.loadBeanDefinitions(resourceLoader.getResource("applicationContext1.xml")); //加载bean的定义

        //beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory); //注册一个特殊的已知类型的依赖bean

        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for(String beanName : beanNames) {
            System.out.println(beanName);
        }

        NewsService newsService1 = beanFactory.getBean(NewsService.class);
        System.out.println(">>> newsService1 = " + newsService1);

        NewsService newsService2 = beanFactory.getBean(NewsService.class);
        System.out.println(">>> newsService2 = " + newsService2);

        System.out.println(">>> newsService1 == newsService2 : " + (newsService1 == newsService2));
    }

}
