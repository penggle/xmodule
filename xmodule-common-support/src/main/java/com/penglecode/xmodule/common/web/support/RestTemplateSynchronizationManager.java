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

	private static final ThreadLocal<Class<? extends ResponseErrorHandler>> RESPONSE_ERROR_HANDLER_TYPE =
			new NamedThreadLocal<>("The type of ResponseErrorHandler for RestTemplate");
	
	
	public static void setCurrentResponseErrorHandlerType(Class<? extends ResponseErrorHandler> errorHandlerType) {
		RESPONSE_ERROR_HANDLER_TYPE.set(errorHandlerType);
	}
	
	public static Class<? extends ResponseErrorHandler> getCurrentResponseErrorHandlerType() {
		return RESPONSE_ERROR_HANDLER_TYPE.get();
	}
	
	public static void removeCurrentResponseErrorHandlerType() {
		RESPONSE_ERROR_HANDLER_TYPE.remove();
	}
	
}
