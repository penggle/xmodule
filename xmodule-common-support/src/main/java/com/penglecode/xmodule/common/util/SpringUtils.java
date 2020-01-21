package com.penglecode.xmodule.common.util;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.consts.ApplicationConstants;

/**
 * Spring Bean工具类
 * 
 * @author  pengpeng
 * @date 	 2015年4月26日 下午8:36:35
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class SpringUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringUtils.class);
	
	private static volatile ApplicationContext applicationContext;
	
	private static volatile Environment environment;
	
	/**
	 * 获取Spring环境变量
	 * @param name
	 * @param constType
	 * @param defaultValue
	 * @return
	 */
	public static <T> T getEnvProperty(String name, Class<T> constType) {
		return getEnvironment().getProperty(name, constType);
	}
	
	/**
	 * 获取Spring环境变量
	 * @param name
	 * @param constType
	 * @param defaultValue
	 * @return
	 */
	public static <T> T getEnvProperty(String name, Class<T> constType, T defaultValue) {
		return getEnvironment().getProperty(name, constType, defaultValue);
	}
	
	public static <T> T getBean(Class<T> requiredType) {
		return getApplicationContext().getBean(requiredType);
	}
	
	public static <T> T getBean(String name, Class<T> requiredType) {
		return getApplicationContext().getBean(name, requiredType);
	}
	
	public static <T> T getBean(String name) {
		return (T) getApplicationContext().getBean(name);
	}
	
	public static <T> Map<String,T> getBeansOfType(Class<T> type) {
		return getApplicationContext().getBeansOfType(type);
	}
	
	public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
		return getApplicationContext().getBeansWithAnnotation(annotationType);
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public static void setApplicationContext(ApplicationContext applicationContext){
		SpringUtils.applicationContext = applicationContext;
	}
	
	public static Environment getEnvironment() {
		return environment;
	}

	public static void setEnvironment(Environment environment) {
		SpringUtils.environment = environment;
	}

	public static ApplicationContext getRootApplicationContext(ApplicationContext applicationContext) {
		if(applicationContext != null) {
			return applicationContext.getParent() == null ? applicationContext : getRootApplicationContext(applicationContext.getParent());
		}
		return null;
	}
	
	/**
	 * 手动创建一个Bean
	 * @param beanName
	 * @param beanClass
	 * @param beanProperties
	 * @param singleton
	 * @param initMethod
	 * @param destroyMethod
	 * @param handleAutowireDependency	 - 当前被创建的bean被其他已经存在的bean依赖需要autowire?
	 * @return 如果return null则表示该名称的bean已经存在了
	 */
	public static <T> void createBean(ApplicationContext applicationContext, String beanName, Class<T> beanClass, Map<String,Object> beanProperties, boolean singleton, String initMethod, String destroyMethod, boolean handleAutowireDependency){
		Assert.notNull(beanClass, "Parameter 'beanClass' can not be null!");
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
		boolean canCreate = true;
		if(singleton){
			if(!StringUtils.isEmpty(beanName)){
				canCreate = !beanFactory.containsBean(beanName);
			}
		}
		boolean hasBeanName = !StringUtils.isEmpty(beanName);
		if(!hasBeanName){
			for(int i = 0; i < Integer.MAX_VALUE; i++){
				beanName = beanClass.getName() + "#" + i;
				if(!beanFactory.containsBean(beanName)){
					break;
				}
			}
		}
		
        if(canCreate){
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(beanClass);
            if(!CollectionUtils.isEmpty(beanProperties)){
            	for(Map.Entry<String,Object> entry : beanProperties.entrySet()){
            		beanDefinitionBuilder.addPropertyValue(entry.getKey(), entry.getValue());
            	}
            }
            if(!StringUtils.isEmpty(initMethod)){
            	beanDefinitionBuilder.setInitMethodName(initMethod);
            }
            if(!StringUtils.isEmpty(destroyMethod)){
            	beanDefinitionBuilder.setDestroyMethodName(destroyMethod);
            }
            beanDefinitionBuilder.setScope(singleton ? "singleton" : "prototype");
            beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
            if(handleAutowireDependency){
            	 String[] existBeanNames = beanFactory.getBeanDefinitionNames();
                 for(String existBeanName : existBeanNames){
             		Object existingBean = beanFactory.getBean(existBeanName);
                 	beanFactory.autowireBean(existingBean); //process @Autowire dependency
                 }
            }
            LOGGER.info("Create and register bean[name = {}, class= {}] to spring container successfully.", beanName, beanClass);
        }else{
        	LOGGER.error("Create and register bean[name = {}, class= {}] to spring container failed：spring container already exists a bean with name '{}'.", beanName, beanClass, beanName);
        }
	}
	
	/**
	 * 手动创建一个Bean
	 * @param beanName
	 * @param beanClass
	 * @param beanProperties
	 * @param singleton
	 * @param initMethod
	 * @param destroyMethod
	 * @param handleAutowireDependency	 - 当前被创建的bean被其他已经存在的bean依赖需要autowire?
	 * @return 如果return null则表示该名称的bean已经存在了
	 */
	public static <T> void createBean(String beanName, Class<T> beanClass, Map<String,Object> beanProperties, boolean singleton, String initMethod, String destroyMethod, boolean handleAutowireDependency){
		createBean(applicationContext, beanName, beanClass, beanProperties, singleton, initMethod, destroyMethod, handleAutowireDependency);
	}
	
	/**
	 * 手动创建一个Bean
	 * @param beanName
	 * @param beanClass
	 * @param beanProperties
	 * @param singleton
	 * @param handleAutowireDependency	 - 当前被创建的bean被其他已经存在的bean依赖需要autowire?
	 * @return 如果return null则表示该名称的bean已经存在了
	 */
	public static <T> void createBean(String beanName, Class<T> beanClass, Map<String,Object> beanProperties, boolean singleton, boolean handleAutowireDependency){
		createBean(beanName, beanClass, beanProperties, singleton, null, null, handleAutowireDependency);
	}
	
	/**
	 * 手动创建一个Bean
	 * @param beanName
	 * @param beanClass
	 * @param beanProperties
	 * @param singleton
	 * @return 如果return null则表示该名称的bean已经存在了
	 */
	public static <T> void createBean(String beanName, Class<T> beanClass, Map<String,Object> beanProperties, boolean singleton){
		createBean(beanName, beanClass, beanProperties, singleton, null, null, true);
	}
	
	/**
	 * 将properties中的值填充到指定bean中去
	 * @param bean
	 * @param properties
	 * @param conversionService
	 */
	public static void setBeanProperty(Object bean, Map<String,Object> properties, ConversionService conversionService) {
		Assert.notNull(bean, "Parameter 'bean' can not be null!");
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		beanWrapper.setConversionService(conversionService);
		for(Map.Entry<String,Object> entry : properties.entrySet()) {
			String propertyName = entry.getKey();
			if(beanWrapper.isWritableProperty(propertyName)) {
				beanWrapper.setPropertyValue(propertyName, entry.getValue());
			}
		}
	}
	
	/**
	 * 将properties中的值填充到指定bean中去
	 * @param bean
	 * @param properties
	 */
	public static void setBeanProperty(Object bean, Map<String,Object> properties) {
		setBeanProperty(bean, properties, ApplicationConstants.DEFAULT_CONVERSION_SERVICE.get());
	}
	
	/**
	 * 将properties中的值填充到指定bean中去
	 * @param beans
	 * @param properties
	 * @param conversionService
	 */
	public static void setBeanProperty(List<Object> beans, Map<String,Object> properties, ConversionService conversionService) {
		Assert.notEmpty(beans, "Parameter 'bean' can not be empty!");
		beans.forEach(bean -> {
			setBeanProperty(bean, properties, conversionService);
		});
	}
	
	/**
	 * 将properties中的值填充到指定bean中去
	 * @param beans
	 * @param properties
	 */
	public static void setBeanProperty(List<Object> beans, Map<String,Object> properties) {
		setBeanProperty(beans, properties, ApplicationConstants.DEFAULT_CONVERSION_SERVICE.get());
	}
	
}
