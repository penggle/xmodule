package com.penglecode.xmodule.common.web.support;

import org.springframework.core.NamedThreadLocal;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * RestTemplate动态配置同步器
 * 
 * @author 	pengpeng
 * @date	2019年7月2日 下午6:14:28
 */
public class RestTemplateSynchronizationManager {

	private static final ThreadLocal<Class<? extends ResponseErrorHandler>> responseErrorHandlerType =
			new NamedThreadLocal<>("The type of ResponseErrorHandler for RestTemplate");
	
	
	public static void setCurrentResponseErrorHandlerType(Class<? extends ResponseErrorHandler> errorHandlerType) {
		responseErrorHandlerType.set(errorHandlerType);
	}
	
	public static Class<? extends ResponseErrorHandler> getCurrentResponseErrorHandlerType() {
		return responseErrorHandlerType.get();
	}
	
	public static void removeCurrentResponseErrorHandlerType() {
		responseErrorHandlerType.remove();
	}
	
}
