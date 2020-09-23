package com.penglecode.xmodule.master4j.spring.beans.instantiation.javaconfig;

import com.penglecode.xmodule.master4j.spring.beans.instantiation.AbstractWebServer;
import com.penglecode.xmodule.master4j.spring.beans.instantiation.BeanInstantiationBeanPostProcessor;
import com.penglecode.xmodule.master4j.spring.beans.util.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * Spring Bean实例化过程示例 (基于JavaConfig)
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 12:14
 */
public class BeanInstantiationProcessExample2 {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = BeanFactoryUtils.createBeanFactory(factory -> {
            factory.setConversionService(DefaultConversionService.getSharedInstance());
            factory.addBeanPostProcessor(new BeanInstantiationBeanPostProcessor());
        }, BeanInstantiationExampleConfiguration.class);

        String beanName = "jettyWebServer";

        AbstractWebServer webServer = (AbstractWebServer) beanFactory.getBean(beanName);
        System.out.println(webServer);
        System.out.println(webServer.getSessionTimeout());

        beanFactory.destroySingleton(beanName);
    }

}
