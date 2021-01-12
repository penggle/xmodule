package com.penglecode.xmodule.common.util;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
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
@SuppressWarnings({"rawtypes"})
public class SpringUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringUtils.class);
	
	private static volatile ApplicationContext applicationContext;
	
	private static volatile Environment environment;
	
	/**
	 * 获取Spring环境变量
	 * @param name
	 * @param constType
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
	
	public static <T> T getBeanQuietly(Class<T> requiredType) {
		try {
			return getBean(requiredType);
		} catch (NoSuchBeanDefinitionException e) {
			return null;
		}
	}
	
	public static <T> T getBean(String name, Class<T> requiredType) {
		return getApplicationContext().getBean(name, requiredType);
	}
	
	public static <T> T getBeanQuietly(String name, Class<T> requiredType) {
		try {
			return getBean(name, requiredType);
		} catch (NoSuchBeanDefinitionException e) {
			return null;
		}
	}
	
	public static <T> T getBean(String name) {
		return (T) getApplicationContext().getBean(name);
	}
	
	public static <T> T getBeanQuietly(String name) {
		try {
			return getBean(name);
		} catch (NoSuchBeanDefinitionException e) {
			return null;
		}
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
	 * 创建默认的beanName
	 * @param applicationContext
	 * @param beanClass
	 * @return
	 */
	public static String createDefaultBeanName(ApplicationContext applicationContext, Class beanClass) {
		String beanName = null;
		for(int i = 0; i < Integer.MAX_VALUE; i++){
			beanName = beanClass.getName() + "#" + i;
			if(!applicationContext.containsBean(beanName)){
				break;
			}
		}
		return beanName;
	}
	
	/**
	 * 创建默认的beanName
	 * @param beanClass
	 * @return
	 */
	public static String createDefaultBeanName(Class beanClass) {
		String beanName = null;
		for(int i = 0; i < Integer.MAX_VALUE; i++){
			beanName = beanClass.getName() + "#" + i;
			if(!applicationContext.containsBean(beanName)){
				break;
			}
		}
		return beanName;
	}
	
	/**
	 * 手动向Spring容器注册一个Bean
	 * @param applicationContext
	 * @param beanName
	 * @param beanClass
	 * @param singleton
	 * @param beanCustomizer
	 */
	public static <T> void registerBean(ApplicationContext applicationContext, String beanName, Class<T> beanClass, boolean singleton, Consumer<BeanDefinitionBuilder> beanCustomizer){
		Assert.notNull(beanClass, "Parameter 'beanClass' can not be null!");
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
		boolean canCreate = true;
		if(!StringUtils.isEmpty(beanName)){
			canCreate = !beanFactory.containsBean(beanName);
		}
		if(StringUtils.isEmpty(beanName)){
			beanName = createDefaultBeanName(beanClass);
		}
        if(canCreate){
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(beanClass);
            beanDefinitionBuilder.setScope(singleton ? "singleton" : "prototype");
            if(beanCustomizer != null) {
            	beanCustomizer.accept(beanDefinitionBuilder);
            }
            beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
            LOGGER.info("Create and register spring managed bean[name = {}, class= {}] successfully.", beanName, beanClass);
        }else{
        	LOGGER.error("Create and register spring managed bean[name = {}, class= {}] failed：already exists a bean with name '{}'.", beanName, beanClass, beanName);
        }
	}
	
	/**
	 * 手动向Spring容器注册一个Bean
	 * @param beanName
	 * @param beanClass
	 * @param singleton
	 * @param beanCustomizer
	 */
	public static <T> void registerBean(String beanName, Class<T> beanClass, boolean singleton, Consumer<BeanDefinitionBuilder> beanCustomizer){
		registerBean(applicationContext, beanName, beanClass, singleton, beanCustomizer);
	}
	
	/**
	 * 根据beanName销毁一个已存在的bean
	 * @param <T>
	 * @param applicationContext
	 * @param beanName
	 * @param beanInstance		- 如果bean是以scope=prototype形式注册的，并且在销毁时需要执行其destroy()方法，那么该参数必须指定
	 */
	public static <T> boolean destroyBean(ApplicationContext applicationContext, String beanName, Object beanInstance) {
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
		try {
			BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
			if(beanDefinition.isSingleton()) {
				beanFactory.destroySingleton(beanName);
			} else if (beanInstance != null) {
				beanFactory.destroyBean(beanName, beanInstance);
			}
			beanFactory.removeBeanDefinition(beanName);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * 根据beanName销毁一个已存在的bean
	 * @param <T>
	 * @param beanName
	 * @param beanInstance		- 如果bean是以scope=prototype形式注册的，并且在销毁时需要执行其destroy()方法，那么该参数必须指定
	 */
	public static <T> boolean destroyBean(String beanName, Object beanInstance) {
		return destroyBean(applicationContext, beanName, beanInstance);
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
		beans.forEach(bean -> setBeanProperty(bean, properties, conversionService));
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
