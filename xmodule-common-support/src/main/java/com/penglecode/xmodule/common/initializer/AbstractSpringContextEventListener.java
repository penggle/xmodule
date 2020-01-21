package com.penglecode.xmodule.common.initializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;

import com.penglecode.xmodule.common.exception.ApplicationInitializeException;
import com.penglecode.xmodule.common.util.CollectionUtils;

/**
 * Spring上下文事件监听器基类，用于在此时机做一些初始化操作
 * 
 * @author 	pengpeng
 * @date	2019年6月13日 下午2:04:54
 */
public abstract class AbstractSpringContextEventListener<T extends ApplicationContextEvent> implements ApplicationListener<T>, ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppStartedEventListener.class);
	
	private ApplicationContext applicationContext;
	
	@Override
	public void onApplicationEvent(T event) {
		ConfigurableApplicationContext eventApplicationContext = getApplicationContext(event);
		if(eventApplicationContext.equals(applicationContext)) {
			Map<String,SpringAppInitializer> initializerBeans = applicationContext.getBeansOfType(SpringAppInitializer.class);
			if(!CollectionUtils.isEmpty(initializerBeans)) {
				List<SpringAppInitializer> initializers = new ArrayList<SpringAppInitializer>();
				for(Map.Entry<String,SpringAppInitializer> entry : initializerBeans.entrySet()) {
					SpringAppInitializer initializer = entry.getValue();
					InitializationRunAt initRunAt = AnnotationUtils.findAnnotation(initializer.getClass(), InitializationRunAt.class);
					if(initRunAt != null && event.getClass().equals(initRunAt.value())) {
						initializers.add(initializer);
					}
				}
				if(!CollectionUtils.isEmpty(initializers)) {
					AnnotationAwareOrderComparator.sort(initializers);
					LOGGER.info(">>> {}事件监听器启动, 即将执行的初始化程序：{}", event.getClass().getSimpleName(), initializers);
				
					for(SpringAppInitializer initializer : initializers) {
						try {
							SpringAppInitializerStateManager.setState(initializer.getClass(), SpringAppInitializerState.EXECUTING);
							initializer.initialize(eventApplicationContext);
							SpringAppInitializerStateManager.setState(initializer.getClass(), SpringAppInitializerState.EXECUTED_SUCCESS);
						} catch (Throwable e) {
							SpringAppInitializerStateManager.setState(initializer.getClass(), SpringAppInitializerState.EXECUTED_FAILURE);
							LOGGER.error(String.format(">>> 执行Spring应用启动初始化程序(%s)失败：%s", initializer.getClass().getSimpleName(), e.getMessage(), e));
							throw new ApplicationInitializeException(e);
						}
					}
				}
			}
		}
	}
	
	protected abstract ConfigurableApplicationContext getApplicationContext(T event);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
