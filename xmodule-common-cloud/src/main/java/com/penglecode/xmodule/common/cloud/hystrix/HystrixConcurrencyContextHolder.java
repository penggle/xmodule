package com.penglecode.xmodule.common.cloud.hystrix;

import org.springframework.core.NamedThreadLocal;

/**
 * Hystrix并发上下文Holder
 * 
 * @author 	pengpeng
 * @date 	2020年2月13日 下午3:52:31
 */
public class HystrixConcurrencyContextHolder {

	private static final ThreadLocal<HystrixConcurrencyContext> hystrixConcurrencyContextHolder = new NamedThreadLocal<>("Hystrix Concurrency Context");
	
	public static void resetContext() {
		hystrixConcurrencyContextHolder.remove();
	}
	
	public static void setContext(HystrixConcurrencyContext context) {
		hystrixConcurrencyContextHolder.set(context);
	}
	
	public static HystrixConcurrencyContext getContext() {
		return hystrixConcurrencyContextHolder.get();
	}
	
}
