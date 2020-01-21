package com.penglecode.xmodule.common.cloud.feign;

import java.lang.reflect.Method;

import org.springframework.cloud.openfeign.HystrixFallbackHandler;

import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;

/**
 * 默认的服务熔断|降级统一拦截处理类
 * 
 * @author 	pengpeng
 * @date	2019年6月3日 上午9:33:14
 */
public class DefaultHystrixFallbackHandler extends HystrixFallbackHandler {

	public DefaultHystrixFallbackHandler(Class<?> feignClientClass, Throwable cause) {
		super(feignClientClass, cause);
	}

	@Override
	protected Object handle(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?> returnType = method.getReturnType();
		if(PageResult.class.equals(returnType)) {
			return HystrixFallbackResults.defaultFallbackPageResult();
		} else if (Result.class.equals(returnType)) {
			return HystrixFallbackResults.defaultFallbackResult();
		} else {
			throw getCause();
		}
	}

}
