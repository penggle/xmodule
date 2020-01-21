package com.penglecode.xmodule.common.cloud.feign;

import org.springframework.cloud.openfeign.HystrixFallbackHandler;
import org.springframework.cloud.openfeign.HystrixFallbackHandlerFactory;

public class DefaultHystrixFallbackHandlerFactory implements HystrixFallbackHandlerFactory {

	@Override
	public HystrixFallbackHandler createHandler(Class<?> feignClientClass, Throwable cause) {
		return new DefaultHystrixFallbackHandler(feignClientClass, cause);
	}

}
