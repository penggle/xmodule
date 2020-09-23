package com.penglecode.xmodule.master4j.spring.context.sample.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.context.ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/15 17:39
 */
@Component
public class ConversionServiceAutoRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("【ConversionServiceAutoRegistryPostProcessor】>>> setBeanFactory(" + beanFactory + ")");
        if(beanFactory instanceof ConfigurableListableBeanFactory) {
            this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("【ConversionServiceAutoRegistryPostProcessor】>>> postProcessBeanFactory(" + beanFactory + ")");
        if(this.beanFactory != null) {
            System.out.println("【ConversionServiceAutoRegistryPostProcessor】>>> this.beanFactory == beanFactory : " + (this.beanFactory == beanFactory));
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("【ConversionServiceAutoRegistryPostProcessor】>>> postProcessBeanDefinitionRegistry(" + registry + ")");
        if(!registry.containsBeanDefinition(CONVERSION_SERVICE_BEAN_NAME)) { //如果没有注册全局的ConversionService，则注册一下
            registry.registerBeanDefinition(CONVERSION_SERVICE_BEAN_NAME, new RootBeanDefinition(ConversionServiceFactoryBean.class));
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("【ConversionServiceAutoRegistryPostProcessor】>>> postProcessAfterInitialization(" + bean + ", " + beanName + ")");
        /**
         * 注意名字为conversionService的全局ConversionService可能是由ConversionServiceFactoryBean构造出来的，
         * 如果是的话，那么名字为conversionService的bean将有两个：ConversionServiceFactoryBean和DefaultConversionService，
         * 后者由ConversionServiceFactoryBean#getObject()得到的
         */
        if(CONVERSION_SERVICE_BEAN_NAME.equals(beanName) && bean instanceof GenericConversionService) {
            Map<String,Converter> converterBeans = beanFactory.getBeansOfType(Converter.class);
            Set<Converter> converters = new HashSet<>(converterBeans.values());
            ConversionServiceFactory.registerConverters(converters, (GenericConversionService) bean);
        }
        return bean;
    }

}
