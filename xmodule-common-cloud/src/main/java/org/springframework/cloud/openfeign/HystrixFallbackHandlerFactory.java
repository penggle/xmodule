package org.springframework.cloud.openfeign;

/**
 * HystrixFallbackHandler工厂
 * 
 * @author 	pengpeng
 * @date	2019年6月3日 上午9:39:52
 */
public interface HystrixFallbackHandlerFactory {

	public HystrixFallbackHandler createHandler(Class<?> feignClientClass, Throwable cause);
	
}
