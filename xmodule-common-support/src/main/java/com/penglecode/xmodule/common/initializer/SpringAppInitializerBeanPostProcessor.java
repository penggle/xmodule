package com.penglecode.xmodule.common.initializer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class SpringAppInitializerBeanPostProcessor implements BeanPostProcessor {

	@Override
	@SuppressWarnings("unchecked")
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof SpringAppInitializer) {
			SpringAppInitializerStateManager.setState((Class<? extends SpringAppInitializer>) bean.getClass(), SpringAppInitializerState.UNEXECUTED);
		}
		return bean;
	}

}
