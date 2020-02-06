package com.penglecode.xmodule.springboot.examples.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.stereotype.Component;

@Component
public class WebServerBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof WebServerFactory) {
			System.out.println(">>> postProcessBeforeInitialization : " + bean);
		}
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof WebServerFactory) {
			System.out.println(">>> postProcessAfterInitialization : " + bean);
		}
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}

}
