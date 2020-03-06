package com.penglecode.xmodule.springboot.examples.dynamicbean;

import org.springframework.stereotype.Component;

import com.penglecode.xmodule.common.util.SpringUtils;

@Component
public class OpennessApiServiceFactory {

	private final Object mutex = new Object();
	
	public <T extends AbstractOpennessApiService> T getOpennessApiService(Class<T> serviceClass, String nodeName) {
		String serviceBeanName = getServiceBeanName(serviceClass, nodeName);
		T bean = SpringUtils.getBeanQuietly(serviceBeanName, serviceClass);
		if(bean == null) {
			synchronized (mutex) {
				bean = SpringUtils.getBeanQuietly(serviceBeanName, serviceClass);
				if(bean == null) {
					SpringUtils.registerBean(serviceBeanName, serviceClass, true, bdb -> {
						bdb.addConstructorArgValue(nodeName);
					});
					bean = SpringUtils.getBean(serviceBeanName, serviceClass);
				}
			}
		}
		return bean;
	}
	
	public <T extends AbstractOpennessApiService> void destroyOpennessApiService(Class<T> serviceClass, String nodeName) {
		String serviceBeanName = getServiceBeanName(serviceClass, nodeName);
		synchronized (mutex) {
			SpringUtils.destroyBean(serviceBeanName, null);
		}
	}
	
	public <T extends AbstractOpennessApiService> String getServiceBeanName(Class<T> serviceClass, String nodeName) {
		return nodeName + serviceClass.getSimpleName();
	}
	
}
