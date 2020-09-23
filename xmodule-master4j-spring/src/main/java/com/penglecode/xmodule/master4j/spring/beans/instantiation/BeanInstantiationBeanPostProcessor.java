package com.penglecode.xmodule.master4j.spring.beans.instantiation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

import java.lang.reflect.Constructor;

/**
 * Spring Bean实例化过程中相关的BeanPostProcessor
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/11 20:57
 */
public class BeanInstantiationBeanPostProcessor implements BeanPostProcessor, InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor, SmartInstantiationAwareBeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanInstantiationBeanPostProcessor.class);
    
    /**
     * 与bean初始化(指的是调用init-method或者afterPropertiesSet方法)相关的BeanPostProcessor接口实现
     */

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        LOGGER.info(">>> 调用自定义的{}.postProcessBeforeInitialization(bean = {}, beanName = {})", "BeanPostProcessor", bean, beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        LOGGER.info("<<< 调用自定义的{}.postProcessAfterInitialization(bean = {}, beanName = {})", "BeanPostProcessor", bean, beanName);
        return bean;
    }

    /**
     * 与bean实例化(指的是new一个bean的实例)相关的InstantiationAwareBeanPostProcessor接口实现
     */

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        LOGGER.info(">>> 调用自定义的{}.postProcessBeforeInstantiation(beanClass = {}, beanName = {})", "InstantiationAwareBeanPostProcessor", beanClass, beanName);
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        LOGGER.info("<<< 调用自定义的{}.postProcessAfterInstantiation(bean = {}, beanName = {})", "InstantiationAwareBeanPostProcessor", bean, beanName);
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        LOGGER.info(">>> 调用自定义的{}.postProcessProperties(pvs = {}, bean = {}, beanName = {})", "InstantiationAwareBeanPostProcessor", pvs, bean, beanName);
        /*if(pvs instanceof MutablePropertyValues) {
            MutablePropertyValues mpvs = (MutablePropertyValues) pvs;
            PropertyValue pv = new PropertyValue("sessionTimeout", null);
            pv.setSource("PT30M");
            mpvs.addPropertyValue(pv); //设置默认的session超时时间
        }*/
        return pvs;
    }

    /**
     * 与bean实例化(指的是new一个bean的实例)相关的SmartInstantiationAwareBeanPostProcessor接口实现
     */

    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        LOGGER.info(">>> 调用自定义的{}.predictBeanType(beanClass = {}, beanName = {})", "SmartInstantiationAwareBeanPostProcessor", beanClass, beanName);
        return beanClass;
    }

    @Override
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
        LOGGER.info(">>> 调用自定义的{}.determineCandidateConstructors(beanClass = {}, beanName = {})", "SmartInstantiationAwareBeanPostProcessor", beanClass, beanName);
        return beanClass.getDeclaredConstructors();
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        LOGGER.info(">>> 调用自定义的{}.getEarlyBeanReference(bean = {}, beanName = {})", "SmartInstantiationAwareBeanPostProcessor", bean, beanName);
        return bean;
    }

    /**
     * 与bean销毁相关的DestructionAwareBeanPostProcessor接口实现
     */

    @Override
    public boolean requiresDestruction(Object bean) {
        LOGGER.info(">>> 调用自定义的{}.requiresDestruction(bean = {})", "DestructionAwareBeanPostProcessor", bean);
        return true;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        LOGGER.info(">>> 调用自定义的{}.postProcessBeforeDestruction(bean = {}, beanName = {})", "DestructionAwareBeanPostProcessor", bean, beanName);
    }
}
